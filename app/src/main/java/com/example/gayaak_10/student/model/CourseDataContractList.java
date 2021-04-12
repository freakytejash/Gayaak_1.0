package com.example.gayaak_10.student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CourseDataContractList{
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
    public Object detail;
    @SerializedName("NoofSession")
    @Expose
    public Integer noofSession;
    @SerializedName("Duration")
    @Expose
    public Object duration;
    @SerializedName("LevelId")
    @Expose
    public Integer levelId;
    @SerializedName("LevelName")
    @Expose
    public Object levelName;
    @SerializedName("CategoryId")
    @Expose
    public Integer categoryId;
    @SerializedName("StudentTutorBookingId")
    @Expose
    public Integer studentTutorBookingId;
    @SerializedName("CategoryName")
    @Expose
    public Object categoryName;
    @SerializedName("Tags")
    @Expose
    public Object tags;
    @SerializedName("CourseTutorsDataContractList")
    @Expose
    public Object courseTutorsDataContractList;
    @SerializedName("LevelList")
    @Expose
    public Object levelList;
    @SerializedName("CategoryList")
    @Expose
    public Object categoryList;
    @SerializedName("OtherPriceinDollars")
    @Expose
    public Object otherPriceinDollars;
    @SerializedName("ThumbnailImage")
    @Expose
    public String thumbnailImage;
    @SerializedName("CourseVideosDataContractList")
    @Expose
    public Object courseVideosDataContractList;
    @SerializedName("CourseEbookDataContractList")
    @Expose
    public Object courseEbookDataContractList;
    @SerializedName("CourseDataContractList")
    @Expose
    public Object courseDataContractList;
    @SerializedName("CourseType")
    @Expose
    public Object courseType;
    @SerializedName("CourseNames")
    @Expose
    public Object courseNames;
    @SerializedName("IsShownInPlanList")
    @Expose
    public Boolean isShownInPlanList;
    @SerializedName("IsActive")
    @Expose
    public Boolean isActive;
    @SerializedName("CreatedDate")
    @Expose
    public String createdDate;
    @SerializedName("ModifiedDate")
    @Expose
    public Object modifiedDate;
    @SerializedName("CreatedBy")
    @Expose
    public Object createdBy;
    @SerializedName("ModifiedBy")
    @Expose
    public Object modifiedBy;
    @SerializedName("LoginUserId")
    @Expose
    public Object loginUserId;

    public Boolean isSelected = false;

    public int indiaCurrencyValue = 0;

    public int usCurrencyValue = 0;

}