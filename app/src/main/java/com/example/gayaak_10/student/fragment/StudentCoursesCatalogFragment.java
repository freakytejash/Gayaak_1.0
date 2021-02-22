package com.example.gayaak_10.student.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentAllCourseCatalogBinding;
import com.example.gayaak_10.model.response.CoursesDetail;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.activity.StudentHomeActivity;
import com.example.gayaak_10.student.adapter.CourseCatalogAdapter;

import java.util.List;


public class StudentCoursesCatalogFragment extends Fragment {

    private FragmentAllCourseCatalogBinding binding;
    private CourseCatalogAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAllCourseCatalogBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!App.allCoursesArrayList.isEmpty()) {
            setCourseData(App.allCoursesArrayList);
        } else {
            binding.layoutEmptyCourse.setVisibility(View.VISIBLE);
            binding.rvCourseCatalogMore.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void setCourseData(final List<CoursesDetail> detail) {
        if (!detail.isEmpty()) {
            binding.progressMoreCourses.setVisibility(View.GONE);
            binding.layoutEmptyCourse.setVisibility(View.GONE);
            binding.rvCourseCatalogMore.setVisibility(View.VISIBLE);

            binding.rvCourseCatalogMore.setLayoutManager(new GridLayoutManager(getActivity(), 2));

            adapter = new CourseCatalogAdapter(getActivity(), detail, "", position -> {
                StudentHomeActivity.addFragment(new StudentModuleFragment(detail.get(position)), Constant.COURSE_CATALOG, getActivity());

                //    StudentHomeActivity.addFragment(new StudentPracticeRoomFragment(), Constant.COURSE_CATALOG, getActivity());
    /*            Intent intent = new Intent(getActivity(), CourseVideoViewActivity.class);
                intent.putExtra("courseId", detail.get(position).courseId);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);*/
            });
            binding.rvCourseCatalogMore.setAdapter(adapter);
        } else {
            binding.layoutEmptyCourse.setVisibility(View.VISIBLE);
            binding.rvCourseCatalogMore.setVisibility(View.GONE);
        }
    }
}
