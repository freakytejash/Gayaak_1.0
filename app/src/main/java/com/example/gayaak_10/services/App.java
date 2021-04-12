package com.example.gayaak_10.services;

import android.app.Application;
import android.content.Context;

import com.example.gayaak_10.model.response.CoursesDetail;
import com.example.gayaak_10.model.response.UserDataProfile;
import com.example.gayaak_10.student.model.BuyCoursesDetail;
import com.example.gayaak_10.student.model.CourseDataContractList;
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
    public static ArrayList<CoursesDetail> allCourseRecommendedList = new ArrayList<CoursesDetail>();                // all the courses
    public static String currencyType = "USD";
    public static ArrayList<BuyCoursesDetail> studentCartList = new ArrayList<BuyCoursesDetail>();
    public static Integer firstSelectedCourseType = -1;
    public static Integer firstSelectedLevelType = -1;
    public static LiveClassDataContractList sessionStarted = new LiveClassDataContractList();
    public static TodaySessions tutorSessionStarted = new TodaySessions();
    public static SelectedSessionDetail selectedSessionDetail = new SelectedSessionDetail();
    public static CourseDataContractList spinnerSelectedCourse = new CourseDataContractList();
    public static int requiredPoints;
    public static String countryCurrencyName ="";
    public static String countryName="";
    public static Integer countryCurrencyValue;
    public static Integer noOfSessions=4;
    public static Integer recommendedCourseCreated=0;
    public static Integer liveClassId;
    public static String StudentName ="";
    public static Integer tutorCoins;
    public static Integer tutorFeedbackId;
    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        userDataContract = new UserDataProfile();

    }
}
