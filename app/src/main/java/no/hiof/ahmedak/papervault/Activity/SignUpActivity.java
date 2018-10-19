package no.hiof.ahmedak.papervault.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import no.hiof.ahmedak.papervault.Model.User;
import no.hiof.ahmedak.papervault.R;
import no.hiof.ahmedak.papervault.Utilities.FirebaseUtilities;

import static android.support.constraint.Constraints.TAG;

public class SignUpActivity extends Activity {


    private EditText firstNameTxt, lastNameTxt, userPasswordTxt;
    private TextView loadingTxt;
    private AutoCompleteTextView userMailTxt;
    private ProgressBar mProgressBar;
    private View RegisterUserFormView;
    private Button buttonSignIn, buttonRegister;
    private View mRegisterUserForm;
    private String firstname, lastname,email,password;
    private Context mContext;
    private String UserID;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private User users;
    private FirebaseUtilities firebaseUtilities;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mContext = SignUpActivity.this;
        // Declare FireBase Instance.
        mAuth = FirebaseAuth.getInstance();
        firebaseUtilities = new FirebaseUtilities(mContext);
        GetUiReference();
        FirebaseAuthSetup();
        if(mAuth.getCurrentUser() != null){
            UserID = mAuth.getCurrentUser().getUid();
        }


        // Navigate back to Sign in if user already got an account.
        buttonSignIn = findViewById(R.id.allReady_register_button);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

        // Register User and Login after
        buttonRegister = findViewById(R.id.Register_button);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateRegistration();
            }
        });


    }

    /**
     * Method for getting UI Reference
     */

    private void GetUiReference(){
        // Get reference
        firstNameTxt = findViewById(R.id.Register_user_FirstName);
        lastNameTxt = findViewById(R.id.Register_user_LastName);
        userPasswordTxt = findViewById(R.id.Register_user_Password);
        userMailTxt = findViewById(R.id.Register_user_Email);
        RegisterUserFormView = findViewById(R.id.Register_user_form);
        mProgressBar = findViewById(R.id.Sign_up_progress);
        mProgressBar.setVisibility(View.GONE);
        loadingTxt = findViewById(R.id.Register_Loading_txt);
        loadingTxt.setVisibility(View.GONE);

    }


    // validate User Registration
    private void ValidateRegistration() {

        /** Convert user input from edit boxes to strings.
        * to do validations and also be able to send it to FireBase.
        */
        password = userPasswordTxt.getText().toString();
        email = userMailTxt.getText().toString();
        // if Everything is ok we register the user, and set progress bar and loading text to visible
        if(CheckUserInput(email,password)){
            mProgressBar.setVisibility(View.VISIBLE);
            loadingTxt.setVisibility(View.VISIBLE);

            firebaseUtilities.RegisterUserWithEmail(email,password);
        }


    }

    private boolean CheckUserInput(String Email,String Password){
        Log.d(TAG, "CheckUserInput: Checking User Input for null value");
        if(Email.equals("") || Password.equals("") ){
            Toast.makeText(mContext,"All fields must be filled out",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void FirebaseAuthSetup(){
        Log.d(TAG, "FirebaseAuthSetup: Setting up firebase auth");
        // Declare FireBase Instance
        mAuth = FirebaseAuth.getInstance();
        // Declare FireBase Database Instance
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        // Get FireBase Database Reference
        myRef = mFirebaseDatabase.getReference();

        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                // Get User First Name and Last Name.
                firstname = firstNameTxt.getText().toString();
                lastname = lastNameTxt.getText().toString();

                Log.d(TAG, "onAuthStateChanged: User Name : " + firstname);
                Log.d(TAG, "onAuthStateChanged: User Name : " + lastname);

                if(user != null){
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged: Signed in " + user.getUid());

                    // Take Peak of the snapshot. Run only once.
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // If Successful
                            firebaseUtilities.RegisterNewUser(email,firstname,lastname);

                            Toast.makeText(mContext,"Registration Successfully Done",Toast.LENGTH_SHORT).show();

                            // Sign Out User
                            mAuth.signOut();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // If not Successful
                            Toast.makeText(mContext,"Registration Failed",Toast.LENGTH_SHORT).show();
                        }
                    });

                    // End activity and return back to login screen.
                    finish();

                }else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged: Signed Out");
                }
            }
        };

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



}
