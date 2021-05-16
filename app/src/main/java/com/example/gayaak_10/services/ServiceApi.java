package com.example.gayaak_10.services;

import com.example.gayaak_10.common.model.TutorCalendar;
import com.example.gayaak_10.common.viewmodel.WalletCoin;
import com.example.gayaak_10.model.PractiseSessionData;
import com.example.gayaak_10.model.request.CourseSubscriptionRequest;
import com.example.gayaak_10.model.response.AllCourses;
import com.example.gayaak_10.model.response.AllCoursesDetail;
import com.example.gayaak_10.model.response.ConfigurationData;
import com.example.gayaak_10.model.response.Countries;
import com.example.gayaak_10.model.response.CourseInfoWithTutor;
import com.example.gayaak_10.model.response.DefaultResponse;
import com.example.gayaak_10.model.response.DefaultResponseStatusMessage;
import com.example.gayaak_10.model.response.FilteredCourse;
import com.example.gayaak_10.model.response.PractiseSessionInfo;
import com.example.gayaak_10.model.response.UserCoursesDetail;
import com.example.gayaak_10.model.response.UserDataContract;
import com.example.gayaak_10.model.response.UserDataProfile;
import com.example.gayaak_10.model.response.UserLoginData;
import com.example.gayaak_10.model.tutor.CourseModule;
import com.example.gayaak_10.model.tutor.StudentsTutorId;
import com.example.gayaak_10.student.model.CoinCurrencyConfig;
import com.example.gayaak_10.student.model.CourseByStudentId;
import com.example.gayaak_10.student.model.CoursePlan;
import com.example.gayaak_10.student.model.DemoUserDashboard;
import com.example.gayaak_10.student.model.LevelCategoryInfo;
import com.example.gayaak_10.student.model.ModuleEbooks;
import com.example.gayaak_10.student.model.ModuleWithVideo;
import com.example.gayaak_10.student.model.RegularCourseProgress;
import com.example.gayaak_10.student.model.TransactionType;
import com.example.gayaak_10.student.model.TutorByCourseLevel;
import com.example.gayaak_10.student.model.request.DemoTutorRequest;
import com.example.gayaak_10.student.model.request.FeedbackContentRequest;
import com.example.gayaak_10.student.model.request.StudentFutureBookingRequest;
import com.example.gayaak_10.student.model.request.StudentSupportRequest;
import com.example.gayaak_10.student.model.request.WalletUpdateRequest;
import com.example.gayaak_10.tutor.model.ScheduleSlotTime;
import com.example.gayaak_10.tutor.model.TutorDashboard;
import com.example.gayaak_10.tutor.model.TutorSchedule;
import com.example.gayaak_10.tutor.model.request.FeedbackTutorContentRequest;
import com.example.gayaak_10.tutor.model.request.FeedbackTutorRegularContentRequest;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceApi {

    /*-------------Login , Registration ----------*/
    @POST("login/RegisterUser")
    Call<UserLoginData> postUser(@Body UserDataContract user);

    @POST("login/RegisterUserForSocial")
    Call<UserLoginData> registerUserBySocial(@Body UserDataContract user);

    @GET("login/GetLogin?")
    Call<UserLoginData> loginByEmail(@Query("login") String email,
                                     @Query("pwd") String password);

    @POST("login/SendOTPVarification?")
    Call<DefaultResponse> sendOtp(@Query("userName") String userName);

    @POST("login/VerifyOTPForPhoneEmail?")
    Call<DefaultResponse> verifyOtp(@Query("userName") String userName,
                                    @Query("OTP") String OTP);

    @POST("login/ChangePassword?")
    Call<DefaultResponse> changeEmailPassword(@Query("userId") Integer userId,
                                              @Query("password") String pwd,
                                              @Query("currentpassword") String currentpassword);

    @POST("login/ForGotPassword?")
    Call<DefaultResponse> forgotPasswordByEmail(@Query("emailId") String emailId);

    @GET("login/GetUserByEmailOrPhone?")
    Call<DefaultResponseStatusMessage> checkEmailExist(@Query("username") String username);

    @GET("Master/GetConfigurationList")
    Call<ConfigurationData> getConfigurationData();

    @POST("Schedule/MarkClassComplete?")
    Call<DefaultResponse> postClassComplete(@Query("classId") String classId,
                                            @Query("userid") String userid);

    /*-------------------------------------------------------------------------------*/

    @GET("User/GetUserById/{id}")
    Call<UserDataProfile> getUserProfile(@Path("id") String id);

    @POST("User/ManageUser")
    Call<DefaultResponse> updateUserInfo(@Body UserDataContract user);

    @GET("Course/GetCourseList")
    Call<AllCourses> getCourseList(@Query("categoryId") Integer categoryId,
                                   @Query("levelId") Integer levelId);

    @GET("Course/GetCourseById?")
    Call<AllCoursesDetail> getCourseListWithId(@Query("id") Integer id);

    @GET("Course/GetCourseWithTutors?")
    Call<CourseInfoWithTutor> getCourseInfoTutor(@Query("courseId") Integer courseId);

    @GET("Course/GetCourseByIdWithVideos/{courseId}")
    Call<AllCoursesDetail> getCourseVideos(@Path("courseId") Integer courseId);

    @Multipart
    @POST("PractiseSession/ManagePractiseSession")
    Call<DefaultResponse> savePracticeSession(@Part MultipartBody.Part file,
                                              @Part("PractiseSessionDataContract") PractiseSessionData practiseSessionDataContract);

    @GET("PractiseSession/GetPractiseSessionByUserId?")
    Call<PractiseSessionInfo> getPracticeSessionByUserId(@Query("userId") int userId);

    @GET("Course/GetCourseByFilter?")
    Call<FilteredCourse> getFilteredData(@Query("catId") String catId,
                                         @Query("levelId") String levelId,
                                         @Query("minPrice") String minPrice,
                                         @Query("maxPrice") String maxPrice,
                                         @Query("tags") String tags,
                                         @Query("pageno") String pageno,
                                         @Query("itemperpage") String itemperpage);

    @POST("Subscription/ManageCourseSubscription?")
    Call<DefaultResponse> addCourseSubscription(@Query("userId") int userId,
                                                @Body CourseSubscriptionRequest courseSubscriptionRequest);

    @GET("Subscription/GetSubscriptionByUserId?")
    Observable<UserCoursesDetail> getUserLearningCourseRx(@Query("userId") Integer userId);


    @GET("Course/GetCourseById?")
    Observable<AllCoursesDetail> getCourseListWithIdRx(@Query("id") Integer id);

    @GET("Course/GetTrandingCourse")
    Observable<AllCourses> getCourseListRx();

  //  @GET("CurrencyName")
    @GET("Master/GetCountryList")
    Observable<Countries> getCountries();

    //get ebook by module id
    @GET("course/GetCourseByIdWithEbooks/{moduleId}")
    Call<ModuleEbooks> getModuleEBooks(@Path("moduleId") Integer moduleId);

    @GET("Master/GetWalletPlan")
    Call<CoursePlan> getCoursePlans();

    @GET("Master/GetCoinCurrencyConfiguration")
    Call<CoinCurrencyConfig> getCoursePlanConfiguration();

    @POST("Transaction/ManageUserWalletTransaction?")
    Call<DefaultResponse> manageWallet(@Query("userId") Integer userId,
                                       @Body WalletUpdateRequest walletUpdateRequest);


    //sumit
    @GET("User/GetUserWalletByUserId/{userId}")
    Call<WalletCoin> getWalletCoinById(@Path("userId") Integer userId);

    @GET("master/GetTransactionTypeListList")
    Call<TransactionType> getTransactionType();
//
//    @GET("master/GetTransactionTypeListList")
//    Call<TransactionType> getTransactionType();

    //    for goto the class for both tutor and student
    @POST("Master/SendNotification?")
    Call<DefaultResponse> postGoToClassNotification(@Query("userId") int userId,
                                                    @Query("liveClassDetailId") int liveClassDetailId);

    /*------------------------------------------TUTOR---------------------------------------------*/
    // for all the time in scheduler
    @GET("Schedule/GetAllMasterSchedule")
    Call<ScheduleSlotTime> getScheduleSlots();

    // get the tutors already create schedule
    @GET("Schedule/GetTutorScheduleByTutorId?")
    Call<TutorSchedule> getTutorScheduleById(@Query("tutorId") Integer tutorId);

    // create tutors schedule in scheduler
    @POST("Schedule/ManageTutorSchedule?")
    Call<DefaultResponse> manageTutorSchedule(@Query("ScheduleId") String scheduleId,
                                              @Query("userId") Integer userId);

    // tutor home total sessions
    @GET("Dashboard/GetTutorDashboardDetail?")
    Call<TutorDashboard> getTutorDashboard(@Query("userId") Integer userId);

    //delete scheduler free slots
    @POST("Schedule/DeleteTutorSchedule?")
    Call<DefaultResponse> deleteTutorSchedule(@Query("TutorScheduleId") Integer TutorScheduleId,
                                              @Query("tutorId") Integer tutorId);

    //get schedule according to student id
    @GET("Schedule/GetTutorCalendarByTutorId?")
    Call<TutorCalendar> getTutorCalendarById(@Query("tutorId") Integer tutorId,
                                             @Query("studentId") Integer studentId,
                                             @Query("month") Integer month,
                                             @Query("year") Integer year);

    //Get students list in calendar -http://103.255.190.131/GaayakAPI/api/User/GetStudentsByTutorId?tutorId=188
    @GET("User/GetStudentsByTutorId?")
    Call<StudentsTutorId> getStudentsByTutorId(@Query("tutorId") Integer tutorId);

    // to get tutors slot is available or not
    @GET("Schedule/GetTutorScheduleAvailability?")
    Call<TutorSchedule> getTutorScheduleAvailability(@Query("tutorId") Integer tutorId);

    //get the modules using course id
    @GET("Module/GetModuleByCourseId/{courseId}")
    Call<CourseModule> getCourseModules(@Path("courseId") Integer courseId);

    //Reschedule session
    //http://localhost:59615/api/Schedule/ManageReschedule?tutorScheduleId= 172tutorId=188
    @POST("Schedule/ManageReschedule?")
    Call<DefaultResponse> rescheduleSession(@Query("tutorScheduleId") Integer tutorScheduleId,
                                            @Query("tutorId") Integer tutorId);

    //ManageCancelscheduleByTutor
    //POST
    //http://103.255.190.131/GaayakAPI/api/Schedule/ManageCancelscheduleByTutor?tutorScheduleId=178&tutorId=188
    @POST("Schedule/ManageCancelscheduleByTutor?")
    Call<DefaultResponse> cancelSession(@Query("tutorScheduleId") Integer tutorScheduleId,
                                            @Query("tutorId") Integer tutorId);

    //post session demo feedback
    @POST("FeedBack/ManageTutorFeedBackSession?")
    Call<DefaultResponse> postTutorSessionFeedback(@Query("UserId") int UserId,
                                              @Body FeedbackTutorContentRequest feedbackTutorContentRequest);

    // post regular session feedback
    @POST("User/ManageProgress?")
    Call<DefaultResponse> postTutorRegularSessionFeedback(@Query("userId") int userId,
                                                          @Body FeedbackTutorRegularContentRequest feedbackTutorContentRequest);


    @POST("User/ManageProgress?")
    Call<DefaultResponse> postTutorRegularFeedback(@Query("userId") int userId,
                                                   @Body FeedbackTutorRegularContentRequest feedbackTutorRegularContentRequest);


    /*------------------------------------------STUDENTS------------------------------------------*/

    //post api support
    @POST("User/ManageSupport?")
    Call<DefaultResponse> postStudentSupport(@Query("userId") int userId,
                                             @Body StudentSupportRequest studentSupportRequest);

    @POST("Schedule/ManageStudentFutureBooking?")
    Call<DefaultResponse> postStudentFutureBooking(@Query("userId") int userId,
                                                   @Body StudentFutureBookingRequest studentFutureBookingRequest);

    //get schedule according to student id
    @GET("Schedule/GetStudentCalendarByStudentId?")
    Call<TutorCalendar> getStudentCalendarById(@Query("studentId") Integer studentId,
                                               @Query("month") Integer month,
                                               @Query("year") Integer year);

    // Student upcoming session - http://103.255.190.131/GaayakAPI/api/Dashboard/GetDemoUserDashboard?studentId=189
    @GET("Dashboard/GetDemoUserDashboard?")
    Call<DemoUserDashboard> getDemoUserDashboard(@Query("studentId") int studentId);

    // in demo view for course list drop down
    @GET("Master/GetLevelCategoryList")
    Call<LevelCategoryInfo> getLevelCategory();

    // in regular view for course list drop down
    @GET("Course/GetRecommentedCourseByStudentId?")
    Call<CourseByStudentId> getRecommendedCourse(@Query("userId") Integer userId);

    // get data of demo tutors
    @GET("User/GetTutorByCourseAndLevelId?")
    Call<TutorByCourseLevel> getDemoTutors(@Query("categoryId") Integer categoryId,
                                           @Query("levelId") Integer levelId);

    // book demo session
    @POST("Schedule/ManageStudentBooking?")
    Call<DefaultResponse> bookDemoTutor(@Query("userId") Integer userId,
                                        @Body DemoTutorRequest demoTutorRequest);

    //get video by module id
    @GET("Module/GetModuledWithVideo?")
    Call<ModuleWithVideo> getModuledWithVideo(@Query("id") Integer id,
                                              @Query("x-userid") Integer userid);

    //get PDF by module id
    @GET("Module/GetModuleWithPDF?")
    Call<ModuleWithVideo> getModuledWithPdf(@Query("id") Integer id,
                                            @Query("x-userid") Integer userid);

    //get mp3 by module id
    @GET("Module/GetModuleWithMP3?")
    Call<ModuleWithVideo> getModuledWithAudio(@Query("id") Integer id,
                                            @Query("x-userid") Integer userid);



    //Reschedule session
    //http://103.255.190.131/GaayakAPI/api/Schedule/ManageStudentBooking?userId=223
   /* @POST("Schedule/ManageStudentBooking?")
    Call<DefaultResponse> rescheduleSessionStudent(@Query("userId") Integer userId,
                                            @Body DemoTutorRequest demoTutorRequest);*/
    @POST("Schedule/ManageReschedule?")
    Call<DefaultResponse> rescheduleSessionStudent(@Query("tutorScheduleId") Integer tutorScheduleId,
                                            @Query("tutorId") Integer tutorId);

    //CancelScheduleBYStudent
    //POST
    //http://103.255.190.131/GaayakAPI/api/Schedule/ManageCancelscheduleByStudent?tutorScheduleId=172userId=209

    @POST("Schedule/ManageCancelscheduleByStudent?")
    Call<DefaultResponse> CancelScheduleByStudent(@Query("tutorScheduleId") Integer tutorScheduleId,
                                        @Query("userId") Integer userId);

    //post session feedback
    @POST("FeedBack/ManageFeedBackSession?")
    Call<DefaultResponse> postSessionFeedback(@Query("userId") int userId,
                                              @Body FeedbackContentRequest feedbackContentRequest);

    @GET("User/GetProgressByUserId?")
    Call<RegularCourseProgress> getRegularCourseProgress(@Query("userId") int userId,
                                                         @Query("courseId") int courseId);

}

