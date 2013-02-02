package com.sportsfire.screening;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.sportsfire.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class TestInputForm extends Activity {
	public static final String ARG_ITEM_TESTS = "argumentTest";
	public static final String ARG_ITEM_TESTVAL = "argumentTestValue";
	Map<String, Integer> TestsMap = new HashMap<String, Integer>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_input_form);
		List test = getIntent().getParcelableExtra(ARG_ITEM_TESTS);
		System.out.println(test);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_test_input_form, menu);
		return true;
	}

}
