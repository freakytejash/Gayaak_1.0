package com.example.gayaak_10.student.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gayaak_10.R;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentStudentModuleInfoBinding;
import com.example.gayaak_10.student.activity.StudentHomeActivity;
import com.example.gayaak_10.student.adapter.CourseModuleInfoAdapter;

public class StudentModuleInfoFragment extends Fragment implements View.OnClickListener {

    private FragmentStudentModuleInfoBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStudentModuleInfoBinding.inflate(getLayoutInflater());

        binding.rvModuleVideo.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        CourseModuleInfoAdapter adapter = new CourseModuleInfoAdapter(getActivity(), new CourseModuleInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void openModuleVideo(int adapterPosition) {
                StudentHomeActivity.addFragment(new StudentCoursesCatalogFragment(), Constant.COURSE_CATALOG, getActivity());
            }
        });
        binding.rvModuleVideo.setAdapter(adapter);


        binding.ivModuleInfoBack.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivModuleInfoBack:
                StudentHomeActivity.addFragment(new StudentCoursesCatalogFragment(), Constant.COURSE_CATALOG, getActivity());
                break;
        }
    }
}
