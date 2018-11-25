package no.hiof.ahmedak.papervault.Utilities;
import android.content.Context;
import android.net.ConnectivityManager;



public  class CommonUtils {

    public static final String DELETE = "Delete";
    public static final String SHARE = "Share";

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() != null) && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }


}
