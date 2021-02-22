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
import com.example.gayaak_10.student.model.WalletRechargePlanDataContractList;

import java.util.ArrayList;

public class CourseWalletPlansAdapter extends RecyclerView.Adapter<CourseWalletPlansAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private ArrayList<WalletRechargePlanDataContractList> coursePlanArrayList = new ArrayList<>();

    public CourseWalletPlansAdapter(Context context, ArrayList<WalletRechargePlanDataContractList> coursePlanArrayList, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.coursePlanArrayList = coursePlanArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemWalletPlanBinding binding = ItemWalletPlanBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mContext, coursePlanArrayList.get(position), onItemClickListener);

        if (coursePlanArrayList.get(position).isSelected != null) {
            if (coursePlanArrayList.get(position).isSelected) {
                setSelectedPlan(holder, R.drawable.plan_selected_bg, R.color.white, R.color.colorAccent);
            } else {
                setSelectedPlan(holder, R.drawable.plan_unselected_bg, R.color.black, R.color.white);
            }
        }

    }

    private void setSelectedPlan(@NonNull ViewHolder holder, int p, int p2, int p3) {
        holder.binding.layoutPlan.setBackground(ContextCompat.getDrawable(mContext, p));
        holder.binding.tvPlanSelect.setBackgroundColor(ContextCompat.getColor(mContext, p2));
        holder.binding.tvPlanSelect.setTextColor(ContextCompat.getColor(mContext, p3));
        holder.binding.tvPlanName.setTextColor(ContextCompat.getColor(mContext, p2));
        holder.binding.tvPlanFees.setTextColor(ContextCompat.getColor(mContext, p2));
        holder.binding.tvExtraCoins.setTextColor(ContextCompat.getColor(mContext, p2));
        holder.binding.tvFree.setTextColor(ContextCompat.getColor(mContext, p2));
    }


    @Override
    public int getItemCount() {
        return coursePlanArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemWalletPlanBinding binding;

        public ViewHolder(ItemWalletPlanBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(Context mContext, WalletRechargePlanDataContractList coursePlan, OnItemClickListener onItemClickListener) {
            binding.tvPlanName.setText(coursePlan.name);
            binding.tvPlanFees.setText(coursePlan.noOfCoin.toString());
            if (coursePlan.extraCoin != 0){
                binding.layoutExtraCoin.setVisibility(View.VISIBLE);
                binding.tvExtraCoins.setText("Get " +coursePlan.extraCoin.toString());
            }


            binding.cardViewPlan.setOnClickListener(new View.OnClickListener() {
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