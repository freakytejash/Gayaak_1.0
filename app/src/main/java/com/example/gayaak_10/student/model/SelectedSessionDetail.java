package com.example.gayaak_10.student.model;

public class SelectedSessionDetail{

    public String courseName;
    public Integer courseId;
    public Integer coursePrice;
    public Integer courseSessions;
    public String courseSelectedDay;
    public String courseSelectedTime;
    public Integer TutorId;
    public Integer TutorScheduleId;
    public Integer levelId;
    public Integer StudentTutorBookingId;
    public Integer ScheduleId;
    public Integer ScheduleId2;
    public Integer liveClassDetailId;
    public Integer categoryId;

    public SelectedSessionDetail(String courseName, Integer courseId, Integer coursePrice, Integer courseSessions,
                                 String courseSelectedDay, String courseSelectedTime, Integer tutorId,
                                 Integer tutorScheduleId, Integer levelId, Integer studentTutorBookingId,
                                 Integer scheduleId, Integer scheduleId2, Integer liveClassDetailId,  Integer categoryId) {
        this.courseName = courseName;
        this.courseId = courseId;
        this.coursePrice = coursePrice;
        this.courseSessions = courseSessions;
        this.courseSelectedDay = courseSelectedDay;
        this.courseSelectedTime = courseSelectedTime;
        TutorId = tutorId;
        TutorScheduleId = tutorScheduleId;
        this.levelId = levelId;
        StudentTutorBookingId = studentTutorBookingId;
        ScheduleId = scheduleId;
        ScheduleId2 = scheduleId2;
        this.liveClassDetailId = liveClassDetailId;
        this.categoryId = categoryId;
    }

    public SelectedSessionDetail(String courseName, Integer courseId, Integer coursePrice, Integer courseSessions, String courseSelectedDay, String courseSelectedTime) {
        this.courseName = courseName;
        this.courseId = courseId;
        this.coursePrice = coursePrice;
        this.courseSessions = courseSessions;
        this.courseSelectedDay = courseSelectedDay;
        this.courseSelectedTime = courseSelectedTime;
    }

    public SelectedSessionDetail() {

    }
}