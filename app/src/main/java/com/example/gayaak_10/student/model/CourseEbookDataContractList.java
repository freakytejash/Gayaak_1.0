package com.example.gayaak_10.student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CourseEbookDataContractList {

    @SerializedName("CourseEbookId")
    @Expose
    public Integer courseEbookId;
    @SerializedName("CourseId")
    @Expose
    public Integer courseId;
    @SerializedName("EbookId")
    @Expose
    public Integer ebookId;
    @SerializedName("EbookName")
    @Expose
    public Object ebookName;
    @SerializedName("Ebooklist")
    @Expose
    public Object ebooklist;
    @SerializedName("EbookDataContract")
    @Expose
    public EbookDataContract ebookDataContract;
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
}
