package com.example.ak.rxproject.model;

import com.google.gson.annotations.SerializedName;

public class Items {
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
