package no.hiof.ahmedak.papervault.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.ArrayList;

import no.hiof.ahmedak.papervault.Adapters.StoreAdapter;
import no.hiof.ahmedak.papervault.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoreFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "StoreFragment";

    private RecyclerView StoreTabRecycleView;
    private RecyclerView.LayoutManager mlayoutManager;
    private RecyclerView.Adapter madapter;

    private FloatingActionButton fab;

    private ArrayList<Integer> Data;

    public StoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_store, container, false);

        // TODO: Get Data from Data Model.
        Data = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            Data.add(R.drawable.receipt_ex);

        }

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddnewStore();
            }
        });


        // Recycle View Store Tab
        StoreTabRecycleView = view.findViewById(R.id.Store_Tab_RecycleView);
        StoreTabRecycleView.setHasFixedSize(true);

        // Setup LayOut Manager with Grid layout and 2 SpanCount
        mlayoutManager = new GridLayoutManager(getContext(),2);
        StoreTabRecycleView.setLayoutManager(mlayoutManager);


        // Recycle View Adapter

        madapter = new StoreAdapter(Data);
        StoreTabRecycleView.setAdapter(madapter);

        return  view;
    }


    /**
     * Open Add new store Dialog
     */
    private void AddnewStore(){
        StoreDialogFragment storeDialogFragment = new StoreDialogFragment();
        storeDialogFragment.show(getFragmentManager(), "AddnewStoreDialog");

    }

}
