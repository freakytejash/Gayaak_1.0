package com.example.gayaak_10.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserWalletDataContract {
    @SerializedName("UserWalletId")
    @Expose
    public Integer UserWalletId;
    @SerializedName("UserId")
    @Expose
    public Integer UserId = 0;
    @SerializedName("Coins")
    @Expose
    public Integer Coins = 0;
    @SerializedName("IsActive")
    @Expose
    public Boolean IsActive = false;
    @SerializedName("createdDate")
    @Expose
    public String createdDate = "";
    @SerializedName("modifiedDate")
    @Expose
    public String modifiedDate = "";
    @SerializedName("createdBy")
    @Expose
    public Integer createdBy = 0;
    @SerializedName("modifiedBy")
    @Expose
    public Integer modifiedBy = 0;
}
