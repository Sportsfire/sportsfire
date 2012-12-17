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
    public static final String KEY_BODYPART_HEAD = "bodypartHead";
    public static final String KEY_BODYPART_SHOULDER = "bodypartShoulder";
    public static final String KEY_BODYPART_HIP = "bodypartHip";
    public static final String KEY_BODYPART_NECK = "bodypartNeck";
    public static final String KEY_BODYPART_UPPERARM = "bodypartUpperarm";
    public static final String KEY_BODYPART_THIGH = "bodypartThigh";
    public static final String KEY_BODYPART_STERNUM = "bodypartSternum";
    public static final String KEY_BODYPART_ELBOW = "bodypartElbow";
    public static final String KEY_BODYPART_KNEE = "bodypartKnee";
    public static final String KEY_BODYPART_ABDOMEN = "bodypartAbdomen";
    public static final String KEY_BODYPART_FOREARM = "bodypartForearm";
    public static final String KEY_BODYPART_LOWERLEG = "bodypartLowerleg";
    public static final String KEY_BODYPART_LOWBACK = "bodypartLowback";
    public static final String KEY_BODYPART_WRIST = "bodypartWrist";
    public static final String KEY_BODYPART_ANKLE = "bodypartAnkle";
    public static final String KEY_BODYPART_HAND = "bodypartHand";
    public static final String KEY_BODYPART_FOOT = "bodypartFoot";
    public static final String KEY_INJURED_BODY_PART = "injuredBodyPart";
    public static final String KEY_TYPE_CONCUSSION = "typeConcussion";
    public static final String KEY_TYPE_LESION = "typeLesion";
    public static final String KEY_TYPE_HAEMATOMA = "typeHaematoma";
    public static final String KEY_TYPE_FRACTURE = "typeFracture";
    public static final String KEY_TYPE_MUSCLE = "typeMuscle";
    public static final String KEY_TYPE_ABRASION = "typeAbrasion";
    public static final String KEY_TYPE_OTHERBONE = "typeOtherbone";
    public static final String KEY_TYPE_LACERATION = "typeLaceration";
    public static final String KEY_TYPE_DISLOCATION = "typeDislocation";
    public static final String KEY_TYPE_TENDON = "typeTendon";
    public static final String KEY_TYPE_NERVE = "typeNerve";
    public static final String KEY_TYPE_SPRAIN = "typeSprain";
    public static final String KEY_TYPE_DENTAL = "typeDental";
    public static final String KEY_TYPE_OTHER = "typeOther";
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
				+ KEY_INJURY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DATE_OF_INJURY + " TEXT DEFAULT '',"
                + KEY_DATE_OF_RETURN + " TEXT DEFAULT ''," 
                + KEY_BODYPART_HEAD + " TEXT DEFAULT '0'," 
                + KEY_BODYPART_SHOULDER + " TEXT DEFAULT '0'," 
                + KEY_BODYPART_HIP + " TEXT DEFAULT '0'," 
                + KEY_BODYPART_NECK + " TEXT DEFAULT '0'," 
                + KEY_BODYPART_UPPERARM + " TEXT DEFAULT '0'," 
                + KEY_BODYPART_THIGH + " TEXT DEFAULT '0'," 
                + KEY_BODYPART_STERNUM + " TEXT DEFAULT '0'," 
                + KEY_BODYPART_ELBOW + " TEXT DEFAULT '0'," 
                + KEY_BODYPART_KNEE + " TEXT DEFAULT '0'," 
                + KEY_BODYPART_ABDOMEN + " TEXT DEFAULT '0'," 
                + KEY_BODYPART_FOREARM + " TEXT DEFAULT '0'," 
                + KEY_BODYPART_LOWERLEG + " TEXT DEFAULT '0'," 
                + KEY_BODYPART_LOWBACK + " TEXT DEFAULT '0'," 
                + KEY_BODYPART_WRIST + " TEXT DEFAULT '0',"
                + KEY_BODYPART_ANKLE + " TEXT DEFAULT '0'," 
                + KEY_BODYPART_HAND + " TEXT DEFAULT '0'," 
                + KEY_BODYPART_FOOT + " TEXT DEFAULT '0'," 
                + KEY_INJURED_BODY_PART + " TEXT DEFAULT '',"
                + KEY_TYPE_CONCUSSION + " TEXT DEFAULT '0',"
                + KEY_TYPE_LESION + " TEXT DEFAULT '0',"
                + KEY_TYPE_HAEMATOMA + " TEXT DEFAULT '0',"
                + KEY_TYPE_FRACTURE + " TEXT DEFAULT '0',"
                + KEY_TYPE_MUSCLE + " TEXT DEFAULT '0',"
                + KEY_TYPE_ABRASION + " TEXT DEFAULT '0',"
                + KEY_TYPE_OTHERBONE + " TEXT DEFAULT '0',"
                + KEY_TYPE_LACERATION + " TEXT DEFAULT '0',"
                + KEY_TYPE_DISLOCATION + " TEXT DEFAULT '0',"
                + KEY_TYPE_TENDON + " TEXT DEFAULT '0',"
                + KEY_TYPE_NERVE + " TEXT DEFAULT '0',"
                + KEY_TYPE_SPRAIN + " TEXT DEFAULT '0',"
                + KEY_TYPE_DENTAL + " TEXT DEFAULT '0',"
                + KEY_TYPE_OTHER + " TEXT DEFAULT '0',"
				+ KEY_DIAGNOSIS + " TEXT DEFAULT '',"
				+ KEY_PREVIOUS + " TEXT DEFAULT '0',"
				+ KEY_PREVIOUS_DATE + " TEXT DEFAULT '',"
				+ KEY_OVERUSE_TRAUMA + " TEXT DEFAULT '0',"
				+ KEY_CONTACT_NO + " TEXT DEFAULT '0',"
				+ KEY_CONTACT_BALL + " TEXT DEFAULT '0',"
				+ KEY_CONTACT_PLAYER + " TEXT DEFAULT '0',"
				+ KEY_CONTACT_OTHER + " TEXT DEFAULT '0',"
				+ KEY_REFEREE + " TEXT DEFAULT '0',"
				+ KEY_SANCTION_PLAYER + " TEXT DEFAULT '0',"
				+ KEY_SANCTION_OPPONENT + " TEXT DEFAULT '0',"
				+ KEY_PLAYER_ID + " INTEGER NOT NULL,"
				+ "FOREIGN KEY("+ KEY_PLAYER_ID +") REFERENCES " + PlayerTable.TABLE_NAME + "(" + PlayerTable.KEY_PLAYER_ID + ")"
				+ ")";
        
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