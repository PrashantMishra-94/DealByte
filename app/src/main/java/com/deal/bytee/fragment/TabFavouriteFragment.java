package com.deal.bytee.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.deal.bytee.R;

import kotlin.jvm.internal.Intrinsics;


public class TabFavouriteFragment extends BaseFragment {


    View view;
    private FrameLayout favouriteContainer;

    public TabFavouriteFragment() {
        super(R.layout.fragment_tab_favourite);
    }

    private void initView(View view) {
        favouriteContainer = (FrameLayout) view.findViewById(R.id.favouriteContainer);
    }

    public static final class Companion {
        private Companion() {
        }

        public static final TabFavouriteFragment instance() {
            return new TabFavouriteFragment();
        }
    }

    private final void loadFavourite() {
        BaseFragment instance = FavouriteFragment.Companion.instance();
        String name = FavouriteFragment.class.getName();
        Intrinsics.checkExpressionValueIsNotNull(name, "FavouriteFragment.javaClass.name");
        launchChild(instance, false, name);
    }

    public final void launchChild(BaseFragment fragment, boolean isPostBack, String tag) {
        Intrinsics.checkParameterIsNotNull(fragment, "fragment");
        Intrinsics.checkParameterIsNotNull(tag, "tag");
        startChildFragment(R.id.favouriteContainer, fragment, isPostBack, tag);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab_favourite, container, false);
        initView(view);
        loadFavourite();
        return view;
    }
}