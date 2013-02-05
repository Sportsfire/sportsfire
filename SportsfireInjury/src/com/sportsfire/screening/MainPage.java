package com.sportsfire.screening;
import com.sportsfire.R;
import com.sportsfire.db.DBHelper;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainPage extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screening_main_page);
	    getActionBar().setDisplayHomeAsUpEnabled(true);
     }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_page, menu);
        return true;
    }
    public void ButtonOnClick(View v) {
        switch (v.getId()) {
          case R.id.button1:
        	  Intent intent2 = new Intent(this,TestSelectionActivity.class);
        	  //setContentView(R.layout.activity_list_page);
        	  startActivity(intent2);
            break;
          case R.id.button2:

            break;
          }
    }
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(this, com.sportsfire.MainPage.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
