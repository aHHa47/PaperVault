package no.hiof.ahmedak.papervault.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import no.hiof.ahmedak.papervault.Fragments.StoreSpecificReceiptFragment;
import no.hiof.ahmedak.papervault.Model.Favorite;
import no.hiof.ahmedak.papervault.Model.Receipt;
import no.hiof.ahmedak.papervault.Model.Store;
import no.hiof.ahmedak.papervault.R;
import no.hiof.ahmedak.papervault.Utilities.CommonUtils;
import no.hiof.ahmedak.papervault.Utilities.FavoriteHeart;


public class ReceiptsAdapter extends RecyclerView.Adapter<ReceiptsAdapter.MyViewHolder>{

    private static final String TAG = "ReceiptsAdapter";
    private LayoutInflater layoutInflater;
    private ArrayList<Receipt> ReceiptList;
    private Context mContext;
    private RecyclerViewClickListener mclickListener;
    private DatabaseReference myRef;
    private FavorittViewClickListner favorittViewClickListner;

    private ArrayList<Store>storeArrayList;

    /**
     * Return ArrayList of Receipts info.
     */
    public ReceiptsAdapter(Context context,ArrayList<Receipt> ReceiptList, ArrayList<Store> stores, RecyclerViewClickListener clickListener,FavorittViewClickListner listner){
        this.layoutInflater = LayoutInflater.from(context);
        this.ReceiptList = ReceiptList;
        this.mContext = context;
        this.mclickListener = clickListener;
        this.storeArrayList = stores;
        this.favorittViewClickListner = listner;
    }


    @NonNull
    @Override
    public ReceiptsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = layoutInflater.from(viewGroup.getContext()).inflate(R.layout.receipts_cardview_items,viewGroup,false);
        myRef = FirebaseDatabase.getInstance().getReference();



        return new MyViewHolder(v, mclickListener, favorittViewClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptsAdapter.MyViewHolder myViewHolder, int position) {
        // Retrieve the data for that position
        Receipt receipt = ReceiptList.get(position);
        Store store = storeArrayList.get(position);

        // Add Receipt Info to CardView.
        myViewHolder.setData(receipt,store);

    }

    @Override
    public int getItemCount() {
        return ReceiptList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
    public Receipt getItem(int position) {
        return ReceiptList.get(position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

        private ImageView Receipt_imageview,StoreLogoImage,orangeHeartBtn,whiteHeartBtn;
        private TextView Receipt_title_txt, Receipt_date_txt, Receipt_location_txt, Receipt_price_txt;
        private RecyclerViewClickListener mrecyclerViewClickListener;
        private FavoriteHeart favoriteHeart;
        private FavorittViewClickListner listner;
        private boolean likedByUser;

        public MyViewHolder(@NonNull View itemView , RecyclerViewClickListener recyclerViewClickListener, FavorittViewClickListner favorittViewClickListner) {
            super(itemView);

            //listenerWeakReference = new WeakReference<>(recyclerViewClickListener);
            mrecyclerViewClickListener = recyclerViewClickListener;
            listner = favorittViewClickListner;


            Receipt_imageview = itemView.findViewById(R.id.All_Receipts_Tab_Image);
            Receipt_title_txt = itemView.findViewById(R.id.CardView_Title);
            Receipt_date_txt = itemView.findViewById(R.id.CardView_Date);
            Receipt_location_txt = itemView.findViewById(R.id.CardView_Location);
            Receipt_price_txt = itemView.findViewById(R.id.CardView_Price);
            StoreLogoImage = itemView.findViewById(R.id.CardView_Company_logo);
            orangeHeartBtn = itemView.findViewById(R.id.CardView_FavoriteBtn2);
            whiteHeartBtn = itemView.findViewById(R.id.CardView_FavoriteBtn);



            favoriteHeart = new FavoriteHeart(whiteHeartBtn,orangeHeartBtn);

            // Set OnClickListener
            itemView.setOnClickListener(this);
            orangeHeartBtn.setOnClickListener(this);
            whiteHeartBtn.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);


        }

        public void setData(Receipt receipt, Store store){
            getFavoriteIcone(receipt);

            Glide.with(mContext).load(receipt.getImage_path()).into(Receipt_imageview);
            String getPrice= Double.toString(receipt.getAmount());
            Receipt_price_txt.setText(getPrice);
            Receipt_date_txt.setText(receipt.getReceipt_date());
            Receipt_title_txt.setText(receipt.getReceipt_title());
            Glide.with(mContext).load(store.getStore_logo()).into(StoreLogoImage);
            if(likedByUser){
                orangeHeartBtn.setVisibility(View.VISIBLE);
                whiteHeartBtn.setVisibility(View.GONE);

            }else if(!likedByUser){
                orangeHeartBtn.setVisibility(View.GONE);
                whiteHeartBtn.setVisibility(View.VISIBLE);
            }

        }

        private void getFavoriteIcone(Receipt receipt){
            Query query = myRef.child(mContext.getString(R.string.dbname_receipt)).child(receipt.getReceipt_id()).child(mContext.getString(R.string.favorite));
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot dataSnapshot1: dataSnapshot.child(mContext.getString(R.string.favorite)).getChildren()) {

                        Log.d(TAG, "onDataChange: Data skjer her: " + dataSnapshot1.getValue(Receipt.class).getFavorite());
                        if (dataSnapshot1.getValue(Receipt.class).getFavorite()) {
                            likedByUser = true;
                        } else if (!dataSnapshot1.getValue(Receipt.class).getFavorite()) {
                            likedByUser = false;
                        }

                    }

                    if(!dataSnapshot.exists()){
                        likedByUser = false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }

        @Override
        public void onClick(View v) {

            if(v.getId() == orangeHeartBtn.getId() || v.getId() == whiteHeartBtn.getId()){
                // Get Adapter Position OnFavoritt Button Clicked
                listner.OnFavorittClicked(v,getAdapterPosition());
                favoriteHeart.ToggleHeart();
            }
            else {
                // Get Adapter Position OnClick
                mrecyclerViewClickListener.onClick(v,getAdapterPosition());
            }

            //
        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select an action");

            menu.add(0,0,getAdapterPosition(),CommonUtils.SHARE);
            menu.add(0,1,getAdapterPosition(),CommonUtils.DELETE);
        }
    }

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }

    public interface FavorittViewClickListner{
        void OnFavorittClicked(View view,int position);

    }


}
