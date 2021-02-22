package com.example.gayaak_10.tutor.activity;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gayaak_10.R;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentTutorCourseVideoViewBinding;
import com.example.gayaak_10.model.response.VideoData;
import com.example.gayaak_10.student.model.ModuleWithVideo;
import com.example.gayaak_10.student.model.ModuleWithVideoDetail;
import com.example.gayaak_10.student.model.VideosModuleDataContractList;
import com.example.gayaak_10.student.viewmodel.StudentViewModel;
import com.example.gayaak_10.tutor.adapter.TutorCourseVideoListAdapter;
import com.example.gayaak_10.utility.SharedPrefsUtil;
import com.example.gayaak_10.videoplayer.playerinterface.ExoPlayerCallBack;

import java.util.ArrayList;

public class TutorCourseVideoViewActivity extends AppCompatActivity implements View.OnClickListener {

    private FragmentTutorCourseVideoViewBinding binding;
    private ModuleWithVideoDetail selectedCourse;
    private TutorCourseVideoListAdapter courseVideoListAdapter;
    private ArrayList<VideoData.PlayedVideoData> playedVideoData = new ArrayList<>();
    private VideoData.PlayedVideoData playedVideoData1 = new VideoData.PlayedVideoData();
    private boolean isMediaEnded = false;
    private int lastPosition = -1;
    private StudentViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentTutorCourseVideoViewBinding.inflate(getLayoutInflater());
        viewModel = ViewModelProviders.of(TutorCourseVideoViewActivity.this).get(StudentViewModel.class);
        setContentView(binding.getRoot());

        Integer selectedCourseId = getIntent().getIntExtra("moduleId", 0);
        getCourseVideos(selectedCourseId);


        playedVideoData = new ArrayList<>();
        playedVideoData = SharedPrefsUtil.getVideoDataPreferences(TutorCourseVideoViewActivity.this, Constant.VIDEO_DATA);


        binding.ivBackList.setOnClickListener(view -> {
            if (!isMediaEnded) {
                saveVideoData();
            }
            finish();
            overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
        });

        binding.layoutPracticeSong.setOnClickListener(this);
        LocalBroadcastManager.getInstance(TutorCourseVideoViewActivity.this).registerReceiver(screenOrientationEnterFull, new IntentFilter("screenOrientationEnterFull"));
        LocalBroadcastManager.getInstance(TutorCourseVideoViewActivity.this).registerReceiver(screenOrientationExitFull, new IntentFilter("screenOrientationExitFull"));
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

    private void getCourseVideos(Integer selectedCourseId) {
        binding.progressBarVideo.setVisibility(View.VISIBLE);
        viewModel.getModuleVideo(selectedCourseId).observe(TutorCourseVideoViewActivity.this, new Observer<ModuleWithVideo>() {
            @Override
            public void onChanged(ModuleWithVideo moduleWithVideo) {
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
        binding.rvVideoList.setLayoutManager(new LinearLayoutManager(TutorCourseVideoViewActivity.this, LinearLayoutManager.VERTICAL, false));
        courseVideoListAdapter = new TutorCourseVideoListAdapter(TutorCourseVideoViewActivity.this, videosModuleDataContractList, new TutorCourseVideoListAdapter.OnItemClickListener() {
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
            }
        });
        binding.rvVideoList.setAdapter(courseVideoListAdapter);
        checkVideos(videosModuleDataContractList);
    }

    private void checkVideos(ArrayList<VideosModuleDataContractList> allCoursesDetail) {
        for (int i = 0; i < allCoursesDetail.size(); i++) {
                lastPosition = i;
                courseVideoListAdapter.selectedPosition = i;
                courseVideoListAdapter.notifyDataSetChanged();
                setVideoPlayer(allCoursesDetail.get(i));
                break;
        }
    }

    private void setVideoPlayer(VideosModuleDataContractList videoData) {
        binding.progressBarVideo.setVisibility(View.GONE);
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
                                Log.d("videoPlayer", " => " + playedVideoData.size());
                                for (int j = 0; j < playedVideoData.size(); j++) {
                                    if (playedVideoData.get(j).videoCourseId == playedVideoData1.videoCourseId
                                            && playedVideoData.get(j).videoId == playedVideoData1.videoId) {
                                        playedVideoData.remove(j);
                                        Log.d("videoPlayer", " if => " + playedVideoData.size());
                                        SharedPrefsUtil.clearSharedPreferencesByKey(TutorCourseVideoViewActivity.this, Constant.VIDEO_DATA);
                                        SharedPrefsUtil.setVideoDataPreferences(TutorCourseVideoViewActivity.this, playedVideoData);
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
        LocalBroadcastManager.getInstance(TutorCourseVideoViewActivity.this).unregisterReceiver(screenOrientationEnterFull);
        LocalBroadcastManager.getInstance(TutorCourseVideoViewActivity.this).unregisterReceiver(screenOrientationExitFull);

        super.onDestroy();
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
        SharedPrefsUtil.setVideoDataPreferences(TutorCourseVideoViewActivity.this, playedVideoData);
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
        SharedPrefsUtil.setVideoDataPreferences(TutorCourseVideoViewActivity.this, playedVideoData);
    }

}
