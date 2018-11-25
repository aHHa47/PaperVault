package no.hiof.ahmedak.papervault.Activity;


import android.content.Context;
import android.os.Bundle;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import no.hiof.ahmedak.papervault.Fragments.EditProfileFragment;
import no.hiof.ahmedak.papervault.Fragments.SigningOut_Fragment;
import no.hiof.ahmedak.papervault.R;
import no.hiof.ahmedak.papervault.Adapters.SectionsPageAdapter;

public class ActivitySettings extends AppCompatActivity {

    private static final String TAG = "ActivitySettings";
    private ImageButton SignOutButtn;
    private RelativeLayout relativeLayout;
    private Context mContext;
    private SectionsPageAdapter sectionsPageAdapter;
    private ViewPager MyViewPager;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        mContext = ActivitySettings.this;
        MyViewPager = findViewById(R.id.Layout_viewPager_Container);
        relativeLayout = findViewById(R.id.settings_relativeLayout1);
        SettingsList();
        FragmentSetup();

    }


    /**
     * Setup Fragment with Page Adapter
     */
    private void FragmentSetup(){

        // Page Adapter
        sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        sectionsPageAdapter.addFragment(new EditProfileFragment(),getString(R.string.profile_fragment));
        sectionsPageAdapter.addFragment(new SigningOut_Fragment(),getString(R.string.Sign_Out_fragment));
    }


    /**
     * ViewPage Setup
     * @param fragnumber
     */
    private void ViewPageSetup(int fragnumber){
        relativeLayout.setVisibility(View.GONE);
        Log.d(TAG, "ViewPageSetup: Navigating to Sign out Fragment");
        MyViewPager.setAdapter(sectionsPageAdapter);
        MyViewPager.setCurrentItem(fragnumber);
    }

    /**
     * Settings List
     */
    private void SettingsList(){
        // Get List Reference
        ListView listView = findViewById(R.id.listView_settings);
        // Create a list of Options
        ArrayList<String> settingsOpt = new ArrayList<>();
        settingsOpt.add(getString(R.string.profile_fragment));
        settingsOpt.add(getString(R.string.Sign_Out_fragment));
        // Create our Adapter for the list
        ArrayAdapter adapter = new ArrayAdapter(mContext,android.R.layout.simple_list_item_1,settingsOpt);
        // sets ListView Adapter.
        listView.setAdapter(adapter);

        // Set onClick on item list.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewPageSetup(position);
            }
        });


    }
}
