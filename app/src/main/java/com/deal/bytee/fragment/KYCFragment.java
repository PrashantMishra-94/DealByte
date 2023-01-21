package com.deal.bytee.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.deal.bytee.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;


public class KYCFragment extends BaseFragment {

    View view;
    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private LinearLayoutCompat llTop;
    private AppCompatImageView ivBack;
    private AppCompatTextView tvSave;
    private AppCompatImageView ivAdhaarCard;
    private AppCompatImageView ivPanCard;
    private AppCompatButton btnRRegister;

    public KYCFragment() {
        // Required empty public constructor
        super(R.layout.fragment_k_y_c);
    }

    private void initView(View view) {
        appbar = (AppBarLayout) view.findViewById(R.id.appbar);
        collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        llTop = (LinearLayoutCompat) view.findViewById(R.id.llTop);
        ivBack = (AppCompatImageView) view.findViewById(R.id.ivBack);
        tvSave = (AppCompatTextView) view.findViewById(R.id.tvSave);
        ivAdhaarCard = (AppCompatImageView) view.findViewById(R.id.ivAdhaarCard);
        ivPanCard = (AppCompatImageView) view.findViewById(R.id.ivPanCard);
        btnRRegister = (AppCompatButton) view.findViewById(R.id.btnRRegister);
    }

    public static final class Companion {
        private Companion() {
        }

        public static final KYCFragment instance() {
            return new KYCFragment();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_k_y_c, container, false);
        initView(view);
        bindClick();
        return view;
    }

    private void bindClick() {

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackStack();
            }
        });
    }
}