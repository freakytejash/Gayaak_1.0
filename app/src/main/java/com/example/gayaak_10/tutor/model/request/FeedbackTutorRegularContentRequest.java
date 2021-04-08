package com.example.gayaak_10.tutor.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FeedbackTutorRegularContentRequest {


    @SerializedName("ProgressId")
    @Expose
    public Integer progressId;
    @SerializedName("StudentId")
    @Expose
    public String studentId;
    @SerializedName("TutorId")
    @Expose
    public Integer tutorId;
    @SerializedName("CourseId")
    @Expose
    public Integer courseId;
    @SerializedName("ModuleId")
    @Expose
    public Object moduleId;
    @SerializedName("StatusId")
    @Expose
    public Object statusId;
    @SerializedName("StudnetTutorBookingId")
    @Expose
    public Integer studnetTutorBookingId;
    @SerializedName("liveClassDetailId")
    @Expose
    public Integer liveClassDetailId;
    @SerializedName("Comment")
    @Expose
    public String comment;
    @SerializedName("Comment2")
    @Expose
    public String comment2;
    @SerializedName("Rating")
    @Expose
    public String rating;
    @SerializedName("ModuleStatusDataContractList")
    @Expose
    public ArrayList<ModuleStatusDataContractList> moduleStatusDataContractList = null;




    public static class ModuleStatusDataContractList{
        @SerializedName("ModuleId")
        @Expose
        public Integer moduleId;
        @SerializedName("StatusId")
        @Expose
        public Integer statusId;

        public ModuleStatusDataContractList(int moduleId, int statusId) {
            this.moduleId=moduleId;
            this.statusId=statusId;
        }
    }
}


