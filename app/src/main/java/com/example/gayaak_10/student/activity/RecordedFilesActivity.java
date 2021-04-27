package com.example.gayaak_10.student.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gayaak_10.R;
import com.example.gayaak_10.adapter.SessionFilesAdapter;
import com.example.gayaak_10.constants.Constant;
//import com.example.gayaak_10.databinding.ActivityRecordedFilesBinng;
import com.example.gayaak_10.databinding.ActivityRecordedFilesBinding;

import com.example.gayaak_10.model.response.ErrorPojoClass;
import com.example.gayaak_10.model.response.PractiseSessionInfo;
import com.example.gayaak_10.model.response.PractiseSessionInfoDetail;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.services.DBHelper;
import com.example.gayaak_10.student.fragment.SessionPlaybackFragment;
import com.example.gayaak_10.utility.Utility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecordedFilesActivity extends AppCompatActivity {

    private ActivityRecordedFilesBinding binding;
    private DBHelper mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecordedFilesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivBackRecordings.setOnClickListener(view -> {
            finish();
            overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
        });

        getUploadedSessions();

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);

    }

    private void getUploadedSessions() {
        Call<PractiseSessionInfo> practiseSessionInfoCall = Constant.retrofitServiceHeader.getPracticeSessionByUserId(App.userDataContract.detail.userId);
        practiseSessionInfoCall.enqueue(new Callback<PractiseSessionInfo>() {
            @Override
            public void onResponse(Call<PractiseSessionInfo> call, Response<PractiseSessionInfo> response) {
                if (response.isSuccessful()){
                    if (response.body().status){
                        if (response.body().detail != null && !response.body().detail.isEmpty()){
                            binding.layoutUploadedFiles.setVisibility(View.VISIBLE);
                            binding.layoutEmptyRecording.setVisibility(View.GONE);
                            setSessionData(response.body().detail);
                        }else {
                            binding.layoutUploadedFiles.setVisibility(View.GONE);
                            binding.layoutEmptyRecording.setVisibility(View.VISIBLE);
                        }
                    }
                }else {
                    Gson gson = new GsonBuilder().create();
                    ErrorPojoClass mError = new ErrorPojoClass();
                    try {
                        mError = gson.fromJson(response.errorBody().string(), ErrorPojoClass.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Utility.customDialogBoxTextWithSingle(RecordedFilesActivity.this, "Something went wrong." ,mError.message );
                }
            }

            @Override
            public void onFailure(Call<PractiseSessionInfo> call, Throwable t) {

            }
        });
    }

    private void setSessionData(ArrayList<PractiseSessionInfoDetail> detail) {
        binding.rvServerFiles.setLayoutManager(new LinearLayoutManager(RecordedFilesActivity.this, LinearLayoutManager.VERTICAL, false));
        binding.rvServerFiles.setAdapter(new SessionFilesAdapter(detail, RecordedFilesActivity.this, new SessionFilesAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                try {
                    SessionPlaybackFragment sessionPlaybackFragment = new SessionPlaybackFragment().newInstance(detail.get(position));
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    sessionPlaybackFragment.show(transaction, "dialog_playback");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
