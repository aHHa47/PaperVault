package no.hiof.ahmedak.papervault.Utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.ConnectException;

import static android.support.constraint.Constraints.TAG;

/**
 * FireBase methods for sign up and login.
 *
 */
public class FirebaseUtilities {

    private static final String TAG = "FirebaseUtilities";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private Context mContext;
    private String UserID;

    public FirebaseUtilities(Context context){
        mContext = context;
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            UserID = mAuth.getCurrentUser().getUid();
        }
    }

    /**
     * Register new user with Email and Password and send it to
     * FireBase Authentication.
     * @param email
     * @param password

     */
    public void RegisterUserWithEmail(final String email, String password){

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "onComplete:" + task.isSuccessful());

                if(!task.isSuccessful()){
                    Toast.makeText(mContext,"Failed to Authenticate",Toast.LENGTH_SHORT).show();

                }else if (task.isSuccessful()){
                    UserID = mAuth.getCurrentUser().getUid();
                    Log.d(TAG, "onComplete: AuthState Changed " + UserID);
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



}
