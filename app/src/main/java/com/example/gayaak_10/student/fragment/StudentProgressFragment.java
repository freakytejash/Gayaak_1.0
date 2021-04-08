package com.example.gayaak_10.student.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gayaak_10.R;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentStudentProgressBinding;
import com.example.gayaak_10.model.response.CoursesDetail;
import com.example.gayaak_10.model.tutor.CourseModuleDetail;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.activity.StudentCourseVideoViewActivity;
import com.example.gayaak_10.student.activity.StudentHomeActivity;
import com.example.gayaak_10.student.adapter.CourseModuleAdapter;
import com.example.gayaak_10.student.adapter.RegularCourseModuleAdapter;
import com.example.gayaak_10.student.adapter.StudentCourseVideoListAdapter;
import com.example.gayaak_10.student.model.ModuleWithVideoDetail;
import com.example.gayaak_10.student.model.RegularCourseProgress;
import com.example.gayaak_10.student.model.VideosModuleDataContractList;
import com.example.gayaak_10.student.viewmodel.StudentViewModel;
import com.example.gayaak_10.utility.Utility;

import java.util.ArrayList;


public class StudentProgressFragment extends Fragment {
    private FragmentStudentProgressBinding binding;
    private StudentViewModel viewModel;
    private Integer courseId = 0;
    private CoursesDetail coursesDetail;
    private ModuleWithVideoDetail selectedCourse;
    private ArrayList<CourseModuleDetail> courseDetail;
    private ArrayList<RegularCourseProgress.ModuledataContractList>moduleStatusList = new ArrayList<>();
    public static Integer moduleId = -1;

    public StudentProgressFragment(CoursesDetail coursesDetail) {
        this.courseId = coursesDetail.courseId;
        this.coursesDetail = coursesDetail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStudentProgressBinding.inflate(getLayoutInflater());
        viewModel = ViewModelProviders.of(getActivity()).get(StudentViewModel.class);

        binding.tvCourseName.setText(coursesDetail.name);
        binding.tvModuleDescription.setText(coursesDetail.detail);

        viewModel.getRegularCourseProgress(App.userDataContract.detail.userId, courseId).observe(getViewLifecycleOwner(), regularCourseProgress -> {
            if (regularCourseProgress.detail.moduledataContractList==null){

            }
            else if (!regularCourseProgress.detail.moduledataContractList.isEmpty()){
                moduleStatusList.addAll(regularCourseProgress.detail.moduledataContractList);
            }
            getCourseList(courseId);
        });

        return binding.getRoot();
    }

    private void getCourseList(int courseId){
        viewModel.getCourseModule(courseId).observe(getViewLifecycleOwner(), courseModule -> {
            if (!courseModule.detail.isEmpty()) {
                binding.layoutModule.setVisibility(View.VISIBLE);
                binding.layoutEmptyModule.setVisibility(View.GONE);
                courseDetail = courseModule.detail;
                setModuleView(courseModule.detail, moduleStatusList);
            } else {
                binding.layoutModule.setVisibility(View.GONE);
                binding.layoutEmptyModule.setVisibility(View.VISIBLE);
            }
        });
    }
    private void setModuleView(ArrayList<CourseModuleDetail> detail, ArrayList<RegularCourseProgress.ModuledataContractList> courseProgressList) {
        binding.rvModule.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        RegularCourseModuleAdapter courseModuleAdapter = new RegularCourseModuleAdapter(getActivity(),
                detail, courseProgressList, new RegularCourseModuleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //  StudentHomeActivity.addFragment(new StudentModuleInfoFragment(), Constant.COURSE_CATALOG, getActivity());
                //StudentHomeActivity.addFragment(new BuyCourseFragment(), Constant.COURSE_CATALOG, getActivity());
            }

            @Override
            public void openModuleVideo(int position) {

               /* getCourseVideos(position);*/
                Intent intent = new Intent(getActivity(), StudentCourseVideoViewActivity.class);
                intent.putExtra("moduleId", detail.get(position).moduleId);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);

            }

