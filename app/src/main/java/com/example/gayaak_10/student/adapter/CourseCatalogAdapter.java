package com.example.gayaak_10.student.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gayaak_10.R;
import com.example.gayaak_10.adapter.CourseTutorAdapter;
import com.example.gayaak_10.databinding.ItemCourseCatalogBinding;
import com.example.gayaak_10.model.response.CoursesDetail;

import java.util.ArrayList;
import java.util.List;

public class CourseCatalogAdapter extends RecyclerView.Adapter<CourseCatalogAdapter.ViewHolder>  implements Filterable {

    public Context mContext;
    private List<CoursesDetail> coursesDetailsList;
    private String rowsCount;
    private OnItemClickListener onItemClickListener;
    ValueFilter valueFilter;

    public CourseCatalogAdapter(Context context, List<CoursesDetail> detail, String moreCourses, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.coursesDetailsList = detail;
      //  this.rowsCount = moreCourses;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemCourseCatalogBinding itemCourseCatalogBinding = ItemCourseCatalogBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemCourseCatalogBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(coursesDetailsList.get(position),mContext, onItemClickListener);
    }


    @Override
    public int getItemCount() {
        /*int displayRows = 4;

        if (coursesDetailsList.size() >= 4){
            if (rowsCount.equalsIgnoreCase("Home")){
                displayRows = 4;
            }else if (rowsCount.equalsIgnoreCase("MoreCourses")){
                displayRows = coursesDetailsList.size();
            }
        }else {
            displayRows = coursesDetailsList.size();
        }*/

        return coursesDetailsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemCourseCatalogBinding itemCourseCatalogBinding;

        public ViewHolder(ItemCourseCatalogBinding itemCourseCatalogBinding) {
            super(itemCourseCatalogBinding.getRoot());
            this.itemCourseCatalogBinding = itemCourseCatalogBinding;
        }

        public void bind(CoursesDetail coursesDetail, Context mContext, final OnItemClickListener onItemClickListener) {
            itemCourseCatalogBinding.tvCourseTitle.setText(coursesDetail.name);
           /* final TypedArray imgs = mContext.getResources().obtainTypedArray(R.array.bgImages);
            final Random rand = new Random();
            final int rndInt = rand.nextInt(imgs.length());
            final int resID = imgs.getResourceId(rndInt, 0);*/

           /*if (coursesDetail.ThumbnailImage.isEmpty()){
               itemCourseCatalogBinding.tvCourseTitle.setTextColor(ContextCompat.getColor(mContext,R.color.white));
           }else {
               itemCourseCatalogBinding.tvCourseTitle.setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
           }*/
            Glide.with(mContext).load(coursesDetail.ThumbnailImage).placeholder(R.drawable.semi_1).into(itemCourseCatalogBinding.ivCourseBackground);
            itemCourseCatalogBinding.layoutCourses.setOnClickListener(view -> onItemClickListener.onItemClickListener(getAdapterPosition()));

            itemCourseCatalogBinding.tvCourseLevel.setText(coursesDetail.levelName);
            if (coursesDetail.courseTutorsDataContractList != null && coursesDetail.courseTutorsDataContractList.size() > 0){
                itemCourseCatalogBinding.layoutTutor.setVisibility(View.VISIBLE);
                itemCourseCatalogBinding.rvTutorList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                CourseTutorAdapter adapter = new CourseTutorAdapter(mContext, coursesDetail.courseTutorsDataContractList);
                itemCourseCatalogBinding.rvTutorList.setAdapter(adapter);
            }else {
                itemCourseCatalogBinding.layoutTutor.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                List<CoursesDetail> filterList = new ArrayList<>();
                for (int i = 0; i < coursesDetailsList.size(); i++) {
                    if ((coursesDetailsList.get(i).name.toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        filterList.add(coursesDetailsList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
                notifyDataSetChanged();
            } else {
                results.count = coursesDetailsList.size();
                results.values = coursesDetailsList;
                notifyDataSetChanged();
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notifyDataSetChanged();
        }

    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }
}
