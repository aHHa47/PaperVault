package no.hiof.ahmedak.papervault.Fragments;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import no.hiof.ahmedak.papervault.Activity.LoginActivity;
import no.hiof.ahmedak.papervault.R;

/**
 * This is a Fragment for Signing User out.
 * we also want to clear history when user is signed out.
 */
public class SigningOut_Fragment extends android.support.v4.app.Fragment {

    private static final String TAG = "SigningOut_Fragment";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private View layoutSigningOut;
    private ProgressBar progressBar;
    private TextView LoadingTxt;
    private Button signOutBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView  = inflater.inflate(R.layout.fragment_signing_out_,container,false);
        FirebaseAuthSetup();
        // Getting UI Reference
        LoadingTxt = myView.findViewById(R.id.Loading_txt_sign_out);
        progressBar = myView.findViewById(R.id.progressBar_sign_out);
        layoutSigningOut = myView.findViewById(R.id.linearLayout_signing_out);


        LoadingTxt.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        signOutBtn = myView.findViewById(R.id.Signing_out_btn);

        // Sign user out
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Signing Out User");
                LoadingTxt.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                layoutSigningOut.setVisibility(View.GONE);



                mAuth.signOut();
                getActivity().finish();
            }
        });

        return myView;
    }


    // Firebase Check setup.
    private void FirebaseAuthSetup(){
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
                    // Clear Activity Task and History
                    Intent intent = new Intent(getActivity(),LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }
            }
        };

    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuthListner != null){
            mAuth.removeAuthStateListener(mAuthListner);
        }
    }
}
