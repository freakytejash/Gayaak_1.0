package com.example.gayaak_10.student.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gayaak_10.student.adapter.ExpertFeedbackAdapter;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentExpertFeedbackBinding;
import com.example.gayaak_10.model.response.ErrorPojoClass;
import com.example.gayaak_10.model.response.PractiseSessionInfo;
import com.example.gayaak_10.model.response.PractiseSessionInfoDetail;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.adapter.ExpertFeedbackAdapter;
import com.example.gayaak_10.utility.Utility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpertFeedbackFragment extends Fragment {
    private FragmentExpertFeedbackBinding binding;
    private ExpertFeedbackAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExpertFeedbackBinding.inflate(inflater);
        getFeedback();
        return binding.getRoot();
    }

    private void getFeedback() {
        Call<PractiseSessionInfo> practiseSessionInfoCall = Constant.retrofitServiceHeader.getPracticeSessionByUserId(App.userDataContract.detail.userId);
        practiseSessionInfoCall.enqueue(new Callback<PractiseSessionInfo>() {
            @Override
            public void onResponse(Call<PractiseSessionInfo> call, Response<PractiseSessionInfo> response) {
                if (response.isSuccessful()){
                    if (response.body().status){
                        if (response.body().detail != null && !response.body().detail.isEmpty()){
                            binding.layoutEmptyFeedback.setVisibility(View.GONE);
                            binding.rvFeedback.setVisibility(View.VISIBLE);
                            setFeedback(response.body().detail);
                        }else {
                            binding.layoutEmptyFeedback.setVisibility(View.VISIBLE);
                            binding.rvFeedback.setVisibility(View.GONE);
                        }
                    }
                }else {
                    binding.layoutEmptyFeedback.setVisibility(View.VISIBLE);
                    binding.rvFeedback.setVisibility(View.GONE);
                    Gson gson = new GsonBuilder().create();
                    ErrorPojoClass mError = new ErrorPojoClass();
                    try {
                        mError = gson.fromJson(response.errorBody().string(), ErrorPojoClass.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Utility.customDialogBoxTextWithSingle(getActivity(), "Something went wrong." ,mError.message );
                }
            }

            @Override
            public void onFailure(Call<PractiseSessionInfo> call, Throwable t) {
                binding.layoutEmptyFeedback.setVisibility(View.VISIBLE);
                binding.rvFeedback.setVisibility(View.GONE);
            }
        });
    }

    private void setFeedback(ArrayList<PractiseSessionInfoDetail> detail) {
        binding.rvFeedback.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new ExpertFeedbackAdapter(detail, getActivity(), position -> {
            try {
                SessionPlaybackFragment sessionPlaybackFragment = new SessionPlaybackFragment().newInstance(detail.get(position));
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                sessionPlaybackFragment.show(transaction, "dialog_playback");

            } catch (Exception e) {

            }
        });
        binding.rvFeedback.setAdapter(adapter);
    }
}
