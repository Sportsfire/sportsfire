package com.sportsfire;
import java.util.ArrayList;

import android.content.Context;

public class Season{
	private SquadList squadList;
    private ArrayList<String> weekList = new ArrayList<String>();
    private String name;
    private String id;
    public Season(String _name, String _id,Context context){
        name = _name;
        id = _id;
        squadList = new SquadList(context);
        for (int i = 1; i <= 52; i++) {
			weekList.add("Week " + Integer.toString(i));
		}     
    }
    public SquadList getSquadList(){
        return squadList;
    }
    
    public ArrayList<String> getWeeklist(){
        return weekList;
    }
    
    public String getSeasonName(){
        return name;
    }

    public String getSeasonID(){
        return id;
    }

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
}