package com.sportsfire.sync;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.sportsfire.db.PlayerTable;
import com.sportsfire.db.ScreeningUpdatesTable;
import com.sportsfire.db.ScreeningValuesTable;
import com.sportsfire.db.SquadTable;
import com.sportsfire.screening.ScreeningData;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
	private AccountManager mAccountManager;
	private ContentResolver mContentResolver;
	private Context context;
	
	/** Base URL for the v2 Sample Sync Service */
    public static final String BASE_URL = "http://sportsfire4.cs.ucl.ac.uk";
    /** URI for authentication service */
    public static final String AUTH_URI = BASE_URL + "/auth";
    /** URI for sync service */
    public static final String SYNC_PLAYERS_URI = BASE_URL + "/players/";
    public static final String SYNC_SQUADS_URI = BASE_URL + "/squads/";
    public static final String SYNC_SCREENINGUPDATES_URI = BASE_URL + "/screeningupdates/";
    public static final int HTTP_REQUEST_TIMEOUT_MS = 70 * 1000;
    
    private static final String SYNC_MARKER_KEY = "com.sportsfire.sync.marker";
    private static final String SYNC_SCREEN_MARKER_KEY = "com.sportsfire.sync.screen marker";



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
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
		String authtoken = null;

		loadSquadsAndPlayers();
		updateScreening(account);
		
		
	}
	
	private void updateScreening(Account account) {
		try{
			
			JSONArray jsonarray = new JSONArray();
			String[] projection = { ScreeningUpdatesTable.KEY_VALUE_ID };
			String[] innerprojection = {ScreeningValuesTable.KEY_PLAYER_ID, ScreeningValuesTable.KEY_WEEK, ScreeningValuesTable.KEY_SEASON_ID, ScreeningValuesTable.KEY_MEASUREMENT_TYPE, ScreeningValuesTable.KEY_VALUE };
			HashMap<Integer,String> listOfIDs = new HashMap<Integer,String>();
			Cursor cursor = mContentResolver.query(Provider.CONTENT_URI_SCREENING_UPDATES, projection, null, null, null);
			if (cursor.moveToFirst()) {
	            do {
	            	if(listOfIDs.containsKey(cursor.getInt(0)))
	            		continue;
	            	listOfIDs.put(cursor.getInt(0), "");
	            	
	            	 
	            	Cursor innercursor = mContentResolver.query(Provider.CONTENT_URI_SCREENING_VALUES, innerprojection, ScreeningValuesTable.KEY_ID + " = '" + cursor.getInt(0) + "'", null, null);
	            	innercursor.moveToFirst();
	            	JSONObject jsonentry = new JSONObject();
	            	for(int i = 0;i<innercursor.getColumnCount();i++){
	            		jsonentry.put(innercursor.getColumnName(i),innercursor.getString(i));
	            	}
	            	jsonarray.put(jsonentry);
	            } while (cursor.moveToNext());
	        }
			JSONObject params = new JSONObject();
			params.accumulate("syncmarker", mAccountManager.getUserData(account, SYNC_SCREEN_MARKER_KEY));
			params.accumulate("updates", jsonarray);
	        final HttpPost post = new HttpPost(SYNC_SCREENINGUPDATES_URI);
	        Log.e("post",post.toString());
	        post.addHeader("Content-Type", "application/json");
	       
	        post.setEntity(new ByteArrayEntity(params.toString().getBytes("UTF8")));
	        final HttpResponse resp = getHttpClient().execute(post);

	        final String response = EntityUtils.toString(resp.getEntity());
	        Log.e("response",response);
	        if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	        	final JSONObject serverResponse = new JSONObject(response);
	        
	
	        	
	        	final JSONArray serverUpdates = new JSONArray(serverResponse.getString("updates"));
	            Log.d("Response", response);
	            	            
	            for (int i = 0; i < serverUpdates.length(); i++) {
		            ContentValues values = new ContentValues();
		            

	            	JSONObject object = new JSONObject(serverUpdates.getString(i));
	            	
	            	@SuppressWarnings("unchecked")
					Iterator<String> it = object.keys();
	            	while(it.hasNext()){
	            		String key = it.next();
	            		if(key != "_id"){
		            		values.put(key, object.getString(key));
	            		}
	            	}	 
	            	
	            	ScreeningData screeningdata = new ScreeningData(context,values.getAsString(ScreeningValuesTable.KEY_SEASON_ID),values.getAsString(ScreeningValuesTable.KEY_WEEK));
	            	
	            	screeningdata.setValue(values.getAsString(ScreeningValuesTable.KEY_PLAYER_ID), values.getAsString(ScreeningValuesTable.KEY_MEASUREMENT_TYPE), values.getAsString(ScreeningValuesTable.KEY_VALUE));
	           
	            }
	            mContentResolver.delete(Provider.CONTENT_URI_SCREENING_UPDATES, null, null);
	            mAccountManager.setUserData(account, SYNC_SCREEN_MARKER_KEY, (String)serverResponse.get("newsyncmarker"));
	            
	        }
		}
		catch (Exception e){
			Log.e("Exception", e.toString());
		}
		
		
	}


	private void loadSquadsAndPlayers() {

		LinkedList<ContentValues> squads = loadSquads();
		LinkedList<ContentValues> players = loadPlayers();
		mContentResolver.delete(Provider.CONTENT_URI_PLAYERS, null, null);
		mContentResolver.delete(Provider.CONTENT_URI_SQUADS, null, null);
		ListIterator<ContentValues> it = squads.listIterator();
		while(it.hasNext()){
			mContentResolver.insert(Provider.CONTENT_URI_SQUADS, it.next());
		}
		if (players != null) it = players.listIterator();
		while(it.hasNext()){
			mContentResolver.insert(Provider.CONTENT_URI_PLAYERS, it.next());
		}
		
		
		
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
	        final HttpResponse resp = getHttpClient().execute(get);

	        final String response = EntityUtils.toString(resp.getEntity());
	        Log.e("response",response);
	        if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	            // Our request to the server was successful - so we assume
	            // that they accepted all the changes we sent up, and
	            // that the response includes the contacts that we need
	            // to update on our side...
	            final JSONArray serverPlayers = new JSONArray(response);
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
	            final JSONArray serverPlayers = new JSONArray(response);
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