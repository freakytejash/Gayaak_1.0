package com.example.gayaak_10.model;



public class CourseType {

    public String name = "";
    public Boolean isSelected = false;
    public int courseId = 0;

    public CourseType(String name, Boolean isSelected){
        this.name = name;
        this.isSelected = isSelected;
    }

    public CourseType(String name, int courseId) {
        this.name = name;
        this.courseId = courseId;
    }
}
