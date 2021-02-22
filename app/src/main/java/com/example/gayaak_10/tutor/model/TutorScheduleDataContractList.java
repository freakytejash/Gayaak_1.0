package com.example.gayaak_10.tutor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TutorScheduleDataContractList {

    @SerializedName("TutorScheduleId")
    @Expose
    public Integer tutorScheduleId;
    @SerializedName("ScheduleId")
    @Expose
    public Integer scheduleId;
    @SerializedName("TutorId")
    @Expose
    public Integer tutorId;
    @SerializedName("time")
    @Expose
    public String time;
    @SerializedName("Day")
    @Expose
    public String day;
    @SerializedName("TutorName")
    @Expose
    public String tutorName;
    @SerializedName("IsScheduleAvailable")
    @Expose
    public Integer IsScheduleAvailable = -1;
    @SerializedName("TimeZone")
    @Expose
    public Object timeZone;
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
