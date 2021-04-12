package com.example.gayaak_10.student.model;

public class CourseAndStudentTutorBookingIdModel {
    Integer CourseId, CategoryId, StudentTutorBookingId;

    public CourseAndStudentTutorBookingIdModel(Integer courseId, Integer categoryId, Integer studentTutorBookingId) {
        CourseId = courseId;
        CategoryId = categoryId;
        StudentTutorBookingId = studentTutorBookingId;
    }

    public Integer getCourseId() {
        return CourseId;
    }

    public void setCourseId(Integer courseId) {
        CourseId = courseId;
    }

    public Integer getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(Integer categoryId) {
        CategoryId = categoryId;
    }

    public Integer getStudentTutorBookingId() {
        return StudentTutorBookingId;
    }

    public void setStudentTutorBookingId(Integer studentTutorBookingId) {
        StudentTutorBookingId = studentTutorBookingId;
    }
}
