package com.deal.bytee.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deal.bytee.R;
import com.deal.bytee.adapter.SortByAdapter;
import com.deal.bytee.model.SortByModel;

import java.util.ArrayList;
import java.util.List;

public class FilterFragment extends BaseFragment {


    View view;
    private NestedScrollView nestedScrollView;
    private AppCompatTextView tvClear;
    private RecyclerView rvSortBy;
    List<SortByModel> sortByModelList = new ArrayList<>();
    List<SortByModel> filterList = new ArrayList<>();
    SortByAdapter sortByAdapter;
    private RecyclerView rvFilter;


    public FilterFragment() {
        super(R.layout.fragment_filter);
    }

    private void initView(View view) {
        nestedScrollView = (NestedScrollView) view.findViewById(R.id.nestedScrollView);
        tvClear = (AppCompatTextView) view.findViewById(R.id.tvClear);
        rvSortBy = (RecyclerView) view.findViewById(R.id.rvSortBy);
        rvFilter = (RecyclerView) view.findViewById(R.id.rvFilter);
    }

    public static final class Companion {
        private Companion() {
        }

        public static final FilterFragment instance() {
            return new FilterFragment();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_filter, container, false);
        initView(view);
        initSortBy();
        initFilter();
        return view;
    }

    private void initFilter() {
        filterList.clear();
        for (int i = 0; i < 3; i++) {
            SortByModel model = new SortByModel();
            if (i == 0) {
                model.setName("Open Now");
                model.isSelected=true;
            } else if (i == 1) {
                model.setName("Credit Cards");
                model.isSelected=true;
            } else if (i == 2) {
                model.setName("Alcohol Served");
            }
            filterList.add(model);
        }
        rvFilter.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        String name = FilterFragment.class.getName();
        sortByAdapter = new SortByAdapter(getActivity(), filterList);
        rvFilter.setAdapter(sortByAdapter);
        rvFilter.setNestedScrollingEnabled(false);

    }

    private void initSortBy() {
        sortByModelList.clear();
        for (int i = 0; i < 5; i++) {
            SortByModel model = new SortByModel();
            if (i == 0) {
                model.setName("Top Rated");
                model.isSelected=true;
            } else if (i == 1) {
                model.setName("Nearest Me");
            } else if (i == 2) {
                model.setName("Cost High to Low");
            } else if (i == 3) {
                model.setName("Cost Low to High");
            } else if (i == 4) {
                model.setName("Most Popular");
            }
            sortByModelList.add(model);
        }

        rvSortBy.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        String name = FilterFragment.class.getName();
        sortByAdapter = new SortByAdapter(getActivity(), sortByModelList);
        rvSortBy.setAdapter(sortByAdapter);
        rvSortBy.setNestedScrollingEnabled(false);

    }
}