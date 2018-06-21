package domain.foo.stockalert;
//written by nick


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.content.ContentValues;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;


public class DataSource {
	
	private static final String LOG_TAG = DataSource.class.getSimpleName();
    private SQLiteDatabase database;
    private DBHelper dbHelper;
	
	  //string arrays, containing the coloumns of each table
    private String[] columns_stock_list = {
            DBHelper.stock_id,
              DBHelper.stock_value,
              DBHelper.stock_name,
              DBHelper.stock_timezone,
              DBHelper.stock_date,

      };

    private String[] columns_alert_list = {
            DBHelper.alert_id,
            DBHelper.stock_pricelimit,
            //DBHelper.stock_alert,
    };

	
    public DataSource(Context context) {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new DBHelper(context); //a instance of DbHelper is initialized
    }

    //method to open the database
    public void open() {
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    //method to close the database
    public void close() {
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }

    public Equity createEquity(String symbol, int price, String date, String timezone){
        ContentValues columns_entry_list = new ContentValues();
        columns_entry_list.put(DBHelper.stock_value, price);
        columns_entry_list.put(DBHelper.stock_name, symbol);
        columns_entry_list.put(DBHelper.stock_date, date);
        columns_entry_list.put(DBHelper.stock_timezone, timezone);




        long insertId = database.insert(DBHelper.table_stock, null, columns_entry_list);

        Cursor cursor = database.query(DBHelper.table_stock, columns_stock_list, DBHelper.stock_id + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Equity equity = cursorToEntry(cursor);
        cursor.close();

        return equity;

	  }

    public Alert createPriceLimit(int pricelimit, long id){
        ContentValues columns_entry_list = new ContentValues();
        columns_entry_list.put(DBHelper.stock_pricelimit, pricelimit);
        //columns_entry_list.put(DBHelper.stock_alert, false);
        columns_entry_list.put(DBHelper.stock_id, id);


        long insertId = database.insert(DBHelper.table_alert, null, columns_entry_list);

        Cursor cursor = database.query(DBHelper.table_alert, columns_alert_list, DBHelper.alert_id + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Alert alert = cursorToAlert(cursor);
        cursor.close();

        return alert;

    }

    private Alert cursorToAlert(Cursor cursor) {
        int id_index = cursor.getColumnIndex(DBHelper.alert_id);
        int id_pricelimit = cursor.getColumnIndex(DBHelper.stock_pricelimit);
        //int id_alert = cursor.getColumnIndex(DBHelper.stock_alert);
        int id_stock = cursor.getColumnIndex(DBHelper.stock_id);


        long index = cursor.getLong(id_index);
        int pricelimit = cursor.getInt(id_pricelimit);
        //Boolean alert = cursor.getBoolean(id_alert);
        long stock_id = cursor.getInt(id_stock);




        Alert alert = new Alert(index, pricelimit, stock_id);

        return alert;
    }

    private Equity cursorToEntry(Cursor cursor) {

        int id_index = cursor.getColumnIndex(DBHelper.stock_id);
        int id_symbol = cursor.getColumnIndex(DBHelper.stock_name);
        int id_price = cursor.getColumnIndex(DBHelper.stock_value);
        int id_date = cursor.getColumnIndex(DBHelper.stock_date);
        int id_timezone = cursor.getColumnIndex(DBHelper.stock_timezone);



        long index = cursor.getLong(id_index);
        String symbol = cursor.getString(id_symbol);
        double value = cursor.getDouble(id_price);

        String date = cursor.getString(id_date);
        String timezone = cursor.getString(id_timezone);



        Equity equity = new Equity(index,symbol,value, date, timezone );

        return equity;
    }

    public List<Equity> getAllEquities() {
        List<Equity> EntryList = new ArrayList<>();

        Cursor cursor = database.query(DBHelper.table_stock,
                columns_stock_list, null, null, null, null, null);

        cursor.moveToFirst();
        Equity equity;

        while(!cursor.isAfterLast()) {
            equity = cursorToEntry(cursor);
            EntryList.add(equity);
            cursor.moveToNext();
        }

        cursor.close();

        return EntryList;
    }

    public List<Alert> getAllAlerts() {
        List<Alert> AlertList = new ArrayList<>();

        Cursor cursor = database.query(DBHelper.table_alert,
                columns_alert_list, null, null, null, null, null);

        cursor.moveToFirst();
        Alert alert;

        while(!cursor.isAfterLast()) {
            alert = cursorToAlert(cursor);
            AlertList.add(alert);
            cursor.moveToNext();
        }

        cursor.close();

        return AlertList;
    }

    public void deleteEquity(Equity equity){
        long id = equity.getId();

        database.delete(DBHelper.table_stock,
                DBHelper.stock_id + "=" + id,
                null);
    }

    public void deleteAlert(Alert alert){
        long id = alert.getID();

        database.delete(DBHelper.table_alert,
                DBHelper.alert_id + "=" + id,
                null);
    }

    public void updateEquity(double value, Equity equity){
        ContentValues newValues = new ContentValues();
        long stock_id = equity.getId();
        newValues.put(DBHelper.stock_value, value);
        database.update(DBHelper.table_stock, newValues, DBHelper.stock_id+"="+stock_id, null);
    }

    public void updateAlert(double pricelimit, Alert alert){
        ContentValues newValues = new ContentValues();
        long alert_id = alert.getID();
        newValues.put(DBHelper.stock_pricelimit, pricelimit);
        database.update(DBHelper.table_alert, newValues, DBHelper.alert_id+"="+alert_id, null);
    }


	
	
	
	
	
	
	
	
	
	
	
	
}