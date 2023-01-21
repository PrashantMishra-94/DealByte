package com.deal.bytee.fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.deal.bytee.R;

import kotlin.jvm.internal.Intrinsics;

public class BaseFragment extends Fragment {


    public void onDestroyView() {
        super.onDestroyView();
    }

    public BaseFragment(int layout) {
        super(layout);
    }

    public BaseFragment() {
        super();
    }

    public final void startChildFragment(int containerViewId, Fragment fragment, boolean addToBackStack, String tag) {
        Intrinsics.checkParameterIsNotNull(fragment, "fragment");
        Intrinsics.checkParameterIsNotNull(tag, "tag");
        FragmentManager fragmentManager = getChildFragmentManager();
        Intrinsics.checkExpressionValueIsNotNull(fragmentManager, "childFragmentManager");
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_close_exit);
        fragmentTransaction.add(containerViewId, fragment, tag);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();

    }

    public final void handleBackStack() {
        try {
            FragmentActivity activity = getActivity();
            if (activity == null) {
                Intrinsics.throwNpe();
            }
            Intrinsics.checkExpressionValueIsNotNull(activity, "activity!!");
            FragmentManager fm = activity.getSupportFragmentManager();
            Intrinsics.checkExpressionValueIsNotNull(fm, "activity!!.supportFragmentManager");
            for (Fragment frag : fm.getFragments()) {
                Intrinsics.checkExpressionValueIsNotNull(frag, "frag");
                FragmentManager childFragmentManager = frag.getChildFragmentManager();
                Intrinsics.checkExpressionValueIsNotNull(childFragmentManager, "frag.childFragmentManager");
                if (frag.isVisible() && childFragmentManager.getBackStackEntryCount() > 0) {
                    int backStackEntryCount = childFragmentManager.getBackStackEntryCount();
                    for (int i = 0; i < backStackEntryCount; i++) {
                        Object obj = childFragmentManager.getFragments().get(i);
                        Intrinsics.checkExpressionValueIsNotNull(obj, "childFragmentManager.fragments[i]");
                        if (((Fragment) obj).isVisible()) {
                            childFragmentManager.popBackStack();
                            return;
                        }
                    }
                    continue;
                }
            }

            FragmentActivity activity2 = getActivity();
            if (activity2 == null) {
                Intrinsics.throwNpe();
            }
            activity2.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
