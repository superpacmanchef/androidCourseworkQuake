package org.me.gcu.equakestartercode.ItemComparators;
//Jay Malley
//s1703629

import org.me.gcu.equakestartercode.models.Item;

import java.util.Comparator;

public class ItemLatComparator implements Comparator<Item> {

    //Default
    @Override
    public int compare(Item o1, Item o2) {
        if(Float.parseFloat(o1.getGeoLat()) == Float.parseFloat(o2.getGeoLat()))
        {
            return 0;
        }
        else if(Float.parseFloat(o1.getGeoLat()) >Float.parseFloat(o2.getGeoLat()) )
        {
            return -1;
        }
        else {
            return 1;
        }
    }
}
