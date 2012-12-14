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
                + KEY_DATE_OF_INJURY + " INTEGER,"
                + KEY_DATE_OF_RETURN + " INTEGER," 
                + KEY_BODYPART_HEAD + " INTEGER," 
                + KEY_BODYPART_SHOULDER + " INTEGER," 
                + KEY_BODYPART_HIP + " INTEGER," 
                + KEY_BODYPART_NECK + " INTEGER," 
                + KEY_BODYPART_UPPERARM + " INTEGER," 
                + KEY_BODYPART_THIGH + " INTEGER," 
                + KEY_BODYPART_STERNUM + " INTEGER," 
                + KEY_BODYPART_ELBOW + " INTEGER," 
                + KEY_BODYPART_KNEE + " INTEGER," 
                + KEY_BODYPART_ABDOMEN + " INTEGER," 
                + KEY_BODYPART_FOREARM + " INTEGER," 
                + KEY_BODYPART_LOWERLEG + " INTEGER," 
                + KEY_BODYPART_LOWBACK + " INTEGER," 
                + KEY_BODYPART_WRIST + " INTEGER,"
                + KEY_BODYPART_ANKLE + " INTEGER," 
                + KEY_BODYPART_HAND + " INTEGER," 
                + KEY_BODYPART_FOOT + " INTEGER," 
                + KEY_INJURED_BODY_PART + " TEXT"
                + KEY_TYPE_CONCUSSION + " INTEGER,"
                + KEY_TYPE_LESION + " INTEGER,"
                + KEY_TYPE_HAEMATOMA + " INTEGER,"
                + KEY_TYPE_FRACTURE + " INTEGER,"
                + KEY_TYPE_MUSCLE + " INTEGER,"
                + KEY_TYPE_ABRASION + " INTEGER,"
                + KEY_TYPE_OTHERBONE + " INTEGER,"
                + KEY_TYPE_LACERATION + " INTEGER,"
                + KEY_TYPE_DISLOCATION + " INTEGER,"
                + KEY_TYPE_TENDON + " INTEGER,"
                + KEY_TYPE_NERVE + " INTEGER,"
                + KEY_TYPE_SPRAIN + " INTEGER,"
                + KEY_TYPE_DENTAL + " INTEGER,"
                + KEY_TYPE_OTHER + " TEXT,"
				+ KEY_DIAGNOSIS + " TEXT"
				+ KEY_PREVIOUS + " INTEGER,"
				+ KEY_PREVIOUS_DATE + " INTEGER,"
				+ KEY_OVERUSE_TRAUMA + " INTEGER,"
				+ KEY_CONTACT_NO + " INTEGER,"
				+ KEY_CONTACT_BALL + " INTEGER,"
				+ KEY_CONTACT_PLAYER + " INTEGER,"
				+ KEY_CONTACT_OTHER + " TEXT,"
				+ KEY_REFEREE + " INTEGER,"
				+ KEY_SANCTION_PLAYER + " INTEGER,"
				+ KEY_SANCTION_OPPONENT + " INTEGER,"
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