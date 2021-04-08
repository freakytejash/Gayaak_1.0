package com.example.gayaak_10.student.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentSupportRequest {

    @SerializedName("SupportId")
    @Expose
    public Integer supportId;
    @SerializedName("TitleId")
    @Expose
    public Integer titleId;
    @SerializedName("Title")
    @Expose
    public String title;
    @SerializedName("Comment")
    @Expose
    public String comment;

}
