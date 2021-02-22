package com.example.gayaak_10.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.model.response.CoursesDetail;
import com.example.gayaak_10.model.response.UserDataProfile;
import com.example.gayaak_10.model.response.VideoData;
import com.example.gayaak_10.student.model.BuyCoursesDetail;
import com.example.gayaak_10.student.model.ModuleWithVideoDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPrefsUtil {

    private static String PREFERENCES = "Gayaak";

    public static void setUserPreferences(Context context, UserDataProfile userDataContract) {
        SharedPreferences setting = context.getSharedPreferences(PREFERENCES, 0);
        SharedPreferences.Editor editor = setting.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userDataContract);
        editor.putString(Constant.USER_DATA, json);
        editor.apply();
    }

    public static UserDataProfile getUserPreferences(Context context, String key) {
        SharedPreferences setting = (SharedPreferences) context
                .getSharedPreferences(PREFERENCES, 0);
        Gson gson = new Gson();
        String json = setting.getString(key, "");
        return gson.fromJson(json, UserDataProfile.class);
    }

    public static ArrayList<BuyCoursesDetail> getCartCourse(Context context, String key) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(PREFERENCES, 0);
        Gson gson = new Gson();
        String json = sharedPrefs.getString(key, "");
        Type type = new TypeToken<ArrayList<BuyCoursesDetail>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static void setCartCourse(Context context, List<BuyCoursesDetail> courseDetail) {
        SharedPreferences setting = (SharedPreferences) context
                .getSharedPreferences(PREFERENCES, 0);
        SharedPreferences.Editor editor = setting.edit();
        Gson gson = new Gson();
        String json = gson.toJson(courseDetail);
        editor.putString(Constant.CART_DATA, json);
        editor.apply();
    }

    public static ArrayList<ModuleWithVideoDetail> getStudentCartCourse(Context context, String key) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(PREFERENCES, 0);
        Gson gson = new Gson();
        String json = sharedPrefs.getString(key, "");
        Type type = new TypeToken<ArrayList<CoursesDetail>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static void setStudentCartCourse(Context context, List<ModuleWithVideoDetail> courseDetail) {
        SharedPreferences setting = (SharedPreferences) context
                .getSharedPreferences(PREFERENCES, 0);
        SharedPreferences.Editor editor = setting.edit();
        Gson gson = new Gson();
        String json = gson.toJson(courseDetail);
        editor.putString(Constant.CART_DATA, json);
        editor.apply();
    }

    public static void setVideoDataPreferences(Context context, ArrayList<VideoData.PlayedVideoData> videoData) {
        SharedPreferences setting = context.getSharedPreferences(PREFERENCES, 0);
        SharedPreferences.Editor editor = setting.edit();
        Gson gson = new Gson();
        String json = gson.toJson(videoData);
        editor.putString(Constant.VIDEO_DATA, json);
        editor.apply();
    }

    public static ArrayList<VideoData.PlayedVideoData> getVideoDataPreferences(Context context, String key) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(PREFERENCES, 0);
        Gson gson = new Gson();
        String json = sharedPrefs.getString(key, "");
        Type type = new TypeToken<ArrayList<VideoData.PlayedVideoData>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static void setBooleanPreferences(Context context, String key,
                                             boolean isCheck) {
        SharedPreferences setting = context
                .getSharedPreferences(PREFERENCES, 0);
        SharedPreferences.Editor editor = setting.edit();
        editor.putBoolean(key, isCheck);
        editor.apply();
    }

    public static boolean getBooleanPreferences(Context context, String key) {
        SharedPreferences setting = context
                .getSharedPreferences(PREFERENCES, 0);
        return setting.getBoolean(key, false);
    }

    public static void setStringPreferences(Context context, String key,
                                            String value) {
        SharedPreferences setting = context
                .getSharedPreferences(PREFERENCES, 0);
        SharedPreferences.Editor editor = setting.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getStringPreferences(Context context, String key) {
        SharedPreferences setting = context.getSharedPreferences(PREFERENCES, 0);
        return setting.getString(key, "");

    }

    public static void setIntegerPreferences(Context context, String key, int value) {
        SharedPreferences setting = context.getSharedPreferences(PREFERENCES, 0);
        SharedPreferences.Editor editor = setting.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getIntegerPreferences(Context context, String key) {
        SharedPreferences setting = context
                .getSharedPreferences(PREFERENCES, 0);
        return setting.getInt(key, 0);

    }

    public static void clearAllSharedPreferences(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }

    public static void clearSharedPreferencesByKey(Context context, String key) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.remove(key);
        editor.apply();
    }

    private static String PREF_HIGH_QUALITY = "pref_high_quality";

    public static void setPrefHighQuality(Context context, boolean isEnabled) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREF_HIGH_QUALITY, isEnabled);
        editor.apply();
    }

    public static boolean getPrefHighQuality(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(PREF_HIGH_QUALITY, false);
    }
}
