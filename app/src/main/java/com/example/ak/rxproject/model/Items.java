package com.example.ak.rxproject.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Items implements Parcelable {
    @SerializedName("rank")
    private String rank;
    @SerializedName("country")
    private String country;
    @SerializedName("population")
    private String population;
    @SerializedName("flag")
    private String flag;

    public Items(String rank, String country, String population, String flag) {
        this.rank = rank;
        this.country = country;
        this.population = population;
        this.flag = flag;
    }


    private Items(Parcel in) {
        rank = in.readString();
        country = in.readString();
        population = in.readString();
        flag = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rank);
        dest.writeString(country);
        dest.writeString(population);
        dest.writeString(flag);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Items> CREATOR = new Creator<Items>() {
        @Override
        public Items createFromParcel(Parcel in) {
            return new Items(in);
        }

        @Override
        public Items[] newArray(int size) {
            return new Items[size];
        }
    };

    public String getRank() {
        return rank;
    }

    public String getCountry() {
        return country;
    }

    public String getPopulation() {
        return population;
    }

    public String getFlag() {
        return flag;
    }
}
