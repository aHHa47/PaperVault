package no.hiof.ahmedak.papervault.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

import no.hiof.ahmedak.papervault.Model.Receipt;
import no.hiof.ahmedak.papervault.R;


public class ReceiptsAdapter extends RecyclerView.Adapter<ReceiptsAdapter.MyViewHolder> {

    private static final String TAG = "ReceiptsAdapter";
    private LayoutInflater layoutInflater;
    private ArrayList<Receipt> ReceiptList;
    private Context mContext;
    private RecyclerViewClickListener clickListener;

    /**
     * Return ArrayList of Receipts info.
     */
    public ReceiptsAdapter(Context context,ArrayList<Receipt> ReceiptList, RecyclerViewClickListener clickListener){
        this.layoutInflater = LayoutInflater.from(context);
        this.ReceiptList = ReceiptList;
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public ReceiptsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = layoutInflater.from(viewGroup.getContext()).inflate(R.layout.receipts_cardview_items,viewGroup,false);

        MyViewHolder viewHolder = new MyViewHolder(v, clickListener);

        mContext = v.getContext();


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptsAdapter.MyViewHolder myViewHolder, int position) {
        // Retrieve the data for that position
        Receipt receipt = ReceiptList.get(position);
        // Add Receipt Info to CardView.
        myViewHolder.setData(receipt);

    }

    @Override
    public int getItemCount() {
        return ReceiptList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView Receipt_imageview;
        public TextView Receipt_title_txt, Receipt_date_txt, Receipt_location_txt, Receipt_price_txt;
        RecyclerViewClickListener mrecyclerViewClickListener;
        public MyViewHolder(@NonNull View itemView , RecyclerViewClickListener recyclerViewClickListener) {
            super(itemView);

            mrecyclerViewClickListener = recyclerViewClickListener;

            Receipt_imageview = itemView.findViewById(R.id.All_Receipts_Tab_Image);
            Receipt_title_txt = itemView.findViewById(R.id.CardView_Title);
            Receipt_date_txt = itemView.findViewById(R.id.CardView_Date);
            Receipt_location_txt = itemView.findViewById(R.id.CardView_Location);
            Receipt_price_txt = itemView.findViewById(R.id.CardView_Price);

            itemView.setOnClickListener(this);


        }

        public void setData(Receipt receipt){

            Glide.with(mContext).load(receipt.getImage_path()).into(Receipt_imageview);
            String getPrice= Double.toString(receipt.getAmount());
            Receipt_price_txt.setText(getPrice);
            Receipt_date_txt.setText(receipt.getReceipt_date());
            Receipt_title_txt.setText(receipt.getReceipt_title());

        }

        @Override
        public void onClick(View v) {
            mrecyclerViewClickListener.onClick(v,getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }
}