            @Override
            public void openModuleAudio(int position) {
                // open pdf view
                //StudentHomeActivity.addFragment(new StudentModulePdfFragment(detail.get(position)), Constant.COURSE_CATALOG, getActivity());
                StudentHomeActivity.addFragment(new StudentAudioFragment(detail.get(position).moduleId), Constant.COURSE_CATALOG, getActivity());
            }
        });
        binding.rvModule.setAdapter(courseModuleAdapter);

    }

   /* private void getCourseVideos(Integer selectedCourseId) {
        //binding.progressBarVideo.setVisibility(View.VISIBLE);
        viewModel.getModuleVideo(selectedCourseId).observe(StudentProgressFragment.this, moduleWithVideo -> {
            if (moduleWithVideo.detail != null){
                if (moduleWithVideo.detail.videosModuleDataContractList != null && !moduleWithVideo.detail.videosModuleDataContractList.isEmpty()){
                    selectedCourse = moduleWithVideo.detail;
                    binding.setVisibility(View.VISIBLE);
                    binding.layoutEmptyVideoList.setVisibility(View.GONE);
                    binding.tvVideoTitle.setText(moduleWithVideo.detail.name);
                    if (moduleWithVideo.detail.description != null && !moduleWithVideo.detail.description.isEmpty()) {
                        binding.tvVideoDescription.setVisibility(View.VISIBLE);
                        binding.tvVideoDescription.setText(moduleWithVideo.detail.description);
                    } else {
                        binding.tvVideoDescription.setVisibility(View.GONE);
                    }
                    setVideoList(moduleWithVideo.detail.videosModuleDataContractList);
                }else {
                    binding.progressBarVideo.setVisibility(View.GONE);
                    binding.layoutVideoList.setVisibility(View.GONE);
                    binding.layoutEmptyVideoList.setVisibility(View.VISIBLE);
                }
            }else {
                binding.progressBarVideo.setVisibility(View.GONE);
                binding.progressBarVideo.setVisibility(View.GONE);
                binding.layoutVideoList.setVisibility(View.GONE);
                binding.layoutEmptyVideoList.setVisibility(View.VISIBLE);
            }
        });
    }*/
  /*  @SuppressLint("SetTextI18n")
    private void setVideoList(final ArrayList<VideosModuleDataContractList> videosModuleDataContractList) {
        if (playedVideoData != null && !playedVideoData.isEmpty()){
            for (int i = 0; i < videosModuleDataContractList.size(); i++) {
                for (int j = 0; j < playedVideoData.size(); j++) {
                    if (videosModuleDataContractList.get(i).moduleId == playedVideoData.get(j).videoCourseId){
                        if (videosModuleDataContractList.get(i).courseVideosId == playedVideoData.get(j).videoId){
                            videosModuleDataContractList.get(i).playedVideoDataArrayList.videoId = playedVideoData.get(j).videoId;
                            videosModuleDataContractList.get(i).playedVideoDataArrayList.videoCourseId = playedVideoData.get(j).videoCourseId;
                            videosModuleDataContractList.get(i).playedVideoDataArrayList.videoPlayedPosition = playedVideoData.get(j).videoPlayedPosition;
                            break;
                        }
                    }
                }
            }
        }

        binding.progressBarVideo.setVisibility(View.GONE);
        binding.tvCourseCount.setText("Course Video 1 of " + videosModuleDataContractList.size());
        binding.rvVideoList.setLayoutManager(new LinearLayoutManager(StudentCourseVideoViewActivity.this, LinearLayoutManager.VERTICAL, false));
        courseVideoListAdapter = new StudentCourseVideoListAdapter(StudentCourseVideoViewActivity.this, videosModuleDataContractList, new StudentCourseVideoListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (!isMediaEnded) {
                    saveVideoDataOnClick(lastPosition, playedVideoData1, binding.courseVideoPlayer.getPlayer().getCurrentPosition());
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                binding.courseVideoPlayer.stopPlayer();
                setVideoPlayer(videosModuleDataContractList.get(position));
                courseVideoListAdapter.notifyDataSetChanged();

                checkVideos(videosModuleDataContractList);
            }

            @Override
            public void paidVideoClick(int position) {
                //check coins
                if (App.userDataContract.detail.userWalletDataContract.Coins <= 0){
                    StudentHomeActivity.addFragment(new CoursePlansFragment(""), Constant.PROFILE, StudentCourseVideoViewActivity.this);
                }
            }
        });
        binding.rvVideoList.setAdapter(courseVideoListAdapter);
    }*/
}