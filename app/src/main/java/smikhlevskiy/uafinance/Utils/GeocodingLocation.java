package smikhlevskiy.uafinance.Utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by tcont98 on 05-Dec-15.
 */
public class GeocodingLocation {

    private static final String TAG = "GeocodingLocation";

    public static void getAddressFromLocation(final String locationAddress,
                                              final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                double resultLatitude=0;
                double resultLongitude=0;
                try {
                    List addressList;
                    int i=0;
                    do {
                        addressList = geocoder.getFromLocationName(locationAddress, 1);
                        i++;
                    } while ((addressList.size()==0) && (i<=10));
                    if (addressList != null && addressList.size() > 0) {
                        Address address = (Address) addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        resultLatitude=address.getLatitude();
                        resultLongitude=address.getLongitude();
                        sb.append(resultLatitude).append(",");
                        sb.append(resultLongitude).append("");
                        result = sb.toString();
                    }

                } catch (IOException e) {

                    e.printStackTrace();
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Address: " + result;
                        bundle.putString("address", result);
                        bundle.putDouble("latitude", resultLatitude);
                        bundle.putDouble("longitude", resultLongitude);
                        message.setData(bundle);
                    } else {
                        message.what = 0;
                        Bundle bundle = new Bundle();
                        result = "Address: " + locationAddress +
                                "\n Unable to get Latitude and Longitude for this address location.";
                        bundle.putString("address", result);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }
}
