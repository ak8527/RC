package com.example.ak.rxproject.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.ak.rxproject.R;
import com.example.ak.rxproject.adaptor.ImageAdaptor;
import com.example.ak.rxproject.client.ApiClient;
import com.example.ak.rxproject.client.ApiInterface;
import com.example.ak.rxproject.model.Items;
import com.example.ak.rxproject.model.WorldPopulation;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final int MY_TELEPHONE_REQUEST_CODE = 111;
    ArrayList<Items> itemsList = new ArrayList<>();

    @BindView(R.id.imageRv)
    RecyclerView imageRv;

    private Disposable disposable;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        imageRv.setLayoutManager(gridLayoutManager);
        imageRv.addItemDecoration(dividerItemDecoration);

        Observable<WorldPopulation> observable = apiInterface.getWorld().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Observer<WorldPopulation>() {
            @Override
            public void onSubscribe(Disposable d) {
                    disposable = d;
            }

            @Override
            public void onNext(WorldPopulation worldPopulation) {
                itemsList = (ArrayList<Items>) worldPopulation.getItemsList();
                ImageAdaptor imageAdaptor = new ImageAdaptor(getBaseContext(),itemsList);
                imageRv.setAdapter(imageAdaptor);



            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    @OnClick(R.id.contactFabBtn)
    public void contactOpen(){
        Intent intent = new Intent(getBaseContext(),ContactActivity.class);
        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED   ) {
            startActivity(intent);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_TELEPHONE_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_TELEPHONE_REQUEST_CODE :
                          if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                                  && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                                Intent intent = new Intent(this,ContactActivity.class);
                                startActivity(intent);
                        }

                        break;



        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
