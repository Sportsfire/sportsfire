package com.sportsfire;
import com.sportsfire.R;
import com.sportsfire.db.DBHelper;

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
        	  DBHelper db = new DBHelper(this);
        	  db.initiateDatabaseWithStubValues();
        	  Intent intent2 = new Intent(this,com.sportsfire.injury.ListPageActivity.class);
        	  //setContentView(R.layout.activity_list_page);
        	  startActivity(intent2);
        	  
            break;
          case R.id.button2:
        	  Intent intent21 = new Intent(this,com.sportsfire.screening.ScreeningMainPage.class);
        	  //setContentView(R.layout.activity_list_page);
        	  startActivity(intent21);
            break;
          }
    }
}
