package com.example.gayaak_10.student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LevelCategoryInfo {

    @SerializedName("LearningLevelDataContractList")
    @Expose
    public ArrayList<LearningLevelDataContractList> learningLevelDataContractList = new ArrayList<>();
    @SerializedName("CategoryDataContractList")
    @Expose
    public ArrayList<CategoryDataContractList> categoryDataContractList = new ArrayList<>();
}
