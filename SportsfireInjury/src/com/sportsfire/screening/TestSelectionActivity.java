package com.sportsfire.screening;

import com.sportsfire.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class TestSelectionActivity extends Activity {
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.screening_testselection_page);
	     }
	 public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.activity_main_page, menu);
	        return true;
	    }
}
