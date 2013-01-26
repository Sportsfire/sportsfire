package com.sportsfire.screening;
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
        setContentView(R.layout.screening_main_page);
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
}
