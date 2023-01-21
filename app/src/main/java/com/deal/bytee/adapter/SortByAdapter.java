package com.deal.bytee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.deal.bytee.R;
import com.deal.bytee.model.SortByModel;

import java.util.List;

public class SortByAdapter extends RecyclerView.Adapter<SortByAdapter.MyViewHolder> {

    Context context;
    List<SortByModel> sortByModelList;

    public SortByAdapter(Context context, List<SortByModel> sortByModelList) {
        this.context = context;
        this.sortByModelList = sortByModelList;
    }

    public SortByAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public SortByAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_sort_by, parent, false);
        return new SortByAdapter.MyViewHolder(inflate);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull SortByAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SortByModel model = sortByModelList.get(position);

        holder.tvName.setText(model.getName());
        if (model.isSelected) {
            holder.ivDone.setVisibility(View.VISIBLE);
            holder.tvName.setTextColor(context.getColor(R.color.red));
        } else {
            holder.ivDone.setVisibility(View.GONE);
            holder.tvName.setTextColor(context.getColor(R.color.black));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMultiple(position);
            }
        });
    }

    private void checkMultiple(int position) {
        SortByModel va = sortByModelList.get(position);
        va.isSelected = !va.isSelected;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return sortByModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView ivDone;
        AppCompatTextView tvName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivDone = itemView.findViewById(R.id.ivDone);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}
