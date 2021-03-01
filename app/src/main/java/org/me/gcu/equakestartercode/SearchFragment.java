package org.me.gcu.equakestartercode;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SearchFragment extends Fragment implements View.OnClickListener {

    private Button search;
    private EditText startDate;
    private EditText endDate;
    private DatePickerDialog.OnDateSetListener from_dateListener, to_dateListener;
    private List<Item> items;
    private LinearLayout eastLinear;
    private LinearLayout westLinear;
    private LinearLayout northLinear;
    private LinearLayout southLinear;
    private LinearLayout biggestLinear;
    private LinearLayout deepestLinear;
    private LinearLayout shallowestLinear;


    private IBottomNavMove bottomNavMove;


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ItemViewModel itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        itemViewModel.getItems().observe(this, listItems -> {
            items = listItems;
        });

        //Sets EditTexts startDate and endDate to uneditable and on press show DatePickerDialog.
        startDate = (EditText) view.findViewById(R.id.startDate);
        startDate.setKeyListener(null);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), from_dateListener, 2021, 01, 01);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });
        from_dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String t = dayOfMonth + "-" + (month + 1) + "-" + year;
                startDate.setText(t);
            }
        };

        endDate = (EditText) view.findViewById(R.id.endDate);
        endDate.setKeyListener(null);
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), to_dateListener, 2021, 01, 01);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());

                datePickerDialog.show();
            }
        });
        to_dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String t = dayOfMonth + "-" + (month + 1) + "-" + year;
                endDate.setText(t);
            }
        };

        //Sets default text for startDate and endDate to current Date.
        startDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        endDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));

        search = (Button) view.findViewById(R.id.search);
        search.setOnClickListener(this::onClick);

        eastLinear = (LinearLayout) view.findViewById(R.id.eastLinear);
        westLinear = (LinearLayout) view.findViewById(R.id.westLinear);
        northLinear = (LinearLayout) view.findViewById(R.id.northLinear);
        southLinear = (LinearLayout) view.findViewById(R.id.southLinear);
        biggestLinear = (LinearLayout) view.findViewById(R.id.biggestLinear);
        deepestLinear = (LinearLayout) view.findViewById(R.id.deepestLinear);
        shallowestLinear = (LinearLayout) view.findViewById(R.id.shallowestLinear);

        bottomNavMove = (IBottomNavMove) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Move bottom nav selected on back press.
        bottomNavMove.bottomNavMoved("Search");
    }

    @Override
    public void onClick(View v) {
        if (v == search) {
            searchDate(startDate.getText().toString(), endDate.getText().toString());
        }
    }

    private void searchDate(String startDate, String endDate) {
        Date startDateDate = parseDate(startDate);
        Date endDateDate = parseDate(endDate);
        ArrayList<Item> itemList = new ArrayList<Item>();

        Item leftist = null;
        Item rightist = null;
        Item upist = null;
        Item downist = null;
        Item biggest = null;
        Item deepest = null;
        Item shallowist = null;

        //Returns list of Items which falls between startDate and endDate.
        for (int i = 0; i < items.size(); i++) {
            Date date = stringToDate(items.get(i).getPubDate());
            if (date.after(startDateDate) && date.before(endDateDate)) {
                itemList.add(items.get(i));
            }
        }

        //Returns the leftist , rightist etc... from the list of Items.
        for (int j = 0; j < itemList.size(); j++) {
            Item currentItem = itemList.get(j);

            if (j == 0) {
                leftist = currentItem;
                rightist = currentItem;
                upist = currentItem;
                downist = currentItem;
                deepest = currentItem;
                shallowist = currentItem;
                biggest = currentItem;
            } else {
                if (Float.parseFloat(leftist.getGeoLong()) > Float.parseFloat(currentItem.getGeoLong())) {
                    leftist = currentItem;
                }

                if (Float.parseFloat(rightist.getGeoLong()) < Float.parseFloat(currentItem.getGeoLong())) {
                    rightist = currentItem;
                }

                if (Float.parseFloat(upist.getGeoLat()) < Float.parseFloat(currentItem.getGeoLat())) {
                    upist = currentItem;
                }

                if (Float.parseFloat(downist.getGeoLat()) > Float.parseFloat(currentItem.getGeoLat())) {
                    downist = currentItem;
                }
                if (Float.parseFloat(biggest.getMagnitude()) < Float.parseFloat(currentItem.getMagnitude())) {
                    biggest = currentItem;
                }

                if (Float.parseFloat(deepest.getDepth().substring(0, 2)) < Float.parseFloat(currentItem.getDepth().substring(0, 2))) {
                    deepest = currentItem;
                }

                if (Float.parseFloat(shallowist.getDepth().substring(0, 2)) > Float.parseFloat(currentItem.getDepth().substring(0, 2))) {
                    shallowist = currentItem;
                }
            }


        }

        //Sets text of each of the specific Items.
        ScrollView sc = (ScrollView) getView().findViewById(R.id.scrollView3);
        sc.setVisibility(View.VISIBLE);
        if (itemList.size() > 0) {
            eastLinear.addView(createButton(rightist));
            westLinear.addView(createButton(leftist));
            northLinear.addView(createButton(upist));
            southLinear.addView(createButton(upist));
            biggestLinear.addView(createButton(biggest));
            deepestLinear.addView(createButton(deepest));
            shallowestLinear.addView(createButton(shallowist));
        } else {
            String nope = "No Earthquakes recorded during that time period";
            TextView t = new TextView(requireContext());
            t.setText(nope);
            eastLinear.addView(t);
            westLinear.addView(t);
            northLinear.addView(t);
            southLinear.addView(t);
            biggestLinear.addView(t);
            deepestLinear.addView(t);
            shallowestLinear.addView(t);
        }


    }

    private Button createButton(Item selectedItem)
    {

        String location = selectedItem.getLocation();

        MaterialButton btn = new MaterialButton(getContext());
        btn.setText("location: " + location);
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
        return btn;

    }
    private Date parseDate(String date) {
        SimpleDateFormat dateFormatOut = new SimpleDateFormat("dd-MM-yyyy");
        Date outDate = null;
        try {
            outDate = dateFormatOut.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outDate;
    }

    private Date stringToDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        Date outDate = null;
        try {
            outDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outDate;

    }


}
