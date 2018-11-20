package no.hiof.ahmedak.papervault.Adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

import no.hiof.ahmedak.papervault.Model.Favorite;
import no.hiof.ahmedak.papervault.Model.Receipt;
import no.hiof.ahmedak.papervault.Model.Store;
import no.hiof.ahmedak.papervault.R;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {

    private static final String TAG = "StoreAdapter";
    private LayoutInflater layoutInflater;
    private ArrayList<Store> storeArrayList;
    private Context mContext;
    private RecyclerViewClickListenerStore clickListener;
    private int Count;
    private TextView receipt_count;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;



    public StoreAdapter(Context context, ArrayList<Store> storeArrayList , RecyclerViewClickListenerStore clickListenerStore){
        this.storeArrayList = storeArrayList;
        this.layoutInflater = LayoutInflater.from(context);
        this.clickListener = clickListenerStore;




    }
    @NonNull
    @Override
    public StoreAdapter.StoreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.store_cardview_item,viewGroup,false);
        mContext = v.getContext();
        StoreViewHolder storeViewHolder = new StoreViewHolder(v,clickListener);
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        Log.d(TAG, "StoreAdapter: Count " + Count);
        receipt_count = v.findViewById(R.id.receipt_count_txt);
        receipt_count.setText(String.valueOf(Count));

        return storeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final StoreAdapter.StoreViewHolder storeViewHolder, final int Position) {

        // Retrieve the data fro that position
        final Store store = storeArrayList.get(Position);
        final ArrayList<Receipt> receipts = new ArrayList<>();


        // Query get Specific Store Receipt Count
        final Query query = myRef.child(mContext.getString(R.string.dbname_receipt)).orderByChild(mContext.getString(R.string.user_id)).equalTo(mAuth.getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Count = 0;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    Receipt receipt = new Receipt();
                    Map<String,Object> objectMap = (HashMap<String, Object>)snapshot.getValue();

                    receipt.setReceipt_date(objectMap.get(mContext.getString(R.string.receipt_date)).toString());
                    receipt.setStore_id(objectMap.get(mContext.getString(R.string.store_id)).toString());
                    receipt.setAmount(Double.parseDouble(objectMap.get((mContext.getString(R.string.amount))).toString()));
                    receipt.setUser_id(objectMap.get(mContext.getString(R.string.user_id)).toString());
                    receipt.setImage_path(objectMap.get(mContext.getString(R.string.image_path)).toString());
                    receipt.setReceipt_title(objectMap.get(mContext.getString(R.string.receipt_title)).toString());
                    receipt.setReceipt_id(objectMap.get(mContext.getString(R.string.receipt_id)).toString());
                    //receipts.add(snapshot.getValue(Receipt.class));

                   /* List<Favorite> favoritesList = new ArrayList<Favorite>();
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.child(mContext.getString(R.string.favorite)).getChildren()){

                        Favorite favorite = new Favorite();
                        favorite.setLiked(dataSnapshot1.getValue(Favorite.class).getLiked());
                        favoritesList.add(favorite);

                    }
                    receipt.setFavorite(favoritesList);*/
                    receipt.setFavorite(Boolean.parseBoolean(objectMap.get(mContext.getString(R.string.favorite)).toString()));



                    receipts.add(receipt);

                    if(receipt.getStore_id().equals(storeArrayList.get(Position).getStore_id())){
                       // If we have a match then we increase our count for the store.
                        Count++;
                    }
                }

                Log.d(TAG, "onDataChange: Getting receipt Count" + Count);
                // Add Store Info to CardView.
                storeViewHolder.setData(store,Count);



                if(!dataSnapshot.exists()){
                    Count = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });



    }

    @Override
    public int getItemCount() {
        return storeArrayList.size();
    }

    // Inner Class StoreViewHolder Extends Recycle View ViewHolder
    public class StoreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView store_logo;
        private TextView receipt_count;
       RecyclerViewClickListenerStore mrecyclerViewClickListener;
        public StoreViewHolder(@NonNull View itemView, RecyclerViewClickListenerStore recyclerViewClickListener) {
            super(itemView);
            Log.d(TAG, "onBindViewHolder: Running here");

            mrecyclerViewClickListener = recyclerViewClickListener;
            store_logo = itemView.findViewById(R.id.Store_CardView_Image);
            receipt_count = itemView.findViewById(R.id.receipt_count_txt);


            // Set OnClickListener
            itemView.setOnClickListener(this);


        }


        public void setData(Store store,int Count){

            Log.d(TAG, "setData: Count: " + Count);
            Glide.with(mContext).load(store.getStore_logo()).into(store_logo);
            receipt_count.setText(String.valueOf(Count));

        }


        @Override
        public void onClick(View v) {
            // Get Adapter Position OnClick
            mrecyclerViewClickListener.onClick(v,getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListenerStore{
        void onClick(View view, int position);
    }

}


