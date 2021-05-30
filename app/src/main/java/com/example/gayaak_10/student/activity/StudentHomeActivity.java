package com.example.gayaak_10.student.activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.gayaak_10.R;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.ActivityStudentHomeBinding;
import com.example.gayaak_10.model.response.AllCoursesDetail;
import com.example.gayaak_10.model.response.CoursesDetail;
import com.example.gayaak_10.model.response.FilteredCourseDetail;
import com.example.gayaak_10.model.response.UserCourseData;
import com.example.gayaak_10.model.response.UserCoursesDetail;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.fragment.CourseCartFragment;
import com.example.gayaak_10.student.fragment.StudentBookDemoCourseFragment;
import com.example.gayaak_10.student.fragment.StudentCalendarFragment;
import com.example.gayaak_10.student.fragment.StudentCoursesCatalogFragment;
import com.example.gayaak_10.student.fragment.StudentDemoHomeFragment;
import com.example.gayaak_10.student.fragment.StudentFeedbackFragment;
import com.example.gayaak_10.student.fragment.StudentProfileFragment;
import com.example.gayaak_10.student.fragment.StudentRegularFragment;
import com.example.gayaak_10.student.viewmodel.StudentViewModel;
import com.example.gayaak_10.utility.SharedPrefsUtil;
import com.example.gayaak_10.utility.Utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.TimeZone;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class StudentHomeActivity extends AppCompatActivity {

    private static ActivityStudentHomeBinding binding;
    private ArrayList<CoursesDetail> coursesDetailArrayList = new ArrayList<CoursesDetail>();
    private Fragment fragment = null;
    private String userType = "";
    private boolean registrationType;
    private static FragmentManager fragmentManager;
    private boolean mDoubleBackToExitPressedOnce;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fragmentManager = getSupportFragmentManager();

        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        Log.d("Time zone", "=" + tz.getDisplayName());

        String[] timezoneArray = TimeZone.getAvailableIDs();
        for (int i = 0; i < timezoneArray.length; i++) {
            if (timezoneArray[i].equals(TimeZone.getDefault().getID())) {
                Log.e("timezone", "onCreate: " + timezoneArray[i]);
                break;
            }
        }

        registrationType = getIntent().hasExtra("isNewRegistration");

        boolean openView = getIntent().hasExtra(Constant.OPEN_CART);
        boolean zoomSession = getIntent().hasExtra(Constant.ZOOM_SESSION_FINISHED);
        if (openView) {
            binding.progressBarHome.setVisibility(View.GONE);
            binding.layoutHome.setVisibility(View.VISIBLE);
            addFragment(new CourseCartFragment(), Constant.PROFILE);
        } else if (zoomSession) {
            binding.progressBarHome.setVisibility(View.GONE);
            binding.layoutHome.setVisibility(View.VISIBLE);
            addFragment(new StudentFeedbackFragment(), Constant.HOME);
        } else {
            getSubscribedCourses();
            getCourseList();
        }

        binding.bottomStudentNavigation.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.home:
                    if (userType.equalsIgnoreCase("Free")) {
                        fragment = new StudentDemoHomeFragment();
                    } else {
                        fragment = new StudentRegularFragment();
                    }
                    break;

                case R.id.course_catalog:
                    fragment = new StudentCoursesCatalogFragment();
                    break;

                case R.id.calendar:
                    fragment = new StudentCalendarFragment();
                    break;

                case R.id.profile:
                    fragment = new StudentProfileFragment("");
                    break;
            }
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.add(R.id.fragmentContainer, fragment).commit();
            return true;
        });
    }

    public static void addFragment(Fragment fragment, String fragmentName, FragmentActivity activity) {
        switch (fragmentName) {
            case "Home":
                binding.bottomStudentNavigation.setSelectedItemId(R.id.home);
                break;
            case "CourseCatalog":
                binding.bottomStudentNavigation.setSelectedItemId(R.id.course_catalog);
                break;
            case "UserProfile":
                binding.bottomStudentNavigation.setSelectedItemId(R.id.profile);
                break;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.add(R.id.fragmentContainer, fragment).commit();


    }


    private void addFragment(Fragment fragment, String fragmentName) {
        switch (fragmentName) {
            case "Home":
                binding.bottomStudentNavigation.setSelectedItemId(R.id.home);
                break;
            case "CourseCatalog":
                binding.bottomStudentNavigation.setSelectedItemId(R.id.course_catalog);
                break;
            case "UserProfile":
                binding.bottomStudentNavigation.setSelectedItemId(R.id.profile);
                break;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.add(R.id.fragmentContainer, fragment).commit();

    }

    private void getCourseList() {
        StudentViewModel studentViewModel = ViewModelProviders.of(StudentHomeActivity.this).get(StudentViewModel.class);
        studentViewModel.getStudentAllCourses().observe(StudentHomeActivity.this, new Observer<FilteredCourseDetail>() {
            @Override
            public void onChanged(FilteredCourseDetail filteredCourseDetail) {
                if (filteredCourseDetail != null && filteredCourseDetail.coursesDetails.size() != 0) {
                    allCoursesFinalList(filteredCourseDetail.coursesDetails);
                } else {
                    openDemoHome();
                }
            }
        });
    }

    private void allCoursesFinalList(ArrayList<CoursesDetail> tempCoursesArrayList) {
        Collections.sort(tempCoursesArrayList, (s1, s2) -> s1.name.compareToIgnoreCase(s2.name));
        App.allCoursesArrayList.clear();
        App.allCoursesArrayList.addAll(tempCoursesArrayList);

        userType = "Free";
        binding.layoutHome.setVisibility(View.VISIBLE);
        binding.progressBarHome.setVisibility(View.GONE);
        SharedPrefsUtil.setStringPreferences(StudentHomeActivity.this, "UserType", userType);
        if (registrationType) {
            addFragment(new StudentBookDemoCourseFragment(userType), Constant.COURSE_CATALOG);
        } else {
            if (userType.equalsIgnoreCase("Paid")) {
                addFragment(new StudentRegularFragment(), Constant.HOME);
            } else if (userType.equalsIgnoreCase("Free")) {
                addFragment(new StudentDemoHomeFragment(), Constant.HOME);
            }
        }
    }

    /*----------------- Has Subscription ------------------------------*/

    private void getSubscribedCourses() {
        binding.progressBarHome.setVisibility(View.GONE);
        binding.layoutHome.setVisibility(View.VISIBLE);
        Constant.retrofitServiceHeader.getUserLearningCourseRx(App.userDataContract.detail.userId)
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableObserver<UserCoursesDetail>() {

                    @Override
                    public void onNext(UserCoursesDetail userCoursesDetail) {
                        if (userCoursesDetail.Status && !userCoursesDetail.Detail.isEmpty()) {
                            // If user has any course subscribed
                            getUsersCourse(userCoursesDetail.Detail);
                        } else {
                            //  openDemoHome();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void openDemoHome() {
        binding.layoutHome.setVisibility(View.VISIBLE);
        binding.progressBarHome.setVisibility(View.GONE);
        userType = "Free";
        SharedPrefsUtil.setStringPreferences(StudentHomeActivity.this, "UserType", userType);
        //    SharedPrefsUtil.setStringPreferences(StudentHomeActivity.this, "UserType", userType);
        if (registrationType) {
            addFragment(new StudentBookDemoCourseFragment(userType), Constant.COURSE_CATALOG);
        } else {
            addFragment(new StudentDemoHomeFragment(), Constant.HOME);
        }
    }

    private void getUsersCourse(ArrayList<UserCourseData> detail) {
        coursesDetailArrayList.clear();
        for (int i = 0; i < detail.size(); i++) {
            int CourseId = detail.get(i).CourseId;
            Constant.retrofitServiceHeader.getCourseListWithIdRx(CourseId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(new DisposableObserver<AllCoursesDetail>() {
                        @Override
                        public void onNext(AllCoursesDetail allCoursesDetail) {
                            if (allCoursesDetail.status && allCoursesDetail != null) {
                                coursesDetailArrayList.add(allCoursesDetail.detail);
                                if (coursesDetailArrayList.size() == detail.size() && !coursesDetailArrayList.isEmpty()) {
                                    App.coursesDetailArrayList.clear();
                                    App.coursesDetailArrayList.addAll(coursesDetailArrayList);
                                    if (!App.coursesDetailArrayList.isEmpty()) {
                                        //   getVideosForSubscribedCourses();
                                        binding.layoutHome.setVisibility(View.VISIBLE);
                                        binding.progressBarHome.setVisibility(View.GONE);
                                        userType = "Paid";
                                        SharedPrefsUtil.setStringPreferences(StudentHomeActivity.this, "UserType", userType);
                                        addFragment(new StudentRegularFragment(), Constant.HOME);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {


                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        Fragment mFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (mFragment instanceof StudentDemoHomeFragment || mFragment instanceof StudentRegularFragment) {
            super.onBackPressed();
            finish();
        } else {
            if (userType.equalsIgnoreCase("Paid")) {
                addFragment(new StudentRegularFragment(), Constant.HOME);
            } else if (userType.equalsIgnoreCase("Free")) {
                addFragment(new StudentDemoHomeFragment(), Constant.HOME);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utility.hideLoader();
    }
}