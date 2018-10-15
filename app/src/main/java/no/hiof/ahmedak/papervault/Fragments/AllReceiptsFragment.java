package no.hiof.ahmedak.papervault.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import no.hiof.ahmedak.papervault.Adapters.ReceiptsAdapter;
import no.hiof.ahmedak.papervault.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllReceiptsFragment extends android.support.v4.app.Fragment {

    RecyclerView AllReceiptsRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    private ArrayList<Integer> TempData;


    public AllReceiptsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_all_receipts, container, false);


        // AlL Receipts Tab Content:
        TempData = new ArrayList<>();

        for (int i = 0; i <30; i++) {
            TempData.add(R.drawable.receipt_ex);
        }
        // TODO: FIX DATABASE CONNECTION
        // RecycleView All Receipts Tab
        AllReceiptsRecyclerView = view.findViewById(R.id.All_Receipts_RecycleView);
        AllReceiptsRecyclerView.setHasFixedSize(true);

        // RecycleView Manager
        mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        AllReceiptsRecyclerView.setLayoutManager(mLayoutManager);

        // RecycleView Adapter

        mAdapter = new ReceiptsAdapter(TempData);
        AllReceiptsRecyclerView.setAdapter(mAdapter);



        return view;


    }




}
