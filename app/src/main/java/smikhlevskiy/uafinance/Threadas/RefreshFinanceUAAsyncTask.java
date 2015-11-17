package smikhlevskiy.uafinance.Threadas;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import smikhlevskiy.uafinance.R;
import smikhlevskiy.uafinance.Utils.UAFinancePreference;
import smikhlevskiy.uafinance.adapters.OrganizationListAdapter;
import smikhlevskiy.uafinance.model.FinanceUA;
import smikhlevskiy.uafinance.model.Organization;

/**
 * Created by tcont98 on 11-Nov-15.
 */


public class RefreshFinanceUAAsyncTask extends AsyncTask<String, Void, FinanceUA> {

    WeakReference<OrganizationListAdapter> organizationListAdapter;

    WeakReference<Spinner> spinnerCurrency;
    WeakReference<Spinner> spinnerCity;
    WeakReference<Context> context;
    WeakReference<Handler> reDrawHandler;

    public RefreshFinanceUAAsyncTask(
            Context context,
            Handler reDrawHandler,
            OrganizationListAdapter organizationListAdapter,
            Spinner spinnerCurrency,
            Spinner spinnerCity) {
        this.organizationListAdapter = new WeakReference<OrganizationListAdapter>(organizationListAdapter);
        this.reDrawHandler = new WeakReference<Handler>(reDrawHandler);
        this.spinnerCurrency = new WeakReference<Spinner>(spinnerCurrency);
        this.spinnerCity = new WeakReference<Spinner>(spinnerCity);
        this.context = new WeakReference<Context>(context);
    }

    @Override
    protected FinanceUA doInBackground(String... params) {
        StringBuilder bulder = new StringBuilder("");
        try {
            //  from URL
            InputStreamReader isr;
            if (true) {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                isr = new InputStreamReader(con.getInputStream());

            } else {
                //***  Test from assets

                if (context.get() != null) {

                    isr = new InputStreamReader(((Context) context.get()).getAssets().open("currency-cash.json"));
                } else return null;
            }
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
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        Gson gson = new Gson();

        FinanceUA financeUA = (FinanceUA) gson.fromJson(bulder.toString(), FinanceUA.class);


        return financeUA;
    }

    @Override
    protected void onPostExecute(FinanceUA financeUA) {

        Log.i("RefreshThread", "onPostExecute");

        if (context.get() == null) {
            Log.i("RefreshThread", "activity is destroy");
            return;//
        } else
            Log.i("RefreshThread", "onPostExecute");


        UAFinancePreference uaFinancePreference = new UAFinancePreference((Context) context.get());


        ArrayList cityArrayList = new ArrayList<String>();
        cityArrayList.add(uaFinancePreference.getCity());
        String[] citiesArray = (String[]) financeUA.getCities().values().toArray(new String[0]);
        for (int i = 0; i < citiesArray.length; i++)
            cityArrayList.add(citiesArray[i]);
        String[] cities = (String[]) cityArrayList.toArray(new String[0]);


        if ((spinnerCity.get() != null) && (context.get() != null)) {
            ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(((Context) context.get()), android.R.layout.simple_spinner_item, cities);
            ((Spinner) spinnerCity.get()).setAdapter(cityAdapter);
            ((Spinner) spinnerCity.get()).setSelection(cityAdapter.getPosition(uaFinancePreference.getCity()));
        }

        //Arrays.allCurrancies
        ArrayList currencyArrayList = new ArrayList<String>();// (HashSet)financeUA.getCurrancies().keySet();

        currencyArrayList.add(((Context) context.get()).getResources().getString(R.string.USD));
        currencyArrayList.add(((Context) context.get()).getResources().getString(R.string.RUB));
        currencyArrayList.add(((Context) context.get()).getResources().getString(R.string.EUR));

        String[] currencyArray = (String[]) financeUA.getCurrancies().keySet().toArray(new String[0]);
        for (int i = 0; i < currencyArray.length; i++)
            currencyArrayList.add(currencyArray[i]);
        String allCurrancies[] = (String[]) currencyArrayList.toArray(new String[0]);

        if ((spinnerCurrency.get() != null) && (context.get() != null)) {
            ArrayAdapter<String> currencyAdapter = new ArrayAdapter<String>(((Context) context.get()), android.R.layout.simple_spinner_item, allCurrancies);
            ((Spinner) spinnerCurrency.get()).setAdapter(currencyAdapter);
            ((Spinner) spinnerCurrency.get()).setSelection(currencyAdapter.getPosition(uaFinancePreference.getCurrancie()));
        }


        ((OrganizationListAdapter) organizationListAdapter.get()).setFinanceUA(financeUA);
        ((OrganizationListAdapter) organizationListAdapter.get()).notifyDataSetChanged();

        if (reDrawHandler.get() != null)
            ((Handler) reDrawHandler.get()).handleMessage(new Message());

        super.onPostExecute(financeUA);
    }
}

