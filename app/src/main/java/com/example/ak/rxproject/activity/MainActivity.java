package com.example.ak.rxproject.activity;

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

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
