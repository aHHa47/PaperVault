package no.hiof.ahmedak.papervault.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import no.hiof.ahmedak.papervault.R;


public class ReceiptsAdapter extends RecyclerView.Adapter<ReceiptsAdapter.MyViewHolder> {

    ArrayList<String>ImageUrls;
    private Context mContext;

    public ReceiptsAdapter(ArrayList<String> url){
        this.ImageUrls = url;
    }


    @NonNull
    @Override
    public ReceiptsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.receipts_cardview_items,viewGroup,false);

        MyViewHolder viewHolder = new MyViewHolder(v);

        mContext = v.getContext();


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptsAdapter.MyViewHolder myViewHolder, int position) {

        Glide.with(mContext).load(ImageUrls.get(position)).into(myViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return ImageUrls.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.All_Receipts_Tab_Image);

        }
    }
}
