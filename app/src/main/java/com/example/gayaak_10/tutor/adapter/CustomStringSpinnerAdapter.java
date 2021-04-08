package com.example.gayaak_10.tutor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.widget.AppCompatTextView;

import com.example.gayaak_10.R;
import com.example.gayaak_10.model.tutor.StudentTutorBookingDataContractList;

import java.util.ArrayList;

public class CustomStringSpinnerAdapter extends BaseAdapter {
    Context context;

    LayoutInflater inflater;
    ArrayList<StudentTutorBookingDataContractList> stringArrayList;
    OnItemClickListener onItemClickListener;

    public CustomStringSpinnerAdapter(Context applicationContext, ArrayList<StudentTutorBookingDataContractList> stringArrayList, OnItemClickListener onItemClickListener) {
        this.context = applicationContext;
        this.stringArrayList = stringArrayList;
        inflater = (LayoutInflater.from(applicationContext));
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getCount() {
        return stringArrayList.size();
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
        names.setText(stringArrayList.get(i).studentName);
        names.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(i);
            }
        });
        return view;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}