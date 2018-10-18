package no.hiof.ahmedak.papervault.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.zip.Inflater;

import no.hiof.ahmedak.papervault.R;

public class NewReceiptFragment extends AppCompatActivity {

    ImageView imageView;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header_new_receipt_layout);
        Intent intent = getIntent();
        savedInstanceState = intent.getExtras();
        imageView = findViewById(R.id.captured_photo);

        Bitmap bitmap = BitmapFactory.decodeFile(savedInstanceState.getString("filePath"));

        imageView.setImageBitmap(bitmap);

    }
}
