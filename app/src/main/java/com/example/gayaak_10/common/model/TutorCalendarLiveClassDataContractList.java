package com.example.gayaak_10.common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TutorCalendarLiveClassDataContractList {
    @SerializedName("LiveclassdetailId")
    @Expose
    public Integer liveclassdetailId;
    @SerializedName("TutorId")
    @Expose
    public Integer tutorId;
    @SerializedName("TutorName")
    @Expose
    public Object tutorName;
    @SerializedName("ClassTittle")
    @Expose
    public Object classTittle;
    @SerializedName("Description")
    @Expose
    public Object description;
    @SerializedName("DateString")
    @Expose
    public String dateString;
    @SerializedName("Date")
    @Expose
    public String date;
    @SerializedName("Time")
    @Expose
    public String time;
    @SerializedName("TimeString")
    @Expose
    public Object timeString;
    @SerializedName("connectionlink")
    @Expose
    public Object connectionlink;
    @SerializedName("StudentId")
    @Expose
    public Integer studentId;
    @SerializedName("LiveClassTypeId")
    @Expose
    public Object liveClassTypeId;
    @SerializedName("IsSessionComplete")
    @Expose
    public Object isSessionComplete;
    @SerializedName("ScheduleDataContract")
    @Expose
    public Object scheduleDataContract;
    @SerializedName("CategoryId")
    @Expose
    public Integer categoryId;
    @SerializedName("CategoryName")
    @Expose
    public String categoryName;
    @SerializedName("LevelName")
    @Expose
    public String LevelName;
    @SerializedName("IsActive")
    @Expose
    public Object isActive;
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
    @SerializedName("LoginUserId")
    @Expose
    public Object loginUserId;
    @SerializedName("TutorScheduleId")
    @Expose
    public Integer TutorScheduleId;
    @SerializedName("ScheduleId")
    @Expose
    public int ScheduleId;
    @SerializedName("StudentTutorBookingId")
    @Expose
    public Integer StudentTutorBookingId;
}
