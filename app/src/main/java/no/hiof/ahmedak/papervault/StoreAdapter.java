package no.hiof.ahmedak.papervault;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {

    ArrayList<Integer> TempData;


    public StoreAdapter(ArrayList<Integer> tempData ){
        this.TempData = tempData;
    }
    @NonNull
    @Override
    public StoreAdapter.StoreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.store_cardview_item,viewGroup,false);

        StoreViewHolder storeViewHolder = new StoreViewHolder(v);

        return storeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StoreAdapter.StoreViewHolder storeViewHolder, int Position) {

        storeViewHolder.imageView.setImageResource(TempData.get(Position));
    }

    @Override
    public int getItemCount() {
        return TempData.size();
    }

    // Inner Class StoreViewHolder Extends Recycle View ViewHolder
    public class StoreViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public StoreViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.Store_CardView_Image);
        }
    }
}


