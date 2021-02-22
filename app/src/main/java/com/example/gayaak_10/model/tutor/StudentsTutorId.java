package com.example.gayaak_10.model.tutor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentsTutorId {
    @SerializedName("Status")
    @Expose
    public Boolean status;
    @SerializedName("Message")
    @Expose
    public String message;
    @SerializedName("Detail")
    @Expose
    public StudentsTutorDetail detail;
}
