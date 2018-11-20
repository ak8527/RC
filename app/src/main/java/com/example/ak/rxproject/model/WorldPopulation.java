package com.example.ak.rxproject.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WorldPopulation {
    @SerializedName("worldpopulation")
    private List<Items> itemsList;

    public WorldPopulation(List<Items> itemsList) {
        this.itemsList = itemsList;
    }

    public List<Items> getItemsList() {
        return itemsList;
    }
}
