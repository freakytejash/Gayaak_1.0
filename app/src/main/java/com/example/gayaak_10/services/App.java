package com.example.gayaak_10.services;

import android.app.Application;
import android.content.Context;

import com.example.gayaak_10.model.response.CoursesDetail;
import com.example.gayaak_10.model.response.UserDataProfile;
import com.example.gayaak_10.student.model.BuyCoursesDetail;
import com.example.gayaak_10.student.model.LiveClassDataContractList;
import com.example.gayaak_10.student.model.SelectedSessionDetail;
import com.example.gayaak_10.tutor.model.TodaySessions;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    public static Context appContext = new Application();
    public static UserDataProfile userDataContract;
    public static boolean isSocial = false;
    public static List<BuyCoursesDetail> cartList = new ArrayList<BuyCoursesDetail>();
    public static ArrayList<CoursesDetail> coursesDetailArrayList = new ArrayList<>();              // user subscribed courses(Your learning courses HOME)
    public static ArrayList<CoursesDetail> trendingDetailArrayList = new ArrayList<CoursesDetail>();            // trending list
    public static ArrayList<CoursesDetail> allCoursesArrayList = new ArrayList<CoursesDetail>();                // all the courses
    public static String currencyType = "USD";
    public static ArrayList<BuyCoursesDetail> studentCartList = new ArrayList<BuyCoursesDetail>();
    public static Integer firstSelectedCourseType = -1;
    public static Integer firstSelectedLevelType = -1;
    public static LiveClassDataContractList sessionStarted = new LiveClassDataContractList();
    public static TodaySessions tutorSessionStarted = new TodaySessions();
    public static SelectedSessionDetail selectedSessionDetail = new SelectedSessionDetail();

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        userDataContract = new UserDataProfile();

    }
}
