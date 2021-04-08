package com.example.gayaak_10.student.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gayaak_10.R;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentStudentModuleBinding;
import com.example.gayaak_10.model.response.CoursesDetail;
import com.example.gayaak_10.model.tutor.CourseModuleDetail;
import com.example.gayaak_10.student.activity.StudentCourseVideoViewActivity;
import com.example.gayaak_10.student.activity.StudentHomeActivity;
import com.example.gayaak_10.student.adapter.CourseModuleAdapter;
import com.example.gayaak_10.student.viewmodel.StudentViewModel;
import com.example.gayaak_10.utility.Utility;

import java.util.ArrayList;

public class StudentModuleFragment extends Fragment implements View.OnClickListener {

    private FragmentStudentModuleBinding binding;
    private Integer courseId = 0;
    private CoursesDetail coursesDetail;
    private ArrayList<CourseModuleDetail> courseDetail;
    public static Integer moduleId = -1;

    public StudentModuleFragment(CoursesDetail coursesDetail) {
        this.courseId = coursesDetail.courseId;
        this.coursesDetail = coursesDetail;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStudentModuleBinding.inflate(getLayoutInflater());
        StudentViewModel viewModel = ViewModelProviders.of(getActivity()).get(StudentViewModel.class);

        binding.tvCourseName.setText(coursesDetail.name);
        binding.tvModuleDescription.setText(coursesDetail.detail);
        binding.tvPrice.setText(Utility.withSuffix(coursesDetail.price));

        viewModel.getCourseModule(courseId).observe(getViewLifecycleOwner(), courseModule -> {
            if (!courseModule.detail.isEmpty()) {
                binding.layoutModule.setVisibility(View.VISIBLE);
                binding.layoutEmptyModule.setVisibility(View.GONE);
                courseDetail = courseModule.detail;
                 setModuleView(courseModule.detail);
            } else {
                binding.layoutModule.setVisibility(View.GONE);
                binding.layoutEmptyModule.setVisibility(View.VISIBLE);
            }
        });

        binding.ivModuleBack.setOnClickListener(this);
        binding.tvEbook.setOnClickListener(this);
        return binding.getRoot();
    }

    private void setModuleView(ArrayList<CourseModuleDetail> detail) {
        binding.rvModule.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        CourseModuleAdapter courseModuleAdapter = new CourseModuleAdapter(getActivity(), detail, new CourseModuleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //  StudentHomeActivity.addFragment(new StudentModuleInfoFragment(), Constant.COURSE_CATALOG, getActivity());
                //StudentHomeActivity.addFragment(new BuyCourseFragment(), Constant.COURSE_CATALOG, getActivity());
            }

            @Override
            public void openModuleVideo(int position) {
                Intent intent = new Intent(getActivity(), StudentCourseVideoViewActivity.class);
                intent.putExtra("moduleId", detail.get(position).moduleId);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);

            }

            @Override
            public void openModulePDF(int position) {
                // open pdf view
                //StudentHomeActivity.addFragment(new StudentModulePdfFragment(detail.get(position)), Constant.COURSE_CATALOG, getActivity());
                StudentHomeActivity.addFragment(new StudentAudioFragment(detail.get(position).moduleId), Constant.COURSE_CATALOG, getActivity());
            }
        });
        binding.rvModule.setAdapter(courseModuleAdapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvEbook:
                if (moduleId != -1){
                    StudentHomeActivity.addFragment(new StudentEbookFragment(moduleId), Constant.COURSE_CATALOG, getActivity());
                }else {
                    Toast.makeText(getActivity(), "Ebook for this module is not available." , Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.ivModuleBack:
                StudentHomeActivity.addFragment(new StudentCoursesCatalogFragment(), Constant.COURSE_CATALOG, getActivity());
                break;
        }
    }
}
