package com.example.gayaak_10.tutor.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.gayaak_10.R;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.ActivityTutorHomeBinding;
import com.example.gayaak_10.model.response.AllCourses;
import com.example.gayaak_10.model.response.CoursesDetail;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.tutor.fragment.TutorCalendarFragment;
import com.example.gayaak_10.tutor.fragment.TutorCoursesCatalogFragment;
import com.example.gayaak_10.tutor.fragment.TutorFeedbackFragment;
import com.example.gayaak_10.tutor.fragment.TutorHomeFragment;
import com.example.gayaak_10.tutor.fragment.TutorProfileFragment;
import com.example.gayaak_10.tutor.fragment.TutorRegularFeedbackFragment;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TutorHomeActivity extends AppCompatActivity {
    
    private static ActivityTutorHomeBinding binding;
    private Fragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTutorHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getCourseList();

        binding.bottomTutorNavigation.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.home:
                    fragment = new TutorHomeFragment();
                    break;

                case R.id.course_catalog:
                    fragment = new TutorCoursesCatalogFragment();
                    break;

                case R.id.calendar:
                     fragment = new TutorCalendarFragment();
                    break;

                case R.id.profile:
                    fragment = new TutorProfileFragment("");
                    break;
            }
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.fragmentTutorContainer, fragment).commit();
            return true;
        });

     //   addFragment(new TutorHomeFragment(), "Home");
    }

    public static void addFragment(Fragment fragment, String fragmentName, FragmentActivity activity) {
        switch (fragmentName) {
            case "Home":
                binding.bottomTutorNavigation.setSelectedItemId(R.id.home);
                break;
            case "CourseCatalog":
                binding.bottomTutorNavigation.setSelectedItemId(R.id.course_catalog);
                break;
            case "Calendar":
                binding.bottomTutorNavigation.setSelectedItemId(R.id.calendar);
                break;
            case "UserProfile":
                binding.bottomTutorNavigation.setSelectedItemId(R.id.profile);
                break;
        }
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fragmentTutorContainer, fragment).commit();

    }


    private void addFragment(Fragment fragment, String fragmentName) {
        switch (fragmentName) {
            case "Home":
                binding.bottomTutorNavigation.setSelectedItemId(R.id.home);
                break;
            case "CourseCatalog":
                binding.bottomTutorNavigation.setSelectedItemId(R.id.course_catalog);
                break;
            case "Calendar":
                binding.bottomTutorNavigation.setSelectedItemId(R.id.calendar);
            case "UserProfile":
                binding.bottomTutorNavigation.setSelectedItemId(R.id.profile);
                break;
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fragmentTutorContainer, fragment).commit();

    }

    @Override
    public void onBackPressed() {
        Fragment mFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentTutorContainer);
        if (mFragment instanceof TutorHomeFragment) {
            super.onBackPressed();
            finish();
        } else {
            addFragment(new TutorHomeFragment(), Constant.HOME);
        }
    }

    private void getCourseList() {
        CustomAsyncTask customAsyncTask = new CustomAsyncTask();
        customAsyncTask.execute();
    }

    private void allCoursesFinalList(ArrayList<CoursesDetail> tempCoursesArrayList) {
        Collections.sort(tempCoursesArrayList, (s1, s2) -> s1.name.compareToIgnoreCase(s2.name));
        App.allCoursesArrayList.clear();
        App.allCoursesArrayList.addAll(tempCoursesArrayList);

         openHome();
    }

    private class CustomAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            Call<AllCourses> coursesCall = Constant.retrofitServiceHeader.getCourseList(null,null);
            coursesCall.enqueue(new Callback<AllCourses>() {
                @Override
                public void onResponse(Call<AllCourses> call, Response<AllCourses> response) {
                    if (response.isSuccessful()) {
                        if (!response.body().detail.isEmpty()) {
                            allCoursesFinalList(/*finalI, */ response.body().detail);
                        }else {
                            openHome();
                        }
                    }else {
                        openHome();
                    }
                }

                @Override
                public void onFailure(Call<AllCourses> call, Throwable t) {
                    t.printStackTrace();
                    openHome();
                }
            });
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    binding.layoutHome.setVisibility(View.GONE);
                    binding.progressBarHome.setVisibility(View.VISIBLE);
                }
            });

        }
    }

    private void openHome() {
        boolean zoomSession = getIntent().hasExtra(Constant.ZOOM_SESSION_FINISHED);
//        int liveClassId = 2 /*App.liveClassId*/;
//        String StudentName = App.StudentName;

        binding.layoutHome.setVisibility(View.VISIBLE);
        binding.progressBarHome.setVisibility(View.GONE);
        if (zoomSession && App.liveClassId==2){
            addFragment(new TutorRegularFeedbackFragment(), Constant.HOME);
            return;
        }
        else if (zoomSession) {
            addFragment(new TutorFeedbackFragment(), Constant.HOME);
        }else {
            addFragment(new TutorHomeFragment(), Constant.HOME);
        }

    }
}
