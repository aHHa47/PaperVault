package no.hiof.ahmedak.papervault.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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
import no.hiof.ahmedak.papervault.Utilities.CommonUtils;
import no.hiof.ahmedak.papervault.Utilities.FavoriteHeart;
import no.hiof.ahmedak.papervault.Utilities.FirebaseUtilities;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllReceiptsFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "AllReceiptsFragment";
    private static  final int ACTIVITY_NUMBER = 4;
    private RecyclerView AllReceiptsRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ReceiptsAdapter receiptsAdapter;
    private ArrayList<Receipt> receipts;
    private ArrayList<Store> stores;
    private Boolean addedToFavorite;
    private FavoriteHeart favoriteHeart;
    private Context mContext;
    private DatabaseReference myRef;
    private FirebaseUtilities firebaseUtilities;
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
        myRef = FirebaseDatabase.getInstance().getReference();
        mContext = getActivity().getApplicationContext();

        firebaseUtilities = new FirebaseUtilities(mContext);
        // RecycleView All Receipts Tab
        AllReceiptsRecyclerView = view.findViewById(R.id.All_Receipts_RecycleView);
        AllReceiptsRecyclerView.setHasFixedSize(true);

        receipts = new ArrayList<>();
        stores = new ArrayList<>();

        // Init Methods
        DisplayReceipts();




        return view;


    }



    /**
     * Display All receipts to CardView List
     */
    private void DisplayReceipts(){
        Log.d(TAG, "DisplayReceipts: Displaying Receipts from database");



        final ArrayList<String> mkeys = new ArrayList<>();
        // query our database for user receipts
        Query query = myRef.child(getString(R.string.dbname_receipt)).orderByChild(getString(R.string.user_id)).equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                receipts.add(dataSnapshot.getValue(Receipt.class));
                    String Keys = dataSnapshot.getKey();
                    mkeys.add(Keys);
                    Query query1 = myRef.child(getString(R.string.dbname_store)).orderByChild(getString(R.string.store_id)).equalTo(dataSnapshot.getValue(Receipt.class).getStore_id());

                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                stores.add(snapshot.getValue(Store.class));

                                // RecycleView layout Manager
                                mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                                AllReceiptsRecyclerView.setLayoutManager(mLayoutManager);

                                // RecycleView Adapter
                                receiptsAdapter = new ReceiptsAdapter(getContext(),receipts,stores, new ReceiptsAdapter.RecyclerViewClickListener() {
                                    @Override
                                    public void onClick(View view, final int position) {

                                        // Clicked on Card.
                                        onCardViewSelectedListner.OnCardViewSelected(receipts.get(position),ACTIVITY_NUMBER);
                                    }
                                }, new ReceiptsAdapter.FavorittViewClickListner(){

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
                            if(!dataSnapshot.exists()){
                                Log.d(TAG, "onDataChange: No snappshot found");
                            }




                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

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


    // On LongPressed open action menu for the receipt
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int mSelectedItem = item.getOrder();
        if(item.getTitle() == CommonUtils.SHARE){
            ShareReceipt(receiptsAdapter.getItem(mSelectedItem));
        }
        else if(item.getTitle() == CommonUtils.DELETE){
                DeleteSelectedReceipt(receiptsAdapter.getItem(mSelectedItem));
                receiptsAdapter.remove(mSelectedItem);
                receiptsAdapter.notifyDataSetChanged();
                Toast.makeText(mContext,"Delete ", Toast.LENGTH_SHORT).show();
            }


        return super.onContextItemSelected(item);
    }

    /**
     * Sharing receipt Url with different apps
     * @param item
     */
    private void ShareReceipt(Receipt item) {
        Intent ShareIntent = new Intent();
        ShareIntent.setType("text/plain ");
        ShareIntent.putExtra(Intent.EXTRA_TEXT, item.getImage_path());
        ShareIntent.putExtra(Intent.EXTRA_SUBJECT,"Share via PaperVault");
        startActivity(Intent.createChooser(ShareIntent,"Share"));
        }

    /**
     * Delete Selected Receipts
     */
    private void DeleteSelectedReceipt(Receipt item) {

        Log.d(TAG, "DeleteSelectedReceipt: Deleting Receipts ");
        myRef.child(mContext.getString(R.string.dbname_receipt)).child(item.getReceipt_id()).removeValue();
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



}
