package smikhlevskiy.uafinance.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

/**
 * Created by tcont98 on 07-Nov-15.
 */
public class FinanceUA {

    private String sourceId;
    private String date;
    private Organization[] organizations;
    private Map<String, String> orgTypes;
    private Map<String, String> currencies;
    private Map<String, String> regions;

    private Map<String, String> cities;


    private ArrayList<Integer> index = new ArrayList<Integer>();


    private HashMap<String, Currencie> minMaxCurrencies = null;
/*
    public Set<String> getAllCurrencies() {
        Set<String> currArr = new HashSet<String>();
        currArr.add("aaa");


        for (Organization organization : organizations) {


            if (organization.getCurrencies() != null) {
                Set<String> namesCurr = organization.getCurrencies().keySet();
                for (String name : namesCurr) currArr.add(name);
            }


        }


        return currArr;
    }
*/

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Organization[] getOrganizations() {
        return organizations;
    }

    public void setOrganizations(Organization[] organizations) {
        this.organizations = organizations;
    }

    public Map<String, String> getOrgTypes() {
        return orgTypes;
    }

    public void setOrgTypes(Map<String, String> orgTypes) {
        this.orgTypes = orgTypes;
    }

    public Map<String, String> getCurrancies() {
        return currencies;
    }

    public void setCurrancies(Map<String, String> currancies) {
        this.currencies = currancies;
    }

    public Map<String, String> getRegions() {
        return regions;
    }

    public void setRegions(Map<String, String> regions) {
        this.regions = regions;
    }

    public Map<String, String> getCities() {
        return cities;
    }

    public void setCities(Map<String, String> cities) {
        this.cities = cities;
    }

    public ArrayList<Integer> getIndex() {
        return index;
    }

    public void sort(boolean askByd, String city, String currancie) {

        index.clear();

        for (int i = 0; i < organizations.length; i++) {
            organizations[i].setSortVal(0.0);

            if (cities.get(organizations[i].getCityId()).equals(city))
                if (organizations[i].currencies.containsKey(currancie)) {
                    index.add(new Integer(i));
                    if (askByd)
                        organizations[i].setSortVal(new Double(organizations[i].currencies.get(currancie).getBid()));
                    else
                        organizations[i].setSortVal(new Double(organizations[i].currencies.get(currancie).getAsk()));
                }
        }

        for (int i = 0; i < index.size() - 1; i++)
            for (int j = 0; j < index.size() - 1; j++) {


                if ((askByd && (organizations[index.get(j + 1)].getSortVal() > organizations[index.get(j)].getSortVal())) ||
                        ((!askByd) && (organizations[index.get(j + 1)].getSortVal() < organizations[index.get(j)].getSortVal()))) {
                    Integer a = index.get(j);
                    index.set(j, index.get(j + 1));
                    index.set(j + 1, a);


                }

                //   s=organization.currencies.get(uaFinancePreference.getCurrancie()).getAsk();

            }

/*
        if (organization.currencies.containsKey(uaFinancePreference.getCurrancie()))
            if (uaFinancePreference.getAskBid().equals(context.getResources().getStringArray(R.array.askbid)[0]))
                s=organization.currencies.get(uaFinancePreference.getCurrancie()).getBid(); else
                s=organization.currencies.get(uaFinancePreference.getCurrancie()).getAsk();
                */


    }

    public void calckMinMaxCurrencies(String[] keyCurrencies,String city) {
        minMaxCurrencies = new HashMap<String, Currencie>();

        for (String key : keyCurrencies) {
            if (!minMaxCurrencies.containsKey(key)) {
                Currencie maxcur = new Currencie();
                maxcur.setAsk("0.0");
                maxcur.setBid("0.0");
                minMaxCurrencies.put(key, maxcur);
            }

            for (int i = 0; i < organizations.length; i++) {
                if (organizations[i].currencies.containsKey(key) && (cities.get(organizations[i].getCityId()).equals(city))) {

                    Currencie maxcur = (Currencie) minMaxCurrencies.get(key);
                    Currencie orgCur = organizations[i].currencies.get(key);

                    double maxAskVal = new Double(maxcur.getAsk());
                    double minBidVal = new Double(maxcur.getBid());
                    double askVal = new Double(orgCur.getAsk());
                    double bidVal = new Double(orgCur.getBid());

                    if ((maxAskVal > askVal) || (maxAskVal == 0)) {
                        maxcur.setAsk(Double.toString(askVal));
                        Log.i("MA:1", Double.toString(askVal));
                        Log.i("MA:2", ((Currencie) minMaxCurrencies.get(key)).getAsk());
                    }
                    if ((minBidVal < bidVal) || (minBidVal == 0))
                        maxcur.setBid(Double.toString(bidVal));


                }
            }

        }

        // Log.i("MainActivity:Ask",minMaxCurrencies.get("USD").getAsk());
        // Log.i("MainActivity:Bid",minMaxCurrencies.get("USD").getBid());
    }

    public HashMap<String, Currencie> getMinMaxCurrencies() {
        return minMaxCurrencies;
    }

}