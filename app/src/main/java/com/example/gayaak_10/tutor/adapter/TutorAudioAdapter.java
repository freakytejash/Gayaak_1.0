package com.example.gayaak_10.tutor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gayaak_10.R;
import com.example.gayaak_10.databinding.ItemAudioCellBinding;
import com.example.gayaak_10.student.model.AudioModuleDataContractList;

import java.util.ArrayList;

public class TutorAudioAdapter extends RecyclerView.Adapter<TutorAudioAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private ArrayList<AudioModuleDataContractList> audioModuleDataContractLists;
    private int lastSelectedPosition = 0;


    public TutorAudioAdapter(Context context, ArrayList<AudioModuleDataContractList> audioModuleDataContractLists, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.audioModuleDataContractLists = audioModuleDataContractLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemAudioCellBinding binding = ItemAudioCellBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mContext, audioModuleDataContractLists.get(position), onItemClickListener);

        if (lastSelectedPosition != position){
            holder.binding.seekbarAudio.setVisibility(View.GONE);
            holder.binding.ivPauseAudio.setVisibility(View.GONE);
            holder.binding.ivPlayAudio.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return audioModuleDataContractLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemAudioCellBinding binding;

        public ViewHolder(ItemAudioCellBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Context mContext, AudioModuleDataContractList pdfModuleDataContractList, OnItemClickListener onItemClickListener) {
            binding.tvAudioName.setText(pdfModuleDataContractList.name);
            Glide.with(mContext).load(pdfModuleDataContractList.thumbnailImage)
                    .apply(new RequestOptions().placeholder(R.drawable.dummy_mp)).into(binding.ivAudio);
            binding.tvAudioDescription.setText(pdfModuleDataContractList.description);

            binding.ivPlayAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lastSelectedPosition = getAdapterPosition();
                    binding.seekbarAudio.setVisibility(View.VISIBLE);
                    binding.ivPauseAudio.setVisibility(View.VISIBLE);
                    binding.ivPlayAudio.setVisibility(View.GONE);
                    onItemClickListener.onPlayAudio(pdfModuleDataContractList.Mp3URL,binding.seekbarAudio, binding.ivPauseAudio,
                            binding.ivPlayAudio, binding.seekbar, binding.currentProgressTextView, binding.fileLengthTextView);
                }
            });

            binding.ivPauseAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.ivPlayAudio.setVisibility(View.VISIBLE);
                    binding.ivPauseAudio.setVisibility(View.GONE);
                    binding.seekbarAudio.setVisibility(View.GONE);
                    onItemClickListener.onStop();
                }
            });

            binding.tvDownload.setOnClickListener(view -> onItemClickListener.onDownload(getAdapterPosition()));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onPlayAudio(String mp3URL, RelativeLayout seekbarAudio, AppCompatTextView ivPlayAudio, AppCompatTextView ivPlayAudio1, AppCompatSeekBar seekbar, AppCompatTextView currentProgressTextView, AppCompatTextView fileLengthTextView);
        void onStop();
        void onDownload(int position);
    }
}