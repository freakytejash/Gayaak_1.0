package com.example.gayaak_10.model.tutor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StudentsTutorDetail {
    @SerializedName("StudentTutorBookingDataContractList")
    @Expose
    public ArrayList<StudentTutorBookingDataContractList> studentTutorBookingDataContractList = new ArrayList<>();
    @SerializedName("Count")
    @Expose
    public Integer count;
}
