package no.hiof.ahmedak.papervault;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class ReceiptsAdapter extends RecyclerView.Adapter<ReceiptsAdapter.MyViewHolder> {

    ArrayList<Integer>TempData;

    public ReceiptsAdapter(ArrayList<Integer> tempData){
        this.TempData = tempData;
    }


    @NonNull
    @Override
    public ReceiptsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.receipts_cardview_items,viewGroup,false);

        MyViewHolder viewHolder = new MyViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptsAdapter.MyViewHolder myViewHolder, int position) {
            myViewHolder.imageView.setImageResource(TempData.get(position));
    }

    @Override
    public int getItemCount() {
        return TempData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.All_Receipts_Tab_Image);

        }
    }
}
