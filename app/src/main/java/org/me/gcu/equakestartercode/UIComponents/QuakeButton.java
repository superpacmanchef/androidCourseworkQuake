package org.me.gcu.equakestartercode.UIComponents;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;

import org.me.gcu.equakestartercode.activites.QuakeActivity;
import org.me.gcu.equakestartercode.models.Item;

public class QuakeButton extends MaterialButton {
    public QuakeButton(@NonNull Context context , Item item , LinearLayout.LayoutParams params) {
        super(context);
        this.setText("location: " + item.getLocation() + "\n" + "mangitude: " + item.getMagnitude());
        this.setTextColor(Color.BLACK);

        this.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), QuakeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Item", item);
            intent.putExtras(bundle);
            getContext().startActivity(intent);
        });
        this.setLayoutParams(params);

        if (Float.parseFloat(item.getMagnitude()) < 1) {
            this.setBackgroundColor(Color.GREEN);
        } else if (Float.parseFloat(item.getMagnitude()) >= 1 && Float.parseFloat(item.getMagnitude()) < 3) {
            this.setBackgroundColor(Color.YELLOW);
        } else if (Float.parseFloat(item.getMagnitude()) >= 3) {
            this.setBackgroundColor(Color.RED);
        }
    }

    public QuakeButton(@NonNull Context context , Item item ) {
        super(context);
        this.setText("location: " + item.getLocation() + "\n" + "mangitude: " + item.getMagnitude());
        this.setTextColor(Color.BLACK);

        this.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), QuakeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Item", item);
            intent.putExtras(bundle);
            getContext().startActivity(intent);
        });

        if (Float.parseFloat(item.getMagnitude()) < 1) {
            this.setBackgroundColor(Color.GREEN);
        } else if (Float.parseFloat(item.getMagnitude()) >= 1 && Float.parseFloat(item.getMagnitude()) < 3) {
            this.setBackgroundColor(Color.YELLOW);
        } else if (Float.parseFloat(item.getMagnitude()) >= 3) {
            this.setBackgroundColor(Color.RED);
        }
    }
}
