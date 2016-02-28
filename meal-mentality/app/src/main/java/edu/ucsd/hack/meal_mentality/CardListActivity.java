package edu.ucsd.hack.meal_mentality;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class CardListActivity extends Fragment  {

    private static final String TAG = "CardListActivity";
    private CardArrayAdapter cardArrayAdapter;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState ){
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.listview, container, false);


        listView = (ListView) rootView.findViewById(R.id.card_listView);


        cardArrayAdapter = new CardArrayAdapter(getActivity(), R.layout.listviewitem);

        Calendar v = Calendar.getInstance();
        // These code snippets use an open-source library.



        //setupList();

        for (int i = 0; i < 10; i++) {
            v.set(2016, 2, i + 1);
            Food card = new Food("Food " + (i+1), "Dinner", i*10, i*15, i*20, i+10,v, "http://barcodedc.com/wp-content/gallery/food/pizza-junk-food-600.jpg");
            cardArrayAdapter.add(card);
        }
        listView.setAdapter(cardArrayAdapter);
        return rootView;
    }


    public void setupList(){


    }

}


