package com.example.gayaak_10.student.model;

import java.util.ArrayList;

public class CourseDayTimeTemp {

    public String dayName;
    public ArrayList<CourseByStudentId.CourseByStudentDetail.CourseDayTime> detail = new ArrayList<>();


    public CourseDayTimeTemp(String dayName, ArrayList<CourseByStudentId.CourseByStudentDetail.CourseDayTime> detail) {
        this.dayName = dayName;
        this.detail = detail;
    }
}