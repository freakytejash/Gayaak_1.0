package com.example.gayaak_10.common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.TimeZone;

public class TutorCalendarLiveClassDataContractList {


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
    @SerializedName("Date")
    @Expose
    public String date;
    @SerializedName("Time")
    @Expose
    public String time;
    @SerializedName("Day")
    @Expose
    public String day;
    @SerializedName("TimeString")
    @Expose
    public String timeString;
    @SerializedName("connectionlink")
    @Expose
    public String connectionlink;
    @SerializedName("StudentId")
    @Expose
    public Integer studentId;
    @SerializedName("LiveClassTypeId")
    @Expose
    public Integer liveClassTypeId;
    @SerializedName("IsSessionComplete")
    @Expose
    public boolean isSessionComplete;
    @SerializedName("IsComplete")
    @Expose
    public Integer isComplete;
    @SerializedName("ScheduleDataContract")
    @Expose
    public Object scheduleDataContract;
    @SerializedName("CategoryId")
    @Expose
    public Integer categoryId;
    @SerializedName("LevelId")
    @Expose
    public Integer levelId;
    @SerializedName("CategoryName")
    @Expose
    public String categoryName;
    @SerializedName("CategoryList")
    @Expose
    public Object categoryList;
    @SerializedName("StudentTutorBookingId")
    @Expose
    public Integer studentTutorBookingId;
    @SerializedName("ScheduleId")
    @Expose
    public Integer scheduleId;
    @SerializedName("TutorScheduleId")
    @Expose
    public Integer tutorScheduleId;
    @SerializedName("FullName")
    @Expose
    public String fullName;
    @SerializedName("LevelName")
    @Expose
    public String levelName;
    @SerializedName("CourseId")
    @Expose
    public String courseId;
    @SerializedName("TutorProfileImage")
    @Expose
    public String tutorProfileImage;
    @SerializedName("ZoomMeetingId")
    @Expose
    public String zoomMeetingId;
    @SerializedName("ZoomMeetingPassword")
    @Expose
    public String zoomMeetingPassword;
    @SerializedName("HostLink")
    @Expose
    public String hostLink;
    @SerializedName("IsDownloadFileProcessed")
    @Expose
    public Object isDownloadFileProcessed;
    @SerializedName("CourseName")
    @Expose
    public String courseName;
    @SerializedName("Price")
    @Expose
    public Integer price;
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
}
