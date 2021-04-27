package com.example.gayaak_10.student.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.gayaak_10.R;
import com.example.gayaak_10.databinding.FragmentPracticeRoomBinding;
import com.google.android.material.tabs.TabLayout;

public class PracticeRoomFragment extends Fragment {


    private FragmentPracticeRoomBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        binding = FragmentPracticeRoomBinding.inflate(getLayoutInflater());

        loadScreenData();

        return binding.getRoot();
    }

     private void loadScreenData() {


        binding.tabLayout.setHorizontalScrollBarEnabled(true);
        binding.viewPager.setOffscreenPageLimit(2);

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getResources().getString(R.string.Record)));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getResources().getString(R.string.Expert_Feedback)));

        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

         MenuTabAdapter menuTabAdapter = new MenuTabAdapter(getChildFragmentManager(), binding.tabLayout.getTabCount());
         binding.viewPager.setAdapter(menuTabAdapter);
         binding.viewPager.setOffscreenPageLimit(2);
         binding.viewPager.setCurrentItem(0);

         binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint("RestrictedApi")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public static class MenuTabAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;


        public MenuTabAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;

        }

        @Override
        public Fragment getItem(int position) {
            Log.e("getItem", "" + position);
            switch (position) {

                case 1:
                    return new ExpertFeedbackFragment();

                default:
                    return new RecordAudioFragment();
            }

        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}
