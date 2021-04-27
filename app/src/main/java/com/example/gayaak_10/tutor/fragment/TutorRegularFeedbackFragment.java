package com.example.gayaak_10.tutor.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.gayaak_10.R;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentTutorRegularFeedbackBinding;
import com.example.gayaak_10.model.response.CoursesDetail;
import com.example.gayaak_10.model.tutor.CourseModule;
import com.example.gayaak_10.model.tutor.CourseModuleDetail;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.activity.StudentCourseVideoViewActivity;
import com.example.gayaak_10.student.activity.StudentHomeActivity;
import com.example.gayaak_10.student.fragment.StudentBookDemoCourseFragment;
import com.example.gayaak_10.tutor.activity.TutorCourseVideoViewActivity;
import com.example.gayaak_10.tutor.activity.TutorHomeActivity;
import com.example.gayaak_10.tutor.adapter.TutorRegularModuleAdapter;
import com.example.gayaak_10.tutor.model.ModuleModel;
import com.example.gayaak_10.tutor.model.TodaySessions;
import com.example.gayaak_10.tutor.model.request.FeedbackTutorRegularContentRequest;
import com.example.gayaak_10.tutor.viewmodel.TutorViewModel;
import com.example.gayaak_10.utility.Utility;

import java.util.ArrayList;

public class TutorRegularFeedbackFragment extends Fragment implements View.OnClickListener {
    private FragmentTutorRegularFeedbackBinding binding;
    private TutorRegularModuleAdapter moduleAdapter;
    private Integer courseId = 23;
    private TutorViewModel viewModel;
    private CoursesDetail coursesDetail;
    public static Integer moduleId = -1;
    public String studentName = "";
    public String ratingForStudent="";
    ArrayList<String> rating = new ArrayList<>();
    ArrayList<FeedbackTutorRegularContentRequest.ModuleStatusDataContractList> moduleList = new ArrayList<>();
    private TodaySessions tutorSession = new TodaySessions();

    public TutorRegularFeedbackFragment(CoursesDetail coursesDetail) {
        this.courseId = coursesDetail.courseId;
        this.coursesDetail = coursesDetail;
    }


    public TutorRegularFeedbackFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTutorRegularFeedbackBinding.inflate(getLayoutInflater());
        viewModel = ViewModelProviders.of(getActivity()).get(TutorViewModel.class);

        studentName = App.StudentName;
        binding.tvStudentName.setText(studentName+ " Feedback");
        binding.btnSubmit.setOnClickListener(this);
        courseId= App.tutorSessionStarted.CourseId;

        rating.add("Rating");
        rating.add("1");
        rating.add("2");
        rating.add("3");
        rating.add("4");
        rating.add("5");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, rating);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spRating.setAdapter(arrayAdapter);
        binding.spRating.setSelection(0);

        binding.spRating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.spRating.setSelection(position);
                ratingForStudent=rating.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        binding.tvCourseName.setText(coursesDetail.name);
//        binding.tvModuleDescription.setText(coursesDetail.detail);

        viewModel.getCourseModule(courseId).observe(getActivity(), new Observer<CourseModule>() {
            @Override
            public void onChanged(CourseModule courseModule) {
                if (!courseModule.detail.isEmpty()) {
                 /*   binding.layoutModule.setVisibility(View.VISIBLE);
                    binding.layoutEmptyModule.setVisibility(View.GONE);
                    moduleId = courseModule.detail.get(0).courseId;*/
                    setModuleView(courseModule.detail);
                } else {
                   /* binding.layoutModule.setVisibility(View.GONE);
                    binding.layoutEmptyModule.setVisibility(View.VISIBLE);*/
                }
            }
        });


        return binding.getRoot();
    }

    private void setModuleView(ArrayList<CourseModuleDetail> detail) {
        binding.rvModuleProgress.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        moduleAdapter = new TutorRegularModuleAdapter(getActivity(), detail, new TutorRegularModuleAdapter.OnItemClickListener() {


            @Override
            public void onModuleStatus(int moduleId, int statusId) {
                
                if (!moduleList.equals(null)){
                    for (int i=0; i<moduleList.size();i++){
                        if (moduleList.get(i).moduleId==moduleId){
                            moduleList.remove(i);
                        }
                    }
                }
                if (statusId!=0){
                    moduleList.add(new FeedbackTutorRegularContentRequest.ModuleStatusDataContractList(moduleId,statusId));
                }

            }
        });
        binding.rvModuleProgress.setAdapter(moduleAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSubmit) {
            if (ratingForStudent.equalsIgnoreCase("Rating")){
                Toast.makeText(getContext(), "Please select rating",Toast.LENGTH_LONG).show();
            }else {
                Utility.showLoader(getContext(),false);
                submitFeedback(App.tutorSessionStarted);
            }

        }
        
    }

    private void submitFeedback(TodaySessions tutorSessionStarted) {
        FeedbackTutorRegularContentRequest feedback = new FeedbackTutorRegularContentRequest();
        feedback.progressId=0;
        feedback.studentId=String.valueOf(tutorSessionStarted.StudentId);
        feedback.tutorId = tutorSessionStarted.TutorId;
        feedback.courseId = tutorSessionStarted.CourseId;
        feedback.moduleId="0";
        feedback.statusId="0";
        feedback.studentId=String.valueOf(tutorSessionStarted.StudentId);
        feedback.studnetTutorBookingId=tutorSessionStarted.StudentTutorBookingId;
        feedback.liveClassDetailId=tutorSessionStarted.liveClassDetailId;
        feedback.comment= binding.etTaught.getText().toString().trim();
        feedback.comment2= binding.etFeedback.getText().toString().trim();
        feedback.rating=ratingForStudent;
        feedback.moduleStatusDataContractList= moduleList;

        viewModel.postRegularSessionFeedback(App.userDataContract.detail.userId, feedback)
                .observe(getActivity(), defaultResponse -> {
                    Utility.hideLoader();
                    if (defaultResponse.status) {
                        Toast.makeText(getActivity(), defaultResponse.message, Toast.LENGTH_SHORT).show();
                        TutorHomeActivity.addFragment(new TutorHomeFragment(), Constant.HOME, getActivity());
                    } else {
                        Toast.makeText(getActivity(), defaultResponse.message, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}