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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.hiof.ahmedak.papervault.Adapters.ReceiptsAdapter;
import no.hiof.ahmedak.papervault.Model.Favorite;
import no.hiof.ahmedak.papervault.Model.Receipt;
import no.hiof.ahmedak.papervault.Model.Store;
import no.hiof.ahmedak.papervault.R;

public class StoreSpecificReceiptFragment extends Fragment {
    private static final String TAG = "AllReceiptsFragment";
    private int ACTIVITY_NUMBER = 2;
    private RecyclerView AllReceiptsRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ImageView BackArrow;
    private ImageButton DeleteBtn, HeartBtn, ShareBtn;
    private DatabaseReference myRef;


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

        DisplayReceipts();

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

        final ArrayList<Receipt> receipts = new ArrayList<>();
        final ArrayList<Store> stores = new ArrayList<>();

        try{
            SelectedStore_id = getReceiptsFromBundle().getStore_id();
            ACTIVITY_NUMBER = getActivityNumberFromBundle();
        }catch (NullPointerException e){

            Log.d(TAG, "DisplayReceipts: NullPointerException" + e.getMessage());
        }
        // Getting DataBase ref

        // TODO: Change Data Base Query for
        // query our database for user receipts
        Query query = myRef.child(getString(R.string.dbname_receipt)).orderByChild(getString(R.string.store_id)).equalTo(SelectedStore_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot SingleDs: dataSnapshot.getChildren()){
                    Receipt receipt = new Receipt();
                    Map<String,Object> objectMap = (HashMap<String, Object>)SingleDs.getValue();

                    receipt.setReceipt_date(objectMap.get(getString(R.string.receipt_date)).toString());
                    receipt.setStore_id(objectMap.get(getString(R.string.store_id)).toString());
                    receipt.setAmount(Double.parseDouble(objectMap.get((getString(R.string.amount))).toString()));
                    receipt.setUser_id(objectMap.get(getString(R.string.user_id)).toString());
                    receipt.setImage_path(objectMap.get(getString(R.string.image_path)).toString());
                    receipt.setReceipt_title(objectMap.get(getString(R.string.receipt_title)).toString());
                    receipt.setReceipt_id(objectMap.get(getString(R.string.receipt_id)).toString());
                    //receipts.add(snapshot.getValue(Receipt.class));

                   /* List<Favorite> favoritesList = new ArrayList<Favorite>();
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.child(getString(R.string.favorite)).getChildren()){

                        Favorite favorite = new Favorite();
                        favorite.setLiked(dataSnapshot1.getValue(Favorite.class).getLiked());
                        favoritesList.add(favorite);

                    }
                    receipt.setFavorite(favoritesList);*/

                    receipt.setFavorite(Boolean.parseBoolean(objectMap.get(getString(R.string.favorite)).toString()));


                    receipts.add(receipt);

                    // Query CardView Store Logo.
                    Query query1 = myRef.child(getString(R.string.dbname_store)).orderByChild(getString(R.string.store_id)).equalTo(receipt.getStore_id());

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
                            mAdapter = new ReceiptsAdapter(getContext(), receipts, stores, new ReceiptsAdapter.RecyclerViewClickListener() {
                                @Override
                                public void onClick(View view, int position) {
                                    Toast.makeText(getContext(), receipts.get(position).getReceipt_title() + " Clicked", Toast.LENGTH_SHORT).show();
                                    onCardViewSelectedListner.OnCardViewSelected(receipts.get(position), ACTIVITY_NUMBER);
                                }
                            }, new ReceiptsAdapter.FavorittViewClickListner() {
                                @Override
                                public void OnFavorittClicked(View view, int position) {
                                    Toast.makeText(getContext(),"Getting Clicked " + receipts.get(position).getReceipt_title(),Toast.LENGTH_SHORT).show();

                                }
                            });
                            AllReceiptsRecyclerView.setAdapter(mAdapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e(TAG, "onCancelled: Database Canceled" + databaseError.getMessage());

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: Database Canceled" + databaseError.getMessage());
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
