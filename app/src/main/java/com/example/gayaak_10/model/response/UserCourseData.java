package com.example.gayaak_10.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserCourseData {

    @SerializedName("CourseSubscriptionId")
    @Expose
    public int CourseSubscriptionId = 0;
    @SerializedName("CourseId")
    @Expose
    public int CourseId = 0;
    @SerializedName("UserId")
    @Expose
    public int UserId = 0;
    @SerializedName("Name")
    @Expose
    public String Name = "";
    @SerializedName("Price")
    @Expose
    public int Price = 0;
    @SerializedName("StartDate")
    @Expose
    public String StartDate = "";
    @SerializedName("EndDate")
    @Expose
    public String EndDate = "";
    @SerializedName("PaymentMethodId")
    @Expose
    public int PaymentMethodId = 0;
    @SerializedName("PaymentMethodName")
    @Expose
    public Object PaymentMethodName;
    @SerializedName("TransactionId")
    @Expose
    public String TransactionId = "0";
    @SerializedName("IsActive")
    @Expose
    public Boolean IsActive = false;
    @SerializedName("CreatedDate")
    @Expose
    public String CreatedDate = "";
    @SerializedName("CreatedBy")
    @Expose
    public int CreatedBy = 0;
}

