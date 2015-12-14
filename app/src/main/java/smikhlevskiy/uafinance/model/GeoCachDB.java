package smikhlevskiy.uafinance.model;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import smikhlevskiy.uafinance.Utils.CachGeocodingLocation;

/**
 * Created by tcont98 on 12-Dec-15.
 */
public class GeoCachDB extends SQLiteOpenHelper {
    public static String TAG=GeoCachDB.class.getSimpleName();
    public static String TABLE_NAME = "GEOCACH";
    public static String KEY_ID = "ID_";
    public static String KEY_ADDRESS = "ADDRESS";
    public static String KEY_LATITUDE = "LATITUDE";
    public static String KEY_LONGITUDE = "LONGITUDE";
    private CachGeocodingLocation cachGeocodingLocation;

    public GeoCachDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, name, factory, version);
        cachGeocodingLocation=new CachGeocodingLocation(context);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ( " +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_ADDRESS + " TEXT, " +
                KEY_LATITUDE + " TEXT, " +
                KEY_LONGITUDE + " TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void ControlAddress(String text) {
        Log.i(TAG, text);

        LatLng latLng=cachGeocodingLocation.getAddressFromLocationByURL(text);

        Log.i(TAG, "By URL:"+latLng.toString());
        /*
        Address address = cachGeocodingLocation.getAddressFromLocation(text);
        if (address != null) {
            double latitude=address.getLatitude();
            double longitude=address.getLongitude();
            Log.i(TAG, "lat:"+latitude+", longi:"+longitude);
        }
        */

    }
}
