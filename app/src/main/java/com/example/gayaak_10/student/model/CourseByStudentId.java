package com.example.gayaak_10.student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CourseByStudentId {

    @SerializedName("Status")
    @Expose
    public Boolean status;
    @SerializedName("Message")
    @Expose
    public String message;
    @SerializedName("Detail")
    @Expose
    public ArrayList<CourseByStudentDetail> detail = new ArrayList();

    public class CourseByStudentDetail{
        @SerializedName("CourseId")
        @Expose
        public Integer courseId;
        @SerializedName("Name")
        @Expose
        public String name;
        @SerializedName("Price")
        @Expose
        public Integer price = 0;
        @SerializedName("Detail")
        @Expose
        public String detail;
        @SerializedName("NoofSession")
        @Expose
        public Integer noofSession;
        @SerializedName("Duration")
        @Expose
        public String duration;
        @SerializedName("LevelId")
        @Expose
        public Integer levelId;
        @SerializedName("LevelName")
        @Expose
        public String levelName = "";
        @SerializedName("CategoryId")
        @Expose
        public Integer categoryId;
        @SerializedName("CategoryName")
        @Expose
        public Object categoryName;
        @SerializedName("CourseTutorDataContract")
        @Expose
        public ArrayList<CourseDayTime> courseTutorsDataContractList = new ArrayList<CourseDayTime>();
        @SerializedName("ThumbnailImage")
        @Expose
        public String thumbnailImage;
        @SerializedName("CourseType")
        @Expose
        public Integer courseType;
        @SerializedName("CourseNames")
        @Expose
        public String courseNames = "";
        @SerializedName("TutorDataContract")
        @Expose
        public TutorDataContract tutorDataContract;

        @SerializedName("DemoTutorSessionFeedbackDataContractList")
        @Expose
        public ArrayList<TutorFeedback> DemoTutorSessionFeedbackDataContractList = new ArrayList<TutorFeedback>();

public class TutorFeedback{
    @SerializedName("DemoTutorSessionFeedbackId")
    @Expose
    public Integer DemoTutorSessionFeedbackId;

    @SerializedName("TutorId")
    @Expose
    public Integer TutorId;

    @SerializedName("feedbackText")
    @Expose
    public String feedbackText;

    @SerializedName("CourseId")
    @Expose
    public Integer CourseId;

    @SerializedName("StudentId")
    @Expose
    public Integer StudentId;

    @SerializedName("Rating")
    @Expose
    public String Rating;

    @SerializedName("ModuleId")
    @Expose
    public Integer ModuleId;

    @SerializedName("IsStudentQualifies")
    @Expose
    public Boolean IsStudentQualifies;

    @SerializedName("IsClassTimeDecided")
    @Expose
    public Boolean IsClassTimeDecided;

    @SerializedName("NoOfClassPerWeek")
    @Expose
    public Integer NoOfClassPerWeek;

    @SerializedName("ScheduleId1")
    @Expose
    public Integer ScheduleId1;

    @SerializedName("ScheduleId2")
    @Expose
    public Integer ScheduleId2;

    @SerializedName("IsStudentGotSittingTolerance")
    @Expose
    public Boolean IsStudentGotSittingTolerance;

    @SerializedName("IsProficientInDiction")
    @Expose
    public String IsProficientInDiction;

    @SerializedName("IsProficientInShruthi")
    @Expose
    public String IsProficientInShruthi;

    @SerializedName("IsProficientInThalam")
    @Expose
    public String IsProficientInThalam;

    @SerializedName("IsProficientInVarisais")
    @Expose
    public String IsProficientInVarisais;

    @SerializedName("IsProficientInSwarasthanam")
    @Expose
    public String IsProficientInSwarasthanam;

    @SerializedName("IsProficientInAlankaram")
    @Expose
    public String IsProficientInAlankaram;

    @SerializedName("IsProficientInGeetham")
    @Expose
    public String IsProficientInGeetham;

    @SerializedName("TutorName")
    @Expose
    public String TutorName;

    @SerializedName("StudentName")
    @Expose
    public String StudentName;

    @SerializedName("FeedbackType")
    @Expose
    public String FeedbackType;

    @SerializedName("StudentTutorBookingId")
    @Expose
    public Integer StudentTutorBookingId;

    @SerializedName("CourseName")
    @Expose
    public String CourseName;

    @SerializedName("IsActive")
    @Expose
    public Boolean IsActive;

    @SerializedName("CreatedDate")
    @Expose
    public String CreatedDate;

    @SerializedName("ModifiedDate")
    @Expose
    public String ModifiedDate;

    @SerializedName("CreatedBy")
    @Expose
    public Integer CreatedBy;

    @SerializedName("ModifiedBy")
    @Expose
    public Integer ModifiedBy;

    @SerializedName("LoginUserId")
    @Expose
    public Integer LoginUserId;
}

        public class CourseDayTime{
            @SerializedName("Id")
            @Expose
            public Integer Id;
            @SerializedName("CourseId")
            @Expose
            public Integer CourseId;
            @SerializedName("TutorId")
            @Expose
            public Integer TutorId;
            @SerializedName("TutorName")
            @Expose
            public String TutorName;
            @SerializedName("CourseName")
            @Expose
            public String CourseName;
            @SerializedName("CategoryId")
            @Expose
            public String CategoryId;
            @SerializedName("CategoryName")
            @Expose
            public String CategoryName;
            @SerializedName("LevelName")
            @Expose
            public String LevelName;
            @SerializedName("VideoURL")
            @Expose
            public String videoURL;
            @SerializedName("ThumbnailImage")
            @Expose
            public String thumbnailImage;
            @SerializedName("TutorEmail")
            @Expose
            public String TutorEmail;
            @SerializedName("Day")
            @Expose
            public String Day;
            @SerializedName("Time")
            @Expose
            public String Time;
            @SerializedName("levelId")
            @Expose
            public String levelId;
            @SerializedName("TutorScheduleId")
            @Expose
            public String TutorScheduleId;
            @SerializedName("ScheduleId")
            @Expose
            public String ScheduleId;
        }


        public class TutorDataContract{
            @SerializedName("UserId")
            @Expose
            public Integer userId;
            @SerializedName("RoleId")
            @Expose
            public Integer roleId;
            @SerializedName("Role")
            @Expose
            public Object role;
            @SerializedName("IsAdmin")
            @Expose
            public Object isAdmin;
            @SerializedName("FirstName")
            @Expose
            public String firstName = "";
            @SerializedName("LastName")
            @Expose
            public String lastName = "";
            @SerializedName("FullName")
            @Expose
            public Object fullName;
            @SerializedName("Email")
            @Expose
            public String email;
            @SerializedName("Phone")
            @Expose
            public String phone;
            @SerializedName("Gender")
            @Expose
            public Integer gender;
            @SerializedName("ProfileImage")
            @Expose
            public String profileImage;
            @SerializedName("ImageName")
            @Expose
            public String imageName;
            @SerializedName("UserImage")
            @Expose
            public Object userImage;
            @SerializedName("VideoURL")
            @Expose
            public String videoURL;
        }
    }


}



