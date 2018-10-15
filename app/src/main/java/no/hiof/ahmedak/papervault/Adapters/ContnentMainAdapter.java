package no.hiof.ahmedak.papervault.Adapters;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import java.util.ArrayList;

import no.hiof.ahmedak.papervault.R;

public class ContnentMainAdapter extends RecyclerView.Adapter<ContnentMainAdapter.ViewHolder> {

    //TODO: Change ArrayList Data from TextView to ImageView.
    private ArrayList<Integer>mdata;



    public ContnentMainAdapter(ArrayList<Integer> mdata) {
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_main_recycleview_row,viewGroup,false);


       ViewHolder viewHolder = new ViewHolder(v);
       return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            viewHolder.imageView.setImageResource(mdata.get(position));
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }


    // Inner Class Extends RecyclerView.ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //title = (TextView) itemView.findViewById(R.id.row_title_txt);
            imageView = itemView.findViewById(R.id.row_image_receipts_content_main);

        }
    }
}
