package com.example.gayaak_10.tutor.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedbackTutorContentRequest {
    @SerializedName("demoTutorSessionFeedbackId")
    @Expose
    public int demoTutorSessionFeedbackId = 0;
    @SerializedName("tutorId")
    @Expose
    public int tutorId = 0;
    @SerializedName("feedbackText")
    @Expose
    public String feedbackText = "";
    @SerializedName("courseId")
    @Expose
    public Object courseId = 0;
    @SerializedName("studentId")
    @Expose
    public Object studentId = 0;
    @SerializedName("rating")
    @Expose
    public Object rating = null;
    @SerializedName("date")
    @Expose
    public String date = "";
    @SerializedName("moduleId")
    @Expose
    public Object moduleId = null;
    @SerializedName("isStudentQualifies")
    @Expose
    public boolean isStudentQualifies = false;
    @SerializedName("isClassTimeDecided")
    @Expose
    public boolean isClassTimeDecided = false;
    @SerializedName("noOfClassPerWeek")
    @Expose
    public int noOfClassPerWeek = 0;
    @SerializedName("scheduleId1")
    @Expose
    public int scheduleId1 = 0;
    @SerializedName("scheduleId2")
    @Expose
    public int scheduleId2 = 0;
    @SerializedName("isStudentGotSittingTolerance")
    @Expose
    public boolean isStudentGotSittingTolerance = false;
    @SerializedName("isProficientInDiction")
    @Expose
    public String isProficientInDiction = "";
    @SerializedName("isProficientInShruthi")
    @Expose
    public String isProficientInShruthi = "";
    @SerializedName("isProficientInThalam")
    @Expose
    public String isProficientInThalam = "";
    @SerializedName("isProficientInVarisais")
    @Expose
    public String isProficientInVarisais = "";
    @SerializedName("isProficientInSwarasthanam")
    @Expose
    public String isProficientInSwarasthanam = "";
    @SerializedName("isProficientInAlankaram")
    @Expose
    public String isProficientInAlankaram = "";
    @SerializedName("isProficientInGeetham")
    @Expose
    public String isProficientInGeetham = "";
    @SerializedName("StudentTutorBookingId")
    @Expose
    public Integer StudentTutorBookingId = 0;
}

