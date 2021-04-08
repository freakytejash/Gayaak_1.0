package com.example.gayaak_10.student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EbookDataContract {

    @SerializedName("EbookId")
    @Expose
    public Integer ebookId;
    @SerializedName("BookName")
    @Expose
    public String bookName;
    @SerializedName("Description")
    @Expose
    public String description;
    @SerializedName("IsActive")
    @Expose
    public Boolean isActive;
    @SerializedName("CreatedBy")
    @Expose
    public Integer createdBy;
    @SerializedName("CreatedDate")
    @Expose
    public String createdDate;
    @SerializedName("ModifiedBy")
    @Expose
    public Integer modifiedBy;
    @SerializedName("ModifiedDate")
    @Expose
    public String modifiedDate;
    @SerializedName("ModuleId")
    @Expose
    public Object moduleId;
    @SerializedName("booklink")
    @Expose
    public String booklink;
    @SerializedName("ThumbnailImage")
    @Expose
    public String thumbnailImage;
    @SerializedName("ImageName")
    @Expose
    public String imageName;
}
