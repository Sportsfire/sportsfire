package com.sportsfire.sync;

import static com.sportsfire.sync.Constants.AUTHTOKEN_TYPE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.sportsfire.db.PlayerTable;
import com.sportsfire.db.ScreeningUpdatesTable;
import com.sportsfire.db.ScreeningValuesTable;
import com.sportsfire.db.SeasonTable;
import com.sportsfire.db.SquadTable;
import com.sportsfire.screening.ScreeningData;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
	private AccountManager mAccountManager;
	private ContentResolver mContentResolver;
	private Context context;
	private Account account;
	/** Base URL for the v2 Sample Sync Service */
	public static final String BASE_URL = "https://sportsfire.tottenhamhotspur.com";
	/** URI for authentication service */
	public static final String AUTH_URI = BASE_URL + "/auth";
	/** URI for sync service */
	public static final String SYNC_PLAYERS_URI = BASE_URL + "/players/";
	public static final String SYNC_SQUADS_URI = BASE_URL + "/squads/";
	public static final String SYNC_SEASONS_URI = BASE_URL + "/seasons/";

	public static final String SYNC_SCREENINGUPDATES_URI = BASE_URL + "/screeningupdates/";
	public static final int HTTP_REQUEST_TIMEOUT_MS = 30 * 1000;

	private static final String SYNC_MARKER_KEY = "com.sportsfire.sync.marker";
	private static final String SYNC_SCREEN_MARKER_KEY = "com.sportsfire.sync.screen marker";

	private HttpEntity getParamsEntity() {
		final AccountManager am = AccountManager.get(context);
		String authToken;
		try {
			authToken = am.blockingGetAuthToken(account, AUTHTOKEN_TYPE, true);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("user", am.getUserData(account, AccountManager.KEY_USERDATA)));
			nameValuePairs.add(new BasicNameValuePair("token", authToken));
			HttpEntity entity = new UrlEncodedFormEntity(nameValuePairs);
			return entity;
		} catch (OperationCanceledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AuthenticatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private String getTokenParamsString(){
		try {
			return "?"+EntityUtils.toString(getParamsEntity());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private String getBasicAuthString() throws Exception{
		final AccountManager am = AccountManager.get(context);
		String
			authToken = am.blockingGetAuthToken(account, AUTHTOKEN_TYPE, true);
			String s = am.getUserData(account, AccountManager.KEY_USERDATA)+":"+authToken;
			return "Basic " + Base64.encodeToString(s.getBytes(), Base64.URL_SAFE);
	}
	public SyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		mAccountManager = AccountManager.get(context);
		mContentResolver = context.getContentResolver();
		this.context = context;
	}

	/**
	 * Configures the httpClient to connect to the URL provided.
	 */
	public static HttpClient getHttpClient() {
		HttpClient httpClient = new DefaultHttpClient();
		final HttpParams params = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
		HttpConnectionParams.setSoTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
		ConnManagerParams.setTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
		params.setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
		return httpClient;
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider,
			SyncResult syncResult) {
		this.account = account;
		loadSquadsAndPlayers();
		updateScreening(account);

	}

	private void updateScreening(Account account) {
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
				} while (cursor.moveToNext());
			}
			JSONObject params = new JSONObject();
			params.accumulate("syncmarker", mAccountManager.getUserData(account, SYNC_SCREEN_MARKER_KEY));
			params.accumulate("updates", jsonarray);
			final HttpPost post = new HttpPost(SYNC_SCREENINGUPDATES_URI+ getTokenParamsString());
			Log.e("post", params.toString());
			//post.addHeader("Content-Type", "application/json");
			//post.addHeader("Authorization", getBasicAuthString());
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
				Log.d("Response", response);

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

			}
		} catch (Exception e) {
			Log.e("Exception", e.toString());
		}

	}

	private void loadSquadsAndPlayers() {

		LinkedList<ContentValues> squads = loadSquads();
		LinkedList<ContentValues> seasons = loadSeasons();
		LinkedList<ContentValues> players = loadPlayers();
		if (squads != null && seasons != null && players != null){
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

	private LinkedList<ContentValues> loadPlayers() {

		try {
			final HttpGet get = new HttpGet(SYNC_PLAYERS_URI + getTokenParamsString());
			final HttpResponse resp = getHttpClient().execute(get);

			final String response = EntityUtils.toString(resp.getEntity());
			Log.e("response", response);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// Our request to the server was successful
				final JSONArray serverPlayers = new JSONArray(response);
				Log.d("Response", response);

				LinkedList<ContentValues> resultsList = new LinkedList<ContentValues>();
				for (int i = 0; i < serverPlayers.length(); i++) {
					ContentValues values = new ContentValues();

					JSONObject object = serverPlayers.getJSONObject(i);
					values.put(PlayerTable.KEY_PLAYER_ID, object.getString("_id"));
					values.put(PlayerTable.KEY_FIRST_NAME, object.getString("firstname"));
					values.put(PlayerTable.KEY_SURNAME, object.getString("surname"));
					values.put(PlayerTable.KEY_SQUAD_ID, object.getString("squadid"));
					values.put(PlayerTable.KEY_DOB, object.getString("dateofbirth"));

					// values.put(Database.COL_NAME,
					// object.getString("firstname") + " " +
					// object.getString("surname"));
					// mContentResolver.insert(Provider.CONTENT_URI, values);
					resultsList.add(values);
				}
				return resultsList;
			}
		} catch (Exception e) {
			Log.e("Exception", e.toString());
		}

		return null;

	}

	private LinkedList<ContentValues> loadSeasons() {

		try {

			final HttpGet get = new HttpGet(SYNC_SEASONS_URI+getTokenParamsString());
			final HttpResponse resp = getHttpClient().execute(get);

			final String response = EntityUtils.toString(resp.getEntity());
			Log.e("response", response);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// Our request to the server was successful
				final JSONArray serverSeasons = new JSONArray(response);
				Log.d("Response", response);

				LinkedList<ContentValues> resultsList = new LinkedList<ContentValues>();
				for (int i = 0; i < serverSeasons.length(); i++) {
					ContentValues values = new ContentValues();

					JSONObject object = serverSeasons.getJSONObject(i);
					values.put(SeasonTable.KEY_SEASON_ID, object.getString("_id"));
					values.put(SeasonTable.KEY_SEASON_NAME, object.getString("name"));

					resultsList.add(values);
				}
				return resultsList;
			}
		} catch (Exception e) {
			Log.e("Exception", e.toString());
		}

		return null;

	}

	private LinkedList<ContentValues> loadSquads() {

		try {

			final HttpGet get = new HttpGet(SYNC_SQUADS_URI+getTokenParamsString());
			// final HttpPost post = new HttpPost(SYNC_PLAYERS_URI);
			// Log.e("post",post.toString());
			// post.addHeader("Content-Type", "application/json");
			// post.setEntity(entity);
			final HttpResponse resp = getHttpClient().execute(get);

			final String response = EntityUtils.toString(resp.getEntity());
			Log.e("response", response);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// Our request to the server was successful
				final JSONArray serverPlayers = new JSONArray(response);
				Log.d("Response", response);

				LinkedList<ContentValues> resultList = new LinkedList<ContentValues>();
				for (int i = 0; i < serverPlayers.length(); i++) {
					ContentValues values = new ContentValues();
					JSONObject object = serverPlayers.getJSONObject(i);
					values.put(SquadTable.KEY_SQUAD_ID, object.getString("_id"));
					values.put(SquadTable.KEY_SQUAD_NAME, object.getString("squadname"));

					// values.put(Database.COL_NAME,
					// object.getString("firstname") + " " +
					// object.getString("surname"));
					// mContentResolver.insert(Provider.CONTENT_URI, values);
					resultList.add(values);
				}
				return resultList;
			}
		} catch (Exception e) {
			Log.e("Exception", e.toString());
		}
		return null;

	}

}