package no.hiof.ahmedak.papervault.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import no.hiof.ahmedak.papervault.Model.Company;
import no.hiof.ahmedak.papervault.R;

public class AutoCompleteCompanyAdapter extends ArrayAdapter<Company> implements Filterable{
    private static final String TAG = "AutoCompleteCompanyAdap";
    private ArrayList<Company> mCompanyArrayList;
    private int layoutresource;

    public AutoCompleteCompanyAdapter( @NonNull Context context, int resource) {
        super(context, 0);
        mCompanyArrayList = new ArrayList<>();
        layoutresource = resource;

    }

    /**
     * Clear Data in arrayList and add new data from @param to list.
     * @param list
     */
    public void setData(List<Company> list){
        mCompanyArrayList.clear();
        mCompanyArrayList.addAll(list);
    }


    /**
     * Return item position
     * @param position
     * @return
     */
    @Nullable
    @Override
    public Company getItem(int position) {
        return mCompanyArrayList.get(position);
    }


    /**
     * return arrayList count
     * @return
     */
    @Override
    public int getCount() {
        return mCompanyArrayList.size();
    }


    /**
     * Overriding view method to have more control over the layout
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, View convertView,ViewGroup parent) {

        // We inflate only when view is equal to null.
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(layoutresource,parent,false);
        }

        Log.d(TAG, "getView: Starting View");

        TextView comp_name =  convertView.findViewById(R.id.auto_row_txt);
        ImageView comp_logo = convertView.findViewById(R.id.auto_row_img);

        Company companyItem = getItem(position);
        if(companyItem != null){
            comp_name.setText(companyItem.getLogo_name());
            Glide.with(parent.getContext()).load(companyItem.getLogo_img_path()).into(comp_logo);
        }

        return  convertView;
    }


    // Filter
    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter  = new Filter() {

            /**
             * Convert selected String from the resultValue.
             * Returns Company Name as results
             * @param resultValue
             * @return
             */
            @Override
            public CharSequence convertResultToString(Object resultValue) {
                String compayName = ((Company) resultValue).getLogo_name();
                return compayName;
            }

            /**
             * Filter Results
             * @param constraint
             * @return
             */
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                if(constraint != null){
                    filterResults.values = mCompanyArrayList;
                    filterResults.count = mCompanyArrayList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results != null &&(results.count > 0)){
                    notifyDataSetChanged();
                }else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }
}
