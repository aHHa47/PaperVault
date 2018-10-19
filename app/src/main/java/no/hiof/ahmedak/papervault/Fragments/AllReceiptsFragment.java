package no.hiof.ahmedak.papervault.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import no.hiof.ahmedak.papervault.Adapters.ReceiptsAdapter;
import no.hiof.ahmedak.papervault.Model.Receipt;
import no.hiof.ahmedak.papervault.R;
import no.hiof.ahmedak.papervault.Utilities.FirebaseUtilities;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllReceiptsFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "AllReceiptsFragment";
    private RecyclerView AllReceiptsRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private ArrayList<Integer> TempData;


    public AllReceiptsFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_all_receipts, container, false);

        // RecycleView All Receipts Tab
        AllReceiptsRecyclerView = view.findViewById(R.id.All_Receipts_RecycleView);
        AllReceiptsRecyclerView.setHasFixedSize(true);

        // RecycleView Manager


        DisplayReceipts();

        return view;


    }


    private void DisplayReceipts(){
        Log.d(TAG, "DisplayReceipts: Displaying Images from database");

        final ArrayList<Receipt> receipts = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // query our database for user receipts
        Query query = databaseReference.child(getString(R.string.dbname_receipt)).child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot SingleDs: dataSnapshot.getChildren()){
                    receipts.add(SingleDs.getValue(Receipt.class));
                }


                ArrayList<String> imgUrl = new ArrayList<>();

                for (int i = 0; i < receipts.size(); i++) {
                    imgUrl.add(receipts.get(i).getImage_path());
                    Log.d(TAG, "onDataChange: ImageUrls" + imgUrl.get(i));

                }

                mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                AllReceiptsRecyclerView.setLayoutManager(mLayoutManager);
                // RecycleView Adapter

                mAdapter = new ReceiptsAdapter(imgUrl);
                AllReceiptsRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: Database Canceled" + databaseError.getMessage());
            }
        });


    }




}
