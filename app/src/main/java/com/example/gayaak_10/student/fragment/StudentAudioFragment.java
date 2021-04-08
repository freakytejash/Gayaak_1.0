package com.example.gayaak_10.student.fragment;

import android.app.DownloadManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.gayaak_10.R;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentEbookBinding;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.activity.StudentHomeActivity;
import com.example.gayaak_10.student.adapter.StudentAudioAdapter;
import com.example.gayaak_10.student.model.AudioModuleDataContractList;
import com.example.gayaak_10.student.viewmodel.StudentViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

//https://www.journaldev.com/22203/android-media-player-song-with-seekbar
public class StudentAudioFragment extends Fragment implements View.OnClickListener {

    private FragmentEbookBinding binding;
    private Integer moduleId;
    private StudentViewModel viewModel;
    private MediaPlayer mMediaPlayer = null;
    private Handler mHandler = new Handler();
    private boolean isPlaying;
    private StudentAudioAdapter adapter;


    public StudentAudioFragment(Integer moduleId) {
        this.moduleId = moduleId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEbookBinding.inflate(getLayoutInflater());
        viewModel = ViewModelProviders.of(getActivity()).get(StudentViewModel.class);
        binding.tvHeaderBooks.setText("Audio");
        binding.ivModuleInfoBack.setOnClickListener(this);

        viewModel.getModuleAudio(moduleId, App.userDataContract.detail.userId).observe(getActivity(), audioModuleDataContractLists -> {
            if (audioModuleDataContractLists != null && !audioModuleDataContractLists.isEmpty()) {
                binding.layoutPdf.setVisibility(View.VISIBLE);
                binding.layoutEmptyLibrary.setVisibility(View.GONE);
                setAudioView(audioModuleDataContractLists);
            } else {
                binding.layoutPdf.setVisibility(View.GONE);
                binding.layoutEmptyLibrary.setVisibility(View.VISIBLE);
            }
        });
        return binding.getRoot();
    }

    private RelativeLayout seekbarAudioTemp;
    private AppCompatTextView ivPauseAudioTemp;
    private AppCompatTextView ivPlayAudioTemp;
    private  AppCompatSeekBar seekbarTemp;
    private AppCompatTextView currentProgressTextViewTemp;
    private AppCompatTextView fileLengthTextViewTemp;

    
    private void setAudioView(ArrayList<AudioModuleDataContractList> audioModuleDataContractLists) {
         adapter = new StudentAudioAdapter(getActivity(), audioModuleDataContractLists, new StudentAudioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onPlayAudio(String mp3URL, RelativeLayout seekbarAudio, AppCompatTextView ivPauseAudio, AppCompatTextView ivPlayAudio,
                                    AppCompatSeekBar seekbar, AppCompatTextView currentProgressTextView, AppCompatTextView fileLengthTextView, int position) {

                if (mMediaPlayer == null) {
                } else {
                    stopPlaying();
                }
                seekbarAudio.setVisibility(View.VISIBLE);
                ivPauseAudio.setVisibility(View.VISIBLE);
                ivPlayAudio.setVisibility(View.GONE);
                seekbarAudioTemp = seekbarAudio;
                ivPauseAudioTemp = ivPauseAudio;
                ivPlayAudioTemp = ivPlayAudio;
                seekbarTemp = seekbar;
                currentProgressTextViewTemp = currentProgressTextView;
                fileLengthTextViewTemp = fileLengthTextView;
                
                onPlay(isPlaying, mp3URL);
                isPlaying = !isPlaying;
            }

             @Override
             public void onStop() {
                 stopPlaying();
             }

             @Override
             public void onDownloadMp3(int position) {
                 Toast.makeText(getActivity(), "Downloading", Toast.LENGTH_SHORT).show();
                 DownloadFiles(audioModuleDataContractLists.get(position).Mp3URL,audioModuleDataContractLists.get(position).name);
             }
         });


