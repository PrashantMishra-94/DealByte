package com.deal.bytee.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.deal.bytee.R;

import kotlin.jvm.internal.Intrinsics;


public class TabDiscoverFragment extends BaseFragment {

    View view;
    private FrameLayout discoverContainer;

    public TabDiscoverFragment() {
        super(R.layout.fragment_tab_discover);
    }


    public static final class Companion {
        private Companion() {
        }

        public static final TabDiscoverFragment instance() {
            return new TabDiscoverFragment();
        }
    }

    private final void loadDiscover() {
        BaseFragment instance = DiscoverFragment.Companion.instance();
        String name = DiscoverFragment.class.getName();
        Intrinsics.checkExpressionValueIsNotNull(name, "DiscoverFragment.javaClass.name");
        launchChild(instance, false, name);
    }

    public final void launchChild(BaseFragment fragment, boolean isPostBack, String tag) {
        Intrinsics.checkParameterIsNotNull(fragment, "fragment");
        Intrinsics.checkParameterIsNotNull(tag, "tag");
        startChildFragment(R.id.discoverContainer, fragment, isPostBack, tag);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab_discover, container, false);
        initView(view);
        loadDiscover();
        return view;
    }

    private void initView(View view) {
        discoverContainer = (FrameLayout) view.findViewById(R.id.discoverContainer);
    }
}