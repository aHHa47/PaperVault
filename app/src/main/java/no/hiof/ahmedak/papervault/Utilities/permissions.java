package no.hiof.ahmedak.papervault.Utilities;

import android.Manifest;

/**
 * Permissions Class
 * Static Class for all app permissions
 */
public class permissions {
    public static  final String [] PERMISSIONS_CAMERA = { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    public static final String [] PERMISSIONS_INTERNET = { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET};

}
