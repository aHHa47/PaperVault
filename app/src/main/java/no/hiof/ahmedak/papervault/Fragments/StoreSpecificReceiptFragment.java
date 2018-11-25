package no.hiof.ahmedak.papervault.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import no.hiof.ahmedak.papervault.Adapters.ReceiptsAdapter;
import no.hiof.ahmedak.papervault.Model.Receipt;
import no.hiof.ahmedak.papervault.Model.Store;
import no.hiof.ahmedak.papervault.R;
import no.hiof.ahmedak.papervault.Utilities.FirebaseUtilities;

public class StoreSpecificReceiptFragment extends Fragment {
    private static final String TAG = "AllReceiptsFragment";
    private int ACTIVITY_NUMBER = 2;
    private RecyclerView AllReceiptsRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ReceiptsAdapter receiptsAdapter;
    private ImageView BackArrow;
    private ImageButton DeleteBtn, HeartBtn, ShareBtn;
    private DatabaseReference myRef;
    private FirebaseUtilities firebaseUtilities;
    private ArrayList<Receipt> receipts;
    private ArrayList<Store> stores;
    private Context mContext;

    private String SelectedStore_id;

    private OnCardViewSelectedListner onCardViewSelectedListner;

    public interface OnCardViewSelectedListner{
        void OnCardViewSelected(Receipt receipt, int ActivityNumber);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.cardview_store_receipt_fragment_layout, container, false);
        myRef = FirebaseDatabase.getInstance().getReference();
        // RecycleView All Receipts Tab
        AllReceiptsRecyclerView = view.findViewById(R.id.All_Receipts_RecycleView);
        AllReceiptsRecyclerView.setHasFixedSize(true);
        mContext = getActivity().getApplicationContext();

        DisplayReceipts();

        receipts = new ArrayList<>();
        stores = new ArrayList<>();
        firebaseUtilities = new FirebaseUtilities(mContext);

        BackArrow = view.findViewById(R.id.BackArrow);
        BackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();

            }
        });

        return view;
    }

    // Display Receipts
    private void DisplayReceipts() {
        Log.d(TAG, "DisplayReceipts: Displaying Receipts from database");
        try{
            SelectedStore_id = getReceiptsFromBundle().getStore_id();
            ACTIVITY_NUMBER = getActivityNumberFromBundle();
        }catch (NullPointerException e){

            Log.d(TAG, "DisplayReceipts: NullPointerException" + e.getMessage());
        }
        final ArrayList<String> mkeys = new ArrayList<>();

        // query our database for user receipts
        Query query = myRef.child(getString(R.string.dbname_receipt)).orderByChild(getString(R.string.store_id)).equalTo(SelectedStore_id);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                receipts.add(dataSnapshot.getValue(Receipt.class));
                String keys = dataSnapshot.getKey();
                mkeys.add(keys);

                // Query CardView Store Logo.
                // Query If store id is equal to receipt store id.
                Query query1 = myRef.child(getString(R.string.dbname_store)).orderByChild(getString(R.string.store_id)).equalTo(dataSnapshot.getValue(Receipt.class).getStore_id());

                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            stores.add(snapshot.getValue(Store.class));

                        }

                        // RecycleView layout Manager
                        mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                        AllReceiptsRecyclerView.setLayoutManager(mLayoutManager);

                        // RecycleView Adapter
                        receiptsAdapter = new ReceiptsAdapter(getContext(), receipts,stores, new ReceiptsAdapter.RecyclerViewClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                Toast.makeText(getContext(), receipts.get(position).getReceipt_title() + " Clicked", Toast.LENGTH_SHORT).show();
                                onCardViewSelectedListner.OnCardViewSelected(receipts.get(position), ACTIVITY_NUMBER);
                            }
                        }, new ReceiptsAdapter.FavorittViewClickListner() {
                            @Override
                            public void OnFavorittClicked(View view, int position) {

                                //getFavorite(position);
                                if(receipts.get(position).getFavorite().equals(true)){
                                    firebaseUtilities.addReceiptToFavorite(receipts.get(position).getReceipt_id(), false);
                                    Toast.makeText(mContext,"Removed from Favorite", Toast.LENGTH_SHORT).show();

                                }else{
                                    firebaseUtilities.addReceiptToFavorite(receipts.get(position).getReceipt_id(), true );
                                    Toast.makeText(mContext,"Added to Favorite", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        receiptsAdapter.notifyDataSetChanged();
                        AllReceiptsRecyclerView.setAdapter(receiptsAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled: Database Canceled" + databaseError.getMessage());

                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Receipt changedReceipt = dataSnapshot.getValue(Receipt.class);
                String key = dataSnapshot.getKey();
                int index = mkeys.indexOf(key);
                receipts.set(index,changedReceipt);
                receiptsAdapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
     * Return Receipt from Bundle
     * @return
     */
    private Store getReceiptsFromBundle(){
        Log.d(TAG, "getReceiptFromBundle: Getting Receipt From bundle");

        Bundle bundle = this.getArguments();

        if(bundle != null) {
            return bundle.getParcelable(getString(R.string.store));
        }else {
            return null;
        }
    }

    /**
     * return activity number from bundle
     * @return
     */
    private int getActivityNumberFromBundle(){
        Log.d(TAG, "getReceiptFromBundle: Getting Receipt From bundle");

        Bundle bundle = this.getArguments();

        if(bundle != null) {
            return bundle.getParcelable(getString(R.string.Activity_number));
        }else {
            return 0;
        }
    }



}
