package com.example.gayaak_10.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Countries {

    @SerializedName("Status")
    @Expose
    public Boolean status = false;
    @SerializedName("Message")
    @Expose
    public String message = "";
    @SerializedName("Detail")
    @Expose
    public ArrayList<CountryDetail> detail = new ArrayList<>();
}
