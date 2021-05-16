package com.example.gayaak_10.student.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.gayaak_10.R;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentStudentFeedbackBinding;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.activity.StudentHomeActivity;
import com.example.gayaak_10.student.model.LiveClassDataContractList;
import com.example.gayaak_10.student.model.request.FeedbackContentRequest;
import com.example.gayaak_10.student.viewmodel.StudentViewModel;
import com.example.gayaak_10.utility.DateTimeUtility;
import com.example.gayaak_10.utility.SharedPrefsUtil;

public class StudentFeedbackFragment extends Fragment implements View.OnClickListener {

    private FragmentStudentFeedbackBinding binding;
    private String technicalDifficulties = "";
    private String joinDecision = "";
    private boolean technicalIssues = false;
    private boolean isJoining = false;
    private StudentViewModel mViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStudentFeedbackBinding.inflate(getLayoutInflater());
        mViewModel = ViewModelProviders.of(getActivity()).get(StudentViewModel.class);
   //     binding.ratingBar.getRating();

        if (App.liveClassId == 2){
            binding.llWishToJoin.setVisibility(View.GONE);
        }
        binding.btnYes.setOnClickListener(this);
        binding.btnNo.setOnClickListener(this);
        binding.btnYesJoin.setOnClickListener(this);
        binding.btnNoJoin.setOnClickListener(this);
        binding.btnDecidJoin.setOnClickListener(this);
        binding.ivFeedbackBack.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivFeedbackBack:
                openHome();
                break;

            case R.id.btnYes:
                technicalIssues = true;
                decideTechnical("yes", binding.btnYes, "no", R.drawable.btn_state_normal, R.drawable.btn_state_pressed);
                break;

            case R.id.btnNo:
                technicalIssues = false;
                decideTechnical("no", binding.btnNo, "yes", R.drawable.btn_state_pressed, R.drawable.btn_state_normal);
                break;

            case R.id.btnYesJoin:
                isJoining = true;
                decideJoin("YesJoin", binding.btnYesJoin, "NoJoin", "YetDecide",
                        R.drawable.btn_state_normal, R.drawable.btn_state_pressed,
                        R.drawable.btn_state_normal);
                break;

            case R.id.btnNoJoin:
                isJoining = false;
                decideJoin("NoJoin", binding.btnNoJoin, "YesJoin", "YetDecide",
                        R.drawable.btn_state_pressed, R.drawable.btn_state_normal, R.drawable.btn_state_normal);
                break;

            case R.id.btnDecidJoin:
                isJoining = false;
                decideJoin("YetDecide", binding.btnDecidJoin, "YesJoin", "NoJoin",
                        R.drawable.btn_state_normal, R.drawable.btn_state_normal, R.drawable.btn_state_pressed);
                break;

            case R.id.btnSubmit:
                String getRating = String.valueOf(binding.ratingBar.getRating());
                if (getRating.equals("0.0")){
                    Toast.makeText(getActivity(), "Please give ratings", Toast.LENGTH_SHORT).show();
                    return;
                }

                submitFeedback(getRating, technicalIssues, isJoining, binding.etFeedbackThoughts.getText().toString().trim());

                break;
        }
    }

    private void submitFeedback(String getRating, boolean technicalIssues, boolean isJoining, String etFeedbackThoughts) {
        LiveClassDataContractList liveClassDataContractList = new LiveClassDataContractList();
        liveClassDataContractList = App.sessionStarted;
        App.sessionStarted = new LiveClassDataContractList();

        FeedbackContentRequest feedbackContentRequest = new FeedbackContentRequest();
        feedbackContentRequest.tutorId = liveClassDataContractList.tutorId;
        feedbackContentRequest.sessionFeedbackId = 0;
        feedbackContentRequest.courseId = null;
        feedbackContentRequest.studentId = liveClassDataContractList.studentId;
        feedbackContentRequest.sessionId = liveClassDataContractList.ScheduleId;
        feedbackContentRequest.feedbackId = null;
        feedbackContentRequest.rating = Float.parseFloat(getRating);
        feedbackContentRequest.lessonPercent = null;
        feedbackContentRequest.date = DateTimeUtility.currentDateTime();
        feedbackContentRequest.moduleId = null;
        if (liveClassDataContractList.liveClassTypeId==1){
            feedbackContentRequest.feedBackTypeIId = 1;
            feedbackContentRequest.IsInterestedToJoin = isJoining;
        }else {
            feedbackContentRequest.feedBackTypeIId =2;
        }
        feedbackContentRequest.IsExpAnyTechnicalDifficulties = technicalIssues;
        feedbackContentRequest.Feedbacktext = etFeedbackThoughts;
        feedbackContentRequest.StudentTutorBookingId = liveClassDataContractList.StudentTutorBookingId;

        mViewModel.postSessionFeedback(App.userDataContract.detail.userId, feedbackContentRequest)
        .observe(getActivity(), defaultResponse -> {
            if (defaultResponse.status){
                Toast.makeText(getActivity(), defaultResponse.message, Toast.LENGTH_SHORT).show();
                openHome();
            }else {
                Toast.makeText(getActivity(), defaultResponse.message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void openHome() {
        String userType = SharedPrefsUtil.getStringPreferences(getActivity(), "UserType");
        if (userType.equalsIgnoreCase("Paid")) {
            StudentHomeActivity.addFragment(new StudentRegularFragment(), Constant.HOME, getActivity());
        } else {
            StudentHomeActivity.addFragment(new StudentDemoHomeFragment(), Constant.HOME, getActivity());
        }
    }

    private void decideJoin(String yesJoin, AppCompatButton p, String noJoin, String yetDecide, int p2, int p3, int p4) {
        if (joinDecision.isEmpty()) {
            joinDecision = yesJoin;
            p.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.btn_state_pressed));
        } else {
            if (joinDecision.equalsIgnoreCase(noJoin) || joinDecision.equalsIgnoreCase(yetDecide)) {
                joinDecision = yesJoin;
                binding.btnNoJoin.setBackground(ContextCompat.getDrawable(getActivity(), p2));
                binding.btnYesJoin.setBackground(ContextCompat.getDrawable(getActivity(), p3));
                binding.btnDecidJoin.setBackground(ContextCompat.getDrawable(getActivity(), p4));
            }
        }
    }

    private void decideTechnical(String yes, AppCompatButton p, String no, int p2, int p3) {
        if (technicalDifficulties.isEmpty()) {
            technicalDifficulties = yes;
            p.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.btn_state_pressed));
        } else {
            if (technicalDifficulties.equalsIgnoreCase(no)) {
                technicalDifficulties = yes;
                binding.btnNo.setBackground(ContextCompat.getDrawable(getActivity(), p2));
                binding.btnYes.setBackground(ContextCompat.getDrawable(getActivity(), p3));
            }
        }
    }
}