package com.sportsfire;

import android.os.Parcel;
import android.os.Parcelable;

public class InjuryReportControl implements Parcelable{
    
    // creates a new injury report for Player p
    public InjuryReportControl(Player p){
        
    }
    
    // loads an injury report with ID == formID
    public InjuryReportControl(InjuryReportID formID){
        
    }
    
    public void saveForm(){
        
    }
    
    // maybe replace field name String with enum?
    public InjuryReportValue getValue(String field){
        return new InjuryReportValueBoolean(false);
    }
    
    public void setValue(String field,InjuryReportValue value){
        
    }

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		
	}
	 public static final Parcelable.Creator CREATOR =
		    	new Parcelable.Creator() {
		            public InjuryReportControl createFromParcel(Parcel in) {
		            	// TODO: Auto-generated method stub
		                return null;
		            }

					public Object[] newArray(int arg0) {
						// TODO Auto-generated method stub
						return null;
					}
		        };
}