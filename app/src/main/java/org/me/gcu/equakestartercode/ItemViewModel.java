package org.me.gcu.equakestartercode;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class ItemViewModel extends ViewModel implements IAsyncResponse {
    private MutableLiveData<List<Item>> items;
    private static String urlSource = "http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";

    public LiveData<List<Item>> getItems()
    {
        if(items == null)
        {
            items = new MutableLiveData<List<Item>>();
            loadItems();
        }
        return items;
    }

    public void loadItems()
    {
        QuakeAsyncTask getQuakeData = new QuakeAsyncTask();
        getQuakeData.listener = this;
        getQuakeData.execute(urlSource);
    }

    @Override
    public void returnXML(String xml) {
        items.setValue(parseXML(xml));
    }

    public List<Item> parseXML(@NotNull String XML) {
        Item item = new Item();
        List<Item> list = new ArrayList<Item>();
        String unull = XML.replace("null", "");

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(unull));
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                // Found a start tag
                if (eventType == XmlPullParser.START_TAG) {
                    // Check which Tag we have
                    if (xpp.getName().equalsIgnoreCase("item")) {
                        item = new Item();
                    } else if (xpp.getName().equalsIgnoreCase("title")) {
                        // Now just get the associated text
                        String temp = xpp.nextText();
                        // Do something with text
                        Log.e("MyTag", "Title " + temp);
                        item.setTitle(temp);
                    } else
                        // Check which Tag we have
                        if (xpp.getName().equalsIgnoreCase("description")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text
                            Log.e("MyTag", "Nut is desc " + temp);
                            item.setDescription(temp);
                        } else
                            // Check which Tag we have
                            if (xpp.getName().equalsIgnoreCase("link")) {
                                // Now just get the associated text
                                String temp = xpp.nextText();
                                // Do something with text
                                Log.e("MyTag", "Washer is link" + temp);
                                item.setLink(temp);
                            } else
                                // Check which Tag we have
                                if (xpp.getName().equalsIgnoreCase("pubDate")) {
                                    // Now just get the associated text
                                    String temp = xpp.nextText();
                                    // Do something with text
                                    Log.e("MyTag", "Washer is pub " + temp);
                                    item.setPubDate(temp);
                                } else
                                    // Check which Tag we have
                                    if (xpp.getName().equalsIgnoreCase("category")) {
                                        // Now just get the associated text
                                        String temp = xpp.nextText();
                                        // Do something with text
                                        Log.e("MyTag", "Washer is  cat" + temp);
                                        item.setCategory(temp);
                                    } else
                                        // Check which Tag we have
                                        if (xpp.getName().equalsIgnoreCase("lat")) {
                                            // Now just get the associated text
                                            String temp = xpp.nextText();
                                            // Do something with text
                                            Log.e("MyTag", "Washer is lat " + temp);
                                            item.setGeoLat(temp);
                                        } else
                                            // Check which Tag we have
                                            if (xpp.getName().equalsIgnoreCase("long")) {
                                                // Now just get the associated text
                                                String temp = xpp.nextText();
                                                // Do something with text
                                                Log.e("MyTag", "Washer is " + temp);
                                                item.setGeoLong(temp);
                                            }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (xpp.getName().equalsIgnoreCase("item")) {
                        list.add(item);
                    }
                }


                // Get the next event
                eventType = xpp.next();

            } // End of while

            return list;

            //return alist;
        } catch (XmlPullParserException ae1) {
            Log.e("MyTag", "Parsing error" + ae1.toString());
        } catch (IOException ae1) {
            Log.e("MyTag", "IO error during parsing");
        }
        return list;

    }
}
