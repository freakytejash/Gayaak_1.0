package com.example.gayaak_10.student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DemoUserDashboard {

    @SerializedName("Status")
    @Expose
    public Boolean status;
    @SerializedName("Message")
    @Expose
    public String message;
    @SerializedName("Detail")
    @Expose
    public DemoUserDashboardDetail detail;
}
