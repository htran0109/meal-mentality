package edu.ucsd.hack.meal_mentality;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Calendar;

public class CardListActivity extends Fragment {

    private static final String TAG = "CardListActivity";
    private CardArrayAdapter cardArrayAdapter;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState ){
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.listview);
        View rootView = inflater.inflate(R.layout.listview, container, false);


        listView = (ListView) rootView.findViewById(R.id.card_listView);


        cardArrayAdapter = new CardArrayAdapter(getActivity(), R.layout.listviewitem);

        Calendar v = Calendar.getInstance();



        HttpClient httpclient = new DefaultHttpClient();
        HttpGet request = new HttpGet(URL);
        // These code snippets use an open-source library.
        HttpResponse<JsonNode> response = Unirest.get("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/searchComplex?cuisine=american&excludeIngredients=coconut,+mango&includeIngredients=onions,+lettuce,+tomato&intolerances=peanut,+shellfish&limitLicense=false&maxCalories=1500&maxCarbs=100&maxFat=100&maxProtein=100&minCalories=150&minCarbs=5&minFat=5&minProtein=5&number=10&offset=0&query=burger&ranking=1&type=main+course")
                .header("X-Mashape-Key", "unjCf6sRtXmshg2zKOJM5hWAm6HZp1kw7qbjsn8tlrzAmAwNnX")
                .asJson();




        for (int i = 0; i < 10; i++) {
            v.set(2016, 2, i + 1);
            Food card = new Food("Food " + (i+1), "Dinner", i*10, i*15, i*20, i+10,v);
            cardArrayAdapter.add(card);
        }
        listView.setAdapter(cardArrayAdapter);
        return rootView;
    }
}