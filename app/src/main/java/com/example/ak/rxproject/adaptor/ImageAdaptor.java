package com.example.ak.rxproject.adaptor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ak.rxproject.R;
import com.example.ak.rxproject.activity.SecondImageActivity;
import com.example.ak.rxproject.model.Items;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;


public class ImageAdaptor extends RecyclerView.Adapter<ImageAdaptor.ImageHolder>{

    private Context context;
    private ArrayList<Items> items;

    public ImageAdaptor(Context context, ArrayList<Items> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_list_item,viewGroup,false);
        return (new ImageHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder imageHolder, int i) {
        final Items item =  items.get(i);

        Picasso.get()
                .load(item.getFlag())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageHolder.imageView);

        imageHolder.rankTv.setText(item.getRank());
        imageHolder.countryTv.setText(item.getCountry());
        imageHolder.populationTv.setText(item.getPopulation());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ImageHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.listIv)
        ImageView imageView;
        @BindView(R.id.rankTV)
        TextView rankTv;
        @BindView(R.id.countryTv)
        TextView countryTv;
        @BindView(R.id.populationTv)
        TextView populationTv;


        ImageHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Items item = items.get(getAdapterPosition());
                    Intent intent = new Intent(context,SecondImageActivity.class);
                    intent.putExtra("items",item);
                    context.startActivity(intent);
                }
            });
        }
    }


}
