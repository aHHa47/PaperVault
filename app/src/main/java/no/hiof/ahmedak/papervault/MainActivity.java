package no.hiof.ahmedak.papervault;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    TableLayout tableLayout;

    // Temp

    private ArrayList<Integer> mData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Floating Action Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        // DataSet
        // TODO: get data from Database.
        
        mData = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            mData.add(R.drawable.receipt_ex);
        }
        
        // RecycleView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view_content_main);
        mRecyclerView.setHasFixedSize(true);

        // RecycleView Manager

        mLayoutManager = new LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // RecycleView Adapter

        mAdapter = new ContnentMainAdapter(mData);
        mRecyclerView.setAdapter(mAdapter);


        //Table Layout
        tableLayout = (TableLayout) findViewById(R.id.table_lay_out);

        // TODO: Change to database
        for (int x = 0; x < 40; x++) {
            View tableRowView = LayoutInflater.from(this).inflate(R.layout.content_main_table_row_items,null,false);
            TextView StoreName = tableRowView.findViewById(R.id.StoreName_table_txt);
            TextView DateStore = tableRowView.findViewById(R.id.Date_table_txt);
            TextView Price = tableRowView.findViewById(R.id.Price_table_txt);

            // Temp
            StoreName.setText("Store Name " + (x+1));
            Date todayDate = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String todayString = formatter.format(todayDate);
            DateStore.setText(todayString);
            Price.setText((299*(x+1))+ " Kr");
            tableLayout.addView(tableRowView);



        }





    }

    // When Back Button is Pressed
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_info) {
            //TODO: Show Dialog box with app info.
            Toast toast = Toast.makeText(getApplicationContext(),"This is info Action ",Toast.LENGTH_LONG);
            toast.show();
        }
        // DONE: Fix Search Action.
        // TODO: implement search engine
        else if(id == R.id.action_search){
            // Expand Search bar onClick.
            item.expandActionView();


        }

        return super.onOptionsItemSelected(item);
    }

    // Navigation
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;

            // Home
        if (id == R.id.nav_Home) {

            intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            // My Receipts
        } else if (id == R.id.nav_my_receipts) {
            intent = new Intent(getApplicationContext(),MyReceipts.class);
            startActivity(intent);
            // My Favourite
        } else if (id == R.id.nav_my_favourite) {
            intent = new Intent(getApplicationContext(),MyFavorite.class);
            startActivity(intent);
            // Statistics
        } else if (id == R.id.nav_statistics) {
           intent = new Intent(getApplicationContext(),Statistics.class);
           startActivity(intent);
            // Settings
        } else if (id == R.id.nav_settings) {
           intent = new Intent(getApplicationContext(),SettingsActivity.class);
           startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
