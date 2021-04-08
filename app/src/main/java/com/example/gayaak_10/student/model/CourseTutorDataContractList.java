package com.example.gayaak_10.student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CourseTutorDataContractList {

    @SerializedName("Id")
    @Expose
    public Integer id;
    @SerializedName("CourseId")
    @Expose
    public Integer courseId;
    @SerializedName("TutorId")
    @Expose
    public Integer tutorId;
    @SerializedName("CourseList")
    @Expose
    public Object courseList;
    @SerializedName("TutorName")
    @Expose
    public String tutorName;
    @SerializedName("CourseName")
    @Expose
    public String courseName;
    @SerializedName("LevelName")
    @Expose
    public String levelName;
    @SerializedName("TutorEmail")
    @Expose
    public String tutorEmail;
    @SerializedName("Day")
    @Expose
    public String day;
    @SerializedName("Time")
    @Expose
    public String time;
    @SerializedName("levelId")
    @Expose
    public String levelId;
    @SerializedName("TutorScheduleId")
    @Expose
    public String tutorScheduleId;
    @SerializedName("ScheduleId")
    @Expose
    public String scheduleId;
    @SerializedName("TutorList")
    @Expose
    public Object tutorList;
    @SerializedName("CourseDataContract")
    @Expose
    public Object courseDataContract;
    @SerializedName("UserDataContract")
    @Expose
    public Object userDataContract;
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
    @SerializedName("VideoURL")
    @Expose
    public String VideoURL = "";
    @SerializedName("ThumbnailImage")
    @Expose
    public String ThumbnailImage = "";
}
