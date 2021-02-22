package com.example.gayaak_10.student.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletUpdateRequest {
    @SerializedName("userWalletTransactionId")
    @Expose
    public Integer userWalletTransactionId;
    @SerializedName("userId")
    @Expose
    public Integer userId;
    @SerializedName("coins")
    @Expose
    public Integer coins;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("courseId")
    @Expose
    public Integer courseId;
    @SerializedName("transactionId")
    @Expose
    public String transactionId;
    @SerializedName("transactionTypeId")
    @Expose
    public Integer transactionTypeId;
    @SerializedName("isActive")
    @Expose
    public Boolean isActive;
    @SerializedName("createdBy")
    @Expose
    public Integer createdBy;
    @SerializedName("createdDate")
    @Expose
    public String createdDate;
    @SerializedName("modifiedBy")
    @Expose
    public Integer modifiedBy;
    @SerializedName("modifiedDate")
    @Expose
    public String modifiedDate;
    @SerializedName("walletRechargePlanId")
    @Expose
    public Integer walletRechargePlanId;
    @SerializedName("CreditDebit")
    @Expose
    public Integer CreditDebit;
}
