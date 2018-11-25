package no.hiof.ahmedak.papervault.Activity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import no.hiof.ahmedak.papervault.Adapters.FavoriteReceiptsAdapter;
import no.hiof.ahmedak.papervault.Adapters.SectionsPageAdapter;
import no.hiof.ahmedak.papervault.Fragments.FavoriteReceiptsFragment;
import no.hiof.ahmedak.papervault.Fragments.ReceiptInfoFragment;
import no.hiof.ahmedak.papervault.Model.Receipt;
import no.hiof.ahmedak.papervault.R;


public class MyFavorite extends AppCompatActivity implements FavoriteReceiptsFragment.OnCardViewSelectedListner {
    private static final String TAG = "MyFavorite";
    private SectionsPageAdapter sectionsPageAdapter;
    private ViewPager MyViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);

        Toolbar toolbar = (Toolbar)findViewById(R.id.my_favorite_toolbar);
        setSupportActionBar(toolbar);
        MyViewPager = findViewById(R.id.Layout_viewPager_Container);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        FragmentSetup();
        ViewPageSetup();


    }


    /**
     * Setup Fragment with Page Adapter
     */
    private void FragmentSetup(){

        // Page Adapter
        sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        sectionsPageAdapter.addFragment(new FavoriteReceiptsFragment(),getString(R.string.Favorit_Receipt_fragment) );
    }

    private void ViewPageSetup(){
        Log.d(TAG, "ViewPageSetup: Navigating to Sign out Fragment");
        MyViewPager.setAdapter(sectionsPageAdapter);
        MyViewPager.setCurrentItem(5);
    }


    // Create Option menu on toolbar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_my_favorite_menu,menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        // TODO: Implement Sorting method
        //noinspection Simplifiable Case Statement
        switch (id){
            case R.id.item_sort_alpha:
                Toast.makeText(getApplicationContext(),"Sort button Name",Toast.LENGTH_LONG).show();
                break;
            case R.id.item_sort_date:
                Toast.makeText(getApplicationContext(),"Sort button Date",Toast.LENGTH_LONG).show();
                break;
            case R.id.item_sort_price:
                Toast.makeText(getApplicationContext(),"Sort button Price", Toast.LENGTH_LONG).show();
                break;
            case R.id.item_sort_near_me:
                Toast.makeText(getApplicationContext(),"Sort button Near me", Toast.LENGTH_LONG).show();
                break;
            default:
                break;


        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void OnCardViewSelected(Receipt receipt, int ActivityNumber) {
        ReceiptInfoFragment fragment = new ReceiptInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.receipt), receipt);
        args.putInt(getString(R.string.Activity_number),ActivityNumber);

        fragment.setArguments(args);

        // Start FragmentTransaction
        FragmentTransaction transaction = MyFavorite.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.TabView_Container,fragment);
        transaction.addToBackStack(getString(R.string.receipt_info_view_fragment));
        transaction.commit();
    }
}
