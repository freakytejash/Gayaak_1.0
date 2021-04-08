package com.example.gayaak_10.model.response;

import java.util.ArrayList;

public class FilterData {
    public String name;
    public ArrayList<CoursesDetail> coursesDetailArrayList = new ArrayList<>();

    public FilterData(String categoryName, ArrayList<CoursesDetail> list) {
        this.name = categoryName;
        this.coursesDetailArrayList = list;
    }
}
