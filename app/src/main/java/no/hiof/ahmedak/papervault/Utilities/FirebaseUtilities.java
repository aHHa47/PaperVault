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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import no.hiof.ahmedak.papervault.Model.User;
import no.hiof.ahmedak.papervault.R;


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
    private User user;
    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference myRef;

    public FirebaseUtilities(Context context){
        mContext = context;
        mAuth = FirebaseAuth.getInstance();
        // Declare FireBase Database Instance
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        // Get FireBase Database Reference
        myRef = mfirebaseDatabase.getReference();

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

                if(task.isSuccessful()){
                    // Task is success send email Verification, and get User ID
                    SendEmailVerification();

                    UserID = mAuth.getCurrentUser().getUid();
                    Log.d(TAG, "onComplete: AuthState Changed " + UserID);
                }
                else{
                    Toast.makeText(mContext,"Failed to Authenticate",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * Used for Sending Email Verification
     */
    public void SendEmailVerification(){
        // Get FireBase User
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){

                    }
                    else {

                        Toast.makeText(mContext,"Failed to send verification mail.",Toast.LENGTH_SHORT);
                    }

                }
            });
        }
    }


    /**
     * Register New User with additional information.
     * @param email
     * @param firstname
     * @param lastname
     */
    public void RegisterNewUser(String email, String firstname,String lastname){
        user = new User(firstname,lastname,email,UserID);

        // Send ref to database node
        myRef.child(mContext.getString(R.string.dbname_Users)).child(UserID).setValue(user);



    }

    public User getUsersInformation(DataSnapshot snapshot){
        Log.d(TAG, "getUsersInformation: Getting user information from FireBase Database");

        User user = new User();


        // looping through our Database
        for(DataSnapshot ds: snapshot.getChildren()){
            if(ds.getKey().equals(mContext.getString(R.string.dbname_Users))){
                Log.d(TAG, "getUsersInformation: Datasnapshot:" + ds);
                Log.d(TAG, "getUsersInformation: Datasnapshot Email:" + ds.child(UserID).getValue(User.class).geteMail());

                try{
                    user.seteMail(ds.child(UserID).getValue(User.class).geteMail());
                    user.setFirstName(ds.child(UserID).getValue(User.class).getFirstName());
                    user.setLastName(ds.child(UserID).getValue(User.class).getLastName());



                }catch (NullPointerException e){
                    Log.d(TAG, "getUsersInformation: NullPointerException" + e.getMessage());
                }




            }

        }
        return user;

    }







}
