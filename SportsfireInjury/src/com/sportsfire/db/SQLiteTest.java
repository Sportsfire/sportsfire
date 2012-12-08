package com.example.sportsfireinjury;

import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class SQLiteTest extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//	    setContentView(R.layout.main);

	    DBHelper db = new DBHelper(this);
	    
	    // Inserting Players
	    Log.d("Insert: ", "Inserting ..");
	    db.addPlayer(new Player(0,"aaron", "aaronson", 19871118, "First Team"));
	    db.addPlayer(new Player(1,"roger", "rogerson", 19871118, "First Team"));
	    db.addPlayer(new Player(2,"pete", "peterson", 19871118, "First Team"));
	    
	    // Reading Players
	    Log.d("Reading: ", "Reading all players..");
	    List<Player> playerList = db.getAllPlayers();
	    
	    // log
	    for (Player p : playerList) {
	    	String log = "ID: " + p.getID() 
	    			 + ", Name: " + p.getFirstName() + " " + p.getSurname()
	    			 + ", D.o.B: " + p.getDateOfBirth()
	    			 + ", Squad: " + p.getSquad();
	    	Log.d("Player info: ", log);
	    }
	}
}
