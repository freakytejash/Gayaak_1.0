package com.example.gayaak_10.tutor.model;

import java.util.ArrayList;

public class ScheduleSlotTimeTemp {

    public String dayName;
    public ArrayList<ScheduleSlotDetail> detail = null;


    public ScheduleSlotTimeTemp(String dayName, ArrayList<ScheduleSlotDetail> detail) {
        this.dayName = dayName;
        this.detail = detail;
    }
}
