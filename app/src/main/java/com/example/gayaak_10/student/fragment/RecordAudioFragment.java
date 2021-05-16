package com.example.gayaak_10.student.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.gayaak_10.R;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentRecordAudioBinding;
import com.example.gayaak_10.model.PractiseSessionData;
import com.example.gayaak_10.model.response.DefaultResponse;
import com.example.gayaak_10.model.response.ErrorPojoClass;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.services.RecordingService;
import com.example.gayaak_10.student.activity.RecordedFilesActivity;
import com.example.gayaak_10.utility.DateTimeUtility;
import com.example.gayaak_10.utility.Utility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecordAudioFragment extends Fragment implements View.OnClickListener {

    private FragmentRecordAudioBinding binding;
    private String[] songs = {"A", "A Sharp", "B", "C", "C Sharp"/*, "D", "D Sharp", "E", "F", "F Sharp", "G", "G Sharp"*/};
    private String item, item1;
    private int position = 0;
    private String instrumentType = "tanpura";
    private String selectedKey = "";
    private boolean mStartRecording = true;
    private boolean mPauseRecording = true;
    private int mRecordPromptCount = 0;
    long timeWhenPaused = 0; //stores time when user clicks pause button

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRecordAudioBinding.inflate(inflater);
        selectedKey = songs[position];
        binding.songsListSpinner.setText(songs[position]);
        binding.btnPlay.setOnClickListener(this);
        binding.btnStop.setOnClickListener(this);
        binding.ivSpinnerDown.setOnClickListener(this);
        binding.ivSpinnerUp.setOnClickListener(this);
        binding.tanpuraLl.setOnClickListener(this);
        binding.linearMetronome.setOnClickListener(this);

        binding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                binding.seekbarText.setText("" + seekBar.getProgress() + "BPM");
            }
        });

        binding.btnRecord.setOnClickListener(this);
        binding.btnOpenSavedFiles.setOnClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOpenSavedFiles:
                startActivity(new Intent(getActivity(), RecordedFilesActivity.class));
                getActivity().overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
                break;
            case R.id.btnRecord:
                if (!Utility.hasPermissions(getActivity(), Utility.PERMISSIONS)) {
                    ActivityCompat.requestPermissions(getActivity(), Utility.PERMISSIONS, Utility.PERMISSION_ALL);
                } else {
                    onRecord(mStartRecording);
                    mStartRecording = !mStartRecording;
                }
                break;


            case R.id.btnPlay:
                binding.btnStop.setVisibility(View.VISIBLE);
                binding.btnPlay.setVisibility(View.GONE);
                if (instrumentType.equalsIgnoreCase("tanpura")) {
                    Intent intent = new Intent(getActivity(), InstrumentService.class);
                    intent.putExtra("selectedSong", selectedKey);
                    getActivity().startService(intent);
                }
                break;

            case R.id.btnStop:
                binding.btnStop.setVisibility(View.GONE);
                binding.btnPlay.setVisibility(View.VISIBLE);
                getActivity().stopService(new Intent(getActivity(), InstrumentService.class));
                break;

            case R.id.ivSpinnerUp:
                getActivity().stopService(new Intent(getActivity(), InstrumentService.class));
                binding.btnStop.setVisibility(View.GONE);
                binding.btnPlay.setVisibility(View.VISIBLE);
                if (position <= (songs.length - 2)) {
                    position++;
                }
                selectedKey = songs[position];
                binding.songsListSpinner.setText(songs[position]);
                break;

            case R.id.ivSpinnerDown:
                getActivity().stopService(new Intent(getActivity(), InstrumentService.class));
                binding.btnStop.setVisibility(View.GONE);
                binding.btnPlay.setVisibility(View.VISIBLE);
                if (position != 0) {
                    position--;
                }
                selectedKey = songs[position];
                binding.songsListSpinner.setText(songs[position]);
                break;

            case R.id.tanpura_ll:
                instrumentType = "tanpura";
                binding.tvAudioType.setText(R.string.choose_path);
                binding.metronomeLl.setVisibility(View.GONE);
                binding.tanpuraLl.setBackgroundResource(R.drawable.linear_background);
                binding.linearMetronome.setBackgroundResource(R.drawable.linear_background_2);
                break;

            case R.id.linear_metronome:
                instrumentType = "metronome";
                binding.tvAudioType.setText(R.string.choose_signature);
                binding.linearMetronome.setBackgroundResource(R.drawable.linear_background);
                binding.tanpuraLl.setBackgroundResource(R.drawable.linear_background_2);
                binding.metronomeLl.setVisibility(View.VISIBLE);
                break;
        }
    }

    public static class InstrumentService extends Service {

        MediaPlayer player;
        private String value = "";

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {

            if (intent != null && intent.getExtras() != null) {
                value = intent.getExtras().getString("selectedSong");
            }
            Log.d("mp3", " ---> " + value);
            String file = "";
           /* switch (value){
                case "A":
                    file = "tanpura_a.mp3";
                    break;
                case "A Sharp":
                    file = "tanpura_asharp.mp3";
                    break;
                case "B":
                    file = "tanpura_b.mp3";
                    break;
                case "C":
                    file = "tanpura_c.mp3";
                    break;
                case "C Sharp":
                    file = "tanpura_csharp.mp3";
                    break;
                case "D":
                    file = "tanpura_d.mp3";
                    break;
                case "D Sharp":
                    file = "tanpura_dsharp.mp3";
                    break;
                case "E":
                    file = "tanpura_e.mp3";
                    break;
                case "F":
                    file = "tanpura_f.mp3";
                    break;
                case "F Sharp":
                    file = "tanpura_fsharp.mp3";
                    break;
                case "G":
                    file = "tanpura_g.mp3";
                    break;
                case "G Sharp":
                    file = "tanpura_gsharp.mp3";
                    break;
            }*/

            AssetFileDescriptor afd = null;
            try {
                afd = getAssets().openFd("tanpura_csharp.mp3");
                player = new MediaPlayer();
                player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                player.prepare();
                Log.d("Mp3", "On start -> " + file);
                player.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return START_STICKY;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            //stopping the player when service is destroyed
            player.stop();
        }
    }

    @Override
    public void onDestroyView() {
        Log.d("Mp3", "On onDestroyView");
        getActivity().stopService(new Intent(getActivity(), InstrumentService.class));
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d("Mp3", "On onDestroy");
        getActivity().stopService(new Intent(getActivity(), InstrumentService.class));
        super.onDestroy();
    }

    /*-----------RECORDING----------------*/
    // Recording Start/Stop
    //TODO: recording pause
    private void onRecord(boolean start) {

        Intent intent = new Intent(getActivity(), RecordingService.class);

        if (start) {
            // start recording
            binding.btnRecord.setImageResource(R.drawable.ic_stop);
            //mPauseButton.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), R.string.toast_recording_start, Toast.LENGTH_SHORT).show();
            File folder = new File(Environment.getExternalStorageDirectory() + "/Gayaak");
            if (!folder.exists()) {
                //folder /SoundRecorder doesn't exist, create the folder
                folder.mkdir();
            }

            //start Chronometer
            binding.chronometer.setBase(SystemClock.elapsedRealtime());
            binding.chronometer.start();
            binding.chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    if (mRecordPromptCount == 0) {
                        binding.tvRecordingPrompt.setText(getString(R.string.record_in_progress) + ".");
                    } else if (mRecordPromptCount == 1) {
                        binding.tvRecordingPrompt.setText(getString(R.string.record_in_progress) + "..");
                    } else if (mRecordPromptCount == 2) {
                        binding.tvRecordingPrompt.setText(getString(R.string.record_in_progress) + "...");
                        mRecordPromptCount = -1;
                    }

                    mRecordPromptCount++;
                }
            });

            //start RecordingService
            getActivity().startService(intent);
            //keep screen on while recording
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            binding.tvRecordingPrompt.setText(getString(R.string.record_in_progress) + ".");
            mRecordPromptCount++;

        } else {
            //stop recording
            binding.btnRecord.setImageResource(R.drawable.ic_mic);
            //mPauseButton.setVisibility(View.GONE);
            binding.chronometer.stop();
            binding.chronometer.setBase(SystemClock.elapsedRealtime());
            timeWhenPaused = 0;
            binding.tvRecordingPrompt.setText(getString(R.string.record_prompt));

            getActivity().stopService(intent);
            //allow the screen to turn off again once recording is finished
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            binding.btnStop.setVisibility(View.GONE);
            binding.btnPlay.setVisibility(View.VISIBLE);
            getActivity().stopService(new Intent(getActivity(), InstrumentService.class));

            //save the session
            saveSessionPopup();
        }
    }

    private void saveSessionPopup() {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.save_audio_custom_dialog, null);

        final AppCompatEditText editText = dialogView.findViewById(R.id.edt_comment);
        AppCompatButton button1 = dialogView.findViewById(R.id.buttonSubmit);
        AppCompatButton button2 = dialogView.findViewById(R.id.buttonCancel);

        button2.setOnClickListener(view -> {
            dialogBuilder.dismiss();
        });

        button1.setOnClickListener(view -> {
            if (editText.getText().toString().isEmpty()) {
                editText.setError("Enter file name.");
                return;
            }
            Utility.hideSoftKeyboard(getActivity(), view);
            saveSessionOnServer(editText.getText().toString());
            binding.progressBarUploading.setVisibility(View.VISIBLE);
            dialogBuilder.dismiss();
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    private void saveSessionOnServer(String comment) {
        PractiseSessionData practiseSessionData = new PractiseSessionData();
        practiseSessionData.PractiseSessionId = 0;
        practiseSessionData.UserId = App.userDataContract.detail.userId;
        practiseSessionData.CourseId = 5;
        practiseSessionData.Comment = comment;
        practiseSessionData.AudioFileName = comment;
        practiseSessionData.AudioFileFormat = ".mp3";
        practiseSessionData.IsActive = true;
        practiseSessionData.CreatedDate = DateTimeUtility.currentDateTime();
        practiseSessionData.ModifiedDate = DateTimeUtility.currentDateTime();
        practiseSessionData.ModifiedBy = null;
        practiseSessionData.CreatedBy = App.userDataContract.detail.userId;
        practiseSessionData.LoginUserId = App.userDataContract.detail.userId;

        File file = new File(RecordingService.mFilePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Call<DefaultResponse> call = Constant.retrofitServiceHeader.savePracticeSession(body, practiseSessionData);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                Log.d("upload", " => " + response);
                binding.progressBarUploading.setVisibility(View.GONE);
                if (!response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    ErrorPojoClass mError = new ErrorPojoClass();
                    try {
                        mError = gson.fromJson(response.errorBody().string(), ErrorPojoClass.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Utility.customDialogBoxTextWithSingle(getActivity(), "Something went wrong.", mError.message);
                }
                binding.progressBarUploading.setVisibility(View.GONE);
                Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                binding.progressBarUploading.setVisibility(View.GONE);
                Log.d("upload", "onFailure => " + t.getLocalizedMessage());
            }
        });
    }

    /*"FORM MultiPart
Key >>  PractiseSessionDataContract
Value >> {
"PractiseSessionId":0,
"UserId": 1,
"CourseId": 4,
"Comment": "test comment",
"AudioFileName": "Heer.mp3",
"AudioFileFormat": ".mp3",
"IsActive": true,
"CreatedDate": null,
"ModifiedDate": null,
"CreatedBy": 1,
"ModifiedBy": null,
"LoginUserId": 1
}

Key >> (FileType)
Value >> Post (MultipartFileData)"*/
}

