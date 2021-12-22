package com.appmobio.androidpractical;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appmobio.androidpractical.model.Item;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.Serializable;

public class DetailsActivity extends AppCompatActivity {
    public static final String EXTRA_DATA = "EXTRA_DATA_DETAILS";
    Serializable item;
    Item itemInstance;
    ImageView imageView;
    TextView title,description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //initializing elements
        imageView = findViewById(R.id.fullImg);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);

        //getData form serializable
        item = getIntent().getSerializableExtra(EXTRA_DATA);

        itemInstance = (Item) item;

        //back button
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();


        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu this add item to action bar if it is present
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

            case R.id.action_location:
                Intent intent = new Intent(getApplicationContext(),LocationActivity.class);
                intent.putExtra(LocationActivity.EXTRA_DATA,this.item);
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
                try {
                    Picasso.get().load(itemInstance.getSmallImg()).into(imageView);
                    Toast.makeText(this, "urlImg", Toast.LENGTH_SHORT).show();
                }
                catch (Exception exception){
                    Picasso.get().load(Url.getDefaultImg()).into(imageView);
                    Toast.makeText(this, "default", Toast.LENGTH_SHORT).show();
                }


        //set Values
        title.setText(itemInstance.getTitle());
        description.setText(itemInstance.getDescription());
    }
}
