package no.hiof.ahmedak.papervault.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import no.hiof.ahmedak.papervault.Adapters.StoreAdapter;
import no.hiof.ahmedak.papervault.Model.Receipt;
import no.hiof.ahmedak.papervault.Model.Store;
import no.hiof.ahmedak.papervault.Model.User;
import no.hiof.ahmedak.papervault.R;
import no.hiof.ahmedak.papervault.Utilities.FirebaseUtilities;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoreFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "StoreFragment";
    private static  final int ACTIVITY_NUMBER = 3;
    private RecyclerView StoreTabRecycleView;
    private RecyclerView.LayoutManager mlayoutManager;
    private RecyclerView.Adapter madapter;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private FirebaseUtilities firebaseUtilities;
    private DatabaseReference myRef;

    private FloatingActionButton fab;

    private ArrayList<Store> stores;
    private int count;


    private OnCardViewSelectedListner onCardViewSelectedListner;

    public interface OnCardViewSelectedListner{
        void OnCardViewSelected(Store store,int ActivityNumber);
    }
    public StoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_store, container, false);
        FirebaseAuthSetup();
        DisplayStores();
        stores = new ArrayList<>();
        // Add new Store Floating Button
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddnewStore();
            }
        });





        // Recycle View Store Tab
        StoreTabRecycleView = view.findViewById(R.id.Store_Tab_RecycleView);
        StoreTabRecycleView.setHasFixedSize(true);

        return  view;
    }


    /**
     * Open Add new store Dialog
     */
    private void AddnewStore(){
        StoreDialogFragment storeDialogFragment = new StoreDialogFragment();
        storeDialogFragment.show(getFragmentManager(), "AddnewStoreDialog");

    }


    /**
     * FireBase Authentication Setup
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

        myRef = FirebaseDatabase.getInstance().getReference();

    }


    /**
     * Display stores
     */
    private void DisplayStores(){
        Log.d(TAG, "DisplayStores: Displaying our stores from Database");

        Query query = myRef.child(getString(R.string.dbname_store)).orderByChild(getString(R.string.user_id)).equalTo(mAuth.getCurrentUser().getUid());
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                stores.add(dataSnapshot.getValue(Store.class));
                // Setup LayOut Manager with Grid layout and 2 SpanCount
                mlayoutManager = new GridLayoutManager(getContext(),2);
                StoreTabRecycleView.setLayoutManager(mlayoutManager);
                myRef = FirebaseDatabase.getInstance().getReference();

                // Recycle View Adapter
                madapter = new StoreAdapter(getContext(),stores, new StoreAdapter.RecyclerViewClickListenerStore() {
                    @Override
                    public void onClick(View view, int position) {

                        Toast.makeText(getContext(),stores.get(position).getStore_name() + " Clicked",Toast.LENGTH_SHORT).show();
                        onCardViewSelectedListner.OnCardViewSelected(stores.get(position),ACTIVITY_NUMBER);
                    }
                });
                StoreTabRecycleView.setAdapter(madapter);



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
                Log.d(TAG, "onCancelled: DatabaseError" + databaseError.getMessage());
            }
        });


    }



    @Override
    public void onAttach(Context context) {
        try {
            onCardViewSelectedListner = (OnCardViewSelectedListner) getActivity();
        }catch (ClassCastException e){
            Log.d(TAG, "onAttach: ClassCastException " + e.getMessage());
            e.printStackTrace();
        }

        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        // look for user Auth on start
        mAuth.addAuthStateListener(mAuthListner);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuthListner != null){
            // Stop and remove Auth listener
            mAuth.removeAuthStateListener(mAuthListner);
        }
    }

}
