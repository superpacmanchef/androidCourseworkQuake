package org.me.gcu.equakestartercode.fragments;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import org.jetbrains.annotations.NotNull;
import org.me.gcu.equakestartercode.UIComponents.QuakeButton;
import org.me.gcu.equakestartercode.interfaces.IBottomNavMove;
import org.me.gcu.equakestartercode.models.Item;
import org.me.gcu.equakestartercode.viewmodels.ItemViewModel;
import org.me.gcu.equakestartercode.R;

import java.util.Collections;

public class HomeFragment extends Fragment {

    private LinearLayout liner;
    private IBottomNavMove bottomNavMove;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
            super.onViewCreated(view, savedInstanceState);
            liner = (LinearLayout)view.findViewById(R.id.liner);
            bottomNavMove = (IBottomNavMove)requireActivity();
            startProgress();
    }

    //Move Bottom Nav to correct position if not explciitly clcik on
    //Bottom Nav - i.e when pressing back
    @Override
    public void onResume()
    {
        super.onResume();
        bottomNavMove.bottomNavMoved("Home");
    }

    //TODO : REDO THIS CAUSE ITS SHIT ALSO ADD HIGH TO LOW AND LOW TO HIGH SORTING
    private void startProgress()
    {
        ItemViewModel itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        itemViewModel.getItems().observe(this , items -> {
            LinearLayout linearLayout = null;
            LinearLayout.LayoutParams param  = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT , LinearLayout.LayoutParams.WRAP_CONTENT);
            boolean orientaion  = false ;

            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                orientaion = true;
                param = new LinearLayout.LayoutParams(
                        3,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0.7f);
                param.setMargins(8, 0, 8, 0);
            }

            Collections.sort(items);
            for (int i = 0; i < items.size(); i++)
            {
                    Item selectedItem = items.get(i);
                    QuakeButton btn = new QuakeButton(getContext() , selectedItem , param);

                if(orientaion == true) {
                    if(i % 2 == 0 && i == items.size() -1 ) {
                        linearLayout = new LinearLayout(getContext());

                        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                        linearLayout.addView(btn);
                        liner.addView(linearLayout);
                    }
                   else if (i % 2 == 0) {
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

        });
    }

}