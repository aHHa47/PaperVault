package no.hiof.ahmedak.papervault.Utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ImageUtils {

    private static final String TAG = "ImageUtils";

    /**
     * get Image url and convert to Bitmap image.
     * @param imageUrl
     * @return bitmap
     */
    public static Bitmap getBitmap(String imageUrl){
        File imageFile = new File(imageUrl);
        FileInputStream stream = null;
        Bitmap bitmap = null;

        try {
            stream = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(stream);

        }catch (FileNotFoundException e ){
            Log.d(TAG, "getBitmap: FileNotFoundException" + e.getMessage());
        }finally {
            try{
                // close the stream
                stream.close();

            }catch (IOException e){
                Log.d(TAG, "getBitmap: IOException" + e.getMessage());
            }
        }
        return bitmap;
    }

    /**
     * Returns byte array from bitmap
     * quality is greater then 0 but less than 100
     * @param bitmap
     * @param quality
     * @return
     */
    public static byte[] ConvertByteFromBitmap(Bitmap bitmap,int quality){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,quality,stream);

        // return our stream
        return stream.toByteArray();

    }

}
