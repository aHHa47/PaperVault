package no.hiof.ahmedak.papervault.Adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import no.hiof.ahmedak.papervault.Model.Store;
import no.hiof.ahmedak.papervault.R;

public class StoreSpinnerAdapter extends ArrayAdapter<Store> {
    private Context mContext;
    private ArrayList<Store> stores;
    private  int layoutresource;

    public StoreSpinnerAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Store> storeList) {
        super(context, resource, storeList);
        this.mContext = context;
        this.stores = storeList;
        this.layoutresource = resource;
    }


    @Nullable
    @Override
    public Store getItem(int position) {
        return stores.get(position);
    }

    @Override
    public int getCount() {
        return stores.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    // Passive State View
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(layoutresource,parent,false);
        }

        TextView Drop_down_name = convertView.findViewById(R.id.DropDown_txt);
        ImageView Drop_down_logo = convertView.findViewById(R.id.DropDown_img);

        Store storeItem = getItem(position);

        if(storeItem != null){
            Drop_down_name.setText(storeItem.getStore_name());
            Glide.with(parent.getContext()).load(storeItem.getStore_logo()).into(Drop_down_logo);
        }



        return convertView;
    }


    // DropDown State View
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(layoutresource,parent,false);
        }

        TextView Drop_down_name = convertView.findViewById(R.id.DropDown_txt);
        ImageView Drop_down_logo = convertView.findViewById(R.id.DropDown_img);

        Store storeItem = getItem(position);

        if(storeItem != null){
            Drop_down_name.setText(storeItem.getStore_name());
            Glide.with(parent.getContext()).load(storeItem.getStore_logo()).into(Drop_down_logo);
        }



        return convertView;
    }
}
