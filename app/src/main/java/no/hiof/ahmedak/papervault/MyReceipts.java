package no.hiof.ahmedak.papervault;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Adapter;
import android.widget.TableLayout;

import java.util.ArrayList;

public class MyReceipts extends AppCompatActivity {

    AppBarLayout appBarLayout;
    TabLayout tabLayout;
    ViewPager viewPager;
    MyReceiptsPageAdapter myReceiptsPageAdapter;
    TabItem tabStore;
    TabItem tabAllReceipts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_receipts);

        appBarLayout = findViewById(R.id.app_bar_layout);

        // Get Reference to our xml
        tabLayout = findViewById(R.id.my_receipt_tab_layOut);
        tabStore = findViewById(R.id.tab_item_Store);
        tabAllReceipts = findViewById(R.id.tab_item_all_receipts);
        viewPager = findViewById(R.id.tab_view_pager);

        // PageAdapter Constructor
        myReceiptsPageAdapter = new MyReceiptsPageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        // Sett Adapter to our View Pager
        viewPager.setAdapter(myReceiptsPageAdapter);
        tabLayout.setupWithViewPager(viewPager);



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            // On Tab Selected we want to get the right tab position.
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // sync the viewPager with TabLayout. Tell our view pager on what tab we are at.
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));




    }
}
