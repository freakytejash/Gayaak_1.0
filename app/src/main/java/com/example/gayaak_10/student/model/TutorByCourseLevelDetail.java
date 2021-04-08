package com.example.gayaak_10.student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TutorByCourseLevelDetail {

    @SerializedName("CourseTutorDataContractList")
    @Expose
    public ArrayList<CourseTutorDataContractList> courseTutorDataContractList = null;
    @SerializedName("TotalCount")
    @Expose
    public Integer totalCount;

}
