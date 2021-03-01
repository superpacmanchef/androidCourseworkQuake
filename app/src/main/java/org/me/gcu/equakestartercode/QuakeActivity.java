package org.me.gcu.equakestartercode;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QuakeActivity extends AppCompatActivity
{
    private TextView pubDate;
    private TextView title;
    private TextView desc;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quake_more);
        Item selectedItem = (Item)getIntent().getExtras().getSerializable("Item") ;

        pubDate = (TextView)findViewById(R.id.PubDate);
        pubDate.setText(selectedItem.getPubDate());
        title = (TextView)findViewById(R.id.Title);
        title.setText(selectedItem.getTitle());
        desc = (TextView)findViewById(R.id.Desc);
        desc.setText(selectedItem.getCategory());
    }
}
