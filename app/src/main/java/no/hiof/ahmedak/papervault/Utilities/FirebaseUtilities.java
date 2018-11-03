package no.hiof.ahmedak.papervault.Utilities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import no.hiof.ahmedak.papervault.Activity.MainActivity;
import no.hiof.ahmedak.papervault.Model.Receipt;
import no.hiof.ahmedak.papervault.Model.Store;
import no.hiof.ahmedak.papervault.Model.User;
import no.hiof.ahmedak.papervault.R;


/**
 * FireBase methods for sign up and login.
 *
 */
public class FirebaseUtilities {

    private static final String TAG = "FirebaseUtilities";

    private FirebaseAuth mAuth;
    private Context mContext;
    private String UserID;
    private User user;
    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference myRef;
    private StorageReference storageReference;

    private String ImageStorgePath = "photos/users/";
    private double photoUploadProgress = 0;


    public FirebaseUtilities(Context context){
        mContext = context;
        mAuth = FirebaseAuth.getInstance();
        // Declare FireBase Database Instance
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        // Get FireBase Database Reference
        myRef = mfirebaseDatabase.getReference();

        storageReference = FirebaseStorage.getInstance().getReference();

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

    /**
     * Gets User Information from @param
     * @param snapshot
     * @return user
     */
    public User getUsersInformation(DataSnapshot snapshot){
        Log.d(TAG, "getUsersInformation: Getting user information from FireBase Database");

        User user = new User();


        // looping through our Database
        for(DataSnapshot ds: snapshot.getChildren()){
            if(ds.getKey().equals(mContext.getString(R.string.dbname_Users))){
                Log.d(TAG, "getUsersInformation: Datasnapshot:" + ds);

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


    /**
     * Return count for all receipt in @param
     * @param dataSnapshot
     * @return
     */
    public int GetRceieptImageCount(DataSnapshot dataSnapshot){

        int count = 0;
        for (DataSnapshot ds: dataSnapshot.child(mContext.getString(R.string.dbname_receipt)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getChildren()){

            count++;
        }

        return count;

    }


    /**
     * Upload Image to FireBase
     * Register new Receipt
     * @param imagepath
     * @param count
     * @param title
     * @param date
     * @param price
     */
    public void UploadReceiptImage(final String imagepath , int count , final String title, final String date, final double price, final String store_id){

        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        storageReference = storageReference.child(ImageStorgePath + "/" + user_id + "/photo" + (count + 1));
        Bitmap bitmap = ImageUtils.getBitmap(imagepath);


        byte[] getBytes = ImageUtils.ConvertByteFromBitmap(bitmap, 100);
        final UploadTask uploadTask = storageReference.putBytes(getBytes);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final Task<Uri> taskUri = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if(!task.isSuccessful()){
                            throw task.getException();
                        }

                        return storageReference.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if(task.isSuccessful()){

                            Uri fireBaseUri = task.getResult();

                            RegisterNewReceipt(fireBaseUri.toString(),title,date,price, store_id);
                            // send user back to MainActivity
                            Intent intent = new Intent(mContext,MainActivity.class);
                            mContext.startActivity(intent);

                        }

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(mContext,"Failed to Upload Photo", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();

                // Print out our uploading progress
                // if progress is higher than 15, then we print our our progress.
                if(progress - 15 > photoUploadProgress){
                    Toast.makeText(mContext,"Photo Upload Progress: " + String.format("%.0f",progress) + "%",Toast.LENGTH_SHORT).show();
                    photoUploadProgress = progress;
                }
        }
        });


    }


    /**
     * Register new User Receipt
     * @param ImageUrl
     * @param title
     * @param date
     * @param price
     */
    public void RegisterNewReceipt(String ImageUrl, String title, String date, double price, String store_id){

        String Photo_id = myRef.child(mContext.getString(R.string.dbname_photo)).push().getKey();

        Receipt receipt = new Receipt();
        receipt.setReceipt_title(title);
        receipt.setImage_path(ImageUrl);
        receipt.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
        receipt.setReceipt_id(Photo_id);
        receipt.setReceipt_date(date);
        receipt.setAmount(price);
        receipt.setStore_id(store_id);

        myRef.child(mContext.getString(R.string.dbname_receipt)).child(Photo_id).setValue(receipt);
    }


    /**
     * Register New Store
     * @param Name
     * @param Retail
     * @param Logo
     * @param lat
     * @param lon
     */
    public void RegisterNewStore(String Name, String Retail,String Logo, long lat,long lon){

        // Getting Store Logo ID.
        String Logo_id = myRef.child(mContext.getString(R.string.dbname_store)).push().getKey();

        Store store = new Store();
        store.setStore_name(Name);
        store.setStore_retail(Retail);
        store.setStore_logo(Logo);
        store.setStore_id(Logo_id);
        store.setStore_lat(lat);
        store.setStore_long(lon);


        myRef.child(mContext.getString(R.string.dbname_store)).child(Logo_id).setValue(store);

    }





}
