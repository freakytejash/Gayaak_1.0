package com.example.gayaak_10.tutor.viewmodel;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gayaak_10.common.model.TutorCalendar;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.model.response.AllCourses;
import com.example.gayaak_10.model.response.DefaultResponse;
import com.example.gayaak_10.model.tutor.CourseModule;
import com.example.gayaak_10.model.tutor.StudentsTutorId;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.model.AudioModuleDataContractList;
import com.example.gayaak_10.student.model.ModuleWithVideo;
import com.example.gayaak_10.tutor.model.ScheduleSlotDetail;
import com.example.gayaak_10.tutor.model.ScheduleSlotTime;
import com.example.gayaak_10.tutor.model.ScheduleSlotTimeTemp;
import com.example.gayaak_10.tutor.model.TodaySessions;
import com.example.gayaak_10.tutor.model.TutorDashboard;
import com.example.gayaak_10.tutor.model.TutorSchedule;
import com.example.gayaak_10.tutor.model.TutorScheduleDataContractList;
import com.example.gayaak_10.tutor.model.request.FeedbackTutorContentRequest;
import com.example.gayaak_10.tutor.model.request.FeedbackTutorRegularContentRequest;
import com.example.gayaak_10.utility.DateTimeUtility;
import com.example.gayaak_10.utility.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TutorViewModel extends ViewModel {

    private MutableLiveData<ArrayList<ScheduleSlotTimeTemp>> tutorScheduleMutableLiveData;
    private MutableLiveData<DefaultResponse> manageScheduleTime;
    private MutableLiveData<TutorDashboard> dashboardMutableLiveData;
    private MutableLiveData<TutorCalendar> tutorCalendarMutableLiveData;
    private MutableLiveData<StudentsTutorId> studentsTutorIdMutableLiveData;
    private MutableLiveData<CourseModule> courseModuleMutableLiveData;
    private MutableLiveData<ModuleWithVideo> moduleWithVideoMutableLiveData;
    private MutableLiveData<ArrayList<AudioModuleDataContractList>> moduleWithAudioMutableLiveData;
    private MutableLiveData<DefaultResponse> defaultResponseMutableLiveData;
    private MutableLiveData<AllCourses> allCoursesMutableLiveData;


    public LiveData<StudentsTutorId> getStudentsForCalendar() {
        studentsTutorIdMutableLiveData = new MutableLiveData<StudentsTutorId>();
        loadStudentCalendar();
        return studentsTutorIdMutableLiveData;
    }

    private void loadStudentCalendar() {
        Call<StudentsTutorId> tutorDashboardCall = Constant.retrofitService.getStudentsByTutorId(App.userDataContract.detail.userId);
        tutorDashboardCall.enqueue(new Callback<StudentsTutorId>() {
            @Override
            public void onResponse(Call<StudentsTutorId> call, Response<StudentsTutorId> response) {
                studentsTutorIdMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<StudentsTutorId> call, Throwable t) {

            }
        });
    }

    public LiveData<ArrayList<ScheduleSlotTimeTemp>> getScheduleSlotTime() {
        tutorScheduleMutableLiveData = new MutableLiveData<ArrayList<ScheduleSlotTimeTemp>>();
        loadScheduleTime();
        return tutorScheduleMutableLiveData;
    }

    ArrayList<ScheduleSlotDetail> timeArray = new ArrayList<>();
    private void loadScheduleTime() {
        ArrayList<String> dateList = new ArrayList<>();
        dateList.add("Monday");
        dateList.add("Tuesday");
        dateList.add("Wednesday");
        dateList.add("Thursday");
        dateList.add("Friday");
        dateList.add("Saturday");
        dateList.add("Sunday");

        Call<ScheduleSlotTime> scheduleSlotTimeCall = Constant.retrofitService.getScheduleSlots();
        scheduleSlotTimeCall.enqueue(new Callback<ScheduleSlotTime>() {
            @Override
            public void onResponse(Call<ScheduleSlotTime> call, Response<ScheduleSlotTime> response) {
                ScheduleSlotTime scheduleSlotTime = response.body();
                if (scheduleSlotTime != null && scheduleSlotTime.detail != null) {
                    ArrayList<ScheduleSlotTimeTemp> scheduleSlotTimeTempArrayList = new ArrayList<>();
                    for (int i = 0; i < dateList.size(); i++) {
                        for (int j = 0; j < scheduleSlotTime.detail.size(); j++) {
                            if (dateList.get(i).equals(scheduleSlotTime.detail.get(j).day)) {
                                timeArray.add(scheduleSlotTime.detail.get(j));
                                if (checkTimeArray(scheduleSlotTimeTempArrayList, i, dateList, response.body()))
                                    break;
                            } else {
                                if (timeArray.size() == 26 || timeArray.size() == 25) {
                                    scheduleSlotTimeTempArrayList.add(new ScheduleSlotTimeTemp(dateList.get(i), timeArray));
                                    timeArray = new ArrayList<>();
                                    if (scheduleSlotTimeTempArrayList.size() == dateList.size()) {
                                        loadSchedule(scheduleSlotTimeTempArrayList);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    loadSchedule(null);
                }
            }

            @Override
            public void onFailure(Call<ScheduleSlotTime> call, Throwable t) {

            }
        });
    }

    private boolean checkTimeArray(ArrayList<ScheduleSlotTimeTemp> scheduleSlotTimeTempArrayList, int i, ArrayList<String> dateList, ScheduleSlotTime body) {
        if (timeArray.size() == 26 || timeArray.size() == 25) {
            scheduleSlotTimeTempArrayList.add(new ScheduleSlotTimeTemp(dateList.get(i), timeArray));
            timeArray = new ArrayList<>();
            if (scheduleSlotTimeTempArrayList.size() == dateList.size()) {
                loadSchedule(scheduleSlotTimeTempArrayList);
            }
            return true;
        }
        return false;
    }

    private void loadSchedule(ArrayList<ScheduleSlotTimeTemp> slotTimeArrayList) {
        if (slotTimeArrayList == null) {
            tutorScheduleMutableLiveData.setValue(null);
        } else {
            Call<TutorSchedule> tutorScheduleCall = Constant.retrofitService.getTutorScheduleById(App.userDataContract.detail.userId);
            tutorScheduleCall.enqueue(new Callback<TutorSchedule>() {
                @Override
                public void onResponse(Call<TutorSchedule> call, Response<TutorSchedule> response) {
                    TutorSchedule tutorSchedule = response.body();
                    if (tutorSchedule != null && tutorSchedule.detail != null) {
                        if (tutorSchedule.detail.tutorScheduleDataContractList != null && !tutorSchedule.detail.tutorScheduleDataContractList.isEmpty()) {
                            for (int i = 0; i < tutorSchedule.detail.tutorScheduleDataContractList.size(); i++) {
                                TutorScheduleDataContractList schedule = tutorSchedule.detail.tutorScheduleDataContractList.get(i);
                                for (int j = 0; j < slotTimeArrayList.size(); j++) {
                                    for (int k = 0; k < slotTimeArrayList.get(j).detail.size(); k++) {
                                        if (schedule.scheduleId.equals(slotTimeArrayList.get(j).detail.get(k).scheduleId)) {
                                            slotTimeArrayList.get(j).detail.get(k).tutorScheduleId = schedule.tutorScheduleId;
                                            slotTimeArrayList.get(j).detail.get(k).isSelected = true;
                                        }
                                    }
                                }
                            }
                        }
                        if (!slotTimeArrayList.isEmpty()) {
                            getTutorAvailability(slotTimeArrayList);
                        }
                    }

                }

                @Override
                public void onFailure(Call<TutorSchedule> call, Throwable t) {

                }
            });
        }
    }

    private void getTutorAvailability(ArrayList<ScheduleSlotTimeTemp> slotTimeArrayList) {
        Call<TutorSchedule> call = Constant.retrofitService.getTutorScheduleAvailability(App.userDataContract.detail.userId);
        call.enqueue(new Callback<TutorSchedule>() {
            @Override
            public void onResponse(Call<TutorSchedule> call, Response<TutorSchedule> response) {
                if (response.body().detail.tutorScheduleDataContractList!= null && !response.body().detail.tutorScheduleDataContractList.isEmpty()) {
                    for (int i = 0; i < response.body().detail.tutorScheduleDataContractList.size(); i++) {
                        TutorScheduleDataContractList tutorSchedule = response.body().detail.tutorScheduleDataContractList.get(i);
                        for (int j = 0; j < slotTimeArrayList.size(); j++) {
                            for (int k = 0; k < slotTimeArrayList.get(j).detail.size(); k++) {
                                if (slotTimeArrayList.get(j).detail.get(k).scheduleId.equals(tutorSchedule.scheduleId)) {
                                    if (slotTimeArrayList.get(j).detail.get(k).isSelected){
                                        slotTimeArrayList.get(j).detail.get(k).IsScheduleAvailable = tutorSchedule.IsScheduleAvailable;
                                    }
                                }
                            }
                        }
                        tutorScheduleMutableLiveData.setValue(slotTimeArrayList);
                    }
                } else {
                    tutorScheduleMutableLiveData.setValue(slotTimeArrayList);
                }
            }

            @Override
            public void onFailure(Call<TutorSchedule> call, Throwable t) {

            }
        });
    }

    /*--------------------------------------------------------------------------------------------*/


    public MutableLiveData<DefaultResponse> manageTutorSchedule(String selectedSchedules) {
        manageScheduleTime = new MutableLiveData<>();
        postManageScheduleTime(selectedSchedules);
        return manageScheduleTime;
    }

    private void postManageScheduleTime(String selectedSchedules) {
        Call<DefaultResponse> manageSchedule = Constant.retrofitService.manageTutorSchedule(selectedSchedules, App.userDataContract.detail.userId);
        manageSchedule.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                manageScheduleTime.setValue(response.body());
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }
        });
    }
    /*--------------------------------------------------------------------------------------------*/

    public MutableLiveData<TutorDashboard> getDashboard() {
        dashboardMutableLiveData = new MutableLiveData<>();
        loadDashboard();
        return dashboardMutableLiveData;
    }

    private void loadDashboard() {
        Call<TutorDashboard> tutorDashboardCall = Constant.retrofitService.getTutorDashboard(App.userDataContract.detail.userId);
        Log.e("userId",String.valueOf(App.userDataContract.detail.userId));
        Log.e("apiCall", "getUpcomingClasses: " +tutorDashboardCall.request());
        tutorDashboardCall.enqueue(new Callback<TutorDashboard>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<TutorDashboard> call, Response<TutorDashboard> response) {
                if (response.body().detail.todaySessionList != null){
                    for (int i = 0; i < response.body().detail.todaySessionList.size(); i++) {
                        response.body().detail.todaySessionList.get(i).sessionDate = DateTimeUtility.convertDateTimeFormate(response.body().detail.todaySessionList.get(i).Date,
                                "yyyy-MM-dd'T'hh:mm:ss", "MMMM, dd");
                        if (response.body().detail.todaySessionList.get(i).time.equalsIgnoreCase("12:00:00")){
                            response.body().detail.todaySessionList.get(i).time="12:00";

                        }else {
                            response.body().detail.todaySessionList.get(i).time = DateTimeUtility.convertDateTimeFormate(response.body().detail.todaySessionList.get(i).Date,
                                    "yyyy-MM-dd'T'hh:mm:ss", "HH:mm");
                        }



                        try {
                            final String dateString = response.body().detail.todaySessionList.get(i).time;
                            final long millisToAdd = (Integer.parseInt(Constant.EndStartClass) * 60000);
                            final long millisToSub = (Integer.parseInt(Constant.ShowStartClass) * 60000);

                            SimpleDateFormat format = new SimpleDateFormat("HH:mm");

                            Date d = format.parse(dateString);
                            Objects.requireNonNull(d).setTime(d.getTime() + millisToAdd);
                            response.body().detail.todaySessionList.get(i).endTime = format.format(d);
                            d.setTime(Objects.requireNonNull(format.parse(dateString)).getTime() - millisToSub);
                            response.body().detail.todaySessionList.get(i).startTime = format.format(d);

                        } catch (ParseException e) {
                            e.printStackTrace();

                        }
                    }

                    Collections.sort(response.body().detail.todaySessionList, new Comparator<TodaySessions>() {
                        @SuppressLint("SimpleDateFormat")
                        @Override
                        public int compare(TodaySessions liveClassDataContractList, TodaySessions t1) {
                            String session = liveClassDataContractList.sessionDate+ " "+DateTimeUtility.convertDateStringFormat("HH:mm a", DateTimeUtility.convertDateFormat("HH:mm:ss" , liveClassDataContractList.time));
                            String session1 = t1.sessionDate+ " "+DateTimeUtility.convertDateStringFormat("HH:mm a", DateTimeUtility.convertDateFormat("HH:mm:ss" , t1.time));
                            return DateTimeUtility.convertDateFormat("MMMM, dd HH:mm a", session).compareTo(DateTimeUtility.convertDateFormat("MMMM, dd HH:mm a", session1));

                        }
                    });
                }
                dashboardMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<TutorDashboard> call, Throwable t) {

            }
        });
    }
    /*--------------------------------------------------------------------------------------------*/
    public MutableLiveData<CourseModule> getCourseModule(Integer courseId) {
        courseModuleMutableLiveData = new MutableLiveData<CourseModule>();
        getModule(courseId);
        return courseModuleMutableLiveData;
    }

    private void getModule(Integer courseId) {
        Call<CourseModule> courseModuleCall = Constant.retrofitService.getCourseModules(courseId);
        courseModuleCall.enqueue(new Callback<CourseModule>() {
            @Override
            public void onResponse(Call<CourseModule> call, Response<CourseModule> response) {
                if (response.body().status){
                    courseModuleMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<CourseModule> call, Throwable t) {

            }
        });
    }
    /*--------------------------------------------------------------------------------------------*/


    public MutableLiveData<DefaultResponse> postDeleteSchedule(int scheduleId, int tutorId) {
        defaultResponseMutableLiveData = new MutableLiveData<>();
        postDeleteTutorSchedule(scheduleId, tutorId);
        return defaultResponseMutableLiveData;
    }

    private void postDeleteTutorSchedule(int scheduleId, int tutorId) {
        Call<DefaultResponse> defaultResponseCall = Constant.retrofitService.deleteTutorSchedule(scheduleId, tutorId);
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


    /*--------------------------------send Notification-------------------------------------------*/
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

    /*--------------------------------------------------------------------------------------------*/

    //188, 189, 10, 2020
    public MutableLiveData<TutorCalendar> getTutorCalendar(int tutorId, int studentId, int month, int year) {
       /* Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;*/
        tutorCalendarMutableLiveData = new MutableLiveData<>();
        getTutorCalendarSchedule(tutorId, studentId, month, year);
        return tutorCalendarMutableLiveData;
    }

    private void getTutorCalendarSchedule(int tutorId, int studentId, int month, int year) {
        Call<TutorCalendar> tutorCalendarCall = Constant.retrofitService.getTutorCalendarById(tutorId, studentId, month, year);
        Log.e("calendar", "getTutorCalendarSchedule: " +tutorCalendarCall.request());
        tutorCalendarCall.enqueue(new Callback<TutorCalendar>() {
            @Override
            public void onResponse(Call<TutorCalendar> call, Response<TutorCalendar> response) {
                tutorCalendarMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<TutorCalendar> call, Throwable t) {

            }
        });
    }

    /*--------------------------------------------------------------------------------------------*/


    public MutableLiveData<DefaultResponse> manageReschedule(int tutorScheduleId, int tutorId) {
        defaultResponseMutableLiveData = new MutableLiveData<>();
        postTutorReschedule(tutorScheduleId, tutorId);
        return defaultResponseMutableLiveData;
    }

    private void postTutorReschedule(int tutorScheduleId, int tutorId) {
        Call<DefaultResponse> call = Constant.retrofitServiceHeader.rescheduleSession(tutorScheduleId, tutorId);
        Log.e("apiCall", "postTutorReschedule: " +call.request() );
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                defaultResponseMutableLiveData.setValue(response.body());
                Utility.hideLoader();
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                defaultResponseMutableLiveData.setValue(null);
                Utility.hideLoader();
            }
        });
    }

    /*--------------------------------------------------------------------------------------------*/

    public MutableLiveData<DefaultResponse> cancelSession(int tutorScheduleId, int tutorId) {
        defaultResponseMutableLiveData = new MutableLiveData<>();
        postTutorCancel(tutorScheduleId, tutorId);
        return defaultResponseMutableLiveData;
    }

    private void postTutorCancel(int tutorScheduleId, int tutorId) {
        Call<DefaultResponse> call = Constant.retrofitService.cancelSession(tutorScheduleId, tutorId);
        Log.e("apiCall", "postTutorCancel: " +call.request() );
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                defaultResponseMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                defaultResponseMutableLiveData.setValue(null);
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
            public void onResponse(Call<ModuleWithVideo> call, Response<ModuleWithVideo> response) {
                if (response.body().status && response.body().detail != null) {
                    moduleWithVideoMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ModuleWithVideo> call, Throwable t) {

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
        Log.e("apiCall", "getModuleAudioById: " +moduleWithVideoCall.request());
        moduleWithVideoCall.enqueue(new Callback<ModuleWithVideo>() {
            @Override
            public void onResponse(Call<ModuleWithVideo> call, Response<ModuleWithVideo> response) {
                if (response.body().status && response.body().detail != null) {
                    moduleWithAudioMutableLiveData.setValue(response.body().detail.mp3ModuleDataContractList);
                }
            }

            @Override
            public void onFailure(Call<ModuleWithVideo> call, Throwable t) {

            }
        });
    }

    /*--------------------------------------------------------------------------------------------*/


    public MutableLiveData<DefaultResponse> postSessionFeedback(int userId, FeedbackTutorContentRequest feedbackContentRequest) {
        defaultResponseMutableLiveData = new MutableLiveData<>();
        postFeedBack(userId, feedbackContentRequest);
        return defaultResponseMutableLiveData;
    }

    private void postFeedBack(int userId, FeedbackTutorContentRequest feedbackContentRequest) {
        Call<DefaultResponse> call = Constant.retrofitService.postTutorSessionFeedback(userId, feedbackContentRequest);
        Log.e("apiCall", "postFeedBack: " + call.request());
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                defaultResponseMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                defaultResponseMutableLiveData.setValue(null);
            }
        });
    }
    /*--------------------------------------------------------------------------------------------*/

    public MutableLiveData<DefaultResponse> postRegularSessionFeedback(int userId, FeedbackTutorRegularContentRequest feedbackContentRequest) {
        defaultResponseMutableLiveData = new MutableLiveData<>();
        postRegularFeedback(userId, feedbackContentRequest);
        return defaultResponseMutableLiveData;
    }

    private void postRegularFeedback(int userId, FeedbackTutorRegularContentRequest feedbackContentRequest){
        Call<DefaultResponse> call = Constant.retrofitService.postTutorRegularFeedback(userId, feedbackContentRequest);
        Log.e("apicall", "postFeedback: "+call.request());
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                defaultResponseMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                defaultResponseMutableLiveData.setValue(null);
            }
        });
    }

    /*---------------------------------------------------------------------------------------------*/

    public MutableLiveData<AllCourses> getAllCourseByCatIdAndLevelId(int categoryId, int levelId) {
        allCoursesMutableLiveData = new MutableLiveData<>();
        getCourseList(categoryId, levelId);
        return allCoursesMutableLiveData;
    }

    private void getCourseList(int categoryId,int levelId){
        Call<AllCourses> call = Constant.retrofitServiceHeader.getCourseList(categoryId, levelId);
        Log.e("apicall", "postFeedback: "+call.request());
        call.enqueue(new Callback<AllCourses>() {
            @Override
            public void onResponse(Call<AllCourses> call, Response<AllCourses> response) {
                allCoursesMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<AllCourses> call, Throwable t) {
                allCoursesMutableLiveData.setValue(null);
            }
        });
    }
}
