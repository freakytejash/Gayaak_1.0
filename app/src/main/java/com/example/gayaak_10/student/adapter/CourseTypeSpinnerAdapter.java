package com.example.gayaak_10.student.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.widget.AppCompatTextView;

import com.example.gayaak_10.R;
import com.example.gayaak_10.student.model.CategoryDataContractList;

import java.util.ArrayList;

public class CourseTypeSpinnerAdapter extends BaseAdapter {
    Context context;

    LayoutInflater inflater;
    ArrayList<CategoryDataContractList> courseTypeArrayList;
    OnItemClickListener onItemClickListener;

    public CourseTypeSpinnerAdapter(Context applicationContext, ArrayList<CategoryDataContractList> courseTypeArrayList, OnItemClickListener onItemClickListener) {
        this.context = applicationContext;
        this.courseTypeArrayList = courseTypeArrayList;
        this.onItemClickListener = onItemClickListener;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return courseTypeArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.spinner_item_country, null);
        AppCompatTextView names = (AppCompatTextView) view.findViewById(R.id.tvCountryName);
        names.setText(courseTypeArrayList.get(i).name);
        names.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(i);
            }
        });
        return view;
    }

    public interface OnItemClickListener{
        public void onItemClick(int position);
    }
}