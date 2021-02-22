package com.example.gayaak_10.student.model;

import com.example.gayaak_10.model.response.VideoData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VideosModuleDataContractList {

    @SerializedName("CourseVideosId")
    @Expose
    public Integer courseVideosId;
    @SerializedName("ModuleId")
    @Expose
    public Integer moduleId;
    @SerializedName("Name")
    @Expose
    public String name;
    @SerializedName("VideoURL")
    @Expose
    public String videoURL;
    @SerializedName("ThumbnailImage")
    @Expose
    public String thumbnailImage;
    @SerializedName("Duration")
    @Expose
    public Object duration;
    @SerializedName("Isfreevedio")
    @Expose
    public Boolean isfreevedio;
    @SerializedName("orderNo")
    @Expose
    public Integer orderNo;
    @SerializedName("IsActive")
    @Expose
    public Boolean isActive;
    @SerializedName("CreatedDate")
    @Expose
    public String createdDate;
    @SerializedName("ModifiedDate")
    @Expose
    public Object modifiedDate;
    @SerializedName("CreatedBy")
    @Expose
    public Integer createdBy;
    @SerializedName("ModifiedBy")
    @Expose
    public Object modifiedBy;
    @SerializedName("LoginUserId")
    @Expose
    public Object loginUserId;

    // to store last position

    public VideoData.PlayedVideoData playedVideoDataArrayList = new VideoData.PlayedVideoData();

    public static class PlayedVideoData {
        public int videoCourseId = 0;
        public boolean isVideoPlayed = false;
        public int videoId = 0;
        public boolean hasWatchedComplete = false; // to check if viewed completely
        public long videoPlayedPosition = 0;
    }
}
