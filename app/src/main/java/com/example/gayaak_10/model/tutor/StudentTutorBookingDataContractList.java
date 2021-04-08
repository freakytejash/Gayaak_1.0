package com.example.gayaak_10.model.tutor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentTutorBookingDataContractList {
    @SerializedName("StudentTutorBookingId")
    @Expose
    public Integer studentTutorBookingId;
    @SerializedName("StudentId")
    @Expose
    public Integer studentId;
    @SerializedName("TutorScheduleId")
    @Expose
    public Integer tutorScheduleId;
    @SerializedName("ScheduleId")
    @Expose
    public Integer scheduleId;
    @SerializedName("TutorId")
    @Expose
    public Integer tutorId;
    @SerializedName("StudentName")
    @Expose
    public String studentName = "";
    @SerializedName("StudentTime")
    @Expose
    public String studentTime;
    @SerializedName("StudentDay")
    @Expose
    public String studentDay;
    @SerializedName("UserDataContractList")
    @Expose
    public Object userDataContractList;
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
