package com.example.gayaak_10.tutor.model;

public class StudentProficiency {

    public boolean isSelected = false;
    public String proficiencyName = "";

    public StudentProficiency(String proficiencyName, boolean isSelected) {
        this.proficiencyName = proficiencyName;
        this.isSelected = isSelected;
    }
}
