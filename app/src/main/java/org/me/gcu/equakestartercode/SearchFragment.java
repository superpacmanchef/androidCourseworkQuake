package org.me.gcu.equakestartercode;

import android.app.DatePickerDialog;
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
import android.widget.TextView;

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
    private TextView eastText;
    private TextView westText;
    private TextView northText;
    private TextView southText;
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

        eastText = (TextView) view.findViewById(R.id.eastText);
        westText = (TextView) view.findViewById(R.id.westText);
        northText = (TextView) view.findViewById(R.id.northText);
        southText = (TextView) view.findViewById(R.id.southText);

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
        if (itemList.size() > 0) {
            eastText.setText(rightist.getGeoLong());
            westText.setText(leftist.getGeoLong());
            northText.setText(upist.getGeoLat());
            southText.setText(downist.getGeoLat());
        } else {
            String nope = "none matched criteria";
            eastText.setText(nope);
            westText.setText(nope);
            northText.setText(nope);
            southText.setText(nope);
        }


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

    ;
}
