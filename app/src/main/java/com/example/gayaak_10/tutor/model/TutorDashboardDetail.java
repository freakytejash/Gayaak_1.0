package com.example.gayaak_10.tutor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TutorDashboardDetail {

    @SerializedName("NoOfSession")
    @Expose
    public Integer noOfSession;
    @SerializedName("NoOfStudent")
    @Expose
    public Integer noOfStudent;
    @SerializedName("TotalCoin")
    @Expose
    public Integer totalCoin;
    @SerializedName("TodaySessionList")
    @Expose
    public ArrayList<TodaySessions> todaySessionList = new ArrayList<TodaySessions>();

}
