package com.example.gayaak_10.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserCoursesDetail {

    @SerializedName("Status")
    @Expose
    public Boolean Status = false;
    @SerializedName("Message")
    @Expose
    public String Message = "";
    @SerializedName("Detail")
    @Expose
    public ArrayList<UserCourseData> Detail = new ArrayList<UserCourseData>();
}
