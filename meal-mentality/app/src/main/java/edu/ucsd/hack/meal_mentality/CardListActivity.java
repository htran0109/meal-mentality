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
        String deets = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/searchComplex?excludeIngredients=coconut%2C+mango&includeIngredients=onions%2C+lettuce%2C+tomato&intolerances=peanut%2C+shellfish&limitLicense=false&maxCalories=1500&maxCarbs=100&maxFat=100&maxProtein=100&minCalories=150&minCarbs=5&minFat=5&minProtein=5&number=1&offset=0&query=burger&ranking=1&type=main+course";
        new GetHTTP().execute(deets);

    }

    void processFinish(String output){

        Log.d(CardListActivity.TAG, "Printing result " + output.toString());

    }


    class GetHTTP extends AsyncTask<String, Void, String> {
        private static final String TAG2 = "CardListActivity";


        @Override
        protected String doInBackground(String... params) {
            String answer = "";
            Log.d(TAG2, "made it to beginning");
            try {
                String val = params[0];
                URL url = new URL(val);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("X-Mashape-Key", "unjCf6sRtXmshg2zKOJM5hWAm6HZp1kw7qbjsn8tlrzAmAwNnX");
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    java.util.Scanner s = new java.util.Scanner(in).useDelimiter("\\A");
                    answer = s.hasNext() ? s.next() : "";
                } finally {
                    urlConnection.disconnect();
                }

                Log.d(TAG2, "made it to beginning");

            }catch (Exception e){
                Log.d(TAG2, "Error with httpresponse " + e.getMessage());
            }
            Log.d(TAG2, "made it to beginning");


            return answer;
        }


        protected void onPostExecute(String page)
        {
            processFinish(page);
        }
    }


}


