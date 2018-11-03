package no.hiof.ahmedak.papervault.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class AllReceiptsFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "AllReceiptsFragment";
    private static  final int ACTIVITY_NUMBER = 4;
    private RecyclerView AllReceiptsRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ImageButton DeleteBtn, HeartBtn, ShareBtn;

    private OnCardViewSelectedListner onCardViewSelectedListner;

    public interface OnCardViewSelectedListner{
        void OnCardViewSelected(Receipt receipt,int ActivityNumber);
    }

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
        // Init Methods
        DisplayReceipts();
        //CardViewButtonsInit(view);



        return view;


    }


    /**
     * Init our CardView Button methods
     * @param view
     */
    private void CardViewButtonsInit(View view){
        Log.d(TAG, "CardViewButtonsInit: ");
        // Favorite Button
        HeartBtn = view.findViewById(R.id.CardView_FavoriteBtn);
        HeartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddToFavoriteTab();
            }
        });

        // Delete Button
        DeleteBtn = view.findViewById(R.id.CardView_DeleteBtn);
        DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteSelectedReceipt();
            }
        });


        // Share Button
        ShareBtn = view.findViewById(R.id.CardView_ShareBtn);
        ShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareReceipt();
            }
        });
    }



    /**
     * Display All receipts to CardView List
     */
    private void DisplayReceipts(){
        Log.d(TAG, "DisplayReceipts: Displaying Receipts from database");

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

                // RecycleView layout Manager
                mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                AllReceiptsRecyclerView.setLayoutManager(mLayoutManager);

                // RecycleView Adapter
                mAdapter = new ReceiptsAdapter(getContext(),receipts, new ReceiptsAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Toast.makeText(getContext(),receipts.get(position).getReceipt_title() + " Clicked",Toast.LENGTH_SHORT).show();
                        onCardViewSelectedListner.OnCardViewSelected(receipts.get(position),ACTIVITY_NUMBER);
                    }
                });
                AllReceiptsRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: Database Canceled" + databaseError.getMessage());
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

    /**
     * Share receipt
     */
    private void ShareReceipt() {
        Log.d(TAG, "ShareReceipt: Sharing Receipt ");
    }

    /**
     * Delete Selected Receipts
     */
    private void DeleteSelectedReceipt() {
        Log.d(TAG, "DeleteSelectedReceipt: Deleting Receipts ");
    }

    /**
     * Add receipts to Favorite Tab
     */
    private void AddToFavoriteTab() {
        Log.d(TAG, "AddToFavoriteTab: Adding Receipt to Favorite Tab ");
    }



}
