package com.sportsfire.sportsfireinjury;

import com.sportsfire.*;
import com.sportsfire.db.InjuryTable;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;


public class InjuryForm extends Activity {
	public static final String ARG_ITEM_INJURY = "argumentInjuryID";
	public static final String ARG_ITEM_PLAYER = "argumentPlayer";
	
	
	private InjuryReportControl reportControl;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_injury_form);
		Player p = getIntent().getParcelableExtra(ARG_ITEM_PLAYER);
		if(p != null){
			reportControl = new InjuryReportControl(p);
		}
		//if (details != null) {
		//	((TextView) findViewById(R.id.EditText01)).setText("10/10/10");
		//}
	//	((TextView)findViewById(R.id.ir314)).setSelected(false);
		//((TextView)findViewById(R.id.ir4)).setSelected(false);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main_page, menu);
		return true;
	}

	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment((TextView) v);
		newFragment.show(getFragmentManager(), "datePicker");
	}
	
	public void onCheckBoxClick(View v){
		CheckBox c = (CheckBox)v;
        if(c.isChecked()){
        	//reportControl.setValue(idToField(c.getId()), new InjuryReportValueBoolean(true));
        	reportControl.setValue(c.getId(), new InjuryReportValueBoolean(true));
        }
        else{
        	//reportControl.setValue(idToField(c.getId()), new InjuryReportValueBoolean(false));
        	reportControl.setValue(c.getId(), new InjuryReportValueBoolean(false));
        }
	}
}
