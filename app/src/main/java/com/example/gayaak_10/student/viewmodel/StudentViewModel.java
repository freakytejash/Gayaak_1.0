package com.example.gayaak_10.student.viewmodel;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gayaak_10.common.model.TutorCalendar;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.model.response.DefaultResponse;
import com.example.gayaak_10.model.response.FilteredCourse;
import com.example.gayaak_10.model.response.FilteredCourseDetail;
import com.example.gayaak_10.model.response.UserDataProfile;
import com.example.gayaak_10.model.tutor.CourseModule;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.model.AudioModuleDataContractList;
import com.example.gayaak_10.student.model.CoinCurrencyConfig;
import com.example.gayaak_10.student.model.CourseByStudentId;
import com.example.gayaak_10.student.model.CourseDataContractList;
import com.example.gayaak_10.student.model.CoursePlan;
import com.example.gayaak_10.student.model.DemoUserDashboard;
import com.example.gayaak_10.student.model.LevelCategoryInfo;
import com.example.gayaak_10.student.model.ModuleWithVideo;
import com.example.gayaak_10.student.model.RegularCourseProgress;
import com.example.gayaak_10.student.model.TransactionType;
import com.example.gayaak_10.student.model.TutorByCourseLevel;
import com.example.gayaak_10.student.model.WalletRechargePlanDataContractList;
import com.example.gayaak_10.student.model.request.DemoTutorRequest;
import com.example.gayaak_10.student.model.request.FeedbackContentRequest;
import com.example.gayaak_10.student.model.request.StudentFutureBookingRequest;
import com.example.gayaak_10.student.model.request.StudentSupportRequest;
import com.example.gayaak_10.student.model.request.WalletUpdateRequest;
import com.example.gayaak_10.tutor.model.ScheduleSlotTimeTemp;
import com.example.gayaak_10.tutor.model.TutorSchedule;
import com.example.gayaak_10.tutor.model.TutorScheduleDataContractList;
import com.example.gayaak_10.utility.DateTimeUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentViewModel extends ViewModel {

    private MutableLiveData<TutorByCourseLevel> tutorByCourseLevelMutableLiveData;
    private MutableLiveData<LevelCategoryInfo> levelCategoryInfoMutableLiveData;
    private MutableLiveData<CourseByStudentId> courseByStudentIdMutableLiveData;
    private MutableLiveData<DefaultResponse> defaultResponseMutableLiveData;
    private MutableLiveData<DemoUserDashboard> demoUserDashboardMutableLiveData;
    private MutableLiveData<TutorCalendar> tutorCalendarMutableLiveData;
    private MutableLiveData<CourseModule> courseModuleMutableLiveData;
    private MutableLiveData<ModuleWithVideo> moduleWithVideoMutableLiveData;

    private MutableLiveData<RegularCourseProgress> regularCourseProgressMutableLiveData;
    private MutableLiveData<TutorSchedule> tutorScheduleMutableLiveData;

    private MutableLiveData<ArrayList<AudioModuleDataContractList>> moduleWithAudioMutableLiveData;
    private MutableLiveData<DefaultResponse> managereSchedule;
    private MutableLiveData<DefaultResponse> cancelSessionMutableLiveData;
    private MutableLiveData<FilteredCourseDetail> coursesDetailMutableLiveData;
    private MutableLiveData<CoursePlan> coursePlanMutableLiveData;
    private MutableLiveData<CoursePlan> regularCourseMutableLiveData;
    private MutableLiveData<UserDataProfile> userDataProfileMutableLiveData;
    private MutableLiveData<TransactionType> transactionTypeData;


    //188, 189, 10, 2020
    public MutableLiveData<TutorCalendar> getStudentCalendar(int studentId, int month, int year) {
/*        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;*/
        if (tutorCalendarMutableLiveData == null) {
            tutorCalendarMutableLiveData = new MutableLiveData<>();
        }
        getTutorCalendarSchedule(studentId, month, year);
        return tutorCalendarMutableLiveData;
    }

    private void getTutorCalendarSchedule(int studentId, int month, int year) {
        Call<TutorCalendar> tutorCalendarCall = Constant.retrofitService.getStudentCalendarById(studentId, month, year);
        tutorCalendarCall.enqueue(new Callback<TutorCalendar>() {
            @Override
            public void onResponse(@NonNull Call<TutorCalendar> call, @NonNull Response<TutorCalendar> response) {
                tutorCalendarMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TutorCalendar> call, @NonNull Throwable t) {

            }
        });
    }

    public LiveData<TutorByCourseLevel> getTutorSchedule(int selectedLevel, int selectedType) {
        tutorByCourseLevelMutableLiveData = new MutableLiveData<>();
        loadDemoTutors(selectedLevel, selectedType);
        return tutorByCourseLevelMutableLiveData;
    }

    public LiveData<LevelCategoryInfo> getAllCourseLevels() {
        levelCategoryInfoMutableLiveData = new MutableLiveData<>();
        getCoursesLevels();
        return levelCategoryInfoMutableLiveData;
    }

    public LiveData<CourseByStudentId> getAllCourseLevelsById() {
        courseByStudentIdMutableLiveData = new MutableLiveData<>();
        getRecommendedCourse();
        return courseByStudentIdMutableLiveData;
    }

    public MutableLiveData<DefaultResponse> postBookingDemo(int userId, DemoTutorRequest demoTutorRequest) {
        defaultResponseMutableLiveData = new MutableLiveData<>();
        postBookingDemoTutor(userId, demoTutorRequest);
        return defaultResponseMutableLiveData;
    }

    public MutableLiveData<DemoUserDashboard> getDemoUserDashboard(int userId) {
        demoUserDashboardMutableLiveData = new MutableLiveData<>();
        getDemoDashboard(userId);
        return demoUserDashboardMutableLiveData;
    }

    public MutableLiveData<CourseModule> getCourseModule(Integer courseId) {
        courseModuleMutableLiveData = new MutableLiveData<>();
        getModule(courseId);
        return courseModuleMutableLiveData;
    }

    @SuppressLint("SimpleDateFormat")
    private void getDemoDashboard(int userId) {
        Call<DemoUserDashboard> demoUserDashboardCall = Constant.retrofitService.getDemoUserDashboard(userId);
        demoUserDashboardCall.enqueue(new Callback<DemoUserDashboard>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NonNull Call<DemoUserDashboard> call, @NonNull Response<DemoUserDashboard> response) {
                Log.e("apiCall", "getUpcomingClasses: " +call.request());
                assert response.body() != null;
                /*if (response.body().detail.courseDataContractList != null){

                    for (int i=0; i<response.body().detail.courseDataContractList.size(); i++){
                        App.userLearningCourseList.add(i,response.body().detail.courseDataContractList.get(i));
                    }

                }*/
                if (response.body().detail.liveClassDataContractList != null) {
                    for (int i = 0; i < response.body().detail.liveClassDataContractList.size(); i++) {

                        String convertedDate = DateTimeUtility.convertDateTimeFormate(response.body().detail.liveClassDataContractList.get(i).date,
                                "yyyy-MM-dd'T'hh:mm:ss", "MMMM, dd");
                        String convertedTime="";
                        if (response.body().detail.liveClassDataContractList.get(i).time.equalsIgnoreCase("12:00:00")){
                           convertedTime="12:00 PM";
                        }
                        else {
                            convertedTime = DateTimeUtility.convertDateTimeFormate(response.body().detail.liveClassDataContractList.get(i).date,
                                    "yyyy-MM-dd'T'hh:mm:ss", "h:mm a");
                        }
                        String convertedDay = DateTimeUtility.convertDateTimeFormate(response.body().detail.liveClassDataContractList.get(i).date,
                                "yyyy-MM-dd'T'hh:mm:ss", "EEEE");
                        response.body().detail.liveClassDataContractList.get(i).sessionDate = convertedDate;
                        response.body().detail.liveClassDataContractList.get(i).time = convertedTime;
                        response.body().detail.liveClassDataContractList.get(i).day = convertedDay;

                        try {
                            final String dateString = response.body().detail.liveClassDataContractList.get(i).time;
                            final long millisToAdd = (Integer.parseInt(Constant.EndStartClass) * 60000);
                            final long millisToSub = (Integer.parseInt(Constant.ShowStartClass) * 60000);

                             SimpleDateFormat format = new SimpleDateFormat("h:mm a");

                            Date d = format.parse(dateString);
                            Objects.requireNonNull(d).setTime(d.getTime() + millisToAdd);
                            if (response.body().detail.liveClassDataContractList.get(i).time.equalsIgnoreCase("00:00:00")){
                                response.body().detail.liveClassDataContractList.get(i).endTime = "11:50 PM";
                                //d.setTime(Objects.requireNonNull(format.parse(dateString)).getTime() - millisToSub);
                                response.body().detail.liveClassDataContractList.get(i).startTime ="00:10 AM";
                            }else {
                                response.body().detail.liveClassDataContractList.get(i).endTime = format.format(d);
                                d.setTime(Objects.requireNonNull(format.parse(dateString)).getTime() - millisToSub);
                                response.body().detail.liveClassDataContractList.get(i).startTime = format.format(d);
                            }


                        } catch (ParseException e) {
                            e.printStackTrace();

                        }
                    }

                    Collections.sort(response.body().detail.liveClassDataContractList, (liveClassDataContractList, t1) -> {
                        String session = liveClassDataContractList.sessionDate + " " + DateTimeUtility.convertDateStringFormat("HH:mm a", DateTimeUtility.convertDateFormat("HH:mm:ss",
                                liveClassDataContractList.time));
                        String session1 = t1.sessionDate + " " + DateTimeUtility.convertDateStringFormat("HH:mm a", DateTimeUtility.convertDateFormat("HH:mm:ss", t1.time));
                        return DateTimeUtility.convertDateFormat("MMMM, dd HH:mm a", session).compareTo(DateTimeUtility.convertDateFormat("MMMM, dd HH:mm a", session1));
                    });
                }

                demoUserDashboardMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<DemoUserDashboard> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void postBookingDemoTutor(int userId, DemoTutorRequest demoTutorRequest) {
        Call<DefaultResponse> call = Constant.retrofitService.bookDemoTutor(userId, demoTutorRequest);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(@NonNull Call<DefaultResponse> call, @NonNull Response<DefaultResponse> response) {
                defaultResponseMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<DefaultResponse> call, @NonNull Throwable t) {
                Log.e("test", "onFailure: " + t.getMessage());
                defaultResponseMutableLiveData.setValue(null);
            }
        });
    }

    private void loadDemoTutors(int selectedLevel, int selectedType) {
        Call<TutorByCourseLevel> tutorByCourseLevelCall = Constant.retrofitServiceHeader.getDemoTutors(selectedType, selectedLevel);
        Log.e("apiCall", "loadDemoTutors: " + tutorByCourseLevelCall.request());
        tutorByCourseLevelCall.enqueue(new Callback<TutorByCourseLevel>() {
            @Override
            public void onResponse(@NonNull Call<TutorByCourseLevel> call, @NonNull Response<TutorByCourseLevel> response) {
                tutorByCourseLevelMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TutorByCourseLevel> call, @NonNull Throwable t) {

            }
        });
    }

    private void getCoursesLevels() {
        Call<LevelCategoryInfo> tutorByCourseLevelCall = Constant.retrofitServiceHeader.getLevelCategory();
        tutorByCourseLevelCall.enqueue(new Callback<LevelCategoryInfo>() {
            @Override
            public void onResponse(@NonNull Call<LevelCategoryInfo> call, @NonNull Response<LevelCategoryInfo> response) {
                levelCategoryInfoMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<LevelCategoryInfo> call, @NonNull Throwable t) {

            }
        });
    }

    private void getRecommendedCourse() {
        Call<CourseByStudentId> tutorByCourseLevelCall = Constant.retrofitServiceHeader.getRecommendedCourse(App.userDataContract.detail.userId);
        tutorByCourseLevelCall.enqueue(new Callback<CourseByStudentId>() {
            @Override
            public void onResponse(@NonNull Call<CourseByStudentId> call, @NonNull Response<CourseByStudentId> response) {
                courseByStudentIdMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<CourseByStudentId> call, @NonNull Throwable t) {

            }
        });
    }

    private void getModule(Integer courseId) {
        Call<CourseModule> courseModuleCall = Constant.retrofitService.getCourseModules(courseId);
        Log.e("apiCall", "getCourseId: " + courseModuleCall.request());
        courseModuleCall.enqueue(new Callback<CourseModule>() {
            @Override
            public void onResponse(@NonNull Call<CourseModule> call, @NonNull Response<CourseModule> response) {
                assert response.body() != null;
                if (response.body().status) {
                    courseModuleMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CourseModule> call, @NonNull Throwable t) {

            }
        });
    }

    public LiveData<ModuleWithVideo> getModuleVideo(int moduleId) {
        moduleWithVideoMutableLiveData = new MutableLiveData<>();
        getModuleVideoById(moduleId);
        return moduleWithVideoMutableLiveData;
    }

    private void getModuleVideoById(int moduleId) {
        Call<ModuleWithVideo> moduleWithVideoCall = Constant.retrofitService.getModuledWithVideo(moduleId, App.userDataContract.detail.userId);
        moduleWithVideoCall.enqueue(new Callback<ModuleWithVideo>() {
            @Override
            public void onResponse(@NonNull Call<ModuleWithVideo> call, @NonNull Response<ModuleWithVideo> response) {
                if (response.body() != null && response.body().status && response.body().detail != null) {
                    moduleWithVideoMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModuleWithVideo> call, @NonNull Throwable t) {

            }
        });
    }
    /*--------------------------------------------------------------------------------------------*/
    public LiveData<ModuleWithVideo> getModulePdf(int moduleId, int userId) {
        moduleWithVideoMutableLiveData = new MutableLiveData<>();
        getModulePDFById(moduleId, userId);
        return moduleWithVideoMutableLiveData;
    }

    private void getModulePDFById(int moduleId, int userId) {
        Call<ModuleWithVideo> moduleWithVideoCall = Constant.retrofitService.getModuledWithPdf(moduleId, userId);
        moduleWithVideoCall.enqueue(new Callback<ModuleWithVideo>() {
            @Override
            public void onResponse(@NonNull Call<ModuleWithVideo> call, @NonNull Response<ModuleWithVideo> response) {
                assert response.body() != null;
                if (response.body().status && response.body().detail != null) {
                    moduleWithVideoMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModuleWithVideo> call, @NonNull Throwable t) {

            }
        });
    }


    /*--------------------------------------------------------------------------------------------*/
    public LiveData<ArrayList<AudioModuleDataContractList>> getModuleAudio(int moduleId, int userId) {
        moduleWithAudioMutableLiveData = new MutableLiveData<>();
        getModuleAudioById(moduleId, userId);
        return moduleWithAudioMutableLiveData;
    }

    private void getModuleAudioById(int moduleId, int userId) {
        Call<ModuleWithVideo> moduleWithVideoCall = Constant.retrofitService.getModuledWithAudio(moduleId, userId);
        Log.e("apiCall", "getModuleAudioById: " + moduleWithVideoCall.request());
        moduleWithVideoCall.enqueue(new Callback<ModuleWithVideo>() {
            @Override
            public void onResponse(@NonNull Call<ModuleWithVideo> call, @NonNull Response<ModuleWithVideo> response) {
                assert response.body() != null;
                if (response.body().status && response.body().detail != null) {
                    moduleWithAudioMutableLiveData.setValue(response.body().detail.mp3ModuleDataContractList);
                } else {
                    moduleWithAudioMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModuleWithVideo> call, @NonNull Throwable t) {
                moduleWithAudioMutableLiveData.setValue(null);
            }
        });
    }

    /*--------------------------------------------------------------------------------------------*/


    public MutableLiveData<DefaultResponse> manageRescheduleStudent(int tutorId, int tutorScheduledId) {
        managereSchedule = new MutableLiveData<>();
        postTutorReschedule(tutorId, tutorScheduledId);
        return managereSchedule;
    }

    private void postTutorReschedule(int tutorId, int tutorScheduledId) {
        Call<DefaultResponse> call = Constant.retrofitServiceHeader.rescheduleSessionStudent(tutorScheduledId, tutorId);
        Log.e("apiCall", "postTutorReschedule: " + call.request());
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(@NonNull Call<DefaultResponse> call, @NonNull Response<DefaultResponse> response) {
                managereSchedule.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<DefaultResponse> call, @NonNull Throwable t) {
                managereSchedule.setValue(null);
            }
        });
    }

    /*--------------------------------------------------------------------------------------------*/

    public MutableLiveData<DefaultResponse> cancelSessionStudent(int tutorScheduleId, int userId) {
        cancelSessionMutableLiveData = new MutableLiveData<>();
        postTutorCancel(tutorScheduleId, userId);
        return cancelSessionMutableLiveData;
    }

    private void postTutorCancel(int tutorScheduleId, int userId) {
        Call<DefaultResponse> call = Constant.retrofitService.CancelScheduleByStudent(tutorScheduleId, userId);
        Log.e("apiCall", "postTutorCancel: " + call.request());
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(@NonNull Call<DefaultResponse> call, @NonNull Response<DefaultResponse> response) {
                cancelSessionMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<DefaultResponse> call, @NonNull Throwable t) {
                cancelSessionMutableLiveData.setValue(null);
            }
        });
    }

    public MutableLiveData<DefaultResponse> postSessionFeedback(int userId, FeedbackContentRequest feedbackContentRequest) {
        defaultResponseMutableLiveData = new MutableLiveData<>();
        postFeedBack(userId, feedbackContentRequest);
        return defaultResponseMutableLiveData;
    }

    private void postFeedBack(int userId, FeedbackContentRequest feedbackContentRequest) {
        Call<DefaultResponse> call = Constant.retrofitService.postSessionFeedback(userId, feedbackContentRequest);
        Log.e("apiCall", "postFeedBack: " + call.request());
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(@NonNull Call<DefaultResponse> call, @NonNull Response<DefaultResponse> response) {
                defaultResponseMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<DefaultResponse> call, @NonNull Throwable t) {
                defaultResponseMutableLiveData.setValue(null);
            }
        });
    }

    /*------------------------------------Student Support-----------------------------------------*/

    public MutableLiveData<DefaultResponse> postStudentSupport(int userId, StudentSupportRequest studentSupportRequest) {
        defaultResponseMutableLiveData = new MutableLiveData<>();
        postSupport(userId, studentSupportRequest);
        return defaultResponseMutableLiveData;
    }

    private void postSupport(int userId, StudentSupportRequest studentSupportRequest) {
        Call<DefaultResponse> call = Constant.retrofitService.postStudentSupport(userId, studentSupportRequest);
        Log.e("apiCall", "postStudentSupport: " + call.request());
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(@NonNull Call<DefaultResponse> call, @NonNull Response<DefaultResponse> response) {
                defaultResponseMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<DefaultResponse> call, @NonNull Throwable t) {
                defaultResponseMutableLiveData.setValue(null);
            }
        });
    }


    /*--------------------------------------------------------------------------------------------*/
    public MutableLiveData<FilteredCourseDetail> getStudentAllCourses() {
        coursesDetailMutableLiveData = new MutableLiveData<>();
        getAllCourses();
        return coursesDetailMutableLiveData;
    }

    private void getAllCourses() {
        Call<FilteredCourse> filterCall = Constant.retrofitServiceHeader.getFilteredData("","", "", "", "", "1", "10");
        Log.d("call", " filter => " + filterCall.request());
        filterCall.enqueue(new Callback<FilteredCourse>() {
            @Override
            public void onResponse(@NonNull Call<FilteredCourse> call, @NonNull Response<FilteredCourse> response) {
                assert response.body() != null;
                if (response.body().detail != null) {
                    coursesDetailMutableLiveData.setValue(response.body().detail);
                }
            }

            @Override
            public void onFailure(@NonNull Call<FilteredCourse> call, @NonNull Throwable t) {
                coursesDetailMutableLiveData.setValue(null);
            }
        });
    }


    /*--------------------------------------------------------------------------------------------*/
    int i=0;
    public MutableLiveData<CoursePlan> getRegularCourse() {
        regularCourseMutableLiveData = new MutableLiveData<>();
        getRegularCourseCall();
        return regularCourseMutableLiveData;
    }

    private void getRegularCourseCall() {
        Call<CoursePlan> filterCall = Constant.retrofitServiceHeader.getCoursePlans();
        Log.d("call", " filter => " + filterCall.request());
        filterCall.enqueue(new Callback<CoursePlan>() {
            @Override
            public void onResponse(@NonNull Call<CoursePlan> call, @NonNull Response<CoursePlan> response) {
                assert response.body() != null;
                if (response.body().detail != null) {
                    regularCourseMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CoursePlan> call, @NonNull Throwable t) {
                regularCourseMutableLiveData.setValue(null);
            }
        });
    }



    /*-------------------------------------coursePlan Dashboard-----------------------------------*/
    public MutableLiveData<CoursePlan> getCoursePlans() {
        coursePlanMutableLiveData = new MutableLiveData<>();
        getAllPlans();
        return coursePlanMutableLiveData;
    }

    private void getAllPlans() {
        Call<CoursePlan> filterCall = Constant.retrofitServiceHeader.getCoursePlans();
        Log.d("call", " filter => " + filterCall.request());
        filterCall.enqueue(new Callback<CoursePlan>() {
            @Override
            public void onResponse(@NonNull Call<CoursePlan> call, @NonNull Response<CoursePlan> response) {
                assert response.body() != null;
                if (response.body().detail != null) {
                    getCoinCurrencyConfig(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CoursePlan> call, @NonNull Throwable t) {
                coursePlanMutableLiveData.setValue(null);
            }
        });
    }

    public void getCoinCurrencyConfig(CoursePlan body) {
        Call<CoinCurrencyConfig> filterCall = Constant.retrofitServiceHeader.getCoursePlanConfiguration();
        Log.d("call", " filter => " + filterCall.request());
        filterCall.enqueue(new Callback<CoinCurrencyConfig>() {
            @Override
            public void onResponse(@NonNull Call<CoinCurrencyConfig> call, @NonNull Response<CoinCurrencyConfig> response) {
                assert response.body() != null;
                if (response.body().detail != null) {
                    for (int i = 0; i < response.body().detail.size(); i++) {
                        for (int j = 0; j < body.detail.walletRechargePlanDataContractList.size(); j++)
                        {
                            if(response.body().detail.get(i).currencyId == 1) {                         // india
                                body.detail.walletRechargePlanDataContractList.get(j).indiaCurrencyValue = response.body().detail.get(i).currencyValue;
                            }else if(response.body().detail.get(i).currencyId == 2){                    // USA
                                body.detail.walletRechargePlanDataContractList.get(j).usCurrencyValue = response.body().detail.get(i).currencyValue;
                            }
                        }

/*                        if ( body.detail.courseDataContractList!=null||!body.detail.courseDataContractList.isEmpty()){
                            for (int j = 0; j < body.detail.courseDataContractList.size(); j++) {
                                if(response.body().detail.get(i).currencyId == 1) {                         // india
                                    body.detail.courseDataContractList.get(j).indiaCurrencyValue = response.body().detail.get(i).currencyValue;
                                }else if(response.body().detail.get(i).currencyId == 2){                    // USA
                                    body.detail.courseDataContractList.get(j).usCurrencyValue = response.body().detail.get(i).currencyValue;
                                }
                            }
                        }*/

                    }
                    coursePlanMutableLiveData.setValue(body);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CoinCurrencyConfig> call, @NonNull Throwable t) {
                coursePlanMutableLiveData.setValue(null);
            }
        });
    }

    public void getCoinCurrencyConfig() {
        Call<CoinCurrencyConfig> filterCall = Constant.retrofitServiceHeader.getCoursePlanConfiguration();
        Log.d("call", " filter => " + filterCall.request());
        filterCall.enqueue(new Callback<CoinCurrencyConfig>() {
            @Override
            public void onResponse(@NonNull Call<CoinCurrencyConfig> call, @NonNull Response<CoinCurrencyConfig> response) {
                assert response.body() != null;
                if (response.body().detail != null) {

                    if (App.userDataContract.detail.currencyName==null){
                        App.userDataContract.detail.currencyName="INR";
                    }

                    for (int i = 0; i < response.body().detail.size(); i++) {

                        if (App.userDataContract.detail.currencyName.equals(response.body().detail.get(i).currencyName))
                        {

                            App.countryCurrencyValue = response.body().detail.get(i).currencyValue;
                        }
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<CoinCurrencyConfig> call, @NonNull Throwable t) {
                coursePlanMutableLiveData.setValue(null);
            }
        });



    }


    /*--------------------------------------------------------------------------------------------*/
    public MutableLiveData<TransactionType>  getTransactionType(){
        transactionTypeData = new  MutableLiveData<>();
        Call<TransactionType> call = Constant.retrofitServiceHeader.getTransactionType();
        Log.d("call", " filter => " + call.request());
        call.enqueue(new Callback<TransactionType>() {
            @Override
            public void onResponse(@NonNull Call<TransactionType> call, @NonNull Response<TransactionType> response) {
                assert response.body() != null;
                if (response.body().detail != null) {
                    transactionTypeData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<TransactionType> call, @NonNull Throwable t) {
                transactionTypeData.setValue(null);
            }
        });
        return transactionTypeData;
    }


    public MutableLiveData<DefaultResponse> updateWallet(WalletRechargePlanDataContractList selectedPlanInfo, String paymentId, Integer transactionId) {
        WalletUpdateRequest walletUpdateRequest = new WalletUpdateRequest();
        walletUpdateRequest.userWalletTransactionId = 0;
        walletUpdateRequest.userId = App.userDataContract.detail.userId;
        walletUpdateRequest.coins = (selectedPlanInfo.noOfCoin + selectedPlanInfo.extraCoin);
        walletUpdateRequest.name = selectedPlanInfo.name;
        walletUpdateRequest.description = null;
        walletUpdateRequest.courseId = null;
        walletUpdateRequest.transactionId = paymentId;
        walletUpdateRequest.transactionTypeId = transactionId;
        walletUpdateRequest.isActive = selectedPlanInfo.isActive;
        walletUpdateRequest.createdBy = App.userDataContract.detail.userId;
        walletUpdateRequest.createdDate = selectedPlanInfo.createdDate;
        walletUpdateRequest.modifiedBy = null;
        walletUpdateRequest.modifiedDate = null;
        walletUpdateRequest.walletRechargePlanId = selectedPlanInfo.walletRechargePlanId;
        walletUpdateRequest.CreditDebit = 1;

        defaultResponseMutableLiveData = new MutableLiveData<>();
        postUpdateWallet(walletUpdateRequest);
        return defaultResponseMutableLiveData;
    }

    public MutableLiveData<DefaultResponse> updateWalletDirect(WalletUpdateRequest walletUpdateRequest) {
        defaultResponseMutableLiveData = new MutableLiveData<>();
        postUpdateWallet(walletUpdateRequest);
        return defaultResponseMutableLiveData;
    }

    private void postUpdateWallet(WalletUpdateRequest walletUpdateRequest) {
        Call<DefaultResponse> call = Constant.retrofitServiceHeader.manageWallet(App.userDataContract.detail.userId, walletUpdateRequest);
        Log.d("call", " filter => " + call.request());
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(@NonNull Call<DefaultResponse> call, @NonNull Response<DefaultResponse> response) {
                assert response.body() != null;
                if (response.body().detail != null) {
                    defaultResponseMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<DefaultResponse> call, @NonNull Throwable t) {
                defaultResponseMutableLiveData.setValue(null);
            }
        });
    }

    /*--------------------------------------------------------------------------------------------*/
    public LiveData<UserDataProfile> getUserProfile(int userId) {
        userDataProfileMutableLiveData = new MutableLiveData<>();
        getUserById(userId);
        return userDataProfileMutableLiveData;
    }

    private void getUserById(int userId) {
        Call<UserDataProfile> call = Constant.retrofitService.getUserProfile(String.valueOf(userId));
        call.enqueue(new Callback<UserDataProfile>() {
            @Override
            public void onResponse(@NonNull Call<UserDataProfile> call, @NonNull Response<UserDataProfile> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (response.body().status && response.body().detail != null) {
                        userDataProfileMutableLiveData.setValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserDataProfile> call, @NonNull Throwable t) {

            }
        });
    }


    /*-----------------------send email notification---------------------------------------------*/

    public MutableLiveData<DefaultResponse> postEmailNotification(int userId, int liveClassDetailId) {
        defaultResponseMutableLiveData = new MutableLiveData<>();
        postEmailNotificationCall(userId, liveClassDetailId);
        return defaultResponseMutableLiveData;
    }

    private void postEmailNotificationCall(int userId, int liveClassDetailId) {
        Call<DefaultResponse> defaultResponseCall = Constant.retrofitService.postGoToClassNotification(userId, liveClassDetailId);
        defaultResponseCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                defaultResponseMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }
        });
    }

    /*-------------------------------------course progress api------------------------------------*/

    public MutableLiveData<RegularCourseProgress> getRegularCourseProgress(int userId, int courseId) {
        regularCourseProgressMutableLiveData = new MutableLiveData<>();
        getRegularCourseProgressCall(userId, courseId);
        return regularCourseProgressMutableLiveData;
    }

    private void getRegularCourseProgressCall(int userId, int courseId) {
        Call<RegularCourseProgress> defaultResponseCall = Constant.retrofitService.getRegularCourseProgress(userId, courseId);
        defaultResponseCall.enqueue(new Callback<RegularCourseProgress>() {
            @Override
            public void onResponse(Call<RegularCourseProgress> call, Response<RegularCourseProgress> response) {
                regularCourseProgressMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<RegularCourseProgress> call, Throwable t) {

            }
        });
    }

    /*-----------------------------------getTutor availability for reschedule------------------------*/

    public MutableLiveData<TutorSchedule> getTutorAvailabilityForReschedule(int tutorId) {
        tutorScheduleMutableLiveData = new MutableLiveData<>();
        getTutorAvailability(tutorId);
        return tutorScheduleMutableLiveData;
    }

    private void getTutorAvailability(int tutorId) {
        Call<TutorSchedule> call = Constant.retrofitService.getTutorScheduleAvailability(tutorId);
        call.enqueue(new Callback<TutorSchedule>() {
            @Override
            public void onResponse(Call<TutorSchedule> call, Response<TutorSchedule> response) {
                tutorScheduleMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<TutorSchedule> call, Throwable t) {

            }
        });
    }

    /*------------------------------------post StudentFutureBooking-----------------------------------*/
    public MutableLiveData<DefaultResponse> postStudentFutureBooking(int userId, StudentFutureBookingRequest studentFutureBookingRequest) {
        defaultResponseMutableLiveData = new MutableLiveData<>();
        postStudentFutureBookingCall(userId, studentFutureBookingRequest);
        return defaultResponseMutableLiveData;
    }

    private void postStudentFutureBookingCall(int userId, StudentFutureBookingRequest studentFutureBookingRequest) {
        Call<DefaultResponse> defaultResponseCall = Constant.retrofitService.postStudentFutureBooking(userId, studentFutureBookingRequest);
        defaultResponseCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                defaultResponseMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }
        });
    }
}
