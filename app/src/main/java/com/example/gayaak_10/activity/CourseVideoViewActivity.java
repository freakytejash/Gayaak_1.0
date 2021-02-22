package com.example.gayaak_10.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gayaak_10.R;
import com.example.gayaak_10.adapter.CourseVideoListAdapter;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentCourseVideoViewBinding;
import com.example.gayaak_10.model.response.AllCoursesDetail;
import com.example.gayaak_10.model.response.CoursesDetail;
import com.example.gayaak_10.model.response.ErrorPojoClass;
import com.example.gayaak_10.model.response.VideoData;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.model.BuyCoursesDetail;
import com.example.gayaak_10.utility.SharedPrefsUtil;
import com.example.gayaak_10.utility.Utility;
import com.example.gayaak_10.videoplayer.playerinterface.ExoPlayerCallBack;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseVideoViewActivity extends AppCompatActivity implements View.OnClickListener {

    private FragmentCourseVideoViewBinding binding;
    private CoursesDetail selectedCourse;
    private CourseVideoListAdapter courseVideoListAdapter;
    private ArrayList<VideoData.PlayedVideoData> playedVideoData = new ArrayList<>();
    private VideoData.PlayedVideoData playedVideoData1 = new VideoData.PlayedVideoData();
    private boolean isMediaEnded = false;
    private String getFromLocation = "" ;
    private int lastPosition = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentCourseVideoViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getFromLocation = getIntent().getStringExtra("isFromLocation");
        Integer selectedCourseId = getIntent().getIntExtra("courseId", 0);
        getCourseVideos(selectedCourseId);


        playedVideoData = new ArrayList<>();
        playedVideoData = SharedPrefsUtil.getVideoDataPreferences(CourseVideoViewActivity.this, Constant.VIDEO_DATA);


        binding.ivBackList.setOnClickListener(view -> {
            if (!isMediaEnded) {
                saveVideoData();
            }
            finish();
            overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
        });

        binding.layoutLockedVideo.setOnClickListener(view -> {
            buyPaidCourse();
        });

        if (App.coursesDetailArrayList != null && !App.coursesDetailArrayList.isEmpty()) {
            for (int i = 0; i < App.coursesDetailArrayList.size(); i++) {
                if (App.coursesDetailArrayList.get(i).courseId.equals(selectedCourseId)) {
                   // binding.btnJoinCourse.setVisibility(View.INVISIBLE);
                    break;
                }
            }
        }

      /*  binding.btnJoinCourse.setOnClickListener(view -> {
            addCourseToCart();

        });

        binding.layoutPracticeSong.setOnClickListener(this);*/
        LocalBroadcastManager.getInstance(CourseVideoViewActivity.this).registerReceiver(screenOrientationEnterFull, new IntentFilter("screenOrientationEnterFull"));
        LocalBroadcastManager.getInstance(CourseVideoViewActivity.this).registerReceiver(screenOrientationExitFull, new IntentFilter("screenOrientationExitFull"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layoutPracticeSong:
                /*LocalBroadcastManager.getInstance(CourseVideoViewActivity.this).sendBroadcast(new Intent("openPracticeRoom"));
                finish();
                overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);*/
                break;
        }
    }

    private void addCourseToCart() {
        BuyCoursesDetail buyCoursesDetail = new BuyCoursesDetail();
        buyCoursesDetail.courseId = selectedCourse.courseId;
        buyCoursesDetail.detail = selectedCourse.detail;
        buyCoursesDetail.name = selectedCourse.name;
        buyCoursesDetail.price = selectedCourse.price;
        buyCoursesDetail.ThumbnailImage = selectedCourse.ThumbnailImage;
        buyCoursesDetail.levelName = selectedCourse.levelName;
        if (App.cartList.size() <= 0) {
            App.cartList.add(buyCoursesDetail);
            SharedPrefsUtil.setCartCourse(CourseVideoViewActivity.this, App.cartList);
            showCartDialog();
        } else {
            Log.d("courseId", "courseId => " + buyCoursesDetail.courseId);
            for (int j = 0; j < App.cartList.size(); j++) {
                if (App.cartList.get(j).courseId.equals(buyCoursesDetail.courseId)) {
                    Toast.makeText(CourseVideoViewActivity.this, R.string.course_already, Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    if (j == App.cartList.size() - 1) {
                        App.cartList.add(buyCoursesDetail);
                        SharedPrefsUtil.setCartCourse(CourseVideoViewActivity.this, App.cartList);
                        showCartDialog();
                        break;
                    }
                }
            }
        }
    }

    private void buyPaidCourse() {
        new MaterialAlertDialogBuilder(CourseVideoViewActivity.this)
                .setIcon(R.drawable.gayaak_icon)
                .setTitle("Subscribe Course")
                .setMessage("Do you want to subscribe this course?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    addCourseToCart();
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    private void getCourseVideos(Integer selectedCourseId) {
        binding.progressBarVideo.setVisibility(View.VISIBLE);
        Call<AllCoursesDetail> getCourseVideo = Constant.retrofitServiceHeader.getCourseVideos(selectedCourseId);
        getCourseVideo.enqueue(new Callback<AllCoursesDetail>() {
            @Override
            public void onResponse(Call<AllCoursesDetail> call, Response<AllCoursesDetail> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().status) {
                            if (response.body().detail.CourseVideosDataContractList != null && !response.body().detail.CourseVideosDataContractList.isEmpty()) {
                                selectedCourse = response.body().detail;
                                binding.layoutVideoList.setVisibility(View.VISIBLE);
                                binding.layoutEmptyVideoList.setVisibility(View.GONE);
                                binding.tvVideoTitle.setText(response.body().detail.name);
                                if (response.body().detail.detail != null && !response.body().detail.detail.isEmpty()) {
                                    binding.tvVideoDescription.setVisibility(View.VISIBLE);
                                    binding.tvVideoDescription.setText(response.body().detail.detail);
                                } else {
                                    binding.tvVideoDescription.setVisibility(View.GONE);
                                }
                              //  setVideoList(response.body().detail.CourseVideosDataContractList);
                            } else {
                                binding.progressBarVideo.setVisibility(View.GONE);
                                binding.layoutVideoList.setVisibility(View.GONE);
                                binding.layoutEmptyVideoList.setVisibility(View.VISIBLE);
                            }
                        } else {
                            binding.progressBarVideo.setVisibility(View.GONE);
                        }
                    }
                } else {
                    Gson gson = new GsonBuilder().create();
                    ErrorPojoClass mError = new ErrorPojoClass();
                    try {
                        mError = gson.fromJson(response.errorBody().string(), ErrorPojoClass.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Utility.customDialogBoxTextWithSingle(CourseVideoViewActivity.this, "Something went wrong.", mError.message);
                }

            }

            @Override
            public void onFailure(Call<AllCoursesDetail> call, Throwable t) {
                binding.progressBarVideo.setVisibility(View.GONE);
                binding.layoutVideoList.setVisibility(View.GONE);
                binding.layoutEmptyVideoList.setVisibility(View.VISIBLE);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setVideoList(final ArrayList<VideoData> allCoursesDetail) {
        if (playedVideoData != null && !playedVideoData.isEmpty()){
            for (int i = 0; i < allCoursesDetail.size(); i++) {
                for (int j = 0; j < playedVideoData.size(); j++) {
                    if (allCoursesDetail.get(i).CourseId == playedVideoData.get(j).videoCourseId){
                        if (allCoursesDetail.get(i).CourseVideosId == playedVideoData.get(j).videoId){
                            allCoursesDetail.get(i).playedVideoDataArrayList.videoId = playedVideoData.get(j).videoId;
                            allCoursesDetail.get(i).playedVideoDataArrayList.videoCourseId = playedVideoData.get(j).videoCourseId;
                            allCoursesDetail.get(i).playedVideoDataArrayList.videoPlayedPosition = playedVideoData.get(j).videoPlayedPosition;
                            break;
                        }
                    }
                }
            }
        }

        binding.progressBarVideo.setVisibility(View.GONE);
        binding.tvCourseCount.setText("Course Video 1 of " + allCoursesDetail.size());
        binding.rvVideoList.setLayoutManager(new LinearLayoutManager(CourseVideoViewActivity.this, LinearLayoutManager.VERTICAL, false));
        courseVideoListAdapter = new CourseVideoListAdapter(CourseVideoViewActivity.this, allCoursesDetail, new CourseVideoListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (!isMediaEnded) {
                    Log.d("positionVideo1", "=> " + binding.courseVideoPlayer.getPlayer().getCurrentPosition());
                    saveVideoDataOnClick(lastPosition, playedVideoData1, binding.courseVideoPlayer.getPlayer().getCurrentPosition());
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                binding.courseVideoPlayer.stopPlayer();
                setVideoPlayer(allCoursesDetail.get(position));
                courseVideoListAdapter.notifyDataSetChanged();


            }

            @Override
            public void paidVideoClick(int position) {
                buyPaidCourse();
            }
        });
        binding.rvVideoList.setAdapter(courseVideoListAdapter);
        checkVideos(allCoursesDetail);

    }

    private void checkVideos(ArrayList<VideoData> allCoursesDetail) {
        boolean isAnyVideoFree = false;
        for (int i = 0; i < allCoursesDetail.size(); i++) {
            if (allCoursesDetail.get(i).IsFreeVideo) {
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

    private void setVideoPlayer(VideoData videoData) {
        binding.progressBarVideo.setVisibility(View.GONE);
        binding.layoutLockedVideo.setVisibility(View.GONE);
        binding.courseVideoPlayer.setVisibility(View.VISIBLE);
        if (!videoData.VideoURL.isEmpty()) {
            playedVideoData1 = new VideoData.PlayedVideoData();
            playedVideoData1.videoId = videoData.CourseVideosId;
            playedVideoData1.videoCourseId = videoData.CourseId;

            binding.courseVideoPlayer.setSource(videoData.VideoURL);
            binding.courseVideoPlayer.setShowController(true);

            if (playedVideoData != null && !playedVideoData.isEmpty()) {
                for (int j = 0; j < playedVideoData.size(); j++) {
                    if (playedVideoData.get(j).videoCourseId == videoData.CourseId
                            && playedVideoData.get(j).videoId == videoData.CourseVideosId) {
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
                                Log.d("videoPlayer", " => " + playedVideoData.size());
                                for (int j = 0; j < playedVideoData.size(); j++) {
                                    if (playedVideoData.get(j).videoCourseId == playedVideoData1.videoCourseId
                                            && playedVideoData.get(j).videoId == playedVideoData1.videoId) {
                                        playedVideoData.remove(j);
                                        Log.d("videoPlayer", " if => " + playedVideoData.size());
                                        SharedPrefsUtil.clearSharedPreferencesByKey(CourseVideoViewActivity.this, Constant.VIDEO_DATA);
                                        SharedPrefsUtil.setVideoDataPreferences(CourseVideoViewActivity.this, playedVideoData);
                                        Log.d("videoPlayer", " removed => " + playedVideoData.size());
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
        LocalBroadcastManager.getInstance(CourseVideoViewActivity.this).unregisterReceiver(screenOrientationEnterFull);
        LocalBroadcastManager.getInstance(CourseVideoViewActivity.this).unregisterReceiver(screenOrientationExitFull);

        super.onDestroy();
    }

    private void showCartDialog() {
        new MaterialAlertDialogBuilder(CourseVideoViewActivity.this)
                .setIcon(R.drawable.gayaak_icon)
                .setTitle("Course added to cart")
                .setMessage(getString(R.string.course_added_cart))
                .setPositiveButton("Go to Cart", (dialogInterface, i) -> {
                    if (!isMediaEnded) {
                        saveVideoData();
                    }
                    Intent intent = new Intent(CourseVideoViewActivity.this, OpenPayPalActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    @Override
    public void onBackPressed() {
        if (!isMediaEnded) {
            Log.d("positionVideo", "=> " + binding.courseVideoPlayer.getPlayer().getCurrentPosition());
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
        SharedPrefsUtil.setVideoDataPreferences(CourseVideoViewActivity.this, playedVideoData);
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
        SharedPrefsUtil.setVideoDataPreferences(CourseVideoViewActivity.this, playedVideoData);
    }

}
