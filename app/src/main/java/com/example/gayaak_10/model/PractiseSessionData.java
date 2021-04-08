package com.example.gayaak_10.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PractiseSessionData {

    @SerializedName("PractiseSessionId")
    @Expose
    public int PractiseSessionId = 0;
    @SerializedName("UserId")
    @Expose
    public int UserId = 0;
    @SerializedName("CourseId")
    @Expose
    public int CourseId = 0;
    @SerializedName("Comment")
    @Expose
    public String Comment = "";
    @SerializedName("AudioFileName")
    @Expose
    public String AudioFileName = "";
    @SerializedName("AudioFileFormat")
    @Expose
    public String AudioFileFormat = "";
    @SerializedName("IsActive")
    @Expose
    public Boolean IsActive = false;
    @SerializedName("CreatedDate")
    @Expose
    public String CreatedDate = "";
    @SerializedName("ModifiedDate")
    @Expose
    public String ModifiedDate = "";
    @SerializedName("CreatedBy")
    @Expose
    public int CreatedBy = 0;
    @SerializedName("ModifiedBy")
    @Expose
    public String ModifiedBy = "";
    @SerializedName("LoginUserId")
    @Expose
    public int LoginUserId = 0;
}
