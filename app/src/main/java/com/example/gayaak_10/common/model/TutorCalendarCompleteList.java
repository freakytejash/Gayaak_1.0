package com.example.gayaak_10.common.model;

public class TutorCalendarCompleteList {

    public String date;
    public String type;
    public TutorCalendarLiveClassDataContractList liveClassData = new TutorCalendarLiveClassDataContractList();
    public TransactionDetailDataContractList transactionData = new TransactionDetailDataContractList();

    public TutorCalendarCompleteList(String type,
                                     TutorCalendarLiveClassDataContractList liveClassData,
                                     TransactionDetailDataContractList transactionData) {
        this.type = type;
        this.liveClassData = liveClassData;
        this.transactionData = transactionData;
    }
}
