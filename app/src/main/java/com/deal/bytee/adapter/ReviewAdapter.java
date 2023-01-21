package com.deal.bytee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.deal.bytee.R;
import com.deal.bytee.model.RestaurantModel;
import com.deal.bytee.model.ReviewModel;
import com.makeramen.roundedimageview.RoundedImageView;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    Context context;
    List<ReviewModel> reviewModelList;

    public ReviewAdapter(Context context, List<ReviewModel> reviewModelList) {
        this.context = context;
        this.reviewModelList = reviewModelList;
    }

    public ReviewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_review, parent, false);
        return new ReviewAdapter.MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

      /*  Glide.with(holder.ivImage.getContext()).load("https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcQU2JRbbl3LBOm_an3eI5iplFhOoLESyBwUfmWDO49BS1EYuGUE")
                .thumbnail(Glide.with(context).load(R.drawable.logoo))
                .into(holder.ivImage);*/

        ReviewModel model = reviewModelList.get(position);
        Glide.with(holder.ivImage.getContext()).load(model.getReviewer_image())
                .placeholder(R.drawable.logoo)
                .into(holder.ivImage);

        holder.tvName.setText(model.getReviewer_name());
        holder.tvTime.setText(model.getTime());
        holder.tvReviewtxt.setText(model.getReview_text());
        System.out.println("rateeee>>>>>>"+Float.parseFloat(model.getRating()));
        holder.simpleRatingBar.setRating(Float.parseFloat(model.getRating()));

    }


    @Override
    public int getItemCount() {
        return reviewModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView ivImage;
        AppCompatTextView tvName;
        ScaleRatingBar simpleRatingBar;
        AppCompatTextView tvReviewtxt;
        AppCompatTextView tvTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
            simpleRatingBar = itemView.findViewById(R.id.simpleRatingBar);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvReviewtxt = itemView.findViewById(R.id.tvReviewtxt);
        }
    }
}
