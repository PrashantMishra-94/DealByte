package com.deal.bytee.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;

import com.deal.bytee.R;

import kotlin.jvm.internal.Intrinsics;


public class TabCartFragment extends BaseFragment {

    View view;
    private FrameLayout cartContainer;

    public TabCartFragment() {
        super(R.layout.fragment_tab_cart);
    }

    public static final class Companion {
        private Companion() {
        }

        public static final TabCartFragment instance() {
            return new TabCartFragment();
        }
    }

    private final void loadCart() {
        //BaseFragment instance = CartFragment.Companion.instance();
        BaseFragment instance = VouchersFragment.Companion.instance();
        //String name = CartFragment.class.getName();
        String name = VouchersFragment.class.getName();
        Intrinsics.checkExpressionValueIsNotNull(name, "CartFragment.javaClass.name");
        launchChild(instance, false, name);
    }

    public final void launchChild(BaseFragment fragment, boolean isPostBack, String tag) {
        Intrinsics.checkParameterIsNotNull(fragment, "fragment");
        Intrinsics.checkParameterIsNotNull(tag, "tag");
        startChildFragment(R.id.cartContainer, fragment, isPostBack, tag);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab_cart, container, false);
        initView(view);
        loadCart();
        return view;
    }

    private void initView(View view) {
        cartContainer = (FrameLayout) view.findViewById(R.id.cartContainer);
    }
}