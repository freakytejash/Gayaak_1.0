package com.example.gayaak_10.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PractiseSessionInfoDetail implements Parcelable {

    @SerializedName("PractiseSessionId")
    @Expose
    public Integer practiseSessionId;
    @SerializedName("UserId")
    @Expose
    public Integer userId;
    @SerializedName("CourseId")
    @Expose
    public Integer courseId;
    @SerializedName("Comment")
    @Expose
    public String comment;
    @SerializedName("AudioFileName")
    @Expose
    public String audioFileName;
    @SerializedName("AudioData")
    @Expose
    public Object audioData;
    @SerializedName("AudioFileFormat")
    @Expose
    public Object audioFileFormat;
    @SerializedName("PractiseSessionDataFeedBackContractList")
    @Expose
    public Object practiseSessionDataFeedBackContractList;
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
    public Object modifiedBy;
    @SerializedName("LoginUserId")
    @Expose
    public Object loginUserId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
