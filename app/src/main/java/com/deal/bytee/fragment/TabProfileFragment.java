package com.deal.bytee.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.deal.bytee.R;

import kotlin.jvm.internal.Intrinsics;


public class TabProfileFragment extends BaseFragment {

    View view;
    private FrameLayout profileContainer;

    public TabProfileFragment() {
        super(R.layout.fragment_tab_profile);
    }

    private void initView(View view) {
        profileContainer = (FrameLayout) view.findViewById(R.id.profileContainer);
    }

    public static final class Companion {
        private Companion() {
        }

        public static final TabProfileFragment instance() {
            return new TabProfileFragment();
        }
    }

    private final void loadProfile() {
        BaseFragment instance = ProfileFragment.Companion.instance();
        String name = ProfileFragment.class.getName();
        Intrinsics.checkExpressionValueIsNotNull(name, "ProfileFragment.javaClass.name");
        launchChild(instance, false, name);
    }

    public final void launchChild(BaseFragment fragment, boolean isPostBack, String tag) {
        Intrinsics.checkParameterIsNotNull(fragment, "fragment");
        Intrinsics.checkParameterIsNotNull(tag, "tag");
        startChildFragment(R.id.profileContainer, fragment, isPostBack, tag);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_tab_profile, container, false);
        initView(view);
        loadProfile();
        return view;
    }
}