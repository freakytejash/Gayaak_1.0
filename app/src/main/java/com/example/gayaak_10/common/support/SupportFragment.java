package com.example.gayaak_10.common.support;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentStudentFeedbackBinding;
import com.example.gayaak_10.databinding.FragmentSupportBinding;
import com.example.gayaak_10.R;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.activity.StudentHomeActivity;
import com.example.gayaak_10.student.fragment.StudentDemoHomeFragment;
import com.example.gayaak_10.student.fragment.StudentRegularFragment;
import com.example.gayaak_10.student.model.request.StudentSupportRequest;
import com.example.gayaak_10.student.viewmodel.StudentViewModel;
import com.example.gayaak_10.utility.SharedPrefsUtil;
import com.example.gayaak_10.utility.Utility;

import java.util.ArrayList;


public class SupportFragment extends Fragment {

    private FragmentSupportBinding binding;
    private StudentViewModel viewModel;
    private String question="";
    private String reason="";
    private int supportId, titleId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSupportBinding.inflate(getLayoutInflater());
        viewModel = ViewModelProviders.of(getActivity()).get(StudentViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       ArrayList<String> questionArray = new ArrayList<>();
        questionArray.add("Select an Option");
        questionArray.add("I wish to switch to a new tutor");
        questionArray.add("I wish to change my class timings");
        questionArray.add("Change frequency of classes");
        questionArray.add("Going on a break");
        questionArray.add("Other");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, questionArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spHelpQuestions.setAdapter(arrayAdapter);
        binding.spHelpQuestions.setSelection(0);

        binding.spHelpQuestions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.spHelpQuestions.setSelection(position);
                question = questionArray.get(position);
                supportId=0;
                titleId=position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                binding.spHelpQuestions.setSelection(0);
            }
        });

        binding.btnSubmitSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                support();
            }
        });

    }


//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.btn_submit_support:
//                support();
//                break;
//
//        }
//    }

    private void support() {
        int userId = App.userDataContract.detail.userId;
        reason= binding.etReason.getText().toString();
        if (titleId==0)
        {
            Toast.makeText(getActivity(),"please select the question",Toast.LENGTH_LONG).show();
        }
        else {
            Utility.showLoader(getActivity(), true);
            StudentSupportRequest request = new StudentSupportRequest();
            request.supportId=supportId;
            request.titleId=titleId;
            request.title=question;
            request.comment=reason;

            viewModel.postStudentSupport(userId,request)
                    .observe(getActivity(), defaultResponse ->
                    {
                        Utility.hideLoader();
                        if (defaultResponse.status){
                            Toast.makeText(getActivity(), defaultResponse.message, Toast.LENGTH_LONG).show();
                            openHome();
                        }else {
                            Toast.makeText(getActivity(), defaultResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

    private void openHome() {
        String userType = SharedPrefsUtil.getStringPreferences(getActivity(), "UserType");
        if (userType.equalsIgnoreCase("Paid")) {
            StudentHomeActivity.addFragment(new StudentRegularFragment(), Constant.HOME, getActivity());
        } else {
            StudentHomeActivity.addFragment(new StudentDemoHomeFragment(), Constant.HOME, getActivity());
        }
    }
}