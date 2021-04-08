package com.example.gayaak_10.student.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gayaak_10.R;
import com.example.gayaak_10.databinding.ItemWalletPlanBinding;
import com.example.gayaak_10.databinding.ItemWalletPlanRecommendedBinding;
import com.example.gayaak_10.student.model.WalletRechargePlanDataContractList;

import java.util.ArrayList;

public class CourseWalletPlansAdapter extends RecyclerView.Adapter<CourseWalletPlansAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private ArrayList<WalletRechargePlanDataContractList> coursePlanArrayList = new ArrayList<>();
    private int price;

    public CourseWalletPlansAdapter(Context context, ArrayList<WalletRechargePlanDataContractList> coursePlanArrayList, int price, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.coursePlanArrayList = coursePlanArrayList;
        this.price = price;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemWalletPlanRecommendedBinding binding = ItemWalletPlanRecommendedBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mContext, coursePlanArrayList.get(position), price, onItemClickListener);

        if (coursePlanArrayList.get(position).isSelected != null) {
            if (coursePlanArrayList.get(position).isSelected) {
                setSelectedPlan(holder, R.color.colorAccent, R.color.white, R.color.white, R.drawable.rectangular_2_filled_white);
            } else {
                setSelectedPlan(holder, R.color.white, R.color.colorAccent, R.color.dark_gray, R.drawable.rectangular_2_filled_coloraccent);
            }
        }
    }

    private void setSelectedPlan(@NonNull ViewHolder holder, int p1, int p2, int p3, int p4) {
        holder.binding.layoutPlan.setBackground(ContextCompat.getDrawable(mContext, p1));
        holder.binding.llPlanSelect.setBackground(ContextCompat.getDrawable(mContext,p4));
        holder.binding.tvExtraCoins.setTextColor(ContextCompat.getColor(mContext,p1));
        holder.binding.tvPlanFees.setTextColor(ContextCompat.getColor(mContext,p2));
        holder.binding.tvFree.setTextColor(ContextCompat.getColor(mContext,p1));
        holder.binding.tvWorthSessions.setTextColor(ContextCompat.getColor(mContext,p3));
        holder.binding.tvPay.setTextColor(ContextCompat.getColor(mContext,p3));
    }


    @Override
    public int getItemCount() {
        return coursePlanArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemWalletPlanRecommendedBinding binding;


        public ViewHolder(ItemWalletPlanRecommendedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(Context mContext, WalletRechargePlanDataContractList coursePlan, int price, OnItemClickListener onItemClickListener) {
           // binding.tvPlanName.setText(coursePlan.name);
            binding.tvPlanFees.setText(coursePlan.noOfCoin.toString());
            if (coursePlan.extraCoin != 0){
                //binding.layoutExtraCoin.setVisibility(View.VISIBLE);
                binding.tvExtraCoins.setText("Get " +coursePlan.extraCoin.toString());
            }

            if (price>0){
                binding.tvWorthSessions.setText("Worth "+(coursePlan.noOfCoin/price)+" Sessions");
            }else {
                binding.tvWorthSessions.setText("");
            }


            binding.layoutPlan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClickListener(getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }
}