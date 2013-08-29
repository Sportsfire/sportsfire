package com.sportsfire.objects;
import java.util.ArrayList;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class Season implements Parcelable{
    private ArrayList<String> weekList = new ArrayList<String>();
    private String name;
    private String id;
    private String startDate;
    
    public Season(String _name, String _id, String _start){
        name = _name;
        id = _id;
        startDate = _start;
        for (int i = 1; i <= 52; i++) {
			weekList.add("Week " + Integer.toString(i));
		}     
    }
    
    public Season(Parcel in) {
		readFromParcel(in);
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

    public String getStartDate(){
    	return startDate;
    }
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static final Parcelable.Creator<Season> CREATOR = new Parcelable.Creator<Season>(){
		 public Season createFromParcel(Parcel in) {  
	            return new Season(in);  
	        }  
	   
	        public Season[] newArray(int size) {  
	            return new Season[size];  
	        }  
	};
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(id);
		dest.writeString(startDate);
	}
	
	public void readFromParcel(Parcel in){
		name = in.readString();
		id = in.readString();
		startDate = in.readString();
	}
}