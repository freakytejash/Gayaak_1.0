package com.example.gayaak_10.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VideoData {

    @SerializedName("CourseVideosId")
    @Expose
    public Integer CourseVideosId;
    @SerializedName("CourseId")
    @Expose
    public Integer CourseId = 0;
    @SerializedName("Name")
    @Expose
    public String Name = "";
    @SerializedName("VideoURL")
    @Expose
    public String VideoURL = "";
    @SerializedName("ThumbnailImage")
    @Expose
    public String ThumbnailImage = "";
    @SerializedName("Duration")
    @Expose
    public String Duration = "";
    @SerializedName("Isfreevedio")
    @Expose
    public Boolean IsFreeVideo = false;
    @SerializedName("IsActive")
    @Expose
    public Boolean IsActive = false;
    @SerializedName("CreatedDate")
    @Expose
    public String CreatedDate = "";
    @SerializedName("ModifiedDate")
    @Expose
    public String ModifiedDate = "";
    @SerializedName("CreatedBy")
    @Expose
    public Integer CreatedBy = 0;
    @SerializedName("ModifiedBy")
    @Expose
    public Object ModifiedBy;

    // to store last position

    public PlayedVideoData playedVideoDataArrayList = new PlayedVideoData();

    public static class PlayedVideoData {
        public int videoCourseId = 0;
        public boolean isVideoPlayed = false;
        public int videoId = 0;
        public boolean hasWatchedComplete = false; // to check if viewed completely
        public long videoPlayedPosition = 0;
    }
}


