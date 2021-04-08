package com.example.gayaak_10.common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TutorCalendarDetail {

    @SerializedName("LiveClassDataContractList")
    @Expose
    public ArrayList<TutorCalendarLiveClassDataContractList> liveClassDataContractList = new ArrayList<>();
    @SerializedName("TotalCount")
    @Expose
    public Integer totalCount;
    @SerializedName("TransactionDetailDataContractList")
    @Expose
    public ArrayList<TransactionDetailDataContractList> transactionDetailDataContractLists = new ArrayList<>();
}
