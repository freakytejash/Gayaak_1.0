package com.example.gayaak_10.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CourseSubscriptionRequest {
    @SerializedName("CourseSubscriptionId")
    @Expose
    public Integer courseSubscriptionId;
    @SerializedName("UserId")
    @Expose
    public Integer userId;
    @SerializedName("CourseId")
    @Expose
    public Integer courseId;
    @SerializedName("Comment")
    @Expose
    public String comment;
    @SerializedName("Price")
    @Expose
    public Integer price;
    @SerializedName("StartDate")
    @Expose
    public String startDate;
    @SerializedName("EndDate")
    @Expose
    public String endDate;
    @SerializedName("PaymentMethodId")
    @Expose
    public Integer paymentMethodId;
    @SerializedName("TransactionId")
    @Expose
    public String transactionId;
    @SerializedName("IsActive")
    @Expose
    public Boolean isActive;
    @SerializedName("CreatedDate")
    @Expose
    public Object createdDate;
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
    public Integer loginUserId;

}
