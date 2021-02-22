package com.example.gayaak_10.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConfigurationDetail {
    @SerializedName("ConfigurationId")
    @Expose
    public Integer configurationId = 0;
    @SerializedName("Name")
    @Expose
    public String name = "";
    @SerializedName("Value")
    @Expose
    public String value = "";
    @SerializedName("ConfigurationDataContractList")
    @Expose
    public Object configurationDataContractList = null;
}
