package no.hiof.ahmedak.papervault.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import no.hiof.ahmedak.papervault.Model.Receipt;
import no.hiof.ahmedak.papervault.R;

public class ReceiptInfoFragment extends Fragment {

    private static final String TAG = "ReceiptInfoFragment";
    private ImageView ReceiptImg, BackArrow;
    private TextView Receipt_title, Receipt_date, Receipt_price, Receipt_location;
    private Receipt receipt;
    private int ACT_number = 0;
    private String Price;


    public ReceiptInfoFragment() {
        super();
        setArguments(new Bundle());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cardview_receipt_fragment_layout,container,false);
        ReceiptImg = view.findViewById(R.id.Receipt_img);
        Receipt_title = view.findViewById(R.id.ReceiptName_textview);
        Receipt_date = view.findViewById(R.id.ReceiptDate_textview);
        Receipt_price = view.findViewById(R.id.ReceiptPrice_textview);
        Receipt_location = view.findViewById(R.id.Receiptlocation_textview);
        BackArrow = view.findViewById(R.id.BackArrow);


        try{
            receipt = getReceiptFromBundle();
            Glide.with(getContext()).load(receipt.getImage_path()).into(ReceiptImg);
            Receipt_title.setText(receipt.getReceipt_title());
            Receipt_date.setText(receipt.getReceipt_date());
            Price = Double.toString(receipt.getAmount());
            Receipt_price.setText(Price + ";-");
            Receipt_location.setText(receipt.getReceipt_location());
            ACT_number = getActivityNumberFromBundle();

        }catch (NullPointerException e){
            Log.d(TAG, "onCreateView: NullPointerException" + e.getMessage());
        }

        // Back button
        BackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        return view;
    }


    /**
     * Return Receipt from Bundle
     * @return
     */
    private Receipt getReceiptFromBundle(){
        Log.d(TAG, "getReceiptFromBundle: Getting Receipt From bundle");

        Bundle bundle = this.getArguments();

        if(bundle != null) {
            return bundle.getParcelable(getString(R.string.receipt));
        }else {
            return null;
        }
    }

    /**
     * return activity number from bundle
     * @return
     */
    private int getActivityNumberFromBundle(){
        Log.d(TAG, "getReceiptFromBundle: Getting Receipt From bundle");

        Bundle bundle = this.getArguments();

        if(bundle != null) {
            return bundle.getParcelable(getString(R.string.Activity_number));
        }else {
            return 0;
        }
    }
}
