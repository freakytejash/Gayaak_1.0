package com.example.gayaak_10.tutor.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gayaak_10.R;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentTutorCourseModuleBinding;
import com.example.gayaak_10.model.response.CoursesDetail;
import com.example.gayaak_10.model.tutor.CourseModule;
import com.example.gayaak_10.model.tutor.CourseModuleDetail;
import com.example.gayaak_10.tutor.activity.TutorCourseVideoViewActivity;
import com.example.gayaak_10.tutor.activity.TutorHomeActivity;
import com.example.gayaak_10.tutor.viewmodel.TutorViewModel;

import java.util.ArrayList;

public class TutorCourseModuleFragment extends Fragment implements View.OnClickListener {

    private FragmentTutorCourseModuleBinding binding;
    private Integer courseId = 0;
    private CoursesDetail coursesDetail;
    private TutorViewModel viewModel;
    public static Integer moduleId = -1;

    public TutorCourseModuleFragment(CoursesDetail coursesDetail) {
        this.courseId = coursesDetail.courseId;
        this.coursesDetail = coursesDetail;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTutorCourseModuleBinding.inflate(getLayoutInflater());
        viewModel = ViewModelProviders.of(getActivity()).get(TutorViewModel.class);

        binding.tvCourseName.setText(coursesDetail.name);
        binding.tvModuleDescription.setText(coursesDetail.detail);

        viewModel.getCourseModule(courseId).observe(getActivity(), new Observer<CourseModule>() {
            @Override
            public void onChanged(CourseModule courseModule) {
                if (!courseModule.detail.isEmpty()) {
                    binding.layoutModule.setVisibility(View.VISIBLE);
                    binding.layoutEmptyModule.setVisibility(View.GONE);
                    moduleId = courseModule.detail.get(0).courseId;
                    setModuleView(courseModule.detail);
                } else {
                    binding.layoutModule.setVisibility(View.GONE);
                    binding.layoutEmptyModule.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.ivModuleBack.setOnClickListener(this);
        binding.tvEbook.setOnClickListener(this);
        return binding.getRoot();
    }

    private void setModuleView(ArrayList<CourseModuleDetail> detail) {
        binding.rvModule.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        TutorCourseModuleAdapter courseModuleAdapter = new TutorCourseModuleAdapter(getActivity(), detail, new TutorCourseModuleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                /*Intent intent = new Intent(getActivity(), StudentCourseVideoViewActivity.class);
                intent.putExtra("moduleId", detail.get(position).moduleId);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);*/
            }

            @Override
            public void openModulePDF(int position) {
                //TutorHomeActivity.addFragment(new TutorModulePdfFragment(detail.get(position)), Constant.COURSE_CATALOG, getActivity());
                TutorHomeActivity.addFragment(new TutorAudioFragment(detail.get(position).moduleId), Constant.COURSE_CATALOG, getActivity());
            }

            @Override
            public void openModuleVideo(int adapterPosition) {
                Intent intent = new Intent(getActivity(), TutorCourseVideoViewActivity.class);
                intent.putExtra("moduleId", detail.get(adapterPosition).moduleId);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);

            }
        });
        binding.rvModule.setAdapter(courseModuleAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivModuleBack:
                TutorHomeActivity.addFragment(new TutorCoursesCatalogFragment(), Constant.COURSE_CATALOG, getActivity());
                break;

            case R.id.tvEbook:
                if (moduleId != -1){
                    TutorHomeActivity.addFragment(new TutorEbookFragment(moduleId), Constant.COURSE_CATALOG, getActivity());
                }else {
                    Toast.makeText(getActivity(), "Ebook for this module is not available." , Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
