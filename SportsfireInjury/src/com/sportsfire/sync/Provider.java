package com.sportsfire.sync;

import com.sportsfire.db.*;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class Provider extends ContentProvider {
	private DBHelper db;
	
	private static String AUTHORITY = "com.sportsfire.sync.Provider";
	public static final int PLAYERS = 100;
	public static final int PLAYERS_ID = 110;
	public static final int SQUADS = 200;
	public static final int SQUADS_ID = 210;
	public static final int INJURIES = 300;
	public static final int INJURIES_ID = 310;
	private static final String BASEPATH = "content://" + AUTHORITY + "/"; 
	private static final String PLAYERS_BASE_PATH = "players";
	private static final String SQUADS_BASE_PATH = "squads";
	private static final String INJURIES_BASE_PATH = "injuries";
	public static final Uri CONTENT_URI_PLAYERS = Uri.parse(BASEPATH + PLAYERS_BASE_PATH);
	public static final Uri CONTENT_URI_SQUADS = Uri.parse(BASEPATH + SQUADS_BASE_PATH);
	public static final Uri CONTENT_URI_INJURIES = Uri.parse(BASEPATH + INJURIES_BASE_PATH);
	public static final String CONTENT_TYPE_PLAYERS = ContentResolver.CURSOR_DIR_BASE_TYPE
	            + "/type-player";
	public static final String CONTENT_TYPE_SQUADS = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/type-squad";
	public static final String CONTENT_TYPE_INJURIES = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/type-injury";
	

	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static{
		sURIMatcher.addURI(AUTHORITY, PLAYERS_BASE_PATH, PLAYERS);
		sURIMatcher.addURI(AUTHORITY, PLAYERS_BASE_PATH + "/#", PLAYERS_ID);
		sURIMatcher.addURI(AUTHORITY, SQUADS_BASE_PATH, SQUADS);
		sURIMatcher.addURI(AUTHORITY, SQUADS_BASE_PATH + "/#", SQUADS_ID);
		sURIMatcher.addURI(AUTHORITY, INJURIES_BASE_PATH, INJURIES);
		sURIMatcher.addURI(AUTHORITY, INJURIES_BASE_PATH + "/*", INJURIES_ID);
	}
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int rowsAffected = 0;
		SQLiteDatabase sqldb = db.getWritableDatabase();
		int uriType = sURIMatcher.match(uri);
		String id;
		switch(uriType){
		case PLAYERS_ID:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)){
				rowsAffected = sqldb.delete(PlayerTable.TABLE_NAME, PlayerTable.KEY_PLAYER_ID + "=" + id, null);
			} else{
				rowsAffected = sqldb.delete(PlayerTable.TABLE_NAME,selection + "and" + PlayerTable.KEY_PLAYER_ID + "=" + id, selectionArgs);
			}
			break;
		case PLAYERS:
			rowsAffected = sqldb.delete(PlayerTable.TABLE_NAME, selection, selectionArgs);
			break;
		case SQUADS_ID:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)){
				rowsAffected = sqldb.delete(SquadTable.TABLE_NAME, SquadTable.KEY_SQUAD_ID + "=" + id, null);
			} else{
				rowsAffected = sqldb.delete(SquadTable.TABLE_NAME,selection + "and" + SquadTable.KEY_SQUAD_ID + "=" + id, selectionArgs);
			}
			break;
		case SQUADS:
			rowsAffected = sqldb.delete(SquadTable.TABLE_NAME, selection, selectionArgs);
			break;
		case INJURIES_ID:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)){
				rowsAffected = sqldb.delete(InjuryTable.TABLE_NAME, InjuryTable.KEY_INJURY_ID + "=" + id, null);
			} else{
				rowsAffected = sqldb.delete(InjuryTable.TABLE_NAME,selection + "and" + InjuryTable.KEY_INJURY_ID + "=" + id, selectionArgs);
			}
			break;
		case INJURIES:
			rowsAffected = sqldb.delete(InjuryTable.TABLE_NAME, selection, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unkown URI");
		}
		
		getContext().getContentResolver().notifyChange(uri,null);
		return rowsAffected;
	}

	@Override
	public String getType(Uri uri) {
		int uriType = sURIMatcher.match(uri);
		switch(uriType){
		case PLAYERS:
			return CONTENT_TYPE_PLAYERS;
		case PLAYERS_ID:
			return CONTENT_TYPE_PLAYERS;
		case SQUADS:
			return CONTENT_TYPE_SQUADS;
		case SQUADS_ID:
			return CONTENT_TYPE_SQUADS;
		case INJURIES:
			return CONTENT_TYPE_INJURIES;
		case INJURIES_ID:
			return CONTENT_TYPE_INJURIES;
		default:
			return null;
				
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
        if ((uriType != PLAYERS) && (uriType != SQUADS) && (uriType != INJURIES)) {
            throw new IllegalArgumentException("Invalid URI for insert");
        }
        SQLiteDatabase sqldb = db.getWritableDatabase();
        long newID = 0;
        if(uriType == PLAYERS){
        	newID = sqldb.insert(PlayerTable.TABLE_NAME, null, values);
        } else if(uriType == SQUADS){
        	newID = sqldb.insert(SquadTable.TABLE_NAME, null, values);
        } else if(uriType == INJURIES){
        	newID = sqldb.insert(InjuryTable.TABLE_NAME, null, values);
        }
        if (newID > 0) {
            Uri newUri = ContentUris.withAppendedId(uri, newID);
            getContext().getContentResolver().notifyChange(uri, null);
            return newUri;
        } else {
            throw new SQLException("Failed to insert row into " + uri);
        }
	}

	@Override
	public boolean onCreate() {
		db = new DBHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, 
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		int uriType = sURIMatcher.match(uri);
		switch(uriType){
		case PLAYERS_ID:
			queryBuilder.setTables(PlayerTable.TABLE_NAME);
			queryBuilder.appendWhere(PlayerTable.KEY_PLAYER_ID + "=" + uri.getLastPathSegment());
			break;
		case PLAYERS:
			queryBuilder.setTables(PlayerTable.TABLE_NAME);
			// no filter
			break;
		case SQUADS_ID:
			queryBuilder.setTables(SquadTable.TABLE_NAME);
			queryBuilder.appendWhere(SquadTable.KEY_SQUAD_ID + "=" + uri.getLastPathSegment());
			break;
		case SQUADS:
			queryBuilder.setTables(SquadTable.TABLE_NAME);
			// no filter
			break;
		case INJURIES_ID:
			queryBuilder.setTables(InjuryTable.TABLE_NAME);
			queryBuilder.appendWhere(InjuryTable.KEY_INJURY_ID + "=" + uri.getLastPathSegment());
			break;
		case INJURIES:
			queryBuilder.setTables(InjuryTable.TABLE_NAME);
			// no filter
			break;
		default:
			throw new IllegalArgumentException("Unkown URI");
		}
		
		Cursor cursor = queryBuilder.query(db.getReadableDatabase(),projection,selection,selectionArgs,null,null,sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqldb = db.getWritableDatabase();

        int rowsAffected;
        String tableid = "";
        String tablename = "";
        switch(uriType){
        case PLAYERS:
        case PLAYERS_ID:
        	tableid = PlayerTable.KEY_PLAYER_ID;
        	tablename = PlayerTable.TABLE_NAME;
        	break;
        case SQUADS:
        case SQUADS_ID:
        	tableid = SquadTable.KEY_SQUAD_ID;
        	tablename = SquadTable.TABLE_NAME;
        	break;
        case INJURIES:
        case INJURIES_ID:
        	tableid = InjuryTable.KEY_INJURY_ID;
        	tablename = InjuryTable.TABLE_NAME;
        	break;
        default:
            throw new IllegalArgumentException("Unknown URI");
        }

        switch (uriType) {
        case PLAYERS_ID:
        case SQUADS_ID:
        case INJURIES_ID:
            String id = uri.getLastPathSegment();
            StringBuilder modSelection = new StringBuilder(tableid
                    + "= '" + id + "'");

            if (!TextUtils.isEmpty(selection)) {
                modSelection.append(" AND " + selection);
            }

            rowsAffected = sqldb.update(tablename,
                    values, modSelection.toString(), null);
            break;
        case PLAYERS:
        case SQUADS:
        case INJURIES:
            rowsAffected = sqldb.update(tablename,
                    values, selection, selectionArgs);
            break;
        default:
            throw new IllegalArgumentException("Unknown URI");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsAffected;
	}

}
