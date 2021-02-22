package com.example.gayaak_10.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.ItemMyCountryBinding;
import com.example.gayaak_10.model.response.CountryDetail;
import com.example.gayaak_10.utility.SharedPrefsUtil;

import java.util.ArrayList;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private ArrayList<CountryDetail> countryDetailArrayList = new ArrayList<>();
    public int selectedPosition = -1;

    public CountryAdapter(ArrayList<CountryDetail> countryDetailArrayList, Context context, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.countryDetailArrayList = countryDetailArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemMyCountryBinding itemMyCountryBinding = ItemMyCountryBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemMyCountryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(countryDetailArrayList.get(position), mContext, onItemClickListener);
        String country = SharedPrefsUtil.getStringPreferences(mContext, Constant.COUNTRY);
        if (country.equalsIgnoreCase(countryDetailArrayList.get(position).Name)){
            selectedPosition = position;
        }

        if(selectedPosition == position)
            holder.itemMyCountryBinding.ivSelectedCountry.setVisibility(View.VISIBLE);
        else
            holder.itemMyCountryBinding.ivSelectedCountry.setVisibility(View.GONE);
    }


    @Override
    public int getItemCount() {
        return countryDetailArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemMyCountryBinding itemMyCountryBinding;

        public ViewHolder(ItemMyCountryBinding itemMyCountryBinding) {
            super(itemMyCountryBinding.getRoot());
            this.itemMyCountryBinding = itemMyCountryBinding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(CountryDetail countryDetailArrayList, Context mContext, final OnItemClickListener onItemClickListener) {
            itemMyCountryBinding.tvCountryName.setText(countryDetailArrayList.Name);

            itemMyCountryBinding.layoutCountry.setOnClickListener(view -> {
                        selectedPosition = getAdapterPosition();
                        onItemClickListener.onItemClickListener(getAdapterPosition());
                        notifyDataSetChanged();
                    }
            );
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }
}