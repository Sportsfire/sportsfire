package com.sportsfire.sync;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
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
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class ServerAuth {
	protected String token, user;
	public String[] userSignIn(String user, String pass, String authType) throws Exception {

        Log.d("SportfireScreening", "userSignIn");
       
        String url = "https://sportsfire.tottenhamhotspur.com/token/new.json";
        
        final HttpPost post = new HttpPost(url);
        Log.e("post",post.toString());
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);  
        nameValuePairs.add(new BasicNameValuePair("username", user));  
        nameValuePairs.add(new BasicNameValuePair("password", pass));  
        HttpEntity entity = new UrlEncodedFormEntity(nameValuePairs);
        Log.e("NVP", EntityUtils.toString(entity));
        post.setEntity(entity); 
        
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("https", 
                    SSLSocketFactory.getSocketFactory(), 443));
        HttpParams params = new BasicHttpParams();
        SingleClientConnManager mgr = new SingleClientConnManager(params, schemeRegistry);
        HttpClient client = new DefaultHttpClient(mgr, params);
        final HttpResponse resp = client.execute(post);
        final String response = EntityUtils.toString(resp.getEntity());
        Log.e("response",response);
        String authtoken = null;
        String number = null;
        if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        	final JSONObject serverResponse = new JSONObject(response);
        	
        	final String status = serverResponse.getString("success");
        	if (status.compareTo("true")==0){
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
	public class UpdateApp extends AsyncTask<String,Void,Void>{
		private Context context;
		public void setContext(Context contextf){
		    context = contextf;
		}

		@Override
		protected Void doInBackground(String... arg0) {
		      try {
		    	  	String link = "https://sportsfire.tottenhamhotspur.com/downloadUpdate" + ;
		            URL url = new URL(arg0[0]);
		            HttpURLConnection c = (HttpURLConnection) url.openConnection();
		            c.setRequestMethod("GET");
		            c.setDoOutput(true);
		            c.connect();

		            String PATH = Environment.getExternalStorageDirectory() + "/download/";
		            File file = new File(PATH);
		            file.mkdirs();
		            File outputFile = new File(file, "update.apk");
		            if(outputFile.exists()){
		                outputFile.delete();
		            }
		            FileOutputStream fos = new FileOutputStream(outputFile);

		            InputStream is = c.getInputStream();

		            byte[] buffer = new byte[1024];
		            int len1 = 0;
		            while ((len1 = is.read(buffer)) != -1) {
		                fos.write(buffer, 0, len1);
		            }
		            fos.close();
		            is.close();

		            Intent intent = new Intent(Intent.ACTION_VIEW);
		            intent.setDataAndType(
		            		Uri.fromFile(new File(PATH+"update.apk")), "application/vnd.android.package-archive");
		            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
		            context.startActivity(intent);


		        } catch (Exception e) {
		            Log.e("UpdateAPP", "Update error! " + e.getMessage());
		        }
		    return null;
		}}  
}
