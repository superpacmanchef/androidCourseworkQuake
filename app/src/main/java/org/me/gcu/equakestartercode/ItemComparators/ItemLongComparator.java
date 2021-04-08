package org.me.gcu.equakestartercode.ItemComparators;
//Jay Malley
//s1703629

import org.me.gcu.equakestartercode.models.Item;

import java.util.Comparator;

public class ItemLongComparator implements Comparator<Item> {
    @Override
    public int compare(Item o1, Item o2) {
        String long1 = o1.getGeoLong();
        String long2 = o2.getGeoLong();
        if(Float.parseFloat(long1) == Float.parseFloat(long2))
        {
            return 0;
        }
        else if(Float.parseFloat(long1) >Float.parseFloat(long2))
        {
            return 1;
        }
        else {
            return -1;
        }
    }
}
