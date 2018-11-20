package com.example.ak.rxproject.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.ak.rxproject.R;
import com.example.ak.rxproject.client.ApiClient;
import com.example.ak.rxproject.client.ApiInterface;
import com.example.ak.rxproject.model.Items;
import com.example.ak.rxproject.model.WorldPopulation;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

//        Log.e("MainActivity", "onCreate: ");
//
//        Call<WorldPopulation> call = apiInterface.getWorld();
//        call.enqueue(new Callback<WorldPopulation>() {
//            @Override
//            public void onResponse(Call<WorldPopulation> call, Response<WorldPopulation> response) {
//                if (response.body() != null) {
//                    List<Items> items = response.body().getItemsList();
//                    for (int i = 0 ; i<items.size();i++)
//                        Log.e("MainActivity", "onResponse: " + items.get(i).getCountry());
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<WorldPopulation> call, Throwable t) {
//                Log.e("MainActivity", "Failure: ");
//
//            }
//        });


        Observable<WorldPopulation> observable = apiInterface.getWorld().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Observer<WorldPopulation>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(WorldPopulation worldPopulation) {
                List<Items> items = worldPopulation.getItemsList();
                for (Items item :items){
                    Log.e("MainActivity", "onNext: "+ item.getCountry());
                }

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
