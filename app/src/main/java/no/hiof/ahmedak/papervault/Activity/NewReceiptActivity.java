package no.hiof.ahmedak.papervault.Activity;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.channels.SelectableChannel;
import java.text.DateFormat;
import java.util.Calendar;

import no.hiof.ahmedak.papervault.Fragments.CalenderFragment;
import no.hiof.ahmedak.papervault.R;
import no.hiof.ahmedak.papervault.Utilities.FirebaseUtilities;


public class NewReceiptActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "NewReceiptActivity";
    private  ImageView mImageView;
    private Bitmap bitmap;
    private EditText etReceiptName, etLocation,etDate,etprice;
    private Button CalenderBtn, LocationBtn;
    private FloatingActionButton mFab;
    private Spinner mSpinner;
    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private FirebaseUtilities firebaseUtilities;
    private DatabaseReference myRef;

    private int imageCount = 0;
    private String ImageUrl;
    private String ReceiptName;
    private String ReceiptDate;
    private double ReceiptAmount;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_receipts_content_layout);
        mContext = NewReceiptActivity.this;
        widgetInit();
        getActivityIntent(savedInstanceState);
        FirebaseAuthSetup();
        firebaseUtilities = new FirebaseUtilities(mContext);



        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Creating new receipt");
                Toast.makeText(mContext,"Creating new Receipt",Toast.LENGTH_SHORT).show();

                initialize();

            }
        });

        CalenderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment calenderPicker = new CalenderFragment();
                calenderPicker.show(getSupportFragmentManager(),getString(R.string.date_picker));
            }
        });

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



    }

    // Init our UI References
    private void widgetInit(){
        mImageView = findViewById(R.id.captured_image);
        etReceiptName = findViewById(R.id.edittxt_Receipt_name);
        etDate = findViewById(R.id.edittext_date);
        etLocation = findViewById(R.id.edittext_location);
        etprice = findViewById(R.id.edittxt_total_price);
        CalenderBtn = findViewById(R.id.calenderBtn);
        LocationBtn = findViewById(R.id.locationBtn);
        mFab = findViewById(R.id.fab);
        mSpinner = findViewById(R.id.DropDown_menu_spinner);

    }

    // init New Receipt
    private void initialize(){

        ReceiptName = etReceiptName.getText().toString();
        ReceiptDate = etDate.getText().toString();
        ReceiptAmount = Double.parseDouble(etprice.getText().toString());


        // Uploading new Receipt
        firebaseUtilities.UploadReceiptImage(ImageUrl,imageCount,ReceiptName, ReceiptDate, ReceiptAmount);


    }


    public void FirebaseAuthSetup(){
        Log.d(TAG, "FirebaseAuthSetup: Setting up firebase auth");
        // Declare FireBase Instance.
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();

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
}
