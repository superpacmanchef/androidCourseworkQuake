package org.me.gcu.equakestartercode.fragments;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import android.widget.ScrollView;


import org.jetbrains.annotations.NotNull;
import org.me.gcu.equakestartercode.UIComponents.QuakeButton;
import org.me.gcu.equakestartercode.interfaces.IBottomNavMove;
import org.me.gcu.equakestartercode.models.Item;
import org.me.gcu.equakestartercode.viewmodels.ItemViewModel;
import org.me.gcu.equakestartercode.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment{

    private LinearLayout liner;
    private IBottomNavMove bottomNavMove;
    private List<Item> items ;
    private ItemViewModel itemViewModel;
    private SwipeRefreshLayout swipeRefresh;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        liner = (LinearLayout) view.findViewById(R.id.liner);
        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // cancle the Visual indication of a refresh
                        swipeRefresh.setRefreshing(false);
                        // Generate a random integer number
                        thing();
                    }
                }, 3000);
            }
        });

        bottomNavMove = (IBottomNavMove) requireActivity();
        itemViewModel.getItems().observe(this, listItems -> {
                Collections.sort(listItems);
                items = listItems;
                startThing();
        });

    }


    private void thing(){
        itemViewModel.getItems().observe(this, listItems -> {
            Collections.sort(listItems);
            items = listItems;
            startThing();
        });
    }

    //Move Bottom Nav to correct position if not explciitly clcik on
    //Bottom Nav - i.e when pressing back
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden == false) {
            bottomNavMove.bottomNavMoved("Home");
        }
        itemViewModel.getItems().observe(this, listItems -> {
            Collections.sort(listItems);
            if(listItems != items) {
                items = listItems;
                startThing();
            }
        });
    }

    private void startThing() {
        liner.removeAllViews();
        LinearLayout linearLayout = null;
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        boolean orientaion = false;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            orientaion = true;
            param = new LinearLayout.LayoutParams(
                    3,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0.7f);
            param.setMargins(8, 0, 8, 0);
        }


        for (int i = 0; i < items.size(); i++) {
            Item selectedItem = items.get(i);
            QuakeButton btn = new QuakeButton(getContext(), selectedItem, param);

            if (orientaion == true) {
                if (i % 2 == 0 && i == items.size() - 1) {
                    linearLayout = new LinearLayout(getContext());

                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayout.addView(btn);
                    liner.addView(linearLayout);
                } else if (i % 2 == 0) {
                    linearLayout = new LinearLayout(getContext());

                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayout.addView(btn);
                } else {
                    linearLayout.addView(btn);
                    liner.addView(linearLayout);
                }

            } else {
                liner.addView(btn);
            }
        }
    }
}