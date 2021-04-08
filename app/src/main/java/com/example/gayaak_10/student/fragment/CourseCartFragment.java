package com.example.gayaak_10.student.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentCourseCartBinding;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.activity.StudentHomeActivity;
import com.example.gayaak_10.student.adapter.CourseCartAdapter;
import com.example.gayaak_10.student.model.BuyCoursesDetail;
import com.example.gayaak_10.utility.SharedPrefsUtil;

import java.util.List;

public class CourseCartFragment extends Fragment {
    private FragmentCourseCartBinding binding;
    private int totalPrice;

    private CourseCartAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCourseCartBinding.inflate(getLayoutInflater());

        binding.ivBackPayment.setOnClickListener(view -> {
            StudentHomeActivity.addFragment(new StudentProfileFragment(""), Constant.PROFILE, getActivity());
        });

        binding.btnContinueBrowsing.setOnClickListener(view -> {
            StudentHomeActivity.addFragment(new StudentCoursesCatalogFragment(), Constant.COURSE_CATALOG, getActivity());
        });

        binding.buyItBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open paypal
                StudentHomeActivity.addFragment(new CheckoutFragment("courseBuy"), Constant.PROFILE, getActivity());

            }
        });

        setCoursesList(App.cartList);
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    private void setCoursesList(final List<BuyCoursesDetail> coursesDetail) {
        totalPrice = 0;
        if (coursesDetail != null && !coursesDetail.isEmpty()) {
            binding.layoutEmptyCart.setVisibility(View.GONE);
            binding.layoutCart.setVisibility(View.VISIBLE);
            binding.tvCartCount.setText(coursesDetail.size() + " Course in cart");

            for (int i = 0; i < App.studentCartList.size(); i++) {
                totalPrice = 100; //totalPrice + App.studentCartList.get(i).price;
            }

            binding.tvTotalPrice.setText("" + totalPrice);
            binding.rvCourseCartList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            adapter = new CourseCartAdapter(coursesDetail, getActivity(), new CourseCartAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int position) {

                }

                @Override
                public void removeCourseCart(int position) {
                    for (int i = 0; i < App.cartList.size(); i++) {
                        if (App.cartList.get(i).courseId.equals(coursesDetail.get(position).courseId)) {
                            App.cartList.remove(i);
                            adapter.notifyDataSetChanged();
                            SharedPrefsUtil.clearSharedPreferencesByKey(getActivity(), Constant.CART_DATA);
                            SharedPrefsUtil.setCartCourse(getActivity(), App.cartList);
                            setCoursesList(App.cartList);
                            break;
                        }
                    }
                }
            });
            binding.rvCourseCartList.setAdapter(adapter);
        } else {
            binding.layoutEmptyCart.setVisibility(View.VISIBLE);
            binding.layoutCart.setVisibility(View.GONE);
            App.cartList.clear();
            SharedPrefsUtil.clearSharedPreferencesByKey(getActivity(), Constant.CART_DATA);
        }
    }
}