package com.example.gayaak_10.student.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gayaak_10.R;
import com.example.gayaak_10.databinding.FragmentStudentFeedbackBinding;
import com.example.gayaak_10.databinding.FragmentStudentRegularFeedbackBinding;
import com.example.gayaak_10.student.viewmodel.StudentViewModel;


public class StudentRegularFeedbackFragment extends Fragment {

    private FragmentStudentRegularFeedbackBinding binding;
    private StudentViewModel mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentStudentRegularFeedbackBinding.inflate(getLayoutInflater());
        mViewModel = ViewModelProviders.of(getActivity()).get(StudentViewModel.class);


        return binding.getRoot();
    }
}