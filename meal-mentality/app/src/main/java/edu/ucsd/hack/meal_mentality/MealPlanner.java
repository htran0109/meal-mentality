package edu.ucsd.hack.meal_mentality;

import android.os.AsyncTask;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;


import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class MealPlanner implements Serializable {

    public Food[] meals;
    private CardArrayAdapter cardArrayAdapter;
    int index;

    public static final int BREAKFAST = 0;
    public static final int LUNCH = 1;
    public static final int DINNER = 2;

    private Calendar vals;
    private int date;

    public MealPlanner(CardArrayAdapter car) {
        meals = new Food[3];
        cardArrayAdapter = car;
        index = 0;
    }

    public void designMeal(int calories, String mealType) {
        String searchURL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/searchComplex?&maxCalories="
                + (int) (calories * (1.25))
                //+ "&maxCarbs=100&maxFat=100&maxProtein=100
                + "&minCalories="
                + (int) (calories / 1.5)
                //+ "&minCarbs=5&minFat=5&minProtein=5" +
                + "&number=1&offset=0&ranking=1&type="
                + mealType;

        Log.d("JSON", "executing JSON Request");
        new GetHTTP().execute(searchURL);


    }

    //0 is first day, tomorrow
    public Food[] designMealPlan(int calories, int day) {
        this.date = day;
        designMeal(calories * 20 / 100, "breakfast");
        designMeal(calories * 40 / 100, "main+course");
        designMeal(calories * 40 / 100, "main+course");
        return meals;
    }

    void add(Food i){
        meals[index] = i;
        cardArrayAdapter.add(meals[index++]);
    }


    void processFinish(String response) {
        Log.d("JSON", "The response is " + response);

        try {
            JSONObject myObj = new JSONObject(response);
            JSONArray resultArray = myObj.getJSONArray("results");
            if (resultArray.length() == 0) {
                vals = Calendar.getInstance();
                vals.add(Calendar.DATE, date);

                meals[index] = new Food("Cheerios", "Breakfast", 110, 3,
                        22, 2, vals, "http://blog.generalmills.com/wp-content/uploads/Cheerios-Ancient.jpg");
            }
            else {
                JSONObject firstResult = resultArray.getJSONObject(0);
                String firstTitle = firstResult.getString("title");
                int calorieValue = firstResult.getInt("calories");
                int protein = Integer.parseInt(firstResult.getString("protein")
                        .substring(0, firstResult.getString("protein").length() - 1));
                int carbohydrate = Integer.parseInt(firstResult.getString("carbs")
                        .substring(0, firstResult.getString("carbs").length() - 1));
                int fat = Integer.parseInt(firstResult.getString("fat").substring(0,
                        firstResult.getString("fat").length() - 1));
                String url = firstResult.getString("image");

                System.out.println(myObj);
                System.out.println(firstTitle);

                String mealType;
                if (index == 0) mealType = "Breakfast";
                else if (index == 1) mealType = "Lunch";
                else mealType = "Dinner";

                vals = Calendar.getInstance();
                vals.add(Calendar.DATE, date);
                meals[index] = new Food(firstTitle, mealType, calorieValue, protein,
                        carbohydrate, fat, vals, url);
            }
            cardArrayAdapter.add(meals[index++]);

        } catch (Exception e) {
            Log.d("error", e.getMessage());
        }

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

            } catch (Exception e) {
                Log.d(TAG2, "Error with httpresponse " + e.getMessage());
            }
            Log.d(TAG2, "made it to beginning");


            return answer;
        }


        protected void onPostExecute(String page) {
            processFinish(page);
        }
    }


}
