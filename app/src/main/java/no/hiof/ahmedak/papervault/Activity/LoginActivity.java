package no.hiof.ahmedak.papervault.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
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
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import no.hiof.ahmedak.papervault.R;
import no.hiof.ahmedak.papervault.Utilities.FirebaseUtilities;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 *
 * TODO: Legg referance i en egen metode og kjør denne i OnCreate.
 */
public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    // UI references.
    private AutoCompleteTextView mEmailtxt;
    private EditText mPasswordtxt;
    private ProgressBar mProgressBar;
    private TextView mLoadingTxt;
    private View mLoginFormView;
    private Button mSignUpButton , mEmailSignInButton;

    // FireBase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;

    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Declare Context
        mContext = LoginActivity.this;

        // Call FireBase Method
        FirebaseAuthSetup();
        initialize();

        // Get UI Reference
        mEmailtxt= findViewById(R.id.email);
        mPasswordtxt = findViewById(R.id.password);
        mPasswordtxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {

                    return true;
                }
                return false;
            }
        });

        mLoginFormView = findViewById(R.id.email_login_form);
        // Set Login Form to visible if its hidden.
        mLoginFormView.setVisibility(View.VISIBLE);
        mProgressBar = findViewById(R.id.login_progress);
        // Set Progress Bar Visibility Hidden
        mProgressBar.setVisibility(View.GONE);
        mLoadingTxt = findViewById(R.id.Loading_txt);
        // Set Loading TextView Visibility Hidden
        mLoadingTxt.setVisibility(View.GONE);

        // sign Up Onclick
        mSignUpButton = findViewById(R.id.Sign_up_button);
        mSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent);

            }
        });

    }

    /**
     * Method checks if string is empty.
     * @param string
     */

    private boolean isStringNull(String string){
        Log.d(TAG, "isStringNull: checking if string is null");
        if(string.equals("")){
            return true;
        }else {
            return false;
        }
    }

    /**
     * We initialize FireBase sign in and move to MainActivity.
     * Email Verification Check will be checked on login.
     */
    private void initialize(){
        mEmailSignInButton = findViewById(R.id.login_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: attempting to log in");

                String email = mEmailtxt.getText().toString();
                String password = mPasswordtxt.getText().toString();

                if (isStringNull(email) && isStringNull(password)) {
                    Toast.makeText(mContext, "You must fill all the fields!", Toast.LENGTH_SHORT).show();

                } else {
                    // Set Progress Bar to visible if its hidden.
                    mProgressBar.setVisibility(View.VISIBLE);
                    // Set Progress Bar to visible if its hidden.
                    mLoadingTxt.setVisibility(View.VISIBLE);
                    // Set Login Form to hidden if its visible.
                    mLoginFormView.setVisibility(View.GONE);

                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();

                            if(task.isSuccessful()){
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "onComplete: Sign in SUCCESS!!");

                                // Check if Email is Verified, if not keep user signed out.
                                try{
                                    if(firebaseUser.isEmailVerified()){
                                        Log.d(TAG, "onComplete: isEmailVerified");
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(mContext,"Email is not verified \n check your mail inbox", Toast.LENGTH_LONG).show();
                                        // Set Login Form to visible if its hidden.
                                        mLoginFormView.setVisibility(View.VISIBLE);
                                        // Set Loading TextView Visibility Hidden
                                        mLoadingTxt.setVisibility(View.GONE);
                                        // Set Progress Bar Visibility Hidden
                                        mProgressBar.setVisibility(View.GONE);
                                        mAuth.signOut();
                                    }

                                }catch (NullPointerException e){

                                    Log.d(TAG, "onComplete: NullPointerException" + e.getMessage());
                                }

                            }else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "onComplete: ",task.getException() );
                                Toast.makeText(mContext,"Authentication failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    /**
     * FireBase Authentication Setup.
     * checks if user is already logged in.
     */

    public void FirebaseAuthSetup(){
        Log.d(TAG, "FirebaseAuthSetup: Setting up firebase auth");
        // Declare FireBase Instance.
        mAuth = FirebaseAuth.getInstance();

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

