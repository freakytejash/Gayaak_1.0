package com.example.gayaak_10.student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModuleWithVideoDetail {

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
    public Object ebookName;
    @SerializedName("Ebooklist")
    @Expose
    public Object ebooklist;
    @SerializedName("Mp3ModuleDataContractList")
    @Expose
    public ArrayList<AudioModuleDataContractList> mp3ModuleDataContractList = new ArrayList();
    @SerializedName("PDFModuleDataContractList")
    @Expose
    public ArrayList<PDFModuleDataContractList> pDFModuleDataContractList = new ArrayList();
    @SerializedName("VediosModuleDataContractList")
    @Expose
    public ArrayList<VideosModuleDataContractList> videosModuleDataContractList = new ArrayList<>();
    @SerializedName("IsActive")
    @Expose
    public Boolean isActive;
    @SerializedName("CreatedDate")
    @Expose
    public String createdDate;
    @SerializedName("ModifiedDate")
    @Expose
    public String modifiedDate;
    @SerializedName("CreatedBy")
    @Expose
    public Integer createdBy;
    @SerializedName("ModifiedBy")
    @Expose
    public Integer modifiedBy;
    @SerializedName("LoginUserId")
    @Expose
    public Object loginUserId;



}
