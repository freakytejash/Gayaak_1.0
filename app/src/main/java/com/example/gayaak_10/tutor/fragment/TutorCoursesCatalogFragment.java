package com.example.gayaak_10.tutor.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentTutorCourseCatalogBinding;
import com.example.gayaak_10.model.response.CoursesDetail;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.tutor.activity.TutorHomeActivity;
import com.example.gayaak_10.tutor.adapter.TutorCourseCatalogAdapter;

import java.util.List;


public class TutorCoursesCatalogFragment extends Fragment {

    private FragmentTutorCourseCatalogBinding binding;
    private TutorCourseCatalogAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTutorCourseCatalogBinding.inflate(getLayoutInflater());
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

            adapter = new TutorCourseCatalogAdapter(getActivity(), detail, "", position -> {
                // open module view
                TutorHomeActivity.addFragment(new TutorCourseModuleFragment(detail.get(position)), Constant.COURSE_CATALOG, getActivity());

            });
            binding.rvCourseCatalogMore.setAdapter(adapter);
        } else {
            binding.layoutEmptyCourse.setVisibility(View.VISIBLE);
            binding.rvCourseCatalogMore.setVisibility(View.GONE);
        }
    }
}
