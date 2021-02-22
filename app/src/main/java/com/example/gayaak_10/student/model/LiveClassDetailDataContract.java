package com.example.gayaak_10.student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiveClassDetailDataContract {

    @SerializedName("LiveclassdetailId")
    @Expose
    public Integer liveclassdetailId;
    @SerializedName("TutorId")
    @Expose
    public Integer tutorId;
    @SerializedName("TutorName")
    @Expose
    public String tutorName;
    @SerializedName("ClassTittle")
    @Expose
    public String classTittle;
    @SerializedName("Description")
    @Expose
    public String description;
    @SerializedName("DateString")
    @Expose
    public String dateString;
    @SerializedName("Time")
    @Expose
    public String time;
    @SerializedName("connectionlink")
    @Expose
    public Object connectionlink;
    @SerializedName("StudentId")
    @Expose
    public Integer studentId;
    @SerializedName("LiveClassTypeId")
    @Expose
    public Integer liveClassTypeId;
    @SerializedName("IsSessionComplete")
    @Expose
    public Object isSessionComplete;
    @SerializedName("ScheduleDataContract")
    @Expose
    public Object scheduleDataContract;
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
    public Integer createdBy;
    @SerializedName("ModifiedBy")
    @Expose
    public Object modifiedBy;
    @SerializedName("LoginUserId")
    @Expose
    public Object loginUserId;
}
