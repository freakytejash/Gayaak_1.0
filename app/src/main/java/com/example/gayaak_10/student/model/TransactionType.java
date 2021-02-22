package com.example.gayaak_10.student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TransactionType {

    @SerializedName("Status")
    @Expose
    public Boolean status;
    @SerializedName("Message")
    @Expose
    public String message;
    @SerializedName("Detail")
    @Expose
    public ArrayList<TransactionTypeDetail> detail = new ArrayList<TransactionTypeDetail>();


    public class TransactionTypeDetail{
        @SerializedName("Disabled")
        @Expose
        public Boolean disabled;
        @SerializedName("Group")
        @Expose
        public Object group;
        @SerializedName("Selected")
        @Expose
        public Boolean selected;
        @SerializedName("Text")
        @Expose
        public String text;
        @SerializedName("Value")
        @Expose
        public String value;
    }
}
