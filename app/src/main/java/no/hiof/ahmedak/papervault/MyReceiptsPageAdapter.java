package no.hiof.ahmedak.papervault;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

// This is an adapter for My Receipts TabLayOut. Extends FragmentPageAdapter since we have multiple tabs.
public class MyReceiptsPageAdapter extends FragmentPagerAdapter {

    private int tabs;

    private String [] tabTitles = new String[]{"Store","All Receipts"};

    // Adapter Constructor
    public MyReceiptsPageAdapter(FragmentManager fm, int tabs) {
        super(fm);
        this.tabs = tabs;
    }

    @Nullable
    // displaying Tab Titles.
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int i) {
        // Get Tab Fragments
       switch (i){
           case 0:
               return new StoreFragment();
           case 1:
               return new AllReceiptsFragment();
           default:
               return null;
       }
    }


    @Override
    public int getCount() {
        // returns number of tabs
        return tabs;
    }
}
