package com.example.gayaak_10.student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletRechargePlanDataContractList{
    @SerializedName("WalletRechargePlanId")
    @Expose
    public Integer walletRechargePlanId;
    @SerializedName("Name")
    @Expose
    public String name;
    @SerializedName("Detail")
    @Expose
    public Object detail;
    @SerializedName("NoOfCoin")
    @Expose
    public Integer noOfCoin;
    @SerializedName("ExtraCoin")
    @Expose
    public Integer extraCoin;
    @SerializedName("WalletRechargePlanDataContractList")
    @Expose
    public Object walletRechargePlanDataContractList;
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

    public Boolean isSelected = false;

    public int indiaCurrencyValue = 0;

    public int usCurrencyValue = 0;
}
