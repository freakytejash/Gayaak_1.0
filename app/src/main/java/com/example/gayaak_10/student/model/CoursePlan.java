package com.example.gayaak_10.student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CoursePlan {

    @SerializedName("Status")
    @Expose
    public Boolean status;
    @SerializedName("Message")
    @Expose
    public String message;
    @SerializedName("Detail")
    @Expose
    public CoursePlanDetail detail = new CoursePlanDetail();


    public static class CoursePlanDetail{
        @SerializedName("CourseDataContractList")
        @Expose
        public ArrayList<CourseDataContractList> courseDataContractList = new ArrayList<>();
        @SerializedName("WalletRechargePlanDataContractList")
        @Expose
        public ArrayList<WalletRechargePlanDataContractList> walletRechargePlanDataContractList = new ArrayList<WalletRechargePlanDataContractList>();

    }


   /* public String planName = "";
    public String planFees = "";
    public String planSessionNo = "";
    public boolean isSelected = false;

    public CoursePlan(String planName, String planFees, String planSessionNo, boolean isSelected) {
        this.planName = planName;
        this.planFees = planFees;
        this.planSessionNo = planSessionNo;
        this.isSelected = isSelected;
    }*/
}
