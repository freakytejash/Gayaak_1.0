package com.example.gayaak_10.student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LiveClassDataContractList implements Serializable {

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
    @SerializedName("TutorProfileImage")
    @Expose
    public String TutorProfileImage;
    @SerializedName("LevelName")
    @Expose
    public String LevelName;
    @SerializedName("Description")
    @Expose
    public String description;
    @SerializedName("DateString")
    @Expose
    public String  dateString = "";
    @SerializedName("Date")
    @Expose
    public String date;
    @SerializedName("Day")
    @Expose
    public String day = "";
    @SerializedName("Time")
    @Expose
    public String time;
    public String endTime = "";
    public String startTime = "";
    @SerializedName("sessionDate")
    @Expose
    public String sessionDate = "";
    @SerializedName("TimeString")
    @Expose
    public Object timeString;
    @SerializedName("connectionlink")
    @Expose
    public String connectionlink = "";
    @SerializedName("StudentId")
    @Expose
    public Integer studentId;

    @SerializedName("Price")
    @Expose
    public Integer Price;

    @SerializedName("LevelId")
    @Expose
    public Integer levelId;

    @SerializedName("CourseId")
    @Expose
    public String courseId;

    @SerializedName("LiveClassTypeId")
    @Expose
    public Integer liveClassTypeId;
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
    public String categoryName = "";

    @SerializedName("CourseName")
    @Expose
    public String courseName = "";

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
    @SerializedName("StudentTutorBookingId")
    @Expose
    public Integer StudentTutorBookingId;
    @SerializedName("ScheduleId")
    @Expose
    public Integer ScheduleId;
    @SerializedName("TutorScheduleId")
    @Expose
    public Integer TutorScheduleId;
    @SerializedName("HostLink")
    @Expose
    public String hostLink = "";
    @SerializedName("ZoomMeetingId")
    @Expose
    public String ZoomMeetingId = "";
    @SerializedName("ZoomMeetingPassword")
    @Expose
    public String ZoomMeetingPassword = "";

}
