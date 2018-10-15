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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import no.hiof.ahmedak.papervault.Model.User;
import no.hiof.ahmedak.papervault.R;

import static android.support.constraint.Constraints.TAG;

public class SignUpActivity extends Activity {


    EditText firstNameTxt, lastNameTxt, userPasswordTxt;
    AutoCompleteTextView userMailTxt;
    Button buttonSignIn, buttonRegister;

    User users;

    String firstname, lastname,email,password;

    private Context mContext;
    private String UserID;

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Declare FireBase Instance.
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            UserID = mAuth.getCurrentUser().getUid();
        }

        // Get reference
        firstNameTxt = findViewById(R.id.Register_user_FirstName);
        lastNameTxt = findViewById(R.id.Register_user_LastName);
        userPasswordTxt = findViewById(R.id.Register_user_Password);
        userMailTxt = findViewById(R.id.Register_user_Email);


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

    // validate User Registration
    private void ValidateRegistration() {

        /** Convert user input from edit boxes to strings.
        * to do validations and also be able to send it to FireBase.
        */

        firstname = firstNameTxt.getText().toString();
        lastname = lastNameTxt.getText().toString();
        password = userPasswordTxt.getText().toString();
        email = userMailTxt.getText().toString();

        // checks if field is empty. if empty, we show an error message.
        /*if(TextUtils.isEmpty(firstname)){
            firstNameTxt.setError(getString(R.string.first_name_missing));
        }

        if (TextUtils.isEmpty(lastname)){
            lastNameTxt.setError(getString(R.string.last_name_missing));
        }*/
        if (TextUtils.isEmpty(email)){
            userMailTxt.setError(getString(R.string.email_missing));
        }
        if (TextUtils.isEmpty(password)){
            userPasswordTxt.setError(getString(R.string.password_missing));
        }

        // if Everything is ok we register the user.
        RegisterUser();
    }


    // Register user
    private void RegisterUser() {

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "onComplete:" + task.isSuccessful());

                if(!task.isSuccessful()){
                    Toast.makeText(mContext,"Failed to Authenticate",Toast.LENGTH_SHORT).show();

                }else if (task.isSuccessful()){
                    UserID = mAuth.getCurrentUser().getUid();
                    Log.d(TAG, "onComplete: Authstate Changed " + UserID);
                }
            }
        });


    }


}
