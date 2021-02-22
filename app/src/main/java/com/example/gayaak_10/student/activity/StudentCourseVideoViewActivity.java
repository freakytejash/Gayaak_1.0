package com.example.gayaak_10.student.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gayaak_10.R;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentCourseVideoViewBinding;
import com.example.gayaak_10.model.response.VideoData;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.adapter.StudentCourseVideoListAdapter;
import com.example.gayaak_10.student.fragment.CoursePlansFragment;
import com.example.gayaak_10.student.model.ModuleWithVideoDetail;
import com.example.gayaak_10.student.model.VideosModuleDataContractList;
import com.example.gayaak_10.student.viewmodel.StudentViewModel;
import com.example.gayaak_10.utility.SharedPrefsUtil;
import com.example.gayaak_10.videoplayer.playerinterface.ExoPlayerCallBack;

import java.util.ArrayList;

public class StudentCourseVideoViewActivity extends AppCompatActivity{

    private FragmentCourseVideoViewBinding binding;
    private ModuleWithVideoDetail selectedCourse;
    private StudentCourseVideoListAdapter courseVideoListAdapter;
    private ArrayList<VideoData.PlayedVideoData> playedVideoData = new ArrayList<>();
    private VideoData.PlayedVideoData playedVideoData1 = new VideoData.PlayedVideoData();
    private boolean isMediaEnded = false;
    private int lastPosition = -1;
    private StudentViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentCourseVideoViewBinding.inflate(getLayoutInflater());
        viewModel = ViewModelProviders.of(StudentCourseVideoViewActivity.this).get(StudentViewModel.class);
        setContentView(binding.getRoot());

        Integer selectedCourseId = getIntent().getIntExtra("moduleId", 0);
        getCourseVideos(selectedCourseId);

        playedVideoData = new ArrayList<>();
        playedVideoData = SharedPrefsUtil.getVideoDataPreferences(StudentCourseVideoViewActivity.this, Constant.VIDEO_DATA);


        binding.ivBackList.setOnClickListener(view -> {
            if (!isMediaEnded) {
                saveVideoData();
            }
            finish();
            overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
        });

        binding.layoutLockedVideo.setOnClickListener(view -> {
          //  buyPaidCourse();
        });

       /* if (App.coursesDetailArrayList != null && !App.coursesDetailArrayList.isEmpty()) {
            for (int i = 0; i < App.coursesDetailArrayList.size(); i++) {
                if (App.coursesDetailArrayList.get(i).courseId.equals(selectedCourseId)) {
                    binding.btnJoinCourse.setVisibility(View.INVISIBLE);
                    break;
                }
            }
        }

        binding.btnJoinCourse.setOnClickListener(view -> {
            addCourseToCart();

        });*/

