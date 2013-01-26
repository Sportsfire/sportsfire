package com.sportsfire.injury;
import com.sportsfire.db.DBHelper;
import com.sportsfire.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;


public class MainPage extends Activity{

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
        switch (v.getId()) {
          case R.id.button1:
        	  //Intent intent = new Intent(this,InjuryForm.class);
        	  //setContentView(R.layout.player_injury_form);
              //startActivity(intent);
        	  DBHelper db = new DBHelper(this);
        	  db.initiateDatabaseWithStubValues();
        	  
            break;
          case R.id.button2:
        	  Intent intent2 = new Intent(this,ListPageActivity.class);
        	  //setContentView(R.layout.activity_list_page);
        	  startActivity(intent2);
            break;
          }
    }
}
