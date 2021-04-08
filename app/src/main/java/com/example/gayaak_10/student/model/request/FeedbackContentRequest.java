package com.example.gayaak_10.student.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedbackContentRequest {
    @SerializedName("sessionFeedbackId")
    @Expose
    public int sessionFeedbackId = 0;
    @SerializedName("tutorId")
    @Expose
    public int tutorId = 0;
    @SerializedName("courseId")
    @Expose
    public Object courseId = null;
    @SerializedName("studentId")
    @Expose
    public int studentId = 0;
    @SerializedName("sessionId")
    @Expose
    public int sessionId = 0;
    @SerializedName("feedbackId")
    @Expose
    public Object feedbackId = null;
    @SerializedName("rating")
    @Expose
    public float rating = 0;
    @SerializedName("lessonPercent")
    @Expose
    public Object lessonPercent = null;
    @SerializedName("date")
    @Expose
    public String date = "";
    @SerializedName("moduleId")
    @Expose
    public Object moduleId = null;
    @SerializedName("feedBackTypeIId")
    @Expose
    public Integer feedBackTypeIId;
    @SerializedName("IsExpAnyTechnicalDifficulties")
    @Expose
    public boolean IsExpAnyTechnicalDifficulties = false;
    @SerializedName("IsInterestedToJoin")
    @Expose
    public boolean IsInterestedToJoin = false;
    @SerializedName("Feedbacktext")
    @Expose
    public String Feedbacktext = "";
    @SerializedName("StudentTutorBookingId")
    @Expose
    public Integer StudentTutorBookingId = 0;

}

