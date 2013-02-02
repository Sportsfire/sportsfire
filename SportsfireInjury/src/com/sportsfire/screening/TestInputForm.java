package com.sportsfire.screening;

import com.sportsfire.R;
import com.sportsfire.R.layout;
import com.sportsfire.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class TestInputForm extends Activity {
	public static final String ARG_ITEM_TESTS = "argumentTest";
	public static final String ARG_ITEM_TESTVAL = "argumentTestValue";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_input_form);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_test_input_form, menu);
		return true;
	}

}
