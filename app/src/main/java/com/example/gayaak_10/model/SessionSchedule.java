package com.example.gayaak_10.model;

import com.example.gayaak_10.model.tutor.TutorSlotTime;
import com.example.gayaak_10.tutor.model.ScheduleSlotDetail;

import java.util.ArrayList;
import java.util.Date;

public class SessionSchedule {

    public Date days = new Date();
    public ArrayList<TutorSlotTime> slotTime = new ArrayList<>();
    public ScheduleSlotDetail scheduleSlotTimeArrayList = new ScheduleSlotDetail();

    public SessionSchedule(Date days, ArrayList<TutorSlotTime> slotTime) {
        this.days = days;
        this.slotTime = slotTime;
    }

    public SessionSchedule(ScheduleSlotDetail scheduleSlotTimeArrayList) {
        this.scheduleSlotTimeArrayList = scheduleSlotTimeArrayList;
    }
}
