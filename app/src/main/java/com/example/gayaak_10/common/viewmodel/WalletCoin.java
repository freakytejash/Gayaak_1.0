package com.example.gayaak_10.common.viewmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletCoin {
    @SerializedName("Status")
    @Expose
    public Boolean status;
    @SerializedName("Message")
    @Expose
    public String message;
    @SerializedName("Detail")
    @Expose
    public WalletCoinDetail detail;
}
