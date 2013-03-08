package com.sportsfire.db;

import android.database.sqlite.*;
import android.util.Log;

public class InjuryTable {
	
    // Table names
	public static final String TABLE_NAME = "injuries";
 
	// Injury Table Keys
    public static final String KEY_INJURY_ID = "_id"; // Primary key
	public static final String KEY_DATE_OF_INJURY = "dateOfInjury"; 
    public static final String KEY_DATE_OF_RETURN = "dateOfReturn";
    public static final String KEY_INJURED_BODY_PART = "injuredBodyPart";
    public static final String KEY_ORCHARD = "orchardCode";
    public static final String KEY_DIAGNOSIS = "diagnosis";
    public static final String KEY_PREVIOUS = "previous";
    public static final String KEY_PREVIOUS_DATE = "previousDate";
    public static final String KEY_OVERUSE_TRAUMA = "overuseTrauma";
    public static final String KEY_TRAINING_MATCH = "trainingMatch";
    public static final String KEY_CONTACT_NO = "contactNo";
    public static final String KEY_CONTACT_PLAYER = "contactPlayer";
    public static final String KEY_CONTACT_BALL = "contactBall";
    public static final String KEY_CONTACT_OTHER = "contactOther";
    public static final String KEY_REFEREE = "referee";
    public static final String KEY_SANCTION_PLAYER = "sanctionPlayer";
    public static final String KEY_SANCTION_OPPONENT = "sanctionOpponent";
	public static final String KEY_PLAYER_ID = "playerID"; // Foreign key
	
    public static void onCreate(SQLiteDatabase db) {
		String createInjuryTable = "CREATE TABLE " + TABLE_NAME + "("
				+ KEY_INJURY_ID + " TEXT PRIMARY KEY,"
                + KEY_DATE_OF_INJURY + " TEXT DEFAULT '',"
                + KEY_DATE_OF_RETURN + " TEXT DEFAULT ''," 
                + KEY_ORCHARD + " TEXT DEFAULT '',"
                + KEY_INJURED_BODY_PART + " TEXT DEFAULT '2',"
				+ KEY_DIAGNOSIS + " TEXT DEFAULT '',"
				+ KEY_PREVIOUS + " TEXT DEFAULT '0',"
				+ KEY_PREVIOUS_DATE + " TEXT DEFAULT '',"
				+ KEY_OVERUSE_TRAUMA + " TEXT DEFAULT '',"
				+ KEY_TRAINING_MATCH + " TEXT DEFAULT '',"
				+ KEY_CONTACT_NO + " TEXT DEFAULT '0',"
				+ KEY_CONTACT_BALL + " TEXT DEFAULT '0',"
				+ KEY_CONTACT_PLAYER + " TEXT DEFAULT '0',"
				+ KEY_CONTACT_OTHER + " TEXT DEFAULT '0',"
				+ KEY_REFEREE + " TEXT DEFAULT '',"
				+ KEY_SANCTION_PLAYER + " TEXT DEFAULT '0',"
				+ KEY_SANCTION_OPPONENT + " TEXT DEFAULT '0',"
				+ KEY_PLAYER_ID + " INTEGER NOT NULL)";
        db.execSQL(createInjuryTable);
   }
 
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(InjuryTable.class.getName(), "Upgrading database from version "
		        + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
		
		// Drop older table if it exists
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
 
        // Create tables again
        onCreate(db);        
	}
}