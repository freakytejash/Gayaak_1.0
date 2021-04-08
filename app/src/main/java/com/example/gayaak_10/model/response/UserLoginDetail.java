package com.example.gayaak_10.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserLoginDetail {

    @SerializedName("UserDataContract")
    @Expose
    public UserDataContract userDataContract;
    @SerializedName("TockenString")
    @Expose
    public String tockenString;
    @SerializedName("StatusDataContract")
    @Expose
    public Object statusDataContract;
    @SerializedName("IsActive")
    @Expose
    public Object isActive;
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
}
