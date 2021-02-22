package com.example.gayaak_10.student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BuyCoursesDetail {

    @SerializedName("CourseId")
    @Expose
    public Integer courseId;
    @SerializedName("Name")
    @Expose
    public String name;
    @SerializedName("Price")
    @Expose
    public Integer price;
    @SerializedName("Detail")
    @Expose
    public String detail;
    @SerializedName("ThumbnailImage")
    @Expose
    public String ThumbnailImage;
    @SerializedName("LevelName")
    @Expose
    public String levelName;
}
