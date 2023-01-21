package com.deal.bytee.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.deal.bytee.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;


public class AddNewBankAccountFragment extends BaseFragment {

    View view;
    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private LinearLayoutCompat llTop;
    private AppCompatImageView ivBack;
    private AppCompatTextView tvSave;
    private AppCompatTextView txtBank;
    private LinearLayout linearAccount;
    private LinearLayout linearAccNum;
    private LinearLayout linearBankCode;
    private LinearLayout linearBranch;
    private AppCompatButton btnAdd;


    public AddNewBankAccountFragment() {
        // Required empty public constructor
        super(R.layout.fragment_add_new_bank_account);
    }

    private void initView(View view) {
        appbar = (AppBarLayout) view.findViewById(R.id.appbar);
        collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        llTop = (LinearLayoutCompat) view.findViewById(R.id.llTop);
        ivBack = (AppCompatImageView) view.findViewById(R.id.ivBack);
        tvSave = (AppCompatTextView) view.findViewById(R.id.tvSave);
        txtBank = (AppCompatTextView) view.findViewById(R.id.txt_bank);
        linearAccount = (LinearLayout) view.findViewById(R.id.linear_account);
        linearAccNum = (LinearLayout) view.findViewById(R.id.linear_acc_num);
        linearBankCode = (LinearLayout) view.findViewById(R.id.linear_bank_code);
        linearBranch = (LinearLayout) view.findViewById(R.id.linear_branch);
        btnAdd = (AppCompatButton) view.findViewById(R.id.btnAdd);
    }


    public static final class Companion {
        private Companion() {
        }

        public static final AddNewBankAccountFragment instance() {
            return new AddNewBankAccountFragment();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_new_bank_account, container, false);
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