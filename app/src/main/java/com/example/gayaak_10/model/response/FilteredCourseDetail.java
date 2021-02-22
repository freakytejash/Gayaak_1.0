package com.example.gayaak_10.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FilteredCourseDetail {

    @SerializedName("CourseDataContractList")
    @Expose
    public ArrayList<CoursesDetail> coursesDetails = new ArrayList<>();
    @SerializedName("TotalCount")
    @Expose
    public Integer totalCount;
}
