package com.example.gayaak_10.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.gayaak_10.R;
import com.example.gayaak_10.databinding.ItemRegisterCourseBinding;
import com.example.gayaak_10.model.response.CoursesDetail;

import java.util.List;

public class NewCardAdapter extends PagerAdapter {

    private List<CoursesDetail> models;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public NewCardAdapter(List<CoursesDetail> models, Context context, OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        this.models = models;
        this.context = context;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemRegisterCourseBinding binding = ItemRegisterCourseBinding.inflate(layoutInflater);
        container.addView(binding.getRoot());

        if (models.get(position).ThumbnailImage.isEmpty()){
            binding.titleTextView.setTextColor(ContextCompat.getColor(context,R.color.white));
        }else {
            binding.titleTextView.setTextColor(ContextCompat.getColor(context,R.color.yellow));

        }
        binding.titleTextView.setText(models.get(position).name);
        Glide.with(context).load(models.get(position).ThumbnailImage).placeholder(R.drawable.group_singers_1).into(binding.ivCourseBackground);
        binding.btnRegister.setOnClickListener(view -> {
            onItemClickListener.onItemClickListener(position);
        });
        return binding.getRoot();
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }
}
