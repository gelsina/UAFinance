package smikhlevskiy.uafinance.UI;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;

import smikhlevskiy.uafinance.R;
import smikhlevskiy.uafinance.Threadas.RefreshFinanceUAAsyncTask;
import smikhlevskiy.uafinance.model.Organization;

public class OrganizationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);

        ActionBar ab = getSupportActionBar();
        ab.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_gradient));

        ab.setDisplayHomeAsUpEnabled(true);


        Organization organization = (Organization) getIntent().getExtras().getParcelable("organization");


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
}
