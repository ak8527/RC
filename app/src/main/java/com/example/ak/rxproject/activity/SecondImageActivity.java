package com.example.ak.rxproject.activity;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.ak.rxproject.R;
import com.example.ak.rxproject.model.Items;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SecondImageActivity extends AppCompatActivity {

    @BindView(R.id.fullIv)
    ImageView fullIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_image);
        ButterKnife.bind(this);

        Bundle data = getIntent().getExtras();
        Items items = null;
        if (data != null) {
            items = data.getParcelable("items");
        }

        if (items != null){
            Log.e("SecondActivity", "onCreate: " + items.getCountry());
            Picasso.get()
                    .load(items.getFlag())
                    .into(fullIv);
        }
    }
}
