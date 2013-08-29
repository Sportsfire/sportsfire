package com.sportsfire.sync;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class ServerAuth {
	protected String token, user;

	public String[] userSignIn(String user, String pass, String authType) throws Exception {

		Log.d("SERVER_AUTH", "userSignIn");

		String url = "https://sportsfire.tottenhamhotspur.com/token/new.json";

		final HttpPost post = new HttpPost(url);
		Log.e("post", post.toString());
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("username", user));
		nameValuePairs.add(new BasicNameValuePair("password", pass));
		HttpEntity entity = new UrlEncodedFormEntity(nameValuePairs);
		Log.e("NVP", EntityUtils.toString(entity));
		post.setEntity(entity);

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
		HttpParams params = new BasicHttpParams();
		SingleClientConnManager mgr = new SingleClientConnManager(params, schemeRegistry);
		HttpClient client = new DefaultHttpClient(mgr, params);
		final HttpResponse resp = client.execute(post);
		final String response = EntityUtils.toString(resp.getEntity());
		Log.e("response", response);
		String authtoken = null;
		String number = null;
		if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			final JSONObject serverResponse = new JSONObject(response);

			final String status = serverResponse.getString("success");
			if (status.compareTo("true") == 0) {
				authtoken = serverResponse.getString("token");
				number = serverResponse.getString("user");
				String[] result = new String[2];
				result[0] = authtoken;
				result[1] = number;
				return result;
			} else {
				throw new Exception("Error signing-in: Wrong User/Pass");
			}
		} else {
			throw new Exception("Error signing-in: Network error");
		}

	}
	
	public boolean checkExpired(String user, String token){
		Log.d("SERVER_AUTH", "checkexpired");
		String url = "https://sportsfire.tottenhamhotspur.com/token/"+token+"/"+user+".json";
		try {
			final HttpResponse resp = new DefaultHttpClient().execute(new HttpGet(url));
			final String response = EntityUtils.toString(resp.getEntity());
			Log.e("response", response);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				final JSONObject serverResponse = new JSONObject(response);
				final String status = serverResponse.getString("success");
				if (status.compareTo("true") == 0) {
					return false;
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public void update(Context context){
		UpdateApp update = new UpdateApp();
		update.setContext(context);
		update.execute();
	}
	public class UpdateApp extends AsyncTask<String, Void, Void> {
		private Context context;

		public void setContext(Context contextf) {
			context = contextf;
		}
		@Override
		protected Void doInBackground(String... arg0) {
			try {
				String appName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
				String fileName = "AUTO"+appName+".apk";
				String link = "https://sportsfire.tottenhamhotspur.com/appupdate?app="+fileName;
				final HttpGet get = new HttpGet(link);
				final HttpResponse resp = new DefaultHttpClient().execute(get);
				// Execute HTTP Post Request
				String PATH = Environment.getExternalStorageDirectory() + "/Download/";
				File outputFile = new File(new File(PATH), fileName);
				if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
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
				Log.e("UpdateAPP", "Update error! " + e.getMessage());
			}
			return null;
		}
	}
}
