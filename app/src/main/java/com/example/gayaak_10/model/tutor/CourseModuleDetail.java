package com.example.gayaak_10.model.tutor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CourseModuleDetail {

    @SerializedName("ModuleId")
    @Expose
    public Integer moduleId;
    @SerializedName("CourseId")
    @Expose
    public Integer courseId;
    @SerializedName("Name")
    @Expose
    public String name;
    @SerializedName("Description")
    @Expose
    public String description = "";
    @SerializedName("EbookId")
    @Expose
    public Integer ebookId;
    @SerializedName("EbookName")
    @Expose
    public String ebookName;
    @SerializedName("Ebooklist")
    @Expose
    public Object ebooklist;
    @SerializedName("Mp3ModuleDataContractList")
    @Expose
    public Object mp3ModuleDataContractList;
    @SerializedName("PDFModuleDataContractList")
    @Expose
    public Object pDFModuleDataContractList;
    @SerializedName("VediosModuleDataContractList")
    @Expose
    public Object vediosModuleDataContractList;
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
