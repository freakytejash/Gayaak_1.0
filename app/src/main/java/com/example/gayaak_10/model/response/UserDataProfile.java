package com.example.gayaak_10.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDataProfile {

    @SerializedName("Status")
    @Expose
    public Boolean status;
    @SerializedName("Message")
    @Expose
    public String message;
    @SerializedName("Detail")
    @Expose
    public UserDetail detail;

}
