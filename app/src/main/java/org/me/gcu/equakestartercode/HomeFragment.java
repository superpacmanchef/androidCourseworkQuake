package org.me.gcu.equakestartercode;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {

    private LinearLayout liner;
    private IBottomNavMove bottomNavMove;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            liner = (LinearLayout)view.findViewById(R.id.liner);
            bottomNavMove = (IBottomNavMove)requireActivity();
            startProgress();
    }

    @Override
    public void onResume() {
        super.onResume();
        bottomNavMove.bottomNavMoved("Home");
    }

    private void startProgress()
    {
        ItemViewModel itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        itemViewModel.getItems().observe(this , items -> {
            for (int i =0 ; i < items.size() ; i++)
            {
                Item selectedItem = items.get(i);
                String location = selectedItem.getLocation();
                String magnitude = selectedItem.getMagnitude();

                MaterialButton btn = new MaterialButton(getContext());
                btn.setText("location: " + location + "\n" + "mangitude: " + magnitude);
                btn.setTextColor(Color.BLACK);
                btn.setOnClickListener(v -> {
                    Intent intent = new Intent(getContext(), QuakeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Item", selectedItem);
                    intent.putExtras(bundle);
                    startActivity(intent);
                });

                if (Float.parseFloat(selectedItem.getMagnitude()) < 1) {
                    btn.setBackgroundColor(Color.GREEN);
                } else if (Float.parseFloat(selectedItem.getMagnitude()) >= 1 && Float.parseFloat(selectedItem.getMagnitude()) < 3) {
                    btn.setBackgroundColor(Color.YELLOW);
                } else if (Float.parseFloat(selectedItem.getMagnitude()) >= 3) {
                    btn.setBackgroundColor(Color.RED);
                }

                liner.addView(btn);
            }
        });
    }


}