package com.example.gayaak_10.common.viewmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletCoinDetail {

    @SerializedName("UserWalletId")
    @Expose
    public Integer userWalletId;
    @SerializedName("UserId")
    @Expose
    public Integer userId;
    @SerializedName("Coins")
    @Expose
    public Integer coins;
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