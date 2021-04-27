package com.example.gayaak_10.student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CourseDataContract  {
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
    @SerializedName("DemoTutorSessionFeedbackDataContractList")
    @Expose
    public Object demoTutorSessionFeedbackDataContractList;
    @SerializedName("CourseType")
    @Expose
    public Integer courseType;
    @SerializedName("CourseNames")
    @Expose
    public String courseNames;
    @SerializedName("IsShownInPlanList")
    @Expose
    public Object isShownInPlanList;
    @SerializedName("DemoDone")
    @Expose
    public Object demoDone;
    @SerializedName("TutorDataContract")
    @Expose
    public Object tutorDataContract;
    @SerializedName("CourseTutorDataContract")
    @Expose
    public Object courseTutorDataContract;
    @SerializedName("zohoItemId")
    @Expose
    public Integer zohoItemId;
    @SerializedName("IsActive")
    @Expose
    public Boolean isActive;
    @SerializedName("CreatedDate")
    @Expose
    public String createdDate;
    @SerializedName("ModifiedDate")
    @Expose
    public String modifiedDate;
    @SerializedName("CreatedBy")
    @Expose
    public Integer createdBy;
    @SerializedName("ModifiedBy")
    @Expose
    public Integer modifiedBy;
    @SerializedName("LoginUserId")
    @Expose
    public Object loginUserId;
}
