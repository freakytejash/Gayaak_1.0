package com.example.gayaak_10.student.model;

import com.example.gayaak_10.model.response.UserWalletDataContract;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DemoUserDashboardDetail {

    @SerializedName("LiveClassDataContractList")
    @Expose
    public ArrayList<LiveClassDataContractList> liveClassDataContractList = new ArrayList<>();


    @SerializedName("TotalCount")
    @Expose
    public Integer totalCount;

   /* @SerializedName("CourseDataContractList")
    @Expose
    public ArrayList<CourseDataContract> courseDataContractList = new ArrayList<>();*/


}
