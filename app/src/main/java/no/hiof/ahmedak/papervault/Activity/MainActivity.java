package no.hiof.ahmedak.papervault.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import android.widget.Toast;;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.hiof.ahmedak.papervault.Adapters.ContnentMainAdapter;
import no.hiof.ahmedak.papervault.Adapters.SectionsPageAdapter;
import no.hiof.ahmedak.papervault.Model.Favorite;
import no.hiof.ahmedak.papervault.Model.Receipt;
import no.hiof.ahmedak.papervault.Model.Store;
import no.hiof.ahmedak.papervault.Model.User;
import no.hiof.ahmedak.papervault.R;
import no.hiof.ahmedak.papervault.Utilities.FirebaseUtilities;
import no.hiof.ahmedak.papervault.Utilities.permissions;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

   private RecyclerView mRecyclerView;
   private RecyclerView.LayoutManager mLayoutManager;
   private RecyclerView.Adapter mAdapter;
   private TableLayout tableLayout;
   private final String TAG = "";
   private static final int REQUEST_TAKE_PHOTO = 1;
   private String mCurrentPhotoPath;
   private Uri photoURI;
   private File ImageFile;
   private Context mContext;
   private FirebaseAuth mAuth;
   private FirebaseAuth.AuthStateListener mAuthListner;
   private FirebaseDatabase firebaseDatabase;
   private DatabaseReference myRef;
   private FirebaseUtilities firebaseUtilities;
   private TextView profileName;
   private TextView profileEmail;
   private permissions perms;
   private static final int CAMERA_PERMS_REQUEST_CODE= 208;

    // Temp
    private ArrayList<Receipt>receipts;




    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = MainActivity.this;
        // Init methods
        FirebaseAuthSetup();
        initialize();
        firebaseUtilities = new FirebaseUtilities(getApplicationContext());


    }


    /**
     * Initialize UI Widgets
     */
    public void initialize(){


        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Drawer Layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        // Navigation View
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Get Reference to navigation drawer profile Text View.
        View headerView = navigationView.getHeaderView(0);
        profileEmail = headerView.findViewById(R.id.user_profile_mail_txt);
        profileName = headerView.findViewById(R.id.user_profile_name_txt);


        // RecycleView
        mRecyclerView = findViewById(R.id.recycle_view_content_main);
        mRecyclerView.setHasFixedSize(true);

        // RecycleView Manager
        mLayoutManager = new LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);



        //Table Layout
        tableLayout = findViewById(R.id.table_lay_out);


        // Floating Button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Open Camera");
                OpenCamera();
            }
        });



    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Send Image Path to New Receipt Activity.
        Intent intent = new Intent(mContext,NewReceiptActivity.class);
        intent.putExtra(getString(R.string.Image_path),mCurrentPhotoPath);
        startActivity(intent);

    }


    /**
     * Get Last Ten Receipt Added
     */
    private void getLastTenReceipt(){

            receipts = new ArrayList<>();
            final ArrayList<Store> storeArrayList = new ArrayList<>();

        Log.d(TAG, "getLastTenReceipt: Running Here ");

        // query for last ten receipt for current user.
        Query query = myRef.child(mContext.getString(R.string.dbname_receipt)).orderByChild(mContext.getString(R.string.user_id)).equalTo(mAuth.getCurrentUser().getUid()).limitToFirst(10);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Receipt receipt = new Receipt();
                    Map<String,Object> objectMap = (HashMap<String, Object>)snapshot.getValue();

                    receipt.setReceipt_date(objectMap.get(getString(R.string.receipt_date)).toString());
                    receipt.setStore_id(objectMap.get(getString(R.string.store_id)).toString());
                    receipt.setAmount(Double.parseDouble(objectMap.get(getString(R.string.amount)).toString()));
                    receipt.setUser_id(objectMap.get(getString(R.string.user_id)).toString());
                    receipt.setImage_path(objectMap.get(getString(R.string.image_path)).toString());
                    receipt.setReceipt_title(objectMap.get(getString(R.string.receipt_title)).toString());
                    receipt.setReceipt_id(objectMap.get(getString(R.string.receipt_id)).toString());
                    //receipts.add(snapshot.getValue(Receipt.class));

                   /* //List<Receipt> receiptList = new ArrayList<Receipt>();
                    for(DataSnapshot dataSnapshot1 : snapshot.child(getString(R.string.favorite)).getChildren()){

                        Receipt FavoriteReceipts = new Receipt();
                        FavoriteReceipts.setFavorite(dataSnapshot1.getValue(Receipt.class).isFavorite());
                        receiptList.add(FavoriteReceipts);

                    }*/
                    receipt.setFavorite(Boolean.parseBoolean(objectMap.get(getString(R.string.favorite)).toString()));
                    receipts.add(receipt);

                    Query query1 = myRef.child(mContext.getString(R.string.dbname_store)).orderByChild(mContext.getString(R.string.store_id)).equalTo(receipt.getStore_id());
                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snap :dataSnapshot.getChildren()) {
                                storeArrayList.add(snap.getValue(Store.class));
                            }

                            // Set Adapter
                            mAdapter = new ContnentMainAdapter(getApplicationContext(),receipts, storeArrayList);
                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.setAdapter(mAdapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            Log.e(TAG, "onCancelled: " + databaseError.getMessage() );
                        }
                    });



                }


                if(!dataSnapshot.exists()){
                    // TODO: Update User That Snapshot is empty
                    Toast.makeText(mContext,"Failed to get Receipts, pleas close the app and try again later",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    /**
     * Camera
     * Open Camera And send file path to New Receipt Activity.
     */
    private void OpenCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(intent.resolveActivity(getPackageManager())!= null) {
            File mfile = null;
            try {
                mfile = CreateImageFile();

            } catch (IOException e) {
                Log.d(TAG, "CreatNewReceiptSetup: NullPointerException" + e.getMessage());
            }

            // Continue only if the File was successfully created
            if (mfile != null) {
                photoURI = FileProvider.getUriForFile(getApplicationContext(), "no.hiof.ahmedak.papervault.fileprovider", mfile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /**
     * Create Image File
     * @return Image File
     * @throws IOException
     */
    private File CreateImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        ImageFile = null;
        ImageFile = File.createTempFile( imageFileName,".jpg", storageDir);

        mCurrentPhotoPath = ImageFile.getAbsolutePath();
        return ImageFile;
    }


    /**
     * EasyPermissions, Returns Requested Permissions from @param
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
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
        } else if (id == R.id.sign_out_user){
          intent = new Intent(getApplicationContext(),ActivitySettings.class);
          startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * FireBase Method
     * FireBase Check if user is signed in.
    */

    private void CheckCurrentUser(FirebaseUser user){
        Log.d(TAG, "CheckCurrentUser: Checking if user is logged in");
        // if user is not logged in, navigate back to LoginActivity
        if(user == null){
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }
    }

    private void FirebaseAuthSetup(){
        Log.d(TAG, "FirebaseAuthSetup: Setting up firebase auth");
        // Declare FireBase Instance.
        mAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();


        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                // Check Current User If he is logged in

                CheckCurrentUser(user);

                if(user != null){
                    // User is signed in

                    getLastTenReceipt();
                    Log.d(TAG, "onAuthStateChanged: Signed in " + user.getUid());
                }else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged: Signed Out");
                }
            }
        };

        // Get Data From data base.
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d(TAG, "onDataChange: Displaying User info");
                // Get User Information From Database.
                User user = firebaseUtilities.getUsersInformation(dataSnapshot);
                profileEmail.setText(user.geteMail());
                profileName.setText(user.getFirstName() + " " + user.getLastName());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d(TAG, "onCancelled: Database Error" + databaseError.getMessage());
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
        // This will always check the user.
        CheckCurrentUser(mAuth.getCurrentUser());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListner != null){
            mAuth.removeAuthStateListener(mAuthListner);
        }
    }


}
