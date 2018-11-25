package no.hiof.ahmedak.papervault.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import no.hiof.ahmedak.papervault.Adapters.FavoriteReceiptsAdapter;
import no.hiof.ahmedak.papervault.Adapters.ReceiptsAdapter;
import no.hiof.ahmedak.papervault.Model.Receipt;
import no.hiof.ahmedak.papervault.Model.Store;
import no.hiof.ahmedak.papervault.R;
import no.hiof.ahmedak.papervault.Utilities.FirebaseUtilities;

public class FavoriteReceiptsFragment extends Fragment {
    private static final String TAG = "FavoriteReceiptsFragmen";
    private Context mContext;
    private static  final int ACTIVITY_NUMBER = 6;
    private DatabaseReference myRef;
    private ArrayList<Receipt> receipts;
    private ArrayList<Store> stores;
    private FavoriteReceiptsAdapter receiptsAdapter;
    private RecyclerView AllReceiptsRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseUtilities firebaseUtilities;
    private OnCardViewSelectedListner onCardViewSelectedListner;
    private TextView emptyListText;


    public interface OnCardViewSelectedListner{
        void OnCardViewSelected(Receipt receipt,int ActivityNumber);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_my_favorite, container, false);
        setHasOptionsMenu(true);
        mContext = getActivity().getApplicationContext();
        receipts = new ArrayList<>();
        stores = new ArrayList<>();

        firebaseUtilities = new FirebaseUtilities(mContext);
        AllReceiptsRecyclerView = view.findViewById(R.id.All_Receipts_RecycleView);
        AllReceiptsRecyclerView.setHasFixedSize(true);
        emptyListText = view.findViewById(R.id.emptyListText);
        myRef = FirebaseDatabase.getInstance().getReference();
        getFavorite();

        return view;

    }


    private void getFavorite(){
        Log.d(TAG, "DisplayReceipts: Displaying Receipts from database");
        // query our database for user receipts
        Query query = myRef.child(getString(R.string.dbname_receipt)).orderByChild(getString(R.string.favorite)).equalTo(true);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if(dataSnapshot.getValue(Receipt.class).getUser_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){

                    if(receipts.isEmpty()){
                        emptyListText.setVisibility(View.VISIBLE);
                    }

                    receipts.add(dataSnapshot.getValue(Receipt.class));


                    Query query1 = myRef.child(getString(R.string.dbname_store)).orderByChild(getString(R.string.store_id)).equalTo(dataSnapshot.getValue(Receipt.class).getStore_id());

                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                stores.add(snapshot.getValue(Store.class));

                                emptyListText.setVisibility(View.GONE);
                                // RecycleView layout Manager
                                mLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
                                AllReceiptsRecyclerView.setLayoutManager(mLayoutManager);

                                // RecycleView Adapter
                                receiptsAdapter = new FavoriteReceiptsAdapter(mContext,receipts,stores, new FavoriteReceiptsAdapter.RecyclerViewClickListener() {
                                    @Override
                                    public void onClick(View view, final int position) {

                                        // Clicked on Card.
                                        onCardViewSelectedListner.OnCardViewSelected(receipts.get(position),ACTIVITY_NUMBER);

                                    }
                                }, new FavoriteReceiptsAdapter.FavorittViewClickListner(){

                                    @Override
                                    public void OnFavorittClicked(View view, int position) {
                                        //getFavorite(position);
                                        if(receipts.get(position).getFavorite().equals(true)){
                                            firebaseUtilities.addReceiptToFavorite(receipts.get(position).getReceipt_id(), false);
                                            Toast.makeText(mContext,"Removed from Favorite", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                receiptsAdapter.notifyDataSetChanged();
                                AllReceiptsRecyclerView.setAdapter(receiptsAdapter);
                            }if(!dataSnapshot.exists()){

                                Log.d(TAG, "onDataChange: Store Data not found");
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
                else if(!dataSnapshot.exists()){
                    emptyListText.setVisibility(View.VISIBLE);
                }



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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_my_favorite_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection Simplifiable Case Statement
        switch (id){
            case R.id.item_sort_alpha:
                Toast.makeText(mContext,"Sorted By Title",Toast.LENGTH_LONG).show();
                receiptsAdapter.SortListAlpha();
                break;
            case R.id.item_sort_date:
                Toast.makeText(mContext,"Sorted By Date",Toast.LENGTH_LONG).show();
                receiptsAdapter.SortByDate();
                break;
            case R.id.item_sort_price:
                Toast.makeText(mContext,"Sorted By Price", Toast.LENGTH_LONG).show();
                receiptsAdapter.SortByPrice();
                break;
            case R.id.item_sort_near_me:
                Toast.makeText(mContext,"Sort button Near me", Toast.LENGTH_LONG).show();
                break;
            default:
                break;


        }

        return super.onOptionsItemSelected(item);
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
