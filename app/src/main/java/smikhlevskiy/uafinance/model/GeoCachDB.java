package smikhlevskiy.uafinance.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import smikhlevskiy.uafinance.Utils.CachGeocodingLocation;

/**
 * Created by tcont98 on 12-Dec-15.
 */
public class GeoCachDB extends SQLiteOpenHelper {
    public static String TAG = GeoCachDB.class.getSimpleName();
    public static String TABLE_NAME = "GEOCACH";
    public static String KEY_ID = "_ID";
    public static String KEY_ADDRESS = "ADDRESS";
    public static String KEY_LATITUDE = "LATITUDE";
    public static String KEY_LONGITUDE = "LONGITUDE";
    private CachGeocodingLocation cachGeocodingLocation;

    public GeoCachDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, name, factory, version);
        cachGeocodingLocation = new CachGeocodingLocation();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "ONCreateDB");
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ( " +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_ADDRESS + " TEXT, " +
                KEY_LATITUDE + " FLOAT, " +
                KEY_LONGITUDE + " FLOAT)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "ONUpgrade");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void ControlAddress(List<String> textAddress) {
        //Log.i(TAG, text);

        SQLiteDatabase db = getWritableDatabase();


        HashMap<String, LatLng> latLonMap = new HashMap<String, LatLng>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {

                latLonMap.put(cursor.getString(1), new LatLng(cursor.getDouble(2), cursor.getDouble(3)));


            } while (cursor.moveToNext());
        }


        for (String text : textAddress)
            if (!latLonMap.containsKey(text)) {
                LatLng latLng = cachGeocodingLocation.getAddressFromLocationByURL(text);

                if (latLng != null) {
                    Log.i(TAG, text + ":" + latLng.toString());
                    ContentValues cv = new ContentValues();

                    cv.put(KEY_ADDRESS, text);
                    cv.put(KEY_LATITUDE, latLng.latitude);
                    cv.put(KEY_LONGITUDE, latLng.longitude);


                    db.insert(TABLE_NAME, null, cv);


                } else Log.i(TAG, text + ":null");
            } else {
                LatLng latLng= latLonMap.get(text);
                Log.i(TAG, text + ":read from DBCach:"+latLng.latitude+","+latLng.longitude);
            }

        db.close();

    }
}
