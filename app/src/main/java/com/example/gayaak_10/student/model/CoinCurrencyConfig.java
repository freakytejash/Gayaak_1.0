package com.example.gayaak_10.student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CoinCurrencyConfig {

    @SerializedName("Status")
    @Expose
    public Boolean status;
    @SerializedName("Message")
    @Expose
    public String message;
    @SerializedName("Detail")
    @Expose
    public ArrayList<CoinCurrencyConfigDetail> detail = new ArrayList<>();


    public class CoinCurrencyConfigDetail{
        @SerializedName("CurrencyCoinConfigId")
        @Expose
        public Integer currencyCoinConfigId;
        @SerializedName("CurrencyId")
        @Expose
        public Integer currencyId;
        @SerializedName("CurrencyName")
        @Expose
        public String currencyName;
        @SerializedName("NoOfCoin")
        @Expose
        public Integer noOfCoin;
        @SerializedName("CurrencyValue")
        @Expose
        public Integer currencyValue;
        @SerializedName("Detail")
        @Expose
        public Object detail;
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
}
