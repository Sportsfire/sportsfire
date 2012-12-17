package com.sportsfire;

import android.os.Parcel;
import android.os.Parcelable;

public class InjuryReportID extends InjuryReportValue implements Parcelable{
	String id;
    String name;
    
    public InjuryReportID(String _id, String _name){
        id = _id;
        name = _name;
    }
    
    public InjuryReportID(Parcel in){
    	readFromParcel(in);
    }
    
    public String getID(){
        return id;
    }
    
    public String getName(){
        return name;
    }

    public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(name);
	}
	
	private void readFromParcel(Parcel in){
		id = in.readString();
		name = in.readString();
	}
	
   public static final Parcelable.Creator CREATOR =
   	new Parcelable.Creator() {
           public InjuryReportID createFromParcel(Parcel in) {
               return new InjuryReportID(in);
           }

           public InjuryReportID[] newArray(int size) {
               return new InjuryReportID[size];
           }
       };

@Override
public int describeContents() {
	// TODO Auto-generated method stub
	return 0;
}
}