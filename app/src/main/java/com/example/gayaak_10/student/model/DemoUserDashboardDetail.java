package com.example.gayaak_10.student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DemoUserDashboardDetail {

    @SerializedName("LiveClassDataContractList")
    @Expose
    public ArrayList<LiveClassDataContractList> liveClassDataContractList = new ArrayList<>();
    @SerializedName("TotalCount")
    @Expose
    public Integer totalCount;
}
