package com.example.gayaak_10.student.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DemoTutorRequest {
    @SerializedName("StudentTutorBookingId")
    @Expose
    public Integer StudentTutorBookingId;
    @SerializedName("TutorScheduleId")
    @Expose
    public String TutorScheduleId;
    @SerializedName("ScheduleId")
    @Expose
    public Integer ScheduleId;
    @SerializedName("ScheduleId2")
    @Expose
    public Integer ScheduleId2;
    @SerializedName("TutorId")
    @Expose
    public Integer TutorId;
    @SerializedName("Date")
    @Expose
    public String Date;
    @SerializedName("CourseId")
    @Expose
    public Integer CourseId;
    @SerializedName("liveclasstype")
    @Expose
    public Integer liveclasstype;
    @SerializedName("levelid")
    @Expose
    public String levelid = "";
    @SerializedName("categoryid")
    @Expose
    public Integer categoryid;
    @SerializedName("ClassTypeId")
    @Expose
    public Integer classTypeId;
    @SerializedName("NoofSession")
    @Expose
    public Integer noOfSession;
    @SerializedName("LiveclassdetailId")
    @Expose
    public String liveClassDetailId;

    @SerializedName("DemoTutorSessionFeedbackId")
    @Expose
    public Integer demoTutorSessionFeedbackId;

    @SerializedName("IsPaymentComplete")
    @Expose
    public Boolean isPaymentComplete;
}
