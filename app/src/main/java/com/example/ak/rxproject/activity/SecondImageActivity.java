package com.example.ak.rxproject.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.ak.rxproject.R;
import com.example.ak.rxproject.model.Items;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("WeakerAccess")
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

        if (items != null) {
            Picasso.get()
                    .load(items.getFlag())
                    .into(fullIv);
        }
    }
}
