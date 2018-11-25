package no.hiof.ahmedak.papervault.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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
                    receipts.add(snapshot.getValue(Receipt.class));

                    if(snapshot.getValue(Receipt.class).getStore_id().equals(storeArrayList.get(Position).getStore_id())){
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


