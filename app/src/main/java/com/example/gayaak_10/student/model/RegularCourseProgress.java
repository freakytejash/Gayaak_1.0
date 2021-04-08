package com.example.gayaak_10.student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RegularCourseProgress {
    @SerializedName("Status")
    @Expose
    public Boolean status;
    @SerializedName("Message")
    @Expose
    public String message;
    @SerializedName("Detail")
    @Expose
    public Detail detail;

    public class Detail {

        @SerializedName("ModuledataContractList")
        @Expose
        public ArrayList<ModuledataContractList> moduledataContractList = null;
        @SerializedName("ModuleCount")
        @Expose
        public Integer moduleCount;
        @SerializedName("TotalPercentage")
        @Expose
        public Integer totalPercentage;

    }

    public class ModuledataContractList {

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
        public String description;
        @SerializedName("EbookId")
        @Expose
        public Integer ebookId;
        @SerializedName("EbookName")
        @Expose
        public Object ebookName;
        @SerializedName("Ebooklist")
        @Expose
        public Object ebooklist;
        @SerializedName("ModuleStatus")
        @Expose
        public String moduleStatus;
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
}
