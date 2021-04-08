package com.example.gayaak_10.student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PDFModuleDataContractList {

    @SerializedName("PDFForModuleId")
    @Expose
    public Integer pDFForModuleId;
    @SerializedName("ModuleId")
    @Expose
    public Integer moduleId;
    @SerializedName("Name")
    @Expose
    public String name;
    @SerializedName("Duration")
    @Expose
    public String duration;
    @SerializedName("IsfreePDF3")
    @Expose
    public Boolean isfreePDF3;
    @SerializedName("orderNo")
    @Expose
    public Integer orderNo;
    @SerializedName("ThumbnailImage")
    @Expose
    public String thumbnailImage;
    @SerializedName("PDFURL")
    @Expose
    public String pDFURL;
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
