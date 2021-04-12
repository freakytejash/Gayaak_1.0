package com.example.gayaak_10.student.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentFutureBookingRequest {
    @SerializedName("StudentTutorBookingId")
    @Expose
    public Integer studentTutorBookingId;
    @SerializedName("TutorScheduleId")
    @Expose
    public Integer tutorScheduleId;
    @SerializedName("ScheduleId")
    @Expose
    public Integer scheduleId;
    @SerializedName("ScheduleId2")
    @Expose
    public Integer scheduleId2;
    @SerializedName("TutorId")
    @Expose
    public Integer tutorId;
    @SerializedName("CourseId")
    @Expose
    public Integer courseId;
    @SerializedName("levelid")
    @Expose
    public Integer levelid;
    @SerializedName("categoryId")
    @Expose
    public Integer categoryId;
    @SerializedName("liveclasstype")
    @Expose
    public Integer liveclasstype;
    @SerializedName("Date")
    @Expose
    public String date;
    @SerializedName("ClassTypeId")
    @Expose
    public Integer classTypeId;
    @SerializedName("liveClassDetailId")
    @Expose
    public Integer liveClassDetailId;
    @SerializedName("NoOfSession")
    @Expose
    public Integer noOfSession;
    @SerializedName("DemoTutorSessionFeedbackId")
    @Expose
    public Integer demoTutorSessionFeedbackId;
    @SerializedName("IsPaymentComplete")
    @Expose
    public Boolean isPaymentComplete;
}
