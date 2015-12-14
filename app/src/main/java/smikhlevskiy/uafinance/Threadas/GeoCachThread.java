package smikhlevskiy.uafinance.Threadas;

import android.content.Context;

import java.util.List;

import smikhlevskiy.uafinance.model.GeoCachDB;
import smikhlevskiy.uafinance.model.Organization;

/**
 * Created by tcont98 on 11-Dec-15.
 */
public class GeoCachThread extends Thread {
    public static String DB_NAME = "GEOCACHDB";
    public static int DB_VERSION = 3;
    private List<String> textAddress;
    private Context context;

    public GeoCachThread(Context context, List<String> textAddress) {
        this.textAddress=textAddress;
        this.context = context;
    }



    @Override
    public void run() {
        GeoCachDB geoCachDB = new GeoCachDB(context, DB_NAME, null, DB_VERSION);

        geoCachDB.ControlAddress(textAddress);
        }


}
