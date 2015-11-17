package smikhlevskiy.uafinance.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import smikhlevskiy.uafinance.R;
import smikhlevskiy.uafinance.Utils.UAFinancePreference;
import smikhlevskiy.uafinance.model.FinanceUA;
import smikhlevskiy.uafinance.model.Organization;

/**
 * Created by tcont98 on 09-Nov-15.
 */
public class OrganizationListAdapter extends BaseAdapter {
    private FinanceUA financeUA = null;
    private Context context;
    private UAFinancePreference uaFinancePreference;

    public OrganizationListAdapter(Context context) {
        this.context = context;
        uaFinancePreference=new UAFinancePreference((context));
    }

    public FinanceUA getFinanceUA() {
        return financeUA;
    }

    public void setFinanceUA(FinanceUA financeUA) {
        this.financeUA = financeUA;
    }

    @Override
    public int getCount() {
        if (financeUA == null)
            return 0;
        else
            return financeUA.getIndex().size();
    }

    @Override
    public Object getItem(int position) {
        if (financeUA == null)
            return null;
        else
            return financeUA.getOrganizations()[(financeUA.getIndex().get(position))];

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater lInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = lInflater.inflate(R.layout.menu_organization_item, parent, false);


        }

        Organization organization = (Organization) getItem(position);

        TextView textName = (TextView) convertView.findViewById(R.id.itemName);

        textName.setText(organization.getTitle());


        //uaFinancePreference.getAskBid(), uaFinancePreference.getCity(), uaFinancePreference.getCurrancie()



        TextView textCurrancie = (TextView) convertView.findViewById(R.id.itemCurrencie);
        textCurrancie.setText(new Double(organization.getSortVal()).toString());



        return convertView;
    }
}
