package no.hiof.ahmedak.papervault.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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
    private FirebaseAuth mAuth;
    private User users;
    private FirebaseUtilities firebaseUtilities;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mContext = SignUpActivity.this;
        // Declare FireBase Instance.
        mAuth = FirebaseAuth.getInstance();
        firebaseUtilities = new FirebaseUtilities(mContext);

        if(mAuth.getCurrentUser() != null){
            UserID = mAuth.getCurrentUser().getUid();
        }

        GetUiReference();


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
        RegisterUserFormView.setVisibility(View.VISIBLE);
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

        firstname = firstNameTxt.getText().toString();
        lastname = lastNameTxt.getText().toString();
        password = userPasswordTxt.getText().toString();
        email = userMailTxt.getText().toString();
        // if Everything is ok we register the user, and set progress bar and loading text to visible
        if(CheckUserInput(firstname,lastname,email,password)){
            RegisterUserFormView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            loadingTxt.setVisibility(View.VISIBLE);

            firebaseUtilities.RegisterUserWithEmail(email,password);
        }


    }

    private boolean CheckUserInput(String firstName, String lastName,String Email,String Password){
        Log.d(TAG, "CheckUserInput: Checking User Input for null value");
        if(firstName.equals("") || lastName.equals("") || Email.equals("") || Password.equals("") ){
            Toast.makeText(mContext,"All fields must be filled out",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }




}
