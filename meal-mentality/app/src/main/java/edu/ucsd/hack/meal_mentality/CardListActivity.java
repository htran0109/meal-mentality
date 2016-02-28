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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class CardListActivity extends Fragment {

    public static boolean makeNew = false;
    private static final String TAG = "CardListActivity";
    private CardArrayAdapter cardArrayAdapter;
    private ListView listView;
    private ArrayList<Food> list;
    private ArrayList<MealPlanner> mp;
    int index;

    public CardListActivity() {
        index = 0;
        mp = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        View rootView = inflater.inflate(R.layout.listview, container, false);


        listView = (ListView) rootView.findViewById(R.id.card_listView);


        cardArrayAdapter = new CardArrayAdapter(getActivity(), R.layout.listviewitem);

        // These code snippets use an open-source library.

        if (!reloadData())
            setupList();

//        for (int i = 0; i < 10; i++) {
//            v.set(2016, 2, i + 1);
//            Food card = new Food("Food " + (i+1), "Dinner", i*10, i*15, i*20, i+10,v, "http://barcodedc.com/wp-content/gallery/food/pizza-junk-food-600.jpg");
//            cardArrayAdapter.add(card);
//        }
        listView.setAdapter(cardArrayAdapter);
        return rootView;
    }


    public void setupList() {
        mp = new ArrayList<>();
        MealPlanner ma = new MealPlanner(cardArrayAdapter);
        ma.designMealPlan(2000, 0);
        mp.add(ma);


        //arr = new MealPlanner(cardArrayAdapter).designMealPlan(2000, 0);

        //newList = Arrays.asList(arr);
        //list.addAll(newList);


    }

    public Serializable close() {
        list = new ArrayList<>();

        for (int i = 0; i < mp.size(); i++){
            for (int j =0; j < mp.get(i).meals.length; j ++) {
                Food item = mp.get(i).meals[j];

                list.add(item);

            }
        }
        try {
            FileOutputStream fos = getActivity().openFileOutput("food.data", getActivity().MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(list);
            Log.d("Debug", "Object array: " + list);
            os.flush();
            os.close();
            fos.close();
        }catch (Exception e){
            Log.d("DEBUG", "error writing to file: " + e.getMessage());
        }


        return list;
    }

    public boolean reloadData(){
        if(makeNew) {
            makeNew = false;
            return false;
        }
        mp = new ArrayList<>();
        list = new ArrayList<>();
        try {
            FileInputStream fis = getActivity().openFileInput("food.data");
            ObjectInputStream is = new ObjectInputStream(fis);
            Object ans = is.readObject();
            ArrayList<Food> simpleClass = (ArrayList) ans ;
            Log.d("DEBUG", "simpleClass " + simpleClass);
            is.close();
            fis.close();
            restart(simpleClass);
        } catch (FileNotFoundException e){
            return false;
        } catch (Exception e){
            return false;
        }
        return true;

    }

    public void restart(ArrayList<Food> li) {

        Log.d("DEBUG", "BEING CALLED");

        MealPlanner mi = new MealPlanner(cardArrayAdapter);
        for (int i = 0; i < li.size() / 3; i++) {
            for (int j = 0; j < 3; j++)
                mi.add(li.get(j));
            mp.add(mi);
            mi = new MealPlanner(cardArrayAdapter);
        }
    }

}


