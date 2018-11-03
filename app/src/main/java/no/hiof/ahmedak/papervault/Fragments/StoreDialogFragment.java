package no.hiof.ahmedak.papervault.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import no.hiof.ahmedak.papervault.Adapters.AutoCompleteCompanyAdapter;
import no.hiof.ahmedak.papervault.Model.Company;
import no.hiof.ahmedak.papervault.R;
import no.hiof.ahmedak.papervault.Utilities.ApiCall;

public class StoreDialogFragment extends DialogFragment {

    private static final String TAG = "StoreDialogFragment";
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private AutoCompleteCompanyAdapter autoCompleteCompanyAdapter;
    private Button CreateBtn, CancelBtn;
    private ImageView Store_logo;
    private EditText auto_search_location;
    private AutoCompleteTextView auto_Search_store;
    private Context mContext;
    private ApiCall apiCall;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_new_store_dialog,container,false);
        mContext = view.getContext();
        // Getting Api Instance
        apiCall = ApiCall.getInstance(mContext);

        // Getting ref for UI
        CreateBtn = view.findViewById(R.id.Create_btn);
        CancelBtn = view.findViewById(R.id.Cancel_btn);
        auto_search_location = view.findViewById(R.id.edittext_Search_location);
        auto_Search_store = view.findViewById(R.id.edittext_Search_store);
        Store_logo = view.findViewById(R.id.Store_logo);

        // AutoComplete Adapter
        autoCompleteCompanyAdapter = new AutoCompleteCompanyAdapter(mContext,R.layout.company_item_row_layout);
        auto_Search_store.setThreshold(2);
        auto_Search_store.setAdapter(autoCompleteCompanyAdapter);
        // On Item click listener for the autocomplete TextView.
        auto_Search_store.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: You Clicked on Item" + parent.getItemAtPosition(position));

                // Getting selected item as company object
                Company company =(Company) parent.getItemAtPosition(position);

                Glide.with(view.getContext()).load(company.getLogo_img_path()).into(Store_logo);

                // TODO: Add Store Name to Autocomplete TextView.
            }
        });


        //  captures all the user inputs with TextWatcher and sends it to a handler
        auto_Search_store.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // Handler
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what == TRIGGER_AUTO_COMPLETE){
                    if(!TextUtils.isEmpty(auto_Search_store.getText())){
                        //  Once a successful call is reached to the handler
                        // we make an API call which returns the results
                        SearchForStore(auto_Search_store.getText().toString(), mContext);
                    }
                }
                return false;
            }
        });




        // Cancel button
        CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing dialog");
                getDialog().dismiss();
            }
        });

        // Create button
        CreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: Create New Store in our FireBase Table

               getDialog().dismiss();
            }
        });


        return view;

    }


    /**
     * Searching for store from user Input @param , and sending API Call
     * @param userInput
     * @param context
     */
    private void SearchForStore(String userInput, Context context){
        Log.d(TAG, "SearchForStore: " + userInput);
        ApiCall.getCompanies(context, userInput, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<Company> companyList = new ArrayList<>();
                        try {

                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectRow = jsonArray.getJSONObject(i);

                                Log.d(TAG, "onResponse: jsonObjectRow " + jsonObjectRow.toString());
                                String compName = jsonObjectRow.getString(getString(R.string.name));
                                String compLogo = jsonObjectRow.getString(getString(R.string.logo));
                                String compDomain = jsonObjectRow.getString(getString(R.string.domain));
                                Company company = new Company(compName, compDomain, compLogo);
                                companyList.add(company);
                            }
                            Log.d(TAG, "onResponse: " + companyList.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        autoCompleteCompanyAdapter.setData(companyList);
                        autoCompleteCompanyAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: VolleyError " + error.getMessage());
                    }
                }
        );



    }


}
