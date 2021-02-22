package com.example.gayaak_10.tutor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScheduleSlotDetail  {

    @SerializedName("IsScheduleAvailable")
    @Expose
    public Integer IsScheduleAvailable = -1;
    @SerializedName("TutorScheduleId")
    @Expose
    public Integer tutorScheduleId;
    @SerializedName("ScheduleId")
    @Expose
    public Integer scheduleId;
    @SerializedName("Time")
    @Expose
    public String time;
    @SerializedName("Day")
    @Expose
    public String day;
    @SerializedName("IsActive")
    @Expose
    public Boolean isActive;

    public Boolean isSelected = false;

}