        binding.rvEbooks.setAdapter(adapter);
    }

    private void onPlay(boolean isPlaying, String mp3URL) {
        if (!isPlaying) {
            //currently MediaPlayer is not playing audio
            if (mMediaPlayer == null) {
                startPlaying(mp3URL); //start from beginning
            } else {
                //resumePlaying(); //resume the currently paused MediaPlayer
            }

        } else {
            //pause the MediaPlayer
            //pausePlaying();
        }
    }

    private void stopPlaying() {
        if (mMediaPlayer != null || mMediaPlayer.isPlaying()) {
            mHandler.removeCallbacks(mRunnable);
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;

            seekbarTemp.setProgress(seekbarTemp.getMax());
            isPlaying = !isPlaying;

            currentProgressTextViewTemp.setText(fileLengthTextViewTemp.getText());
            seekbarTemp.setProgress(seekbarTemp.getMax());

            //allow the screen to turn off again once audio is finished playing
            // getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            adapter.notifyDataSetChanged();
        }
    }

    private void startPlaying(String mp3URL) {
        //   ivPlayAudioTemp.setImageResource(R.drawable.ic_pause);
        mMediaPlayer = new MediaPlayer();

        try {
            mMediaPlayer.setDataSource(mp3URL);
            mMediaPlayer.prepare();
            seekbarTemp.setMax(mMediaPlayer.getDuration());

            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });
        } catch (IOException e) {
            Log.e("audio", "prepare() failed");
        }

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlaying();
                ivPlayAudioTemp.setVisibility(View.VISIBLE);
                ivPauseAudioTemp.setVisibility(View.GONE);
                seekbarTemp.setVisibility(View.GONE);
            }
        });

        updateSeekBar();

        //keep screen on while playing audio
        // getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    //updating mSeekBar
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mMediaPlayer != null) {

                int mCurrentPosition = mMediaPlayer.getCurrentPosition();
                seekbarTemp.setProgress(mCurrentPosition);

                long minutes = TimeUnit.MILLISECONDS.toMinutes(mCurrentPosition);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(mCurrentPosition)
                        - TimeUnit.MINUTES.toSeconds(minutes);
                currentProgressTextViewTemp.setText(String.format("%02d:%02d", minutes, seconds));

                updateSeekBar();
            }
        }
    };

    private void updateSeekBar() {
        mHandler.postDelayed(mRunnable, 1000);
    }


    private void prepareMediaPlayerFromPoint(int progress, String mp3URL) {
        //set mediaPlayer to start from middle of the audio file
        mMediaPlayer = new MediaPlayer();

        try {
            mMediaPlayer.setDataSource(mp3URL);
            mMediaPlayer.prepare();
            seekbarTemp.setMax(mMediaPlayer.getDuration());
            mMediaPlayer.seekTo(progress);

            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlaying();
                }
            });

        } catch (IOException e) {
            Log.e("audio", "prepare() failed");
        }

        // keep screen on while playing audio
        // mContext.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivModuleInfoBack:
                StudentHomeActivity.addFragment(new StudentCoursesCatalogFragment(), Constant.COURSE_CATALOG, getActivity());
                break;
        }
    }

    @Override
    public void onDestroy() {
        if (mMediaPlayer == null) {
        } else {
            stopPlaying();
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        if (mMediaPlayer == null) {
        } else {
            stopPlaying();
        }
        super.onPause();
    }

    @Override
    public void onDetach() {
        if (mMediaPlayer == null) {
        } else {
            stopPlaying();
        }
        super.onDetach();
    }

    public void DownloadFiles(String url, String name) {
        try {
            DownloadManager downloadmanager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(url);

            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle(name);
            request.setDescription("Downloading..");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name);
            downloadmanager.enqueue(request);

        } catch (SecurityException se) {
            Log.e("SYNC getUpdate", "security error", se);
        }
    }
}
