package com.sportsfire.sync;

import static com.sportsfire.sync.Constants.AUTHTOKEN_TYPE;
import static com.sportsfire.sync.Constants.server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.sportsfire.db.PlayerTable;
import com.sportsfire.db.SeasonTable;
import com.sportsfire.db.SquadTable;

public abstract class BasicSyncAdapter extends AbstractThreadedSyncAdapter {
	protected Context context;
	protected Account account;
	/** Base URL for the v2 Sample Sync Service */
	public static final String BASE_URL = "https://sportsfire.tottenhamhotspur.com";
	/** URI for authentication service */
	public static final String AUTH_URI = BASE_URL + "/auth";
	/** URI for sync service */
	public static final String SYNC_PLAYERS_URI = BASE_URL + "/players/";
	public static final String SYNC_SQUADS_URI = BASE_URL + "/squads/";
	public static final String SYNC_SEASONS_URI = BASE_URL + "/seasons/";
	public static final int HTTP_REQUEST_TIMEOUT_MS = 30 * 1000;
	protected String tokenParams;
	private static final String SYNC_MARKER_KEY = "com.sportsfire.sync.marker";

	private HttpEntity getParamsEntity() {
		final AccountManager am = AccountManager.get(context);
		String authToken;
		try {
			authToken = am.blockingGetAuthToken(account, AUTHTOKEN_TYPE, true);
			String authUser = am.getUserData(account, AccountManager.KEY_USERDATA);
			if (server.checkExpired(authUser, authToken)) {
				am.invalidateAuthToken(account.type, authToken);
				return getParamsEntity();
			}
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("user", am.getUserData(account, AccountManager.KEY_USERDATA)));
			nameValuePairs.add(new BasicNameValuePair("token", authToken));
			HttpEntity entity = new UrlEncodedFormEntity(nameValuePairs);
			return entity;
		} catch (OperationCanceledException e) {
			e.printStackTrace();
		} catch (AuthenticatorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected String getTokenParamsString() {
		if (tokenParams == null) {
			try {
				tokenParams = "?" + EntityUtils.toString(getParamsEntity());
				return tokenParams;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		} else {
			return tokenParams;
		}
	}

	private String getBasicAuthString() throws Exception {
		final AccountManager am = AccountManager.get(context);
		String authToken = am.blockingGetAuthToken(account, AUTHTOKEN_TYPE, true);
		String s = am.getUserData(account, AccountManager.KEY_USERDATA) + ":" + authToken;
		return "Basic " + Base64.encodeToString(s.getBytes(), Base64.URL_SAFE);
	}

	public BasicSyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
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

	protected void appAutoUpdate() {
		try {
			String version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
			String appName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
			String fileName = "AUTO" + appName + ".apk";
			Log.e("VERSION", version);
			String link = "https://sportsfire.tottenhamhotspur.com/appupdate?app=" + appName + "version=" + version;
			// Create a new HttpClient and Post Header
			final HttpGet get = new HttpGet(link);
			// Execute HTTP Post Request
			final HttpResponse resp = new DefaultHttpClient().execute(get);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String PATH = Environment.getExternalStorageDirectory() + "/Download/";
				File outputFile = new File(new File(PATH), fileName);
				if (outputFile.exists()) {
					outputFile.delete();
				}
				outputFile.createNewFile();
				BufferedOutputStream objectOut = new BufferedOutputStream(new FileOutputStream(outputFile));
				resp.getEntity().writeTo(objectOut);
				objectOut.close();
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(new File(PATH + fileName)),
						"application/vnd.android.package-archive");
				// without this flag android returned an intent error!
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}

		} catch (Exception e) {
			Log.e("AutoUpdateAPP", "Update error! " + e.getMessage());
		}
	}

	protected abstract void loadSquadsAndPlayers();

	protected LinkedList<ContentValues> loadPlayers() {

		try {
			final HttpGet get = new HttpGet(SYNC_PLAYERS_URI + getTokenParamsString());
			final HttpResponse resp = new DefaultHttpClient().execute(get);

			final String response = EntityUtils.toString(resp.getEntity());
			Log.e("player_response", response);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// Our request to the server was successful
				final JSONArray serverPlayers = new JSONArray(response);
				LinkedList<ContentValues> resultsList = new LinkedList<ContentValues>();
				for (int i = 0; i < serverPlayers.length(); i++) {
					ContentValues values = new ContentValues();

					JSONObject object = serverPlayers.getJSONObject(i);
					values.put(PlayerTable.KEY_PLAYER_ID, object.getString("_id"));
					values.put(PlayerTable.KEY_FIRST_NAME, object.getString("firstname"));
					values.put(PlayerTable.KEY_SURNAME, object.getString("surname"));
					values.put(PlayerTable.KEY_SQUAD_ID, object.getString("squadid"));
					values.put(PlayerTable.KEY_DOB, object.getString("dateofbirth"));
					resultsList.add(values);
				}
				return resultsList;
			}
		} catch (Exception e) {
			Log.e("PlayersException", e.toString());
		}

		return null;

	}

	protected LinkedList<ContentValues> loadSeasons() {

		try {

			final HttpGet get = new HttpGet(SYNC_SEASONS_URI + getTokenParamsString());
			final HttpResponse resp = new DefaultHttpClient().execute(get);

			final String response = EntityUtils.toString(resp.getEntity());
			Log.e("season_response", response);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// Our request to the server was successful
				final JSONArray serverSeasons = new JSONArray(response);

				LinkedList<ContentValues> resultsList = new LinkedList<ContentValues>();
				for (int i = 0; i < serverSeasons.length(); i++) {
					ContentValues values = new ContentValues();

					JSONObject object = serverSeasons.getJSONObject(i);
					values.put(SeasonTable.KEY_SEASON_ID, object.getString("_id"));
					values.put(SeasonTable.KEY_SEASON_NAME, object.getString("name"));
					values.put(SeasonTable.KEY_START_DATE, object.getString("startdate"));
					resultsList.add(values);
				}
				return resultsList;
			}
		} catch (Exception e) {
			Log.e("SeasonsException", e.toString());
		}

		return null;

	}

	protected LinkedList<ContentValues> loadSquads() {

		try {

			final HttpGet get = new HttpGet(SYNC_SQUADS_URI + getTokenParamsString());
			final HttpResponse resp = new DefaultHttpClient().execute(get);

			final String response = EntityUtils.toString(resp.getEntity());
			Log.e("squad_response", response);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// Our request to the server was successful
				final JSONArray serverPlayers = new JSONArray(response);

				LinkedList<ContentValues> resultList = new LinkedList<ContentValues>();
				for (int i = 0; i < serverPlayers.length(); i++) {
					ContentValues values = new ContentValues();
					JSONObject object = serverPlayers.getJSONObject(i);
					values.put(SquadTable.KEY_SQUAD_ID, object.getString("_id"));
					values.put(SquadTable.KEY_SQUAD_NAME, object.getString("squadname"));
					resultList.add(values);
				}
				return resultList;
			}
		} catch (Exception e) {
			Log.e("SquadsException", e.toString());
		}
		return null;

	}

}