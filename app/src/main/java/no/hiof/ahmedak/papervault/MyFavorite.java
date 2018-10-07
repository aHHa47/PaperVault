package no.hiof.ahmedak.papervault;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MyFavorite extends AppCompatActivity {
    Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);

        Toolbar toolbar = (Toolbar)findViewById(R.id.my_favorite_toolbar);
        setSupportActionBar(toolbar);
    }

    // Create Option menu on toolbar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_my_favorite_menu,menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        // TODO: Implement Sorting method
        //noinspection Simplifiable Case Statement
        switch (id){
            case R.id.item_sort_alpha:
                toast = Toast.makeText(getApplicationContext(),"Sort button Name",Toast.LENGTH_LONG);
                toast.show();
                break;
            case R.id.item_sort_date:
                toast = Toast.makeText(getApplicationContext(),"Sort button Date",Toast.LENGTH_LONG);
                toast.show();
                break;
            case R.id.item_sort_price:
                toast = Toast.makeText(getApplicationContext(),"Sort button Price", Toast.LENGTH_LONG);
                toast.show();
                break;
            case R.id.item_sort_near_me:
                toast = Toast.makeText(getApplicationContext(),"Sort button Near me", Toast.LENGTH_LONG);
                toast.show();
                break;
            default:
                break;


        }

        return super.onOptionsItemSelected(item);
    }
}
