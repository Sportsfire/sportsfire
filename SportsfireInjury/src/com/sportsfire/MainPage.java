package com.sportsfire;

import com.sportsfire.R;
import com.sportsfire.db.DBHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

//First page you see
public class MainPage extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_page);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main_page, menu);
		return true;
	}

	public void ButtonOnClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.button1:
			DBHelper db = new DBHelper(this);
			db.initiateDatabaseWithStubValues();
			intent = new Intent(this, com.sportsfire.injury.ListPageActivity.class);
			startActivity(intent);

			break;
		case R.id.button2:
			intent = new Intent(this, com.sportsfire.screening.ScreeningMainPage.class);
			startActivity(intent);
			break;
		case R.id.settingsBtn:
			intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			break;
		}
	}
}
