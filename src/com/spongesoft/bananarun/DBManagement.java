package com.spongesoft.bananarun;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.FloatMath;
import android.util.Log;

public class DBManagement {
	
	/* Last locations */
	double lastLongitude = -99999;
	double lastLatitude = -99999;
	/**
	 * Fields
	 */
	// ---------- Database params and declarations ----------
	// DB name and tables
	private static final String DATABASE_NAME = "RunningDB.db";
	private static final String DATABASE_SESSION_TABLE = "sessionTable";
	private static final String DATABASE_LOCATION_TABLE = "locationTable";
	private static final int DATABASE_VERSION = 1;
	// Class declaration
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
	public static final String KEY_L_DISTANCE = "distance";
	public static final String KEY_L_SECONDS = "seconds";
	public static final String KEY_L_SPEED = "speed";

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
			//db.execSQL("DROP TABLE IF EXISTS " + DATABASE_SESSION_TABLE);
			db.execSQL("CREATE TABLE " + DATABASE_SESSION_TABLE + " ("
					+ KEY_S_RACEID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ KEY_S_DATE + " DOUBLE," 
					+ KEY_S_AVG_SPEED + " DOUBLE, " 
					+ KEY_S_TOTAL_DISTANCE + " DOUBLE," 
					+ KEY_S_TOTAL_TIME + " DOUBLE," 
					+ KEY_S_AVG_TIME_PER_KM + " DOUBLE,"
					+ KEY_S_KCAL + " DOUBLE" 
					+ ");");
		
			db.execSQL("CREATE TABLE " + DATABASE_LOCATION_TABLE + " ("
					+ KEY_L_LOCATIONID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ KEY_L_RACEID + " INT NOT NULL, "
					+ KEY_L_TIMESTAMP + " DOUBLE," 
					+ KEY_L_LATITUDE + " DOUBLE, " 
					+ KEY_L_LONGITUDE + " DOUBLE, " 
					+ KEY_L_ALTITUDE + " DOUBLE, "
					+ KEY_L_DISTANCE + " DOUBLE, "
					+ KEY_L_SECONDS + " DOUBLE, "
					+ KEY_L_SPEED + " DOUBLE "
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
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_LOCATION_TABLE);
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
	
	// -------------------- All setters --------------------

	/**
	 * setRace. Creates a new race from scratch. NEEDS to be updated later
	 * with function updateRace();
	 * To do this, we need to add the only preliminary value: date,
	 * which is auto-generated, so no need for params.
	 * @return the last inserted id.
	 */
	@SuppressLint("SimpleDateFormat")
	public long setRace() {
		// TODO Auto-generated method stub
		// set the format to sql date time, obtained from http://stackoverflow.com/a/819605/1197418

		ContentValues cv = new ContentValues();
		cv.put(KEY_S_DATE, System.currentTimeMillis());
		//cv.put(KEY_S_AVG_SPEED, 12);
		//cv.put(KEY_S_TOTAL_TIME, 23);
		//cv.put(KEY_S_TOTAL_DISTANCE, 34);
		cv.put(KEY_S_AVG_TIME_PER_KM, 5);
		cv.put(KEY_S_KCAL, 0);
		return ourDB.insert(DATABASE_SESSION_TABLE, null, cv);
	}
	
	/**
	 * Function to be called whenever you wish to add a new position.
	 * @param raceID: Race this point belongs to.
	 * @param latitude
	 * @param logitude
	 * @param altitude
	 * @param distance (if <0, calculates distance from last point)
	 * @return The row ID. Probably useless at this point.
	 */
	@SuppressLint("SimpleDateFormat")
	public long setLocation(long raceID, double latitude, double longitude, double altitude, double distance, double speed){
		// set the format to sql date time, obtained from http://stackoverflow.com/a/819605/1197418
		
		//if distance = -1, uses last point
		if(distance < 0) {
			if(lastLatitude == -99999 || lastLongitude == -99999) {
				distance = 0;
			}else{
				distance = gps2m((float) latitude, (float) longitude, (float) lastLatitude, (float) lastLongitude);
			}
		}
		
		//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		//Date date = new Date();
		ContentValues cv = new ContentValues();
		cv.put(KEY_L_RACEID, raceID);
		cv.put(KEY_L_TIMESTAMP, System.currentTimeMillis() );
		cv.put(KEY_L_LATITUDE, latitude);
		cv.put(KEY_L_LONGITUDE, longitude);
		cv.put(KEY_L_ALTITUDE, altitude);
		cv.put(KEY_L_DISTANCE, distance);
		//cv.put(KEY_L_SECONDS, seconds);
		cv.put(KEY_L_SPEED, speed);
		return ourDB.insert(DATABASE_LOCATION_TABLE, null, cv);
	}
	
	/**
	 * This function will update all static fields in the Session tables once
	 * the race is over. There are 5 fields to be updated:
	 * - Average Speed
	 * - Total Time
	 * - Total distance
	 * - Average time per km
	 * - Kcal burned
	 * @param raceID is the ID of the race we want to modify
	 * @return the ID of the row just modified
	 */
	public long updateRace(long raceID) {
		// TODO Auto-generated method stub

		ContentValues cv = getStats(raceID);
		return ourDB.update(DATABASE_SESSION_TABLE, cv, KEY_S_RACEID + "=" + raceID, null);
	}
	
	/** 
	 * Returns the contentValues for: Average speed, Time running, Distance travelled, Calories burned
	 */
	public ContentValues getStats(long raceID) {
		ContentValues cv = new ContentValues();
		
		// -------------- Average speed, Total time & distance --------------- //
		
		
		String query = "SELECT AVG(" + KEY_L_SPEED + "), MAX(" + KEY_L_TIMESTAMP + ")-MIN(" + KEY_L_TIMESTAMP + "), SUM(" + KEY_L_DISTANCE + ") FROM " + DATABASE_LOCATION_TABLE + " WHERE " + KEY_L_RACEID + " = " + raceID;
		Cursor cursor = ourDB.rawQuery(query, null);

		double totalTime = 1.0;
		double totalDist = 1.0;
		double avgSpeed = 0.0;

		if (cursor.moveToLast()) {
			avgSpeed =  cursor.getDouble(0);

			totalTime = cursor.getDouble(1);
			totalDist = cursor.getDouble(2);
		}
		
		cv.put(KEY_S_TOTAL_TIME, totalTime / 1000L);
		cv.put(KEY_S_TOTAL_DISTANCE, totalDist);
		cv.put(KEY_S_AVG_SPEED, avgSpeed);

		
		// --------------- Average time per KM --------------- //
		cv.put(KEY_S_AVG_TIME_PER_KM, (totalTime/totalDist));
		
		// --------------- Kcal --------------- //
		double weight = 73.5;
		double constFactor = 0.0175;
		double kcal = constFactor * totalDist * weight * totalTime;
		cv.put(KEY_S_KCAL, kcal);
		
		cursor.close();
		
		return cv;
	}

	
	// -------------------- All getters --------------------
	/**
	 * Returns the number of races
	 * @return then number of races
	 */
	public int getRaceCount(){
		int result = -1;
		
		SQLiteStatement s = ourDB.compileStatement("SELECT COUNT(*" + 
				") FROM " + DATABASE_SESSION_TABLE);

		result = (int) s.simpleQueryForLong();
		
		return result;
	}
	
	/**
	 * Returns the number of locations a given race has
	 * @return then number of locations
	 */
	public int getLocationCount(long raceID){
		int result = -1;
		
		SQLiteStatement s = ourDB.compileStatement("SELECT COUNT(*" + 
				") FROM " + DATABASE_LOCATION_TABLE + 
				" WHERE " + KEY_L_RACEID + "=" + raceID);

		result = (int) s.simpleQueryForLong();
		
		return result;
	}
	
	/**
	 * Returns the params of a specific race.
	 * As an Array!
	 * @param raceID: the ID of the race
	 * @param param: the parameter:
	 *      - 0: Distance
	 *      - 1: Speed
	 *      - 2: Altitude
	 * @return An array with that parmeter and timetamp
	 */
	public double[][] getRaceParam(long raceID, int param){
		// TODO Auto-generated method stub
		String columnToLookFor = "";
		int locationCount = getLocationCount(raceID);
		double[][] result = new double[locationCount][2];
		
		switch (param) {
			case 0:
				columnToLookFor = KEY_L_DISTANCE;
				break;
			case 1:
				columnToLookFor = KEY_L_SPEED;
				break;
			case 2:
				columnToLookFor = KEY_L_ALTITUDE;
				break;
			
			default:
				result[0][0] = -1;
				return result;
		}
		
		Cursor c = ourDB.rawQuery("SELECT " + columnToLookFor + " , " + KEY_L_TIMESTAMP + 
				" FROM " + DATABASE_LOCATION_TABLE +
				" WHERE " + KEY_L_RACEID + "= " + raceID, null);
		
		int a = 0;
		Log.d("DEBUG", "Start");
		if (c.moveToFirst()){
			while(!c.isAfterLast()){
				Double data = c.getDouble(c.getColumnIndex(columnToLookFor));
				Log.d("DEBUG", data.toString());
				result[a][0] = data;
				data = c.getDouble(c.getColumnIndex(KEY_L_TIMESTAMP));
				result[a][1] = data;
				a++;
				c.moveToNext();
			}
		}
		c.close();

		return result;
	}
	
	/**
	 * Returns the params of all races.
	 * As an Array!
	 * @param raceID: the ID of the race
	 * @param param: the parameter:
	 *      - 0: Time
	 *      - 1: Average Speed
	 *      - 2: Distance
	 *      - 3: Kcal
	 * @return An array with the parameter and the timestamps
	 */
	public double[][] getSessionsParam(int param){
		// TODO Auto-generated method stub
		String columnToLookFor = "";
		int raceCount = getRaceCount();
		
		if (raceCount == 0) {
			return null;
		}
		
		double[][] result = new double[raceCount][2];
		
		switch (param) {
			case 0:
				columnToLookFor = KEY_S_TOTAL_TIME;
				break;
			case 1:
				columnToLookFor = KEY_S_AVG_SPEED;
				break;
			case 2:
				columnToLookFor = KEY_S_TOTAL_DISTANCE;
				break;
			case 3:
				columnToLookFor = KEY_S_KCAL;
				break;
			
			default:
				return null;
		}
		
		Cursor c = ourDB.rawQuery("SELECT " + columnToLookFor + " , " + KEY_S_DATE + 
				" FROM " + DATABASE_SESSION_TABLE, null);
		
		int a = 0;
		if (c.moveToFirst()){
			while(!c.isAfterLast()){
				Double data = c.getDouble(c.getColumnIndex(columnToLookFor));
				result[a][0] = data;
				data = c.getDouble(c.getColumnIndex(KEY_S_DATE));
				result[a][1] = data;
				a++;
				c.moveToNext();
			}
		}
		c.close();

		return result;
	}
	
	/**
	 * Gets all races with IDs and Distances, used for listing.
	 * As an Array!
	 * @return An array with the parameter and the IDs
	 */
	public double[][] getSessionsIdsAndDistance(){
		// TODO Auto-generated method stub
		int raceCount = getRaceCount();
		if (raceCount == 0) {
			return null;
		}
		double[][] result = new double[raceCount][2];
		
		Cursor c = ourDB.rawQuery("SELECT " + KEY_S_RACEID + " , " + KEY_S_TOTAL_DISTANCE + 
				" FROM " + DATABASE_SESSION_TABLE, null);
		
		int a = 0;
		if (c.moveToFirst()){
			while(!c.isAfterLast()){
				Double data = c.getDouble(c.getColumnIndex(KEY_S_RACEID));
				result[a][0] = data;
				data = c.getDouble(c.getColumnIndex(KEY_S_TOTAL_DISTANCE));
				result[a][1] = data;
				a++;
				c.moveToNext();
			}
		}
		c.close();

		return result;
	}
		
	/**
	 * Deletes an entry based on the ID from the race.
	 * @param raceID
	 * @throws SQLException
	 */
	public void deleteRace(long raceID) throws SQLException {
		// TODO Auto-generated method stub
		ourDB.delete(DATABASE_SESSION_TABLE, KEY_S_RACEID + "=" + raceID, null);
		ourDB.delete(DATABASE_LOCATION_TABLE, KEY_L_RACEID + "=" + raceID, null);
	}

	public String getRace(long raceID, int param){
		// TODO Auto-generated method stub
		String columnToLookFor = "";
		switch (param) {
		case 0:
			columnToLookFor = KEY_S_AVG_SPEED;
			break;
		case 1:
			columnToLookFor = KEY_S_TOTAL_TIME;
			break;
		case 2:
			columnToLookFor = KEY_S_TOTAL_DISTANCE;
			break;
		case 3:
			columnToLookFor = KEY_S_AVG_TIME_PER_KM;
			break;
		case 4:
			columnToLookFor = KEY_S_KCAL;
			break;

		default:
			return "error, no parameter found";
			
		}
		String[] columns = new String[] { columnToLookFor };
		Cursor c = ourDB.query(DATABASE_SESSION_TABLE, columns, KEY_S_RACEID + "=" + raceID, null, null, null, null);
		String result = "";
		
		if (c.moveToFirst()){
			while(!c.isAfterLast()){
					String data = c.getString(c.getColumnIndex(columnToLookFor));
					result += data;
					c.moveToNext();
			}
		}
		c.close();

		return result;
	}
	
	/**
	 * Distance between two GPS coordinates (in meter)
	 * Obtained from http://www.androidsnippets.com/distance-between-two-gps-coordinates-in-meter
	 * @param lat_a Latitude of point a
	 * @param lng_a	Latitude of point a
	 * @param lat_b	Latitude of point b
	 * @param lng_b	Latitude of point b
	 * @return The distance from point a to point b (in meters)
	 */
	@SuppressLint("FloatMath")
	private double gps2m(float lat_a, float lng_a, float lat_b, float lng_b) {
	    float pk = (float) (180/3.14169);

	    float a1 = lat_a / pk;
	    float a2 = lng_a / pk;
	    float b1 = lat_b / pk;
	    float b2 = lng_b / pk;

	    float t1 = FloatMath.cos(a1)*FloatMath.cos(a2)*FloatMath.cos(b1)*FloatMath.cos(b2);
	    float t2 = FloatMath.cos(a1)*FloatMath.sin(a2)*FloatMath.cos(b1)*FloatMath.sin(b2);
	    float t3 = FloatMath.sin(a1)*FloatMath.sin(b1);
	    double tt = Math.acos(t1 + t2 + t3);
	   
	    return 6366000*tt;
	}
	
	
}
