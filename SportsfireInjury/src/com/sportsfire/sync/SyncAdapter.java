package com.sportsfire.sync;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sportsfire.db.PlayerTable;
import com.sportsfire.db.SquadTable;

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
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
	private AccountManager mAccountManager;
	private ContentResolver mContentResolver;
	
	/** Base URL for the v2 Sample Sync Service */
    public static final String BASE_URL = "http://128.16.80.141/api";
    /** URI for authentication service */
    public static final String AUTH_URI = BASE_URL + "/auth";
    /** URI for sync service */
    public static final String SYNC_PLAYERS_URI = BASE_URL + "/player/";
    public static final String SYNC_SQUADS_URI = BASE_URL + "/squads/";
    public static final int HTTP_REQUEST_TIMEOUT_MS = 70 * 1000;


	public SyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		mAccountManager = AccountManager.get(context);
		mContentResolver = context.getContentResolver();
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
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
		String authtoken = null;

		LinkedList<ContentValues> squads = loadSquads();
		LinkedList<ContentValues> players = loadPlayers();
		mContentResolver.delete(Provider.CONTENT_URI_PLAYERS, null, null);
		mContentResolver.delete(Provider.CONTENT_URI_SQUADS, null, null);
		ListIterator<ContentValues> it = squads.listIterator();
		while(it.hasNext()){
			mContentResolver.insert(Provider.CONTENT_URI_SQUADS, it.next());
		}
		it = players.listIterator();
		while(it.hasNext()){
			mContentResolver.insert(Provider.CONTENT_URI_PLAYERS, it.next());
		}
		
        Log.e("hello","marker");

		
		
	}

	private LinkedList<ContentValues> loadPlayers() {

		try{
			final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	        final HttpEntity entity;
	        try {
	            entity = new UrlEncodedFormEntity(params);
	        } catch (final UnsupportedEncodingException e) {
	            // this should never happen.
	            throw new IllegalStateException(e);
	        }
	        final HttpGet get = new HttpGet(SYNC_PLAYERS_URI);
	       // final HttpPost post = new HttpPost(SYNC_PLAYERS_URI);
	       // Log.e("post",post.toString());
	        //post.addHeader("Content-Type", "application/json");
	        //post.setEntity(entity);
	        final HttpResponse resp = getHttpClient().execute(get);

	        final String response = EntityUtils.toString(resp.getEntity());
	        Log.e("response",response);
	        if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	            // Our request to the server was successful - so we assume
	            // that they accepted all the changes we sent up, and
	            // that the response includes the contacts that we need
	            // to update on our side...
	        	final JSONObject serverJSON = new JSONObject(response);
	            final JSONArray serverPlayers = serverJSON.getJSONArray("objects");
	            Log.d("Response", response);
	            
	            LinkedList<ContentValues> resultsList = new LinkedList<ContentValues>();
	            for (int i = 0; i < serverPlayers.length(); i++) {
		            ContentValues values = new ContentValues();

	            	JSONObject object = serverPlayers.getJSONObject(i);
	            	values.put(PlayerTable.KEY_PLAYER_ID,object.getString("_id"));
	            	values.put(PlayerTable.KEY_FIRST_NAME,object.getString("firstname"));
	            	values.put(PlayerTable.KEY_SURNAME,object.getString("surname"));
	            	values.put(PlayerTable.KEY_SQUAD_ID,object.getString("squadid"));
	            	values.put(PlayerTable.KEY_DOB,object.getString("dateofbirth"));

	            	//values.put(Database.COL_NAME, object.getString("firstname") + " " + object.getString("surname"));
	    			//mContentResolver.insert(Provider.CONTENT_URI, values);
	            	resultsList.add(values);
	            }
	            return resultsList;
	        }
		}
		catch (Exception e){
			Log.e("Exception", e.toString());
		}
		
		return null;
		
	}
	
	private LinkedList<ContentValues> loadSquads() {

		try{
			final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	        final HttpEntity entity;
	        try {
	            entity = new UrlEncodedFormEntity(params);
	        } catch (final UnsupportedEncodingException e) {
	            // this should never happen.
	            throw new IllegalStateException(e);
	        }
	        final HttpGet get = new HttpGet(SYNC_SQUADS_URI);
	       // final HttpPost post = new HttpPost(SYNC_PLAYERS_URI);
	       // Log.e("post",post.toString());
	        //post.addHeader("Content-Type", "application/json");
	        //post.setEntity(entity);
	        final HttpResponse resp = getHttpClient().execute(get);

	        final String response = EntityUtils.toString(resp.getEntity());
	        Log.e("response",response);
	        if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	            // Our request to the server was successful - so we assume
	            // that they accepted all the changes we sent up, and
	            // that the response includes the contacts that we need
	            // to update on our side...
	        	final JSONObject serverJSON = new JSONObject(response);
	            final JSONArray serverPlayers = serverJSON.getJSONArray("objects");
	            Log.d("Response", response);
	           
	            LinkedList<ContentValues> resultList = new LinkedList<ContentValues>();
	            for (int i = 0; i < serverPlayers.length(); i++) {
	            	ContentValues values = new ContentValues();
	            	JSONObject object = serverPlayers.getJSONObject(i);
	            	values.put(SquadTable.KEY_SQUAD_ID,object.getString("_id"));
	            	values.put(SquadTable.KEY_SQUAD_NAME,object.getString("squadname"));
	            	
	            	//values.put(Database.COL_NAME, object.getString("firstname") + " " + object.getString("surname"));
	    			//mContentResolver.insert(Provider.CONTENT_URI, values);
	            	resultList.add(values);
	            }
	            return resultList;
	        }
		}
		catch (Exception e){
			Log.e("Exception", e.toString());
		}
		return null;
		
	}

	 
	    }