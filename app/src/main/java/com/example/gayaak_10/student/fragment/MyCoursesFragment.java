package com.example.gayaak_10.student.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gayaak_10.R;
import com.example.gayaak_10.activity.CourseVideoViewActivity;
import com.example.gayaak_10.adapter.UserSubscribedCourseAdapter;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentMyCoursesBinding;
import com.example.gayaak_10.model.response.AllCoursesDetail;
import com.example.gayaak_10.model.response.CoursesDetail;
import com.example.gayaak_10.model.response.UserCourseData;
import com.example.gayaak_10.model.response.UserCoursesDetail;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.activity.StudentHomeActivity;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.example.gayaak_10.services.App.coursesDetailArrayList;

public class MyCoursesFragment extends Fragment {

    private FragmentMyCoursesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMyCoursesBinding.inflate(getLayoutInflater());


        if (!coursesDetailArrayList.isEmpty()){
            setLearningCourseData(coursesDetailArrayList);
        }else {
            binding.layoutNoCourses.setVisibility(View.VISIBLE);
           // getMyCourses();
        }

        binding.ivCourseBack.setOnClickListener(view -> {
            StudentHomeActivity.addFragment(new StudentProfileFragment("Student"), Constant.PROFILE, getActivity());
        });

        return binding.getRoot();
    }

    @SuppressLint("CheckResult")
    private void getMyCourses() {
        Constant.retrofitServiceHeader.getUserLearningCourseRx(App.userDataContract.detail.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserCoursesDetail>() {

                    @Override
                    public void onNext(UserCoursesDetail userCoursesDetail) {
                        if (userCoursesDetail.Status && !userCoursesDetail.Detail.isEmpty()) {
                            getCourse(userCoursesDetail.Detail);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        binding.progressBarMyCourse.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @SuppressLint("CheckResult")
    private void getCourse(ArrayList<UserCourseData> detail) {
        ArrayList<CoursesDetail> coursesDetailArrayList = new ArrayList<>();
        for (int i = 0; i < detail.size(); i++) {
            int CourseId = detail.get(i).CourseId;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Constant.retrofitServiceHeader.getCourseListWithIdRx(CourseId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<AllCoursesDetail>() {
                        @Override
                        public void onNext(AllCoursesDetail allCoursesDetail) {
                            if (allCoursesDetail.status && allCoursesDetail != null) {
                                if (allCoursesDetail.detail.courseTutorsDataContractList != null) {
                                    coursesDetailArrayList.add(allCoursesDetail.detail);
                                    if (coursesDetailArrayList.size() == detail.size() && !coursesDetailArrayList.isEmpty()) {
                                        App.coursesDetailArrayList.clear();
                                        App.coursesDetailArrayList = coursesDetailArrayList;
                                        setLearningCourseData(coursesDetailArrayList);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            binding.progressBarMyCourse.setVisibility(View.GONE);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    private void setLearningCourseData(ArrayList<CoursesDetail> coursesDetailArrayList) {
        binding.progressBarMyCourse.setVisibility(View.GONE);
        if (!coursesDetailArrayList.isEmpty()) {
            binding.rvCourses.setVisibility(View.VISIBLE);
            binding.layoutNoCourses.setVisibility(View.GONE);
            App.coursesDetailArrayList = coursesDetailArrayList;
            binding.rvCourses.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            binding.rvCourses.setAdapter(new UserSubscribedCourseAdapter(getActivity(), coursesDetailArrayList, new UserSubscribedCourseAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int position) {

                    Intent intent = new Intent(getActivity(), CourseVideoViewActivity.class);
                    intent.putExtra("courseId", coursesDetailArrayList.get(position).courseId);
                    intent.putExtra("isFromLocation", "UserProfile");
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);

              /*      FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainer, new CourseInfoFragment("UserProfile", coursesDetailArrayList.get(position).courseId));
                    fragmentTransaction.commit();*/
                }
            }));
        } else {
            binding.layoutNoCourses.setVisibility(View.VISIBLE);
            binding.rvCourses.setVisibility(View.GONE);
        }
    }
}
