package com.deal.bytee.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import kotlin.jvm.internal.Intrinsics;

public final class TabAdapter extends FragmentStatePagerAdapter {

    private final ArrayList<Fragment> mFragmentList;
    private final ArrayList<String> mFragmentTitleList;

    public TabAdapter(@NonNull FragmentManager fm) {
        super(fm);
        this.mFragmentList = new ArrayList();
        this.mFragmentTitleList = new ArrayList();
    }


    public TabAdapter(@NonNull FragmentManager fm, ArrayList<Fragment> mFragmentList, ArrayList<String> mFragmentTitleList) {
        super(fm);
        this.mFragmentList = mFragmentList;
        this.mFragmentTitleList = mFragmentTitleList;
    }

    public TabAdapter(@NonNull FragmentManager fm, int behavior, ArrayList<Fragment> mFragmentList, ArrayList<String> mFragmentTitleList) {
        super(fm, behavior);
        this.mFragmentList = mFragmentList;
        this.mFragmentTitleList = mFragmentTitleList;
    }

    public final void addFragment(Fragment fragment, String string2) {
        Intrinsics.checkParameterIsNotNull((Object) fragment, (String) "fragment");
        Intrinsics.checkParameterIsNotNull((Object) string2, (String) "title");
        this.mFragmentList.add(fragment);
        this.mFragmentTitleList.add(string2);
    }

    public int getCount() {
        return this.mFragmentList.size();
    }

    public Fragment getItem(int n) {
        Object object = this.mFragmentList.get(n);
        Intrinsics.checkExpressionValueIsNotNull((Object) object, (String) "mFragmentList[position]");
        return (Fragment) object;
    }

    public String getPageTitle(int n) {
        Object object = this.mFragmentTitleList.get(n);
        Intrinsics.checkExpressionValueIsNotNull((Object) object, (String) "mFragmentTitleList[position]");
        return (String) object;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}