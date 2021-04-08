package com.example.gayaak_10.student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LearningLevelDataContractList {

    @SerializedName("LearningLevelId")
    @Expose
    public Integer learningLevelId;
    @SerializedName("Name")
    @Expose
    public String name;
    @SerializedName("Description")
    @Expose
    public String description;
    @SerializedName("ImageName")
    @Expose
    public Object imageName;
    @SerializedName("IsActive")
    @Expose
    public Boolean isActive;
    @SerializedName("CreatedDate")
    @Expose
    public Object createdDate;
    @SerializedName("ModifiedDate")
    @Expose
    public Object modifiedDate;
    @SerializedName("CreatedBy")
    @Expose
    public Object createdBy;
    @SerializedName("ModifiedBy")
    @Expose
    public Object modifiedBy;
    @SerializedName("LoginUserId")
    @Expose
    public Object loginUserId;

    public Boolean isSelected = false;

}
