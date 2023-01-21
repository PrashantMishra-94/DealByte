package com.deal.bytee.fragment;

import static com.deal.bytee.activities.DashBoardActivity.bottomNavigation;
import static com.deal.bytee.activities.DashBoardActivity.viewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.deal.bytee.R;
import com.deal.bytee.Utils.App;
import com.deal.bytee.Utils.MySharedPreferences;
import com.deal.bytee.activities.LoginActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.makeramen.roundedimageview.RoundedImageView;

import kotlin.jvm.internal.Intrinsics;


public class ProfileFragment extends BaseFragment {

    View view;
    private CoordinatorLayout mainContent;
    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private LinearLayout llprofile;
    private RoundedImageView ivProfileImage;
    public static AppCompatTextView tvUserName;
    public static AppCompatTextView tvEmail;
    private AppCompatTextView tvEdit;
    private RelativeLayout rlEarning;
    private AppCompatTextView earningTxt;
    private CardView cvEarningBtn;
    private AppCompatTextView tvEarning;
    private RelativeLayout rlKYC;
    private AppCompatTextView tvKYCStatus;
    private RelativeLayout rlAccount;
    private RelativeLayout rlMyVouchers;
    private AppCompatTextView tvEventCreateCount;
    private RelativeLayout rlCallCenter;
    private AppCompatTextView eventGoingTxt;
    private AppCompatTextView tvEventGoingCount;
    private RelativeLayout rlLogOut;
    private RelativeLayout rlChangePass;
    private AppCompatTextView tvLog;
    private TabProfileFragment parent;


    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    private void initView(View vview) {
        mainContent = (CoordinatorLayout) vview.findViewById(R.id.main_content);
        appbar = (AppBarLayout) vview.findViewById(R.id.appbar);
        collapsingToolbar = (CollapsingToolbarLayout) vview.findViewById(R.id.collapsing_toolbar);
        llprofile = (LinearLayout) vview.findViewById(R.id.llprofile);
        ivProfileImage = (RoundedImageView) vview.findViewById(R.id.ivProfileImage);
        tvUserName = (AppCompatTextView) vview.findViewById(R.id.tvUserName);
        tvEmail = (AppCompatTextView) vview.findViewById(R.id.tvEmail);
        tvEdit = (AppCompatTextView) vview.findViewById(R.id.tvEdit);
        rlEarning = (RelativeLayout) vview.findViewById(R.id.rlEarning);
        earningTxt = (AppCompatTextView) vview.findViewById(R.id.earningTxt);
        cvEarningBtn = (CardView) vview.findViewById(R.id.cvEarningBtn);
        tvEarning = (AppCompatTextView) vview.findViewById(R.id.tvEarning);
        rlKYC = (RelativeLayout) vview.findViewById(R.id.rlKYC);
        tvKYCStatus = (AppCompatTextView) vview.findViewById(R.id.tvKYCStatus);
        rlAccount = (RelativeLayout) vview.findViewById(R.id.rlAccount);
        rlMyVouchers = (RelativeLayout) vview.findViewById(R.id.rlMyVouchers);
        tvEventCreateCount = (AppCompatTextView) vview.findViewById(R.id.tvEventCreateCount);
        rlCallCenter = (RelativeLayout) vview.findViewById(R.id.rlCallCenter);
        eventGoingTxt = (AppCompatTextView) vview.findViewById(R.id.eventGoingTxt);
        tvEventGoingCount = (AppCompatTextView) vview.findViewById(R.id.tvEventGoingCount);
        rlLogOut = (RelativeLayout) vview.findViewById(R.id.rlLogOut);
        rlChangePass = (RelativeLayout) vview.findViewById(R.id.rlChangePass);
        tvLog = (AppCompatTextView) vview.findViewById(R.id.tvLog);


        String islogin = "";
        if (App.sharedPreferences.chk(MySharedPreferences.isLogin)) {
            islogin = App.sharedPreferences.getKey(MySharedPreferences.isLogin);
        }
        if (islogin.equals(MySharedPreferences.YES)) {
            tvUserName.setText(App.sharedPreferences.getKey(MySharedPreferences.name));
            tvEmail.setText(App.sharedPreferences.getKey(MySharedPreferences.email));
            tvEarning.setText("Rs."+App.sharedPreferences.getKey(MySharedPreferences.cashback_earned));
            String profilePic = App.sharedPreferences.getKey(MySharedPreferences.profile_pic);
            if (profilePic != null && !profilePic.isEmpty()) {
                byte[] imageAsBytes = Base64.decode(profilePic.getBytes(), Base64.DEFAULT);
                ivProfileImage.setImageBitmap(
                        BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
                );
            }
        }
    }

    public static final class Companion {
        private Companion() {
        }

        public static final ProfileFragment instance() {
            return new ProfileFragment();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        initView(view);
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            this.parent = (TabProfileFragment) parentFragment;
        }
        bindClcik();
        return view;
    }

    private void bindClcik() {

        rlLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Logout?");
                builder.setIcon(R.drawable.ic_close);
                builder.setMessage("Are you sure you want to Logout?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        App.sharedPreferences.setKey(MySharedPreferences.isLogin, MySharedPreferences.NO);
                        App.sharedPreferences.setKey(MySharedPreferences.isSubscribe, MySharedPreferences.NO);
                        App.sharedPreferences.ClearAllData();
                        getActivity().finish();
                        startActivity(getActivity().getIntent());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditprofileClick();
            }
        });


        rlChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onchangePassClick();
            }
        });

        rlKYC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onKYCCLick();
            }
        });

        rlAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAccountCLick();
            }
        });

        rlMyVouchers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
                bottomNavigation.setSelectedItemId(R.id.nav_cart);
            }
        });
    }

    private void onchangePassClick() {
        TabProfileFragment tabHome = this.parent;
        if (tabHome != null) {
            BaseFragment instance = ChangePassFragment.Companion.instance();
            String name = ChangePassFragment.class.getName();
            Intrinsics.checkExpressionValueIsNotNull(name, "ChangePassFragment::class.java.name");
            tabHome.launchChild(instance, true, name);
        }
    }

    private void onAccountCLick() {
        TabProfileFragment tabHome = this.parent;
        if (tabHome != null) {
            BaseFragment instance = AddNewBankAccountFragment.Companion.instance();
            String name = AddNewBankAccountFragment.class.getName();
            Intrinsics.checkExpressionValueIsNotNull(name, "AddNewBankAccountFragment::class.java.name");
            tabHome.launchChild(instance, true, name);
        }

    }

    private void onKYCCLick() {
        TabProfileFragment tabHome = this.parent;
        if (tabHome != null) {
            BaseFragment instance = KYCFragment.Companion.instance();
            String name = KYCFragment.class.getName();
            Intrinsics.checkExpressionValueIsNotNull(name, "KYCFragment::class.java.name");
            tabHome.launchChild(instance, true, name);
        }
    }

    private void onEditprofileClick() {
        TabProfileFragment tabHome = this.parent;
        if (tabHome != null) {
            BaseFragment instance = EditProfileFragment.Companion.instance();
            String name = EditProfileFragment.class.getName();
            Intrinsics.checkExpressionValueIsNotNull(name, "EditProfileFragment::class.java.name");
            tabHome.launchChild(instance, true, name);
        }

    }
}