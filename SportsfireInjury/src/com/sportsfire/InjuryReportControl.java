package com.sportsfire;

import com.sportsfire.db.InjuryTable;
import com.sportsfire.sportsfireinjury.R;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class InjuryReportControl implements Parcelable{
    
    // creates a new injury report for Player p
    public InjuryReportControl(Player p){
        Log.e("### Creating new Injury Report", "...");
   
    }
    
    // loads an injury report with ID == formID
    public InjuryReportControl(InjuryReportID formID){
        Log.e("###  Loading existing injury report", "...");

    }
    
    public void saveForm(){
        
    }
	private String idToField(int id){
		switch(id){
		case R.id.ir1a:
			return InjuryTable.KEY_DATE_OF_INJURY;
		case R.id.ir1b:
			return InjuryTable.KEY_DATE_OF_RETURN;
		case R.id.ir2a1:
			return InjuryTable.KEY_BODYPART_HEAD;
		case R.id.ir2a2:
			return InjuryTable.KEY_BODYPART_SHOULDER;
		case R.id.ir2a3:
			return InjuryTable.KEY_BODYPART_HIP;
		case R.id.ir2a4:
			return InjuryTable.KEY_BODYPART_NECK;
		case R.id.ir2a5:
			return InjuryTable.KEY_BODYPART_UPPERARM;
		case R.id.ir2a6:
			return InjuryTable.KEY_BODYPART_THIGH;
		case R.id.ir2a7:
			return InjuryTable.KEY_BODYPART_STERNUM;
		case R.id.ir2a8:
			return InjuryTable.KEY_BODYPART_ELBOW;
		case R.id.ir2a9:
			return InjuryTable.KEY_BODYPART_KNEE;
		case R.id.ir2a10:
			return InjuryTable.KEY_BODYPART_ABDOMEN;
		case R.id.ir2a11:
			return InjuryTable.KEY_BODYPART_FOREARM;
		case R.id.ir2a12:
			return InjuryTable.KEY_BODYPART_LOWERLEG;
		case R.id.ir2a13:
			return InjuryTable.KEY_BODYPART_LOWBACK;
		case R.id.ir2a14:
			return InjuryTable.KEY_BODYPART_WRIST;
		case R.id.ir2a15:
			return InjuryTable.KEY_BODYPART_ANKLE;
		case R.id.ir2a16:
			return InjuryTable.KEY_BODYPART_HAND;
		case R.id.ir2a17:
			return InjuryTable.KEY_BODYPART_FOOT;
		case R.id.ir31:
			return InjuryTable.KEY_TYPE_CONCUSSION;
		case R.id.ir32:
			return InjuryTable.KEY_TYPE_LESION;
		case R.id.ir33:
			return InjuryTable.KEY_TYPE_HAEMATOMA;
		case R.id.ir34:
			return InjuryTable.KEY_TYPE_FRACTURE;
		case R.id.ir35:
			return InjuryTable.KEY_TYPE_MUSCLE;
		case R.id.ir36:
			return InjuryTable.KEY_TYPE_ABRASION;
		case R.id.ir37:
			return InjuryTable.KEY_TYPE_OTHERBONE;
		case R.id.ir38:
			return InjuryTable.KEY_TYPE_LACERATION;
		case R.id.ir39:
			return InjuryTable.KEY_TYPE_DISLOCATION;
		case R.id.ir310:
			return InjuryTable.KEY_TYPE_TENDON;
		case R.id.ir311:
			return InjuryTable.KEY_TYPE_NERVE;
		case R.id.ir312:
			return InjuryTable.KEY_TYPE_SPRAIN;
		case R.id.ir313:
			return InjuryTable.KEY_TYPE_DENTAL;
		default:
			return "";
		}		
	}
    // maybe replace field name String with enum?
    public InjuryReportValue getValue(String field){
        return new InjuryReportValueBoolean(false);
    }
    
   /* public void setValue(String field,InjuryReportValue value){
    	if(((InjuryReportValueBoolean)value).value == true)
    		Log.e("checked", field);
    	else
    		Log.e("unchecked", field);
    }*/
    public void setValue(int id,InjuryReportValue value){
    	String field = idToField(id);
    	if(((InjuryReportValueBoolean)value).value == true)
    		Log.e("checked", field);
    	else
    		Log.e("unchecked", field);
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