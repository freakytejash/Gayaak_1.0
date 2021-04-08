package com.example.gayaak_10.tutor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TutorScheduleDetail {

    @SerializedName("TutorScheduleDataContractList")
    @Expose
    public ArrayList<TutorScheduleDataContractList> tutorScheduleDataContractList = new ArrayList<>();
    @SerializedName("Count")
    @Expose
    public Integer count;
}
