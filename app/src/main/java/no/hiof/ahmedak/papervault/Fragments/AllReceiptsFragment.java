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
import android.widget.ImageButton;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.hiof.ahmedak.papervault.Adapters.ReceiptsAdapter;
import no.hiof.ahmedak.papervault.Model.Favorite;
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
    private Boolean addedToFavorite;

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
        // Init Methods
        DisplayReceipts();




        return view;


    }






    /**
     * Display All receipts to CardView List
     */
    private void DisplayReceipts(){
        Log.d(TAG, "DisplayReceipts: Displaying Receipts from database");


        final ArrayList<Store> stores = new ArrayList<>();
        // Getting DataBase ref
        final
        // query our database for user receipts
        Query query = myRef.child(getString(R.string.dbname_receipt)).orderByChild(getString(R.string.user_id)).equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                Receipt receipt = new Receipt();
                Map<String,Object> objectMap = (HashMap<String, Object>)dataSnapshot.getValue();

                receipt.setReceipt_date(objectMap.get(getString(R.string.receipt_date)).toString());
                receipt.setStore_id(objectMap.get(getString(R.string.store_id)).toString());
                receipt.setAmount(Double.parseDouble(objectMap.get(getString(R.string.amount)).toString()));
                receipt.setUser_id(objectMap.get(getString(R.string.user_id)).toString());
                receipt.setImage_path(objectMap.get(getString(R.string.image_path)).toString());
                receipt.setReceipt_title(objectMap.get(getString(R.string.receipt_title)).toString());
                receipt.setReceipt_id(objectMap.get(getString(R.string.receipt_id)).toString());
                //receipts.add(snapshot.getValue(Receipt.class));

                /*List<Favorite> favoritesList = new ArrayList<Favorite>();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.child(getString(R.string.favorite)).getChildren()){

                    Favorite favorite = new Favorite();
                    favorite.setLiked(dataSnapshot1.getValue(Favorite.class).getLiked());
                    favoritesList.add(favorite);

                }
                receipt.setFavorite(favoritesList);*/
                receipt.setFavorite(Boolean.parseBoolean(objectMap.get(getString(R.string.favorite)).toString()));



                receipts.add(receipt);

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
                            mAdapter = new ReceiptsAdapter(getContext(),receipts, stores, new ReceiptsAdapter.RecyclerViewClickListener() {
                                @Override
                                public void onClick(View view, final int position) {

                                        // Clicked on Card.
                                        onCardViewSelectedListner.OnCardViewSelected(receipts.get(position),ACTIVITY_NUMBER);
                                }
                            }, new ReceiptsAdapter.FavorittViewClickListner(){

                                @Override
                                public void OnFavorittClicked(View view, int position) {

                                    // add new receipt to Favorite
                                    firebaseUtilities.addReceiptToFavorite(receipts.get(position).getReceipt_id());
                                }
                            });

                            mAdapter.notifyDataSetChanged();
                            AllReceiptsRecyclerView.setAdapter(mAdapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

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

            }
        });

    }







    // On LongPressed open action menu for the receipt
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle() == CommonUtils.SHARE){
            // TODO: Add Logic to Share Button
           // ShareReceipt(mAdapter.getItemId(item.getOrder()),mAdapter.getItem(item.getOrder()));
            Toast.makeText(mContext,"Sharing is Caring ", Toast.LENGTH_SHORT).show();

        }
        else if(item.getTitle() == CommonUtils.DELETE){
            // TODO: Add Logic to Delete Button
            //DeleteSelectedReceipt(mAdapter.getItemId(item.getOrder()));
            Toast.makeText(mContext,"Delete ", Toast.LENGTH_SHORT).show();

        }


        return super.onContextItemSelected(item);
    }

   /* private void ShareReceipt(long itemId, Receipt item) {
        Intent ShareIntent = new Intent();
        ShareIntent.setType("text/plain");
        ShareIntent.putExtra(Intent.EXTRA_TEXT, item.getReceipt_title());
        ShareIntent.putExtra(Intent.EXTRA_SUBJECT,"Share via");
        startActivity(Intent.createChooser(ShareIntent,"Share"));

    }*/

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
     * Delete Selected Receipts
     */
    private void DeleteSelectedReceipt(Long key ) {

        Log.d(TAG, "DeleteSelectedReceipt: Deleting Receipts ");



    }

    /**
     * Add receipts to Favorite Tab
     */
    private void AddToFavoriteTab() {
        Log.d(TAG, "AddToFavoriteTab: Adding Receipt to Favorite Tab ");
    }



}