        LocalBroadcastManager.getInstance(StudentCourseVideoViewActivity.this).registerReceiver(screenOrientationEnterFull, new IntentFilter("screenOrientationEnterFull"));
        LocalBroadcastManager.getInstance(StudentCourseVideoViewActivity.this).registerReceiver(screenOrientationExitFull, new IntentFilter("screenOrientationExitFull"));
    }

   /*  private void addCourseToCart() {

        BuyCoursesDetail buyCoursesDetail = new BuyCoursesDetail();
        buyCoursesDetail.courseId = selectedCourse.courseId;
        buyCoursesDetail.detail = "";
        buyCoursesDetail.name = selectedCourse.name;
        buyCoursesDetail.price = 0;
        buyCoursesDetail.ThumbnailImage = "";
        buyCoursesDetail.levelName = "";


        if (App.cartList.size() <= 0) {
            App.cartList.add(buyCoursesDetail);
            SharedPrefsUtil.setCartCourse(StudentCourseVideoViewActivity.this, App.cartList);
            showCartDialog();
        } else {
            Log.d("courseId", "courseId => " + selectedCourse.courseId);
            for (int j = 0; j < App.cartList.size(); j++) {
                if (App.cartList.get(j).courseId.equals(selectedCourse.courseId)) {
                    Toast.makeText(StudentCourseVideoViewActivity.this, R.string.course_already, Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    if (j == App.cartList.size() - 1) {
                        App.cartList.add(buyCoursesDetail);
                        SharedPrefsUtil.setCartCourse(StudentCourseVideoViewActivity.this, App.cartList);
                        showCartDialog();
                        break;
                    }
                }
            }
        }
    }

   private void buyPaidCourse() {
        new MaterialAlertDialogBuilder(StudentCourseVideoViewActivity.this)
                .setIcon(R.drawable.gayaak_icon)
                .setTitle("Subscribe Course")
                .setMessage("Do you want to subscribe this course?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    addCourseToCart();
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }*/

    private void getCourseVideos(Integer selectedCourseId) {
        binding.progressBarVideo.setVisibility(View.VISIBLE);
        viewModel.getModuleVideo(selectedCourseId).observe(StudentCourseVideoViewActivity.this, moduleWithVideo -> {
            if (moduleWithVideo.detail != null){
                if (moduleWithVideo.detail.videosModuleDataContractList != null && !moduleWithVideo.detail.videosModuleDataContractList.isEmpty()){
                    selectedCourse = moduleWithVideo.detail;
                    binding.layoutVideoList.setVisibility(View.VISIBLE);
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
    }

    @SuppressLint("SetTextI18n")
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
    }

    private void checkVideos(ArrayList<VideosModuleDataContractList> allCoursesDetail) {
        boolean isAnyVideoFree = false;
        for (int i = 0; i < allCoursesDetail.size(); i++) {
            if (allCoursesDetail.get(i).isfreevedio) {
                lastPosition = i;
                courseVideoListAdapter.selectedPosition = i;
                courseVideoListAdapter.notifyDataSetChanged();
                isAnyVideoFree = true;
                setVideoPlayer(allCoursesDetail.get(i));
                break;
            }
        }
        if (!isAnyVideoFree) {
            binding.layoutLockedVideo.setVisibility(View.VISIBLE);
            binding.courseVideoPlayer.setVisibility(View.GONE);
            isMediaEnded = true;
        } else {
            binding.layoutLockedVideo.setVisibility(View.GONE);
            binding.courseVideoPlayer.setVisibility(View.VISIBLE);
            isMediaEnded = false;
        }
    }

    private void setVideoPlayer(VideosModuleDataContractList videoData) {
        binding.progressBarVideo.setVisibility(View.GONE);
        binding.layoutLockedVideo.setVisibility(View.GONE);
        binding.courseVideoPlayer.setVisibility(View.VISIBLE);
        if (!videoData.videoURL.isEmpty()) {
            playedVideoData1 = new VideoData.PlayedVideoData();
            playedVideoData1.videoId = videoData.courseVideosId;
            playedVideoData1.videoCourseId = videoData.moduleId;

            binding.courseVideoPlayer.setSource(videoData.videoURL);
            binding.courseVideoPlayer.setShowController(true);

            if (playedVideoData != null && !playedVideoData.isEmpty()) {
                for (int j = 0; j < playedVideoData.size(); j++) {
                    if (playedVideoData.get(j).videoCourseId == videoData.moduleId
                            && playedVideoData.get(j).videoId == videoData.courseVideosId) {
                        binding.courseVideoPlayer.getPlayer().seekTo(playedVideoData.get(j).videoPlayedPosition);
                        break;
                    }
                }
            }



            binding.courseVideoPlayer.setExoPlayerCallBack(new ExoPlayerCallBack() {
                @Override
                public void onError() {

                }

                @Override
                public void videoStatus(String stateString) {
                    switch (stateString) {
                        case "ExoPlayer.STATE_ENDED":
                            isMediaEnded = true;
                            if (playedVideoData != null && !playedVideoData.isEmpty()) {
//                                Log.d("videoPlayer", " => " + playedVideoData.size());
                                for (int j = 0; j < playedVideoData.size(); j++) {
                                    if (playedVideoData.get(j).videoCourseId == playedVideoData1.videoCourseId
                                            && playedVideoData.get(j).videoId == playedVideoData1.videoId) {
                                        playedVideoData.remove(j);
//                                        Log.d("videoPlayer", " if => " + playedVideoData.size());
                                        SharedPrefsUtil.clearSharedPreferencesByKey(StudentCourseVideoViewActivity.this, Constant.VIDEO_DATA);
                                        SharedPrefsUtil.setVideoDataPreferences(StudentCourseVideoViewActivity.this, playedVideoData);
//                                        Log.d("videoPlayer", " removed => " + playedVideoData.size());
                                        break;
                                    }
                                }
                            }
                            break;
                    }
                }
            });

        }
    }

    BroadcastReceiver screenOrientationEnterFull = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.layoutHeader.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            binding.layoutHeader.setLayoutParams(params);
            binding.layoutVideoData.setVisibility(View.GONE);
        }
    };

    BroadcastReceiver screenOrientationExitFull = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, getResources().getDisplayMetrics());

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.layoutHeader.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = height;
            binding.layoutHeader.setLayoutParams(params);
            binding.layoutVideoData.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(StudentCourseVideoViewActivity.this).unregisterReceiver(screenOrientationEnterFull);
        LocalBroadcastManager.getInstance(StudentCourseVideoViewActivity.this).unregisterReceiver(screenOrientationExitFull);

        super.onDestroy();
    }

 /*   private void showCartDialog() {
        new MaterialAlertDialogBuilder(StudentCourseVideoViewActivity.this)
                .setIcon(R.drawable.gayaak_icon)
                .setTitle("Course added to cart")
                .setMessage(getString(R.string.course_added_cart))
                .setPositiveButton("Go to Cart", (dialogInterface, i) -> {
                    if (!isMediaEnded) {
                        saveVideoData();
                    }
                    Intent intent = new Intent(StudentCourseVideoViewActivity.this, StudentHomeActivity.class);
                    intent.putExtra(Constant.OPEN_CART, "open_cart");
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);

                *//*    Intent intent = new Intent(StudentCourseVideoViewActivity.this, OpenPayPalActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);*//*
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }*/

    @Override
    public void onBackPressed() {
        if (!isMediaEnded) {
//            Log.d("positionVideo", "=> " + binding.courseVideoPlayer.getPlayer().getCurrentPosition());
            saveVideoData();
        }
        super.onBackPressed();
    }

    private void saveVideoDataOnClick(int lastPosition, VideoData.PlayedVideoData playedVideoData1, long currentPosition) {
        if (playedVideoData != null && !playedVideoData.isEmpty()) {
            for (int i = 0; i < playedVideoData.size(); i++) {
                if (playedVideoData.get(i).videoId == playedVideoData1.videoId &&
                        playedVideoData.get(i).videoCourseId == playedVideoData1.videoCourseId) {
                    playedVideoData.get(i).videoPlayedPosition = currentPosition;
                    break;
                }else {
                    if (i == playedVideoData.size() -1){
                        playedVideoData1.videoPlayedPosition = currentPosition;
                        playedVideoData.add(playedVideoData1);
                    }
                }
            }
        } else {
            playedVideoData = new ArrayList<>();
            playedVideoData1.videoPlayedPosition = currentPosition;
            playedVideoData.add(playedVideoData1);
        }
        SharedPrefsUtil.setVideoDataPreferences(StudentCourseVideoViewActivity.this, playedVideoData);
    }

    private void saveVideoData() {
        if (playedVideoData != null && !playedVideoData.isEmpty()) {
            for (int i = 0; i < playedVideoData.size(); i++) {
                if (playedVideoData.get(i).videoId == playedVideoData1.videoId &&
                        playedVideoData.get(i).videoCourseId == playedVideoData1.videoCourseId) {
                    playedVideoData.get(i).videoPlayedPosition = binding.courseVideoPlayer.getPlayer().getCurrentPosition();
                    break;
                } else {
                    if (i == playedVideoData.size() -1){
                        playedVideoData1.videoPlayedPosition = binding.courseVideoPlayer.getPlayer().getCurrentPosition();
                        playedVideoData.add(playedVideoData1);
                    }
                }
            }
        } else {
            playedVideoData = new ArrayList<>();
            playedVideoData1.videoPlayedPosition = binding.courseVideoPlayer.getPlayer().getCurrentPosition();
            playedVideoData.add(playedVideoData1);
        }
        SharedPrefsUtil.setVideoDataPreferences(StudentCourseVideoViewActivity.this, playedVideoData);
    }

}
