package domain.foo.stockalert;
//written by nick

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper{
	
	private static final String LOG_TAG = DBHelper.class.getSimpleName();
    public static final String DB_name = "stock.db";
    public static final int DB_version = 2;
	
	public static final String table_stock = "table_stock";
    public static final String stock_id = "stock_id";
    public static final String stock_name = "stock_name";
    public static final String stock_timezone = "stock_timezone";
    public static final String stock_value = "stock_value";
    public static final String stock_date = "stock_date";


    public static final String table_alert = "table_alert";
    public static final String alert_id = "alert_id";
    public static final String stock_pricelimit = "stock_upper";
    public static final String stock_pricelimit1 = "stock_lower";
    //public static final String stock_alert = "stock_alert";

    public static final String table_observation = "table_observation";
    public static final String observation_id = "observation_id";
    public static final String observation_open = "observation_open";
    public static final String observation_high = "observation_high";
    public static final String observation_low = "observation_low";
    public static final String observation_close = "observation_close";
    public static final String observation_volume = "observation_volume";
    public static final String observation_date = "observation_date";







    //the SQL statements to create the tables
	 public static final String SQL_CREATE_STOCKS =
            "CREATE TABLE " + table_stock + " (" + stock_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "+stock_name+
                    " TEXT NOT NULL, "+stock_date+" TEXT NOT NULL, "+stock_value+" NUMERIC(6,2) NOT NULL, "+stock_timezone+" TEXT NOT NULL);";

	 public static final String SQL_CREATE_LIMITS = "CREATE TABLE "+table_alert+" ("+alert_id+" INTEGER PRIMARY KEY AUTOINCREMENT, "+stock_pricelimit+" NUMERIC(6,2), "+stock_pricelimit1+" NUMERIC(6,2), "+
             stock_id+" INTEGER, FOREIGN KEY("+stock_id+") REFERENCES "+table_stock+"("+stock_id+"));";


    public static final String SQL_CREATE_OBSERVATION ="CREATE TABLE "+table_observation+" ("+observation_id+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +observation_open+" NUMERIC(8,4), "+observation_high+" NUMERIC(8,4), "+observation_low+" NUMERIC(8,4), "+observation_close+" NUMERIC(8,4), " +
            observation_volume+" NUMERIC(8,4), "+observation_date+" TEXT, "+stock_id+" INTEGER NOT NULL, FOREIGN KEY("+stock_id+") REFERENCES "+table_stock+"("+stock_id+"));";
			

			
			
	 public DBHelper(Context context) {
        super(context, DB_name, null, DB_version);
        Log.d(LOG_TAG, "DBHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }
	
	@Override
    public void onCreate(SQLiteDatabase db) {
        try {
            //create the tables
            db.execSQL(SQL_CREATE_STOCKS);
            db.execSQL(SQL_CREATE_LIMITS);
            db.execSQL(SQL_CREATE_OBSERVATION);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }

	 }

	 @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}