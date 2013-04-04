package com.spongesoft.bananarun;


import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManagement {
	/**
	 * Fields
	 */
	// ---------- Database params and declarations ----------
	// DB name and tables
	private static final String DATABASE_NAME = "RunningDB";
	private static final String DATABASE_SESSION_TABLE = "sessionTable";
	private static final String DATABASE_LOCATION_TABLE = "locationTable";
	private static final int DATABASE_VERSION = 1;
	// Other
	private SQLiteDatabase ourDB;
	private DbHelper ourHelper;
	private final Context ourContext;

	// ---------- Table field declarations ----------
	// Table 1 - Sessions (Stores all sessions with their average stats)
	public static final String KEY_S_RACEID = "_id";
	public static final String KEY_S_DATE = "date";
	public static final String KEY_S_AVG_SPEED = "average_speed"; //In m/s
	public static final String KEY_S_TOTAL_TIME = "total_time";	//In seconds
	public static final String KEY_S_TOTAL_DISTANCE = "total_distance"; //In meters
	public static final String KEY_S_AVG_TIME_PER_KM = "average_time_per_km";
	public static final String KEY_S_KCAL = "kcal";

	// Table 2 - Points (records all points passed by GPS)
	public static final String KEY_L_LOCATIONID = "_id";
	public static final String KEY_L_RACEID = "race_id";
	public static final String KEY_L_TIMESTAMP = "timestamp";
	public static final String KEY_L_LATITUDE = "latitude";
	public static final String KEY_L_LONGITUDE = "longitude";
	public static final String KEY_L_ALTITUDE = "altitude";

	/**
	 * DbHelper class, creates a simple way to add and remove data from the
	 * database.
	 */
	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}
		/**
		 * onCreate functions creates both tables.
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + DATABASE_SESSION_TABLE + " ("
					+ KEY_S_RACEID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ KEY_S_DATE + " TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," 
					+ KEY_S_AVG_SPEED + " INT, " 
					+ KEY_S_TOTAL_DISTANCE + " INT," 
					+ KEY_S_TOTAL_DISTANCE + " INT,"
					+ KEY_S_AVG_TIME_PER_KM + " INT,"
					+ KEY_S_KCAL + " INT" 
					+ ");");
		
			db.execSQL("CREATE TABLE " + DATABASE_LOCATION_TABLE + " ("
					+ KEY_L_LOCATIONID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ KEY_L_TIMESTAMP + " TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," 
					+ KEY_L_LATITUDE + " INT, " 
					+ KEY_L_LONGITUDE + " INT," 
					+ KEY_L_ALTITUDE + " INT"
					+ ");");
	}
		/**
		 * OnUpgrade function, set to delete old tables on upgrade.
		 * To be modified further.
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_SESSION_TABLE);
			onCreate(db);
		}
	}

	/**
	 * Basic constructor.
	 * @param c: Context to be initialized
	 */
	public DBManagement(Context c) {
		ourContext = c;
	}

	/**
	 * Open function. Initializes the Database based on the context init'd
	 * in the constructor.
	 * @return The initialized DB class
	 * @throws SQLException
	 */
	public DBManagement open() throws SQLException {
		ourHelper = new DbHelper(ourContext);
		ourDB = ourHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Close function. No further comment necessary.
	 */
	public void close() {
		ourHelper.close();
	}

	/**
	 * setRace. Creates a new race from scratch.
	 * To do this, we need to add the only preliminar value: date,
	 * which is auto-generated, so no need for params.
	 * @return the last inserted id.
	 */
	@SuppressLint("SimpleDateFormat")
	public long setRace() {
		// TODO Auto-generated method stub
		// set the format to sql date time, obtained from http://stackoverflow.com/a/819605/1197418
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date date = new Date();
		ContentValues cv = new ContentValues();
		cv.put(KEY_S_DATE, dateFormat.format(date));
		return ourDB.insert(DATABASE_SESSION_TABLE, null, cv);
	}
	
	// TODO: Function COMPLETE RACE

	public String getData() {
		// TODO Auto-generated method stub
		String[] columns = new String[] { KEY_RACEID, KEY_NAME, KEY_HOTNESS };
		Cursor c = ourDB.query(DATABASE_SESSION_TABLE, columns, null, null,
				null, null, null);
		String result = "";

		int iRow = c.getColumnIndex(KEY_RACEID);
		int iName = c.getColumnIndex(KEY_NAME);
		int iHotness = c.getColumnIndex(KEY_HOTNESS);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result = result + c.getString(iRow) + " " + c.getString(iName)
					+ " " + c.getString(iHotness) + "\n";
		}

		return result;
	}

	public String getName(long l) throws SQLException {
		// TODO Auto-generated method stub
		String[] columns = new String[] { KEY_RACEID, KEY_NAME, KEY_HOTNESS };
		Cursor c = ourDB.query(DATABASE_SESSION_TABLE, columns, KEY_RACEID
				+ "=" + l, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
			String name = c.getString(1);
			return name;
		}
		return null;
	}

	public String getHotness(long l) throws SQLException {
		// TODO Auto-generated method stub
		String[] columns = new String[] { KEY_RACEID, KEY_NAME, KEY_HOTNESS };
		Cursor c = ourDB.query(DATABASE_SESSION_TABLE, columns, KEY_RACEID
				+ "=" + l, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
			String hotness = c.getString(2);
			return hotness;
		}
		return null;
	}

	public void updateEntry(long lRow, String mName, String mHotness)
			throws SQLException {
		// TODO Auto-generated method stub
		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put(KEY_NAME, mName);
		cvUpdate.put(KEY_HOTNESS, mHotness);
		ourDB.update(DATABASE_SESSION_TABLE, cvUpdate, KEY_RACEID + "=" + lRow,
				null);
	}

	public void deleteEntry(long lRow1) throws SQLException {
		// TODO Auto-generated method stub
		ourDB.delete(DATABASE_SESSION_TABLE, KEY_RACEID + "=" + lRow1, null);
	}
}
