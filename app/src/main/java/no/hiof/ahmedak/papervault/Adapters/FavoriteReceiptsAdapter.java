package no.hiof.ahmedak.papervault.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import no.hiof.ahmedak.papervault.Model.Receipt;
import no.hiof.ahmedak.papervault.Model.Store;
import no.hiof.ahmedak.papervault.R;
import no.hiof.ahmedak.papervault.Utilities.CommonUtils;
import no.hiof.ahmedak.papervault.Utilities.FavoriteHeart;


public class FavoriteReceiptsAdapter extends RecyclerView.Adapter<FavoriteReceiptsAdapter.MyViewHolder>{

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
    public FavoriteReceiptsAdapter(Context context, ArrayList<Receipt> ReceiptList,ArrayList<Store> stores, RecyclerViewClickListener clickListener, FavorittViewClickListner listner){
        this.layoutInflater = LayoutInflater.from(context);
        this.ReceiptList = ReceiptList;
        this.mContext = context;
        this.mclickListener = clickListener;
        this.storeArrayList = stores;
        this.favorittViewClickListner = listner;
    }


    @NonNull
    @Override
    public FavoriteReceiptsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = layoutInflater.from(viewGroup.getContext()).inflate(R.layout.receipts_cardview_items,viewGroup,false);
        myRef = FirebaseDatabase.getInstance().getReference();




        return new MyViewHolder(v, mclickListener, favorittViewClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteReceiptsAdapter.MyViewHolder myViewHolder, int position) {
        // Retrieve the data for that position
        Receipt receipt = ReceiptList.get(position);
        Store store = storeArrayList.get(position);

        Log.d(TAG, "onBindViewHolder: StoreArry : " + storeArrayList.size());
        Log.d(TAG, "onBindViewHolder: StoreArry : " + ReceiptList.size());

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

    // Sort Alphabetic
    public void SortListAlpha(){
        Collections.sort(ReceiptList, new Comparator<Receipt>() {
            @Override
            public int compare(Receipt o1, Receipt o2) {
                if(o1.getReceipt_title() == null || o2.getReceipt_title() == null){
                    return 0;
                }
                return o1.getReceipt_title().compareTo(o2.getReceipt_title());
            }
        });
        notifyDataSetChanged();
    }

    // Sort By Price
    public void SortByPrice(){
        Collections.sort(ReceiptList, new Comparator<Receipt>() {
            @Override
            public int compare(Receipt o1, Receipt o2) {

                if(o1.getAmount() == o2.getAmount()){
                    return 0;
                }
                else if(o1.getAmount()>o2.getAmount()){
                    return 1;
                }
                else {
                    return -1;
                }
            }
        });
        notifyDataSetChanged();
    }

    // Sort By Date
    public void SortByDate(){
        Collections.sort(ReceiptList, new Comparator<Receipt>() {
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
            @Override
            public int compare(Receipt o1, Receipt o2) {
                if(o1.getReceipt_date() == null || o2.getReceipt_date() == null){
                    return 0;
                }
                try {
                    return dateFormat.parse(o1.getReceipt_date()).compareTo( dateFormat.parse(o2.getReceipt_date()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
        notifyDataSetChanged();
    }




    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

        private ImageView Receipt_imageview,StoreLogoImage;
        private TextView Receipt_title_txt, Receipt_date_txt, Receipt_location_txt, Receipt_price_txt;
        private RecyclerViewClickListener mrecyclerViewClickListener;
        private FavoriteHeart favoriteHeart;
        private FavorittViewClickListner listner;
        private ToggleButton favoriteBtn;

        public MyViewHolder(@NonNull View itemView , RecyclerViewClickListener recyclerViewClickListener, FavorittViewClickListner favorittViewClickListner) {
            super(itemView);

            mrecyclerViewClickListener = recyclerViewClickListener;
            listner = favorittViewClickListner;

            Receipt_imageview = itemView.findViewById(R.id.All_Receipts_Tab_Image);
            Receipt_title_txt = itemView.findViewById(R.id.CardView_Title);
            Receipt_date_txt = itemView.findViewById(R.id.CardView_Date);
            Receipt_location_txt = itemView.findViewById(R.id.CardView_Location);
            Receipt_price_txt = itemView.findViewById(R.id.CardView_Price);
            StoreLogoImage = itemView.findViewById(R.id.CardView_Company_logo);
            Receipt_location_txt = itemView.findViewById(R.id.CardView_Location);
            favoriteBtn = itemView.findViewById(R.id.favorite_toggleBtn);
            favoriteHeart = new FavoriteHeart(favoriteBtn);
            favoriteHeart.ToggleHeart();
            // Set OnClickListener
            itemView.setOnClickListener(this);
            favoriteBtn.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);


        }

        public void setData(Receipt receipt, Store store){

            if(receipt != null){
                Glide.with(mContext).load(receipt.getImage_path()).into(Receipt_imageview);
                String getPrice= Double.toString(receipt.getAmount());
                Receipt_price_txt.setText(getPrice);
                Receipt_date_txt.setText(receipt.getReceipt_date());
                Receipt_title_txt.setText(receipt.getReceipt_title());
                Receipt_location_txt.setText(receipt.getReceipt_location());
                Glide.with(mContext).load(store.getStore_logo()).into(StoreLogoImage);
                if(receipt.getFavorite()){
                    favoriteBtn.setChecked(true);
                }
                else{
                    favoriteBtn.setChecked(false);
                }
            }
        }

        @Override
        public void onClick(View v) {
            if(getAdapterPosition()!= -1) {
                if (v.getId() == favoriteBtn.getId()) {
                    listner.OnFavorittClicked(v, getAdapterPosition());
                    removeItemAt(getAdapterPosition());
                } else {
                    // Get Adapter Position OnClick
                    mrecyclerViewClickListener.onClick(v, getAdapterPosition());
                }
            }

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
        void OnFavorittClicked(View view, int position);
    }

    public void removeItemAt(int position){
        ReceiptList.remove(position);
        storeArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,ReceiptList.size());
        notifyItemRangeChanged(position,storeArrayList.size());
    }






}
