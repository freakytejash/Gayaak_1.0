package com.example.gayaak_10.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CourseTutorsDataContractList {

    @SerializedName("Id")
    @Expose
    public Integer id;
    @SerializedName("CourseId")
    @Expose
    public Integer courseId;
    @SerializedName("TutorId")
    @Expose
    public Integer tutorId;
    @SerializedName("CourseDataContract")
    @Expose
    public Object courseDataContract;
    @SerializedName("UserDataContract")
    @Expose
    public UserDataContract userDataContract;
    @SerializedName("IsActive")
    @Expose
    public Boolean isActive;
    @SerializedName("CreatedDate")
    @Expose
    public Object createdDate;
    @SerializedName("ModifiedDate")
    @Expose
    public Object modifiedDate;
    @SerializedName("CreatedBy")
    @Expose
    public Object createdBy;
    @SerializedName("ModifiedBy")
    @Expose
    public Object modifiedBy;
}
