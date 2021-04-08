package com.example.gayaak_10.student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AudioModuleDataContractList {

    @SerializedName("mp3ForModuleId")
    @Expose
    public Integer mp3ForModuleId;
    @SerializedName("ModuleId")
    @Expose
    public Integer moduleId;
    @SerializedName("Name")
    @Expose
    public String name;
    @SerializedName("Duration")
    @Expose
    public String duration;
    @SerializedName("IsfreeMp3")
    @Expose
    public Boolean IsfreeMp3;
    @SerializedName("orderNo")
    @Expose
    public Integer orderNo;
    @SerializedName("ThumbnailImage")
    @Expose
    public String thumbnailImage;
    @SerializedName("Mp3URL")
    @Expose
    public String Mp3URL;
    @SerializedName("IsActive")
    @Expose
    public Boolean isActive;
    @SerializedName("CreatedDate")
    @Expose
    public String createdDate;
    @SerializedName("ModifiedDate")
    @Expose
    public Object modifiedDate;
    @SerializedName("CreatedBy")
    @Expose
    public Integer createdBy;
    @SerializedName("ModifiedBy")
    @Expose
    public Object modifiedBy;
    @SerializedName("LoginUserId")
    @Expose
    public Object loginUserId;
    @SerializedName("description")
    @Expose
    public String description = "";
}
