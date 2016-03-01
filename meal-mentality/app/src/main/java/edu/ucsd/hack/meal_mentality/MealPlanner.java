package edu.ucsd.hack.meal_mentality;

import android.os.AsyncTask;
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

    // Nutrition ranges
    private double minMult = 0.6;   // Minimum nutrient multiplier
    private double maxMult = 1.2;   // Maximum nutrient multiplier
    private double calorieBreakfastMult = 0.2;
    private double calorieLunchMult = 0.45;
    private double calorieDinnerMult = 0.35;

    //Food API Integration
    String APIbase = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/" +
            "searchComplex?";
    String APIKey = "unjCf6sRtXmshg2zKOJM5hWAm6HZp1kw7qbjsn8tlrzAmAwNnX";
    String
    
    private Calendar vals;
    private int date;

    public MealPlanner(CardArrayAdapter car) {
        meals = new Food[6];
        cardArrayAdapter = car;
        index = 0;
    }


    public void designMeal(double calories, int carbs, int protein, int fat, String mealType) {
        String searchURL = APIbase
                // Maximum nutrients
                + "&maxCalories="
                + (int) (calories * maxMult)
                + "&maxCarbs="
                + (int) (carbs * maxMult)
                + "&maxFat=100"
                + (int) (fat * maxMult)
                + "&maxProtein=100"
                + (int) (protein * maxMult)

                //Minimum nutrients
                + "&minCalories="
                + (int) (calories * minMult)
                //+ "&minCarbs=5&minFat=5&minProtein=5" +
                + "&number=1&offset=0&ranking=1&type="
                + mealType;

                //Intolerances, possible values are:
                // dairy, egg, gluten, peanut, sesame, seafood, shellfish,
                // soy, sulfite, tree nut, and wheat.

        Log.d("JSON", "executing JSON Request");
        new GetHTTP().execute(searchURL);
    }

    /**
     * Generates a meal plan for a given day considering the target nutritional values,
     * saving the completed Food objects into the meals array.
     * Delegates to designMeal, which calls the Food API.
     * Days are zero-indexed, with 0 being this day.
     * 
     * @param calories  The total target calories per day
     * @param carbs     Total target carbs per day
     * @param protein   Total target protein per day
     * @param day       The day to generate a plan for
     * @return          The generated meals in an array
     */
    public Food[] designMealPlan(int calories, int carbs, int protein, int day) {
        this.date = day;
        //Design first choices
        designMeal(calories * calorieBreakfastMult, carbs, protein, "breakfast");
        designMeal(calories * calorieLunchMult, carbs, protein, "main+course");
        designMeal(calories * calorieDinnerMult, carbs, protein, "main+course");

        // Design second choices
        designMeal(calories * calorieBreakfastMult, carbs, protein, "breakfast");
        designMeal(calories * calorieLunchMult, carbs, protein, "main+course");
        designMeal(calories * calorieDinnerMult, carbs, protein, "main+course");
        return meals;
    }

    /**
     * Wrapper for full designMealPlan method, for current compatibility.
     * @param calories  Target calories per day
     * @param day       The day to generate a plan for
     * @return          The generated meals in an array
     */
    public Food[] designMealPlan(int calories, int day) {
        return designMealPlan(calories, 150 /* carbs */, 48 /* protein */, day);
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


                String mealType;
                if (index%3 == 0) mealType = "Breakfast";
                else if (index%3 == 1) mealType = "Lunch";
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
                urlConnection.setRequestProperty("X-Mashape-Key", APIKey);
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
