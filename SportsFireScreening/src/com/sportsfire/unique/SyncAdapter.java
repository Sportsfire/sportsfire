package com.sportsfire.unique;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.sportsfire.screening.ScreeningData;
import com.sportsfire.screening.db.ScreeningUpdatesTable;
import com.sportsfire.screening.db.ScreeningValuesTable;
import com.sportsfire.sync.BasicSyncAdapter;

public class SyncAdapter extends BasicSyncAdapter {
	private AccountManager mAccountManager;
	private ContentResolver mContentResolver;
	private Context context;
	public static final String SYNC_SCREENINGUPDATES_URI = BASE_URL + "/screeningupdates/";
	
	private static final String SYNC_SCREEN_MARKER_KEY = "com.sportsfire.sync.screen marker";


	public SyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		mAccountManager = AccountManager.get(context);
		mContentResolver = context.getContentResolver();
		this.context = context;
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider,
			SyncResult syncResult) {
		this.account = account;
		loadSquadsAndPlayers();
		updateScreening();
		if (updateCount == 100){
			appAutoUpdate();
			updateCount = 0;
		}

	}

	private void updateScreening() {
		try {

			JSONArray jsonarray = new JSONArray();
			String[] projection = { ScreeningUpdatesTable.KEY_VALUE_ID };
			String[] innerprojection = { ScreeningValuesTable.KEY_PLAYER_ID, ScreeningValuesTable.KEY_WEEK,
					ScreeningValuesTable.KEY_SEASON_ID, ScreeningValuesTable.KEY_MEASUREMENT_TYPE,
					ScreeningValuesTable.KEY_VALUE };
			HashMap<Integer, String> listOfIDs = new HashMap<Integer, String>();
			Cursor cursor = mContentResolver
					.query(Provider.CONTENT_URI_SCREENING_UPDATES, projection, null, null, null);
			if (cursor.moveToFirst()) {
				do {
					if (listOfIDs.containsKey(cursor.getInt(0)))
						continue;
					listOfIDs.put(cursor.getInt(0), "");

					Cursor innercursor = mContentResolver.query(Provider.CONTENT_URI_SCREENING_VALUES, innerprojection,
							ScreeningValuesTable.KEY_ID + " = '" + cursor.getInt(0) + "'", null, null);
					innercursor.moveToFirst();
					JSONObject jsonentry = new JSONObject();
					for (int i = 0; i < innercursor.getColumnCount(); i++) {
						jsonentry.put(innercursor.getColumnName(i), innercursor.getString(i));
					}
					jsonarray.put(jsonentry);
					innercursor.close();
				} while (cursor.moveToNext());
			}
			cursor.close();
			JSONObject params = new JSONObject();
			params.accumulate("syncmarker", mAccountManager.getUserData(account, SYNC_SCREEN_MARKER_KEY));
			params.accumulate("updates", jsonarray);
			final HttpPost post = new HttpPost(SYNC_SCREENINGUPDATES_URI + getTokenParamsString());
			Log.e("post", params.toString());
			// post.addHeader("Content-Type", "application/json");
			// post.addHeader("Authorization", getBasicAuthString());
			post.setEntity(new ByteArrayEntity(params.toString().getBytes("UTF8")));

			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
			HttpParams params1 = new BasicHttpParams();
			SingleClientConnManager mgr = new SingleClientConnManager(params1, schemeRegistry);
			HttpClient client = new DefaultHttpClient(mgr, params1);
			final HttpResponse resp = client.execute(post);
			// final HttpResponse resp = getHttpClient().execute(post);
			final String response = EntityUtils.toString(resp.getEntity());
			Log.e("response", response);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				final JSONObject serverResponse = new JSONObject(response);

				final JSONArray serverUpdates = new JSONArray(serverResponse.getString("updates"));

				for (int i = 0; i < serverUpdates.length(); i++) {
					ContentValues values = new ContentValues();

					JSONObject object = new JSONObject(serverUpdates.getString(i));

					@SuppressWarnings("unchecked")
					Iterator<String> it = object.keys();
					while (it.hasNext()) {
						String key = it.next();
						if (key != "_id") {
							values.put(key, object.getString(key));
						}
					}

					ScreeningData screeningdata = new ScreeningData(context,
							values.getAsString(ScreeningValuesTable.KEY_SEASON_ID),
							values.getAsString(ScreeningValuesTable.KEY_WEEK));

					screeningdata.setValue(values.getAsString(ScreeningValuesTable.KEY_PLAYER_ID),
							values.getAsString(ScreeningValuesTable.KEY_MEASUREMENT_TYPE),
							values.getAsString(ScreeningValuesTable.KEY_VALUE));

				}
				mContentResolver.delete(Provider.CONTENT_URI_SCREENING_UPDATES, null, null);
				mAccountManager.setUserData(account, SYNC_SCREEN_MARKER_KEY,
						(String) serverResponse.get("newsyncmarker"));
				appAutoUpdate();

			}
		} catch (Exception e) {
			Log.e("Exception", e.toString());
		}

	}

	@Override
	protected void loadSquadsAndPlayers() {
		LinkedList<ContentValues> squads = loadSquads();
		LinkedList<ContentValues> seasons = loadSeasons();
		LinkedList<ContentValues> players = loadPlayers();
		if (squads != null && seasons != null && players != null) {
			mContentResolver.delete(Provider.CONTENT_URI_PLAYERS, null, null);
			mContentResolver.delete(Provider.CONTENT_URI_SQUADS, null, null);
			mContentResolver.delete(Provider.CONTENT_URI_SEASONS, null, null);
			ListIterator<ContentValues> it = null;
			if (squads != null) {
				it = squads.listIterator();

				while (it.hasNext()) {
					mContentResolver.insert(Provider.CONTENT_URI_SQUADS, it.next());
				}
			}
			if (seasons != null) {
				it = seasons.listIterator();
				while (it.hasNext()) {
					mContentResolver.insert(Provider.CONTENT_URI_SEASONS, it.next());
				}
			}

			if (players != null) {
				it = players.listIterator();
				while (it.hasNext()) {
					mContentResolver.insert(Provider.CONTENT_URI_PLAYERS, it.next());
				}
			}
		}

	}



}