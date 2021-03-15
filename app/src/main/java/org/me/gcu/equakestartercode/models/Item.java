package org.me.gcu.equakestartercode.models;

import android.util.Log;

import java.io.Serializable;

public class Item  implements Serializable, Comparable< Item > {

    private String title;
    private  String description;
    private String link;
    private String pubDate;
    private String category;
    private String geoLat;
    private String  geoLong;
    private String location;
    private String depth;
    private String magnitude;

    public Item(String title , String description , String link , String category , String pubDate , String geoLat  , String geoLong){
        this.title = title;
        this.description = description;
        this.link = link;
        this.category = category;
        this.pubDate = pubDate;
        this.geoLat = geoLat;
        this.geoLong = geoLong;
    }

    public Item(){}

    //Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getCategory() {
        return category;
    }

    public String getGeoLat() {
        return geoLat;
    }

    public String getGeoLong() {
        return geoLong;
    }

    public String getLocation() {
        return location;
    }

    public String getDepth() {
        return depth;
    }

    public String getMagnitude() {
        return magnitude;
    }



    //Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
        String[] tokens = description.split(";|: ");

        for(int i = 0 ; i < tokens.length ; i++)
        {
           if (i == 3)
           {
               this.setLocation(tokens[i].trim());
           }
           else if (i == 7)
           {
               this.setDepth(tokens[i].trim());
           }
           else if (i == 9)
           {
               this.setMagnitude(tokens[i].trim());
           }
        }
       }

    public void setLink(String link) {
        this.link = link;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setGeoLat(String geoLat) {
        this.geoLat = geoLat;
    }

    public void setGeoLong(String geoLong) {
        this.geoLong = geoLong;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public void setMagnitude(String magnitude) {
        this.magnitude = magnitude;
    }


    @Override
    public int compareTo(Item i) {
        Float mag1 = Float.parseFloat(this.getMagnitude());
        Float mag2 = Float.parseFloat(i.getMagnitude());
        return mag1.compareTo(mag2);
    }
}
