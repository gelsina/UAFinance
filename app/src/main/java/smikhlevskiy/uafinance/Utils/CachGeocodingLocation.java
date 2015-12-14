package smikhlevskiy.uafinance.Utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import smikhlevskiy.uafinance.R;

/**
 * Created by tcont98 on 05-Dec-15.
 */
public class CachGeocodingLocation {

    private static final String TAG = CachGeocodingLocation.class.getSimpleName();
    //private Geocoder geocoder;




    public LatLng getAddressFromLocationByURL(final String locationAddress) {

        //LatLng address=new LatLng();
        StringBuilder bulder = new StringBuilder("");
        try {
            InputStreamReader isr;

            URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=" + locationAddress + "&key=AIzaSyCN-atwyMdbfyAW3FKeHlCGb7aSn8sY4Ew");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            isr = new InputStreamReader(con.getInputStream());


            //***

            BufferedReader reader = new BufferedReader(isr);
            String str = null;

            do {
                str = reader.readLine();
                if (str != null)
                    bulder.append(str);
            } while (str != null);

        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            return null;
        } catch (IOException e1) {
            e1.printStackTrace();
            return null;

        }
        try {
            JSONObject obj = new JSONObject(bulder.toString());
            JSONArray array = obj.getJSONArray("results");
            if (array.length()<=0) return null;

            JSONObject item = array.getJSONObject(0);
            JSONObject geoJson=item.getJSONObject("geometry");
            JSONObject locJson=geoJson.getJSONObject("location");
            return new LatLng(Double.parseDouble(locJson.getString("lat")),Double.parseDouble(locJson.getString("lng")));

        } catch (JSONException e) {
            return null;
        }

    }
/* //
    public Address getAddressFromLocation(final String locationAddress) {


        try {


            List addressList;
            addressList = geocoder.getFromLocationName(locationAddress, 1);

            if ((addressList != null) && (addressList.size() > 0)) {
                Log.i(TAG, locationAddress + " true" + "Lat:" + ((Address) addressList.get(0)).getLatitude() +
                        "Lon:" + ((Address) addressList.get(0)).getLongitude());
                return (Address) addressList.get(0);
            } else {
                Log.i(TAG, locationAddress + " null");
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;

        }
    }

*/
    public void getHandleAddressFromLocation(final String locationAddress
            , final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {


                //Address address = getAddressFromLocation(locationAddress);
                LatLng latLng=getAddressFromLocationByURL(locationAddress);


                Message message = Message.obtain();
                message.setTarget(handler);


                if (latLng != null) {

                    message.what = 1;
                    Bundle bundle = new Bundle();

                    bundle.putDouble("latitude", latLng.latitude);
                    bundle.putDouble("longitude", latLng.longitude);
                    message.setData(bundle);
                } else {
                    message.what = 0;
                    Bundle bundle = new Bundle();

                    bundle.putString("address", locationAddress +
                            ":null");
                    message.setData(bundle);
                }
                message.sendToTarget();


            }
        };
        thread.start();
    }
}
