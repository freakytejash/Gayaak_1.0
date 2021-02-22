package com.example.gayaak_10.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.widget.AppCompatTextView;

import com.example.gayaak_10.R;
import com.example.gayaak_10.model.response.CountryDetail;

import java.util.ArrayList;

public class CustomSpinnerAdapter  extends BaseAdapter {
    Context context;

    LayoutInflater inflater;
    ArrayList<CountryDetail> countryList;

    public CustomSpinnerAdapter(Context applicationContext, ArrayList<CountryDetail> countryList) {
        this.context = applicationContext;
        this.countryList = countryList;
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
        names.setText(countryList.get(i).Name);
        return view;
    }
}