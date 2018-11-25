package no.hiof.ahmedak.papervault.Activity;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.nio.channels.SelectableChannel;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import no.hiof.ahmedak.papervault.Adapters.PlaceAutocompleteAdapter;
import no.hiof.ahmedak.papervault.Adapters.StoreSpinnerAdapter;
import no.hiof.ahmedak.papervault.Fragments.CalenderFragment;
import no.hiof.ahmedak.papervault.Model.Store;
import no.hiof.ahmedak.papervault.R;
import no.hiof.ahmedak.papervault.Utilities.CommonUtils;
import no.hiof.ahmedak.papervault.Utilities.FirebaseUtilities;


public class NewReceiptActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "NewReceiptActivity";
    private  ImageView mImageView, dropDownImg;
    private TextView dorpDownTxt;
    private Bitmap bitmap;
    private EditText etReceiptName,etDate,etprice;
    private Button CalenderBtn, LocationBtn;
    private FloatingActionButton mFab;
    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private FirebaseUtilities firebaseUtilities;
    private DatabaseReference myRef;

    private Spinner mSpinner;
    private StoreSpinnerAdapter storeSpinnerAdapter;
    private ArrayList<Store> stores;
    private AutoCompleteTextView placeAutoComplete;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private GeoDataClient geoDataClient;

    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));

    private int imageCount = 0;
    private String ImageUrl;
    private String ReceiptName;
    private String ReceiptDate;
    private double ReceiptAmount;
    private String ReceiptLocation;
    private String store_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_receipts_content_layout);
        mContext = NewReceiptActivity.this;
        widgetInit();
        getActivityIntent(savedInstanceState);
        FirebaseAuthSetup();
        firebaseUtilities = new FirebaseUtilities(mContext);

        geoDataClient = Places.getGeoDataClient(mContext);

        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(mContext,geoDataClient,BOUNDS_GREATER_SYDNEY,null);

        placeAutoComplete.setAdapter(placeAutocompleteAdapter);

        // Floating button Click listener
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Creating new receipt");
                Toast.makeText(mContext,"Creating new Receipt",Toast.LENGTH_SHORT).show();

                // app used to crash when the keyboard is still in use. Temp fix we force to hide keyboard before navigating to Main Activity.
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                // Initializing Receipt Register
                initialize();
            }
        });
        // Calender button Click listener
        CalenderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Open Calender Dialog
                OpenCalenderDialog();
            }
        });
        // Date Click listener
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open Calender Dialog
                OpenCalenderDialog();
            }
        });

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Getting selected Store ID From Drop Down menu
                store_id = stores.get(position).getStore_id();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    /**
     * Open Calender Dialog Fragment
     */
    private void OpenCalenderDialog() {

        DialogFragment calenderPicker = new CalenderFragment();
        calenderPicker.show(getSupportFragmentManager(),getString(R.string.date_picker));
    }

    /**
     * Getting Image From @param
     * @param bundle
     */
    private void getActivityIntent(Bundle bundle){

        Log.d(TAG, "getActivityIntent: Getting Activity Intent ");
        Intent intent = getIntent();
        bundle = intent.getExtras();

        if(intent.hasExtra(getString(R.string.Image_path))){
            ImageUrl  = bundle.getString(getString(R.string.Image_path));
            bitmap = BitmapFactory.decodeFile(ImageUrl);
            mImageView.setImageBitmap(bitmap);

        }
        else{
            Log.d(TAG, "getActivityIntent: Intent is empty");
        }



    }

    // Init our UI References
    private void widgetInit(){
        mImageView = findViewById(R.id.captured_image);
        etReceiptName = findViewById(R.id.edittxt_Receipt_name);
        etDate = findViewById(R.id.edittext_date);
        etprice = findViewById(R.id.edittxt_total_price);
        CalenderBtn = findViewById(R.id.calenderBtn);
        LocationBtn = findViewById(R.id.locationBtn);
        mFab = findViewById(R.id.fab);
        mSpinner = findViewById(R.id.DropDown_menu_spinner);
        dorpDownTxt = findViewById(R.id.DropDown_txt);
        dropDownImg = findViewById(R.id.DropDown_img);


        placeAutoComplete = findViewById(R.id.place_autocomplete_search);
    }

    /**
     * Initialize and upload new Receipt to FireBase
     */
    private void initialize(){

        ReceiptName = etReceiptName.getText().toString();
        ReceiptDate = etDate.getText().toString();
        ReceiptAmount = Double.parseDouble(etprice.getText().toString());
        ReceiptLocation = placeAutoComplete.getText().toString();



        // Register new Receipt and Upload image to FireBase storage
        firebaseUtilities.UploadReceiptImage(ImageUrl,imageCount,ReceiptName, ReceiptDate, ReceiptAmount,store_id, ReceiptLocation);


    }


    /**
     * FireBase Auth
     */
    public void FirebaseAuthSetup(){
        Log.d(TAG, "FirebaseAuthSetup: Setting up firebase auth");
        // Declare FireBase Instance.
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        stores = new ArrayList<>();

        Log.d(TAG, "onDataChange: Getting Count " + imageCount);


        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null){
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged: Signed in " + user.getUid());
                }else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged: Signed Out");
                }
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // getting count
                imageCount = firebaseUtilities.GetRceieptImageCount(dataSnapshot);
                Log.d(TAG, "onDataChange: Getting Count " + imageCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // query Store Names

        Query query = myRef.child(getString(R.string.dbname_store)).orderByChild(getString(R.string.user_id)).equalTo(mAuth.getCurrentUser().getUid());
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildAdded: Data Has been changec" + dataSnapshot.toString());
                stores.add(dataSnapshot.getValue(Store.class));
                ArrayAdapter<Store> arrayAdapter = new StoreSpinnerAdapter(mContext,R.layout.spinner_dropdown_item,stores);
                mSpinner.setAdapter(arrayAdapter);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        // look for user Auth on start
        mAuth.addAuthStateListener(mAuthListner);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListner != null){
            // Stop and remove Auth listener
            mAuth.removeAuthStateListener(mAuthListner);
        }
    }


    /**
     * Create Calender Dialog
     * @param view
     * @param year
     * @param month
     * @param dayOfMonth
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        String selectedDate = DateFormat.getDateInstance().format(calendar.getTime());

        etDate.setText(selectedDate);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
