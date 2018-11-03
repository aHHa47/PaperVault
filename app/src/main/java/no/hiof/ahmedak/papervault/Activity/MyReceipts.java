package no.hiof.ahmedak.papervault.Activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import no.hiof.ahmedak.papervault.Adapters.MyReceiptsPageAdapter;
import no.hiof.ahmedak.papervault.Fragments.AllReceiptsFragment;
import no.hiof.ahmedak.papervault.Fragments.ReceiptInfoFragment;
import no.hiof.ahmedak.papervault.Model.Receipt;
import no.hiof.ahmedak.papervault.R;


public class MyReceipts extends AppCompatActivity implements AllReceiptsFragment.OnCardViewSelectedListner {
    private static final String TAG = "MyReceiptsActivity";
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private MyReceiptsPageAdapter myReceiptsPageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_receipts);


        // Get Reference to our UI
        tabLayout = findViewById(R.id.my_receipt_tab_layOut);
        viewPager = findViewById(R.id.tab_view_pager);
        toolbar = findViewById(R.id.my_receipt_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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

    @Override
    public void OnCardViewSelected(Receipt receipt, int ActivityNumber) {
        Log.d(TAG, "OnCardViewSelected: Selected an CardView" + receipt.toString());

        ReceiptInfoFragment fragment = new ReceiptInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.receipt), receipt);
        args.putInt(getString(R.string.Activity_number),ActivityNumber);

        fragment.setArguments(args);

        // Start FragmentTransaction
        FragmentTransaction transaction = MyReceipts.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.TabView_Container,fragment);
        transaction.addToBackStack(getString(R.string.receipt_info_view_fragment));
        transaction.commit();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
