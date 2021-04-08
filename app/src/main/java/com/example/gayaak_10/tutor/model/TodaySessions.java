package com.example.gayaak_10.tutor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TodaySessions implements Serializable {

    @SerializedName("TutorScheduleId")
    @Expose
    public Integer TutorScheduleId = 0;
    @SerializedName("ScheduleId")
    @Expose
    public Integer ScheduleId = 0;
    @SerializedName("TutorId")
    @Expose
    public Integer TutorId = 0;
    @SerializedName("LiveClassTypeId")
    @Expose
    public Integer LiveClassTypeId = 0;
    @SerializedName("StudentId")
    @Expose
    public Integer StudentId = 0;
    @SerializedName("time")
    @Expose
    public String time = "";
    public String endTime = "";
    public String startTime = "";
    @SerializedName("Day")
    @Expose
    public String Day = "";

    @SerializedName("Date")
    @Expose
    public String Date = "";

    @SerializedName("StudentName")
    @Expose
    public String StudentName = "";

    @SerializedName("CourseId")
    @Expose
    public Integer CourseId = 1;

    @SerializedName("CategoryId")
    @Expose
    public Integer CategoryId = 1;

    @SerializedName("LevelId")
    @Expose
    public Integer levelId = 1;


    @SerializedName("TutorName")
    @Expose
    public String TutorName = "";
    @SerializedName("IsScheduleAvailalbe")
    @Expose
    public Integer IsScheduleAvailalbe = -1;
    @SerializedName("TimeZone")
    @Expose
    public Object TimeZone = "";
    @SerializedName("StudentProfileImage")
    @Expose
    public String StudentProfileImage = "";
    @SerializedName("IsActive")
    @Expose
    public Object IsActive = "";
    @SerializedName("CreatedDate")
    @Expose
    public Object CreatedDate = "";
    @SerializedName("ModifiedDate")
    @Expose
    public Object ModifiedDate = "";
    @SerializedName("CreatedBy")
    @Expose
    public Object CreatedBy = "";
    @SerializedName("ModifiedBy")
    @Expose
    public Object ModifiedBy = "";
    public String sessionDate = "";
    @SerializedName("LevelName")
    @Expose
    public String LevelName = "";
    @SerializedName("CategoryName")
    @Expose
    public String CategoryName = "";

    @SerializedName("CourseName")
    @Expose
    public String courseName = "";
    @SerializedName("connectionlink")
    @Expose
    public String connectionlink = "";
    @SerializedName("HostLink")
    @Expose
    public String HostLink = "";
    @SerializedName("ZoomMeetingId")
    @Expose
    public String ZoomMeetingId = "";
    @SerializedName("ZoomMeetingPassword")
    @Expose
    public String ZoomMeetingPassword = "";
    @SerializedName("StudentTutorBookingId")
    @Expose
    public Integer StudentTutorBookingId = 0;
    @SerializedName("LiveclassdetailId")
    @Expose
    public Integer liveClassDetailId = 0;
}
