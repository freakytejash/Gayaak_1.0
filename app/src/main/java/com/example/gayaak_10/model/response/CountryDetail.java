package com.example.gayaak_10.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountryDetail {

    @SerializedName("CountryId")
    @Expose
    public int CountryId = 0;
    @SerializedName("Name")
    @Expose
    public String Name = "";
    @SerializedName("Code")
    @Expose
    public String Code = "";
}
