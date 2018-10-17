package no.hiof.ahmedak.papervault.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SectionsPageAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> myFragmentsList = new ArrayList<>();
    private final HashMap<Fragment,Integer> mFragment = new HashMap<>();
    private final HashMap<String,Integer> mFragmentNr = new HashMap<>();
    private final HashMap<Integer,String> mFragmentName = new HashMap<>();


    public SectionsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return myFragmentsList.get(i);
    }

    @Override
    public int getCount() {
        return myFragmentsList.size();
    }

    public void addFragment(Fragment fragment, String fragmentName){
        myFragmentsList.add(fragment);
        mFragment.put(fragment,myFragmentsList.size()-1);
        mFragmentNr.put(fragmentName,myFragmentsList.size()-1);
        mFragmentName.put(myFragmentsList.size()-1,fragmentName);
    }

    /**
     * Returns fragment with the name @param
     * Passing Name of the fragment
     * @param fragmentName
     * @return
     */
    public Integer getFragmentNumber(String fragmentName){
        if(mFragmentNr.containsKey(fragmentName)){
            return mFragmentNr.get(fragmentName);
        }else {
            return null;
        }
    }


    /**
     * Returns fragment with name.
     * passing fragment object.
     * @param fragment
     * @return
     */
    public Integer getFragmentNumber(Fragment fragment){
        if(mFragmentNr.containsKey(fragment)){
            return mFragmentNr.get(fragment);
        }else {
            return null;
        }
    }

    /**
     * Return the fragment with the name
     * @param fragmentNumber
     * @return
     */
    public String getFragmentName(Integer fragmentNumber){
        if(mFragmentName.containsKey(fragmentNumber)){
            return mFragmentName.get(fragmentNumber);
        }else {
            return null;
        }
    }




}
