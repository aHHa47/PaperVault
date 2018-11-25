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





}
