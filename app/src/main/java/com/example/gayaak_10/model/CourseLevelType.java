package com.example.gayaak_10.model;



public class CourseLevelType {

    public String name = "";
    public int levelId = 0;
    public boolean isSelected = false;

    public CourseLevelType(String name, int levelId, boolean isSelected){
        this.name = name;
        this.levelId = levelId;
        this.isSelected = isSelected;
    }
}
