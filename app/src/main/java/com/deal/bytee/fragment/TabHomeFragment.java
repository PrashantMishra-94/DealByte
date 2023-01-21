package com.deal.bytee.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.deal.bytee.R;

import kotlin.jvm.internal.Intrinsics;


public class TabHomeFragment extends BaseFragment {

    View view;
    private FrameLayout homeContainer;

    public TabHomeFragment() {
        super(R.layout.fragment_tab_home);
    }

    public static final class Companion {
        private Companion() {
        }

        public static final TabHomeFragment instance() {
            return new TabHomeFragment();
        }
    }

    private final void loadHome() {
        BaseFragment instance = HomeFragment.Companion.instance();
        String name = HomeFragment.class.getName();
        Intrinsics.checkExpressionValueIsNotNull(name, "HomeFragment.javaClass.name");
        launchChild(instance, false, name);
    }

    public final void launchChild(BaseFragment fragment, boolean isPostBack, String tag) {
        Intrinsics.checkParameterIsNotNull(fragment, "fragment");
        Intrinsics.checkParameterIsNotNull(tag, "tag");
        startChildFragment(R.id.homeContainer, fragment, isPostBack, tag);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab_home, container, false);
        initView(view);
        loadHome();
        return view;
    }

    private void initView(View view) {
        homeContainer = (FrameLayout) view.findViewById(R.id.homeContainer);
    }

}