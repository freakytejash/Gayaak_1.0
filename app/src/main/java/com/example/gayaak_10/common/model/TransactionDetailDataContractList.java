package com.example.gayaak_10.common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionDetailDataContractList {

    @SerializedName("UserId")
    @Expose
    public Integer UserId = 0;
    @SerializedName("CreditAmount")
    @Expose
    public Integer CreditAmount = 0;
    @SerializedName("CreditDateAndTime")
    @Expose
    public String CreditDateAndTime = "";
    @SerializedName("TotalCredit")
    @Expose
    public Integer TotalCredit = 0;
}