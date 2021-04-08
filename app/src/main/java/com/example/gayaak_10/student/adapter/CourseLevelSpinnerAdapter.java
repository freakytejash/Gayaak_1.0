package com.example.gayaak_10.student.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.widget.AppCompatTextView;

import com.example.gayaak_10.R;
import com.example.gayaak_10.student.model.LearningLevelDataContractList;

import java.util.ArrayList;

public class CourseLevelSpinnerAdapter extends BaseAdapter {
    Context context;

    LayoutInflater inflater;
    ArrayList<LearningLevelDataContractList> countryList;
    OnItemClickListener onItemClickListener;

    public CourseLevelSpinnerAdapter(Context applicationContext, ArrayList<LearningLevelDataContractList> countryList, OnItemClickListener onItemClickListener) {
        this.context = applicationContext;
        this.countryList = countryList;
        this.onItemClickListener = onItemClickListener;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return countryList.size();
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
        names.setText(countryList.get(i).name);
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