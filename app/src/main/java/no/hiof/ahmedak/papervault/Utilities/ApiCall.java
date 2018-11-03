package no.hiof.ahmedak.papervault.Utilities;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class ApiCall {
    private static final String TAG = "ApiCall";



    private static  ApiCall INSTANCE = null;
    private static Context mContext;
    private RequestQueue requestQueue;



    public ApiCall(Context context) {
        mContext = context;
        requestQueue = getRequestQueue();

    }


    public static ApiCall getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = new ApiCall(context);
        }
        return(INSTANCE);

    }

    /**
     * return Request
     * @return
     */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public static void getCompanies(Context context, String query,Response.Listener<String>
            listener,Response.ErrorListener errorListener) {
        Log.d(TAG, "getCompanies: getting Companies");
        String Url = "https://autocomplete.clearbit.com/v1/companies/suggest?query=" + query;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url,listener,errorListener);

        ApiCall.getInstance(context).addToRequestQueue(stringRequest);

    }


}
