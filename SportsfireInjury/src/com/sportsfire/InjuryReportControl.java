package com.sportsfire;

<<<<<<< HEAD
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.sportsfire.db.DBHelper;
import com.sportsfire.db.InjuryTable;
import com.sportsfire.db.PlayerTable;
import com.sportsfire.db.SquadTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
=======
import com.sportsfire.db.InjuryTable;
import com.sportsfire.sportsfireinjury.R;

>>>>>>> fab1d6cf3062df8a0b9b0084c368fb0db33a92aa
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class InjuryReportControl{
	private HashMap<String,String> changedData = new HashMap<String,String>();
	private HashMap<String,String> oldData = new HashMap<String,String>();
	private Context context;
	private String playerID;
	private String injuryID;
	boolean newReport;
    
    // creates a new injury report for Player p
    public InjuryReportControl(Player p,Context c){
        Log.e("### Creating new Injury Report", "...");
        newReport = true;
        playerID = p.getID();
        context = c;
    }
    
    // loads an injury report with ID == formID
    public InjuryReportControl(InjuryReportID formID,Context c){
        Log.e("###  Loading existing injury report", "...");
        newReport = false;
        context = c;
        playerID = ""; // here we need to set this from DB later
        
        injuryID = formID.getID();
        
     // open db for read / write
    	DBHelper dbHelp = new DBHelper(context);
        SQLiteDatabase db = dbHelp.getReadableDatabase();
        
        String selectSquadData = "SELECT  * FROM " + InjuryTable.TABLE_NAME + " WHERE "+InjuryTable.KEY_INJURY_ID + " = "+ injuryID+";";
        Cursor cursor = db.rawQuery(selectSquadData, null);
        if (cursor.moveToFirst()) {
            do {
            	for(int i=0; i<cursor.getColumnCount();i++){
            		oldData.put(cursor.getColumnName(i), cursor.getString(i));
            	}
            	
            } while (cursor.moveToNext());
        }
        dbHelp.close();
    }
    
    private void createNewReport(SQLiteDatabase db){
    
        
        // Open for Read-Only
        //db = dBHelper.getReadableDatabase();

    	
    	
        ContentValues values = new ContentValues();
        // No need to include squad id, is automatically added
        values.put(InjuryTable.KEY_PLAYER_ID, playerID);
        Log.e("### Adding a new injury", "...");
        long rowID = db.insert(InjuryTable.TABLE_NAME, null, values);
        
        /*String getInjuryID = "SELECT * FROM " + InjuryTable.TABLE_NAME + " WHERE "+InjuryTable.KEY_PLAYER_ID+" = '"+playerID+"';";
        Cursor cursor = db.rawQuery(getInjuryID, null);
        if (cursor.moveToFirst()) {
             injuryID = cursor.getString(0);
        }*/
        injuryID = String.valueOf(rowID);
        newReport = false;
    }
    
    public void saveForm(){
    	// open db for read / write
    	DBHelper dbHelp = new DBHelper(context);
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        
    	if(newReport == true){
    		createNewReport(db);
    	}
    	
    	// process changes
    	ContentValues values = new ContentValues();
    	Iterator it = changedData.entrySet().iterator();
    	while(it.hasNext()){
    		Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
    		
    			values.put(pairs.getKey(),pairs.getValue());
    		
    	}
    	db.update(InjuryTable.TABLE_NAME, values, InjuryTable.KEY_INJURY_ID + " = '"+injuryID+"'",null);    	
    }
    
    private String getStandardValue(String field){
    	if(field.equals(InjuryTable.KEY_DATE_OF_INJURY) || 
    			field.equals(InjuryTable.KEY_DATE_OF_RETURN) || 
    			field.equals(InjuryTable.KEY_PREVIOUS_DATE) || 
    			field.equals(InjuryTable.KEY_DIAGNOSIS) || 
    			field.equals(InjuryTable.KEY_INJURED_BODY_PART)){
    		return "";
    	}
    	return "0";
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
    public String getValue(String field){
        String result = changedData.get(field);
        
        if(result != null)
        	return result;
       
        if(newReport){
        	return getStandardValue(field);
        }
        
        result = oldData.get(field);
       
        if(result == null){
        	result = "";
        	Log.e("getValueError",field);
        }
        
        return result;    
    }
    
<<<<<<< HEAD
    public void setValue(String field,String value){
    	changedData.put(field, value);
=======
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
>>>>>>> fab1d6cf3062df8a0b9b0084c368fb0db33a92aa
    }
    
    
}