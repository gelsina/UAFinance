package smikhlevskiy.uafinance.UI;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.text.DecimalFormat;

import smikhlevskiy.uafinance.R;
import smikhlevskiy.uafinance.Utils.UAFinancePreference;
import smikhlevskiy.uafinance.model.Organization;

public class OrganizationActivity extends AppCompatActivity  implements OnMapReadyCallback {
    UAFinancePreference uaFinancePreference;
    EditText editTextSum;
    TextView calcResultTextView;
    Organization organization;

    private String calcResult(String s) {


        try {
            Double d = Double.parseDouble(s) * organization.getSortVal();
            String ss = new DecimalFormat("####.###").format(d);
            if (ss.length() > 7)
                return "######.##";
            else
                return ss;


        } catch (NumberFormatException e) {
            return "0.0";
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);

/*
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        */

        uaFinancePreference = new UAFinancePreference(this);

        ActionBar ab = getSupportActionBar();
        ab.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_gradient));

        ab.setDisplayHomeAsUpEnabled(true);


        organization = (Organization) getIntent().getExtras().getParcelable("organization");


        ((TextView) findViewById(R.id.organization_title)).setText(organization.getTitle());


        String city = getIntent().getExtras().getString("city");
        ((TextView) findViewById(R.id.organization_city)).setText(city);
        String region = getIntent().getExtras().getString("region");
        if (region.equals(city))
            ((TextView) findViewById(R.id.organization_region)).setText("");
        else
            ((TextView) findViewById(R.id.organization_region)).setText(region);

        ((TextView) findViewById(R.id.organization_adress)).setText(organization.getAddress());

        ((TextView) findViewById(R.id.organization_link)).setText(organization.getLink());

        ((TextView) findViewById(R.id.organization_telephone)).setText(organization.getPhone());

        ((Button) findViewById(R.id.buttonCallPhone)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "Your Phone_number"));
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", organization.getPhone(), null));
                startActivity(intent);
            }
        });
        //---Calculator----------
        ((TextView) findViewById(R.id.calckSum)).setText(uaFinancePreference.getAskBid());
        ((TextView) findViewById(R.id.calckCurr)).setText(uaFinancePreference.getCurrancie());
        editTextSum = (EditText) findViewById(R.id.editTextSum);

        calcResultTextView = (TextView) findViewById(R.id.calckResult);
        calcResultTextView.setText(calcResult(editTextSum.getText().toString()));

        editTextSum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calcResultTextView.setText(calcResult(s.toString()).toString());


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_organization, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch (item.getItemId()) {
            case android.R.id.home:

                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                                    // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }

                return true;
        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
