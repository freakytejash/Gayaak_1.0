package com.example.gayaak_10.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CoursesDetail {

    @SerializedName("CourseId")
    @Expose
    public Integer courseId;
    @SerializedName("Name")
    @Expose
    public String name;
    @SerializedName("Price")
    @Expose
    public Integer price = 0;
    @SerializedName("Detail")
    @Expose
    public String detail;
    @SerializedName("NoofSession")
    @Expose
    public Integer noofSession;
    @SerializedName("Duration")
    @Expose
    public String duration;
    @SerializedName("LevelId")
    @Expose
    public Integer levelId;
    @SerializedName("LevelName")
    @Expose
    public String levelName;
    @SerializedName("CategoryId")
    @Expose
    public Integer categoryId;
    @SerializedName("CategoryName")
    @Expose
    public String categoryName;
    @SerializedName("Tags")
    @Expose
    public String tags;
    @SerializedName("ThumbnailImage")
    @Expose
    public String ThumbnailImage;
    @SerializedName("CourseTutorsDataContractList")
    @Expose
    public List<CourseTutorsDataContractList> courseTutorsDataContractList;
    @SerializedName("IsActive")
    @Expose
    public Object isActive;
    @SerializedName("CreatedDate")
    @Expose
    public String createdDate;
    @SerializedName("ModifiedDate")
    @Expose
    public String  modifiedDate;
    @SerializedName("CreatedBy")
    @Expose
    public String createdBy;
    @SerializedName("ModifiedBy")
    @Expose
    public String modifiedBy;
    @SerializedName("CourseVideosDataContractList")
    @Expose
    public ArrayList<VideoData> CourseVideosDataContractList = new ArrayList<>();
}
