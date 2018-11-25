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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

import no.hiof.ahmedak.papervault.Model.Receipt;
import no.hiof.ahmedak.papervault.R;

public class ContnentMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ContnentMainAdapter";

    //TODO: Change ArrayList Data from TextView to ImageView.
    private ArrayList<Receipt> receiptArrayList;
    private LayoutInflater layoutInflater;
    private Context mContext;
    private DatabaseReference myRef;




    public ContnentMainAdapter(Context context, ArrayList<Receipt> receipts) {
        this.mContext = context;
        this.receiptArrayList = receipts;
        this.layoutInflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = layoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_main_recycleview_row,viewGroup,false);
        myRef = FirebaseDatabase.getInstance().getReference();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        Receipt receipt = receiptArrayList.get(position);

        ((ViewHolder)viewHolder).setData(receipt);

    }

    @Override
    public int getItemCount() {
        return receiptArrayList.size();
    }

    // Inner Class Extends RecyclerView.ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ReceiptImage;
        private TextView ReceiptDate,ReceiptTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ReceiptImage = itemView.findViewById(R.id.row_image_receipts_content_main);
            ReceiptDate = itemView.findViewById(R.id.ReceiptDate_content_main);
            ReceiptTitle = itemView.findViewById(R.id.ReceiptTitle_content_main);

        }

        public void setData(Receipt receipt /*, Store store */){


            if(receipt != null /*&& store != null*/){
                // Set Data
                Glide.with(mContext).load(receipt.getImage_path()).into(ReceiptImage);
                ReceiptDate.setText(receipt.getReceipt_date());
                ReceiptTitle.setText(receipt.getReceipt_title());
            }else {

                Log.d(TAG, "setData: There is noe Receipt Found at the moment");
            }

        }

    }




}
