/**
 * MealPlanner is the engine that generates the meals based on the restrictions and parameters
 * given to it. Uses the Spootacular API to retrieve the closest match to the target nutritional
 * values.
 *
 * @author  Chirag Toprani
 * @author  Hung Tran
 * @author  Tejas Badadare
 * @version 1.0
 */

package edu.ucsd.hack.meal_mentality;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;


import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.Serializable; //For saving to disk
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
    private String APIbase = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/"
            + "searchComplex?";
    private String APIKey = "unjCf6sRtXmshg2zKOJM5hWAm6HZp1kw7qbjsn8tlrzAmAwNnX";
    
    private Calendar vals;
    private int date;

    public MealPlanner(CardArrayAdapter car) {
        meals = new Food[6];
        cardArrayAdapter = car;
        index = 0;
    }

    /**
     * Concatenates several strings into one, with each item delimited by the comma and space
     * URL encoding characters. Used to interface with Spoontacular API.
     * @param elements  The Strings to concatenate together
     * @return  The URL-ready string
     */
    public String buildURLFragment(String[] elements) {
        String ret = "";

        // Convert array into one long string
        for (int i = 0; i < elements.length; i++) {
            ret += elements[i];

            //Delimit items with URL encoding comma and space symbol
            if (i == elements.length) {
                ret += "%2C+";
            }
        }

        return ret;
    }

    /**
     * Calls the Spoontacular API to retrieve a meal that fits the target values as closely as
     * possible. Deviation: 60% to 120% of target value. Stores this meal into the meals array.
     *
     * NOTE: Since this API call combines three other functions, each request counts as 3.
     *
     * @param calories  Target calories
     * @param carbs     Target carbs
     * @param protein   Target proteins
     * @param fat       Target fat
     * @param intolerances      List of intolerances. Possible values: dairy, egg, gluten, peanut,
     *                          sesame, seafood, shellfish, soy, sulfite, tree nut, and wheat
     * @param mealType          Type of meal requested. Possible values: breakfast, main+course
     */
    public void designMeal(double calories, int carbs, int protein, int fat, String[] intolerances,
                           String mealType) {

        // Building the API call
        String searchURL = APIbase;

        // Include intolerances if there are any
        if (intolerances != null && intolerances.length != 0) {
            searchURL += "intolerances=" + buildURLFragment(intolerances);
        }

        // Build rest of URL
        searchURL +=

                /* MAXIMUM NUTRIENTS */
                "&maxCalories="
                + (int) (calories * maxMult)
                + "&maxCarbs="
                + (int) (carbs * maxMult)
                + "&maxFat="
                + (int) (fat * maxMult)
                + "&maxProtein="
                + (int) (protein * maxMult)

                /* MINIMUM NUTRIENTS */
                + "&minCalories="
                + (int) (calories * minMult)
                + "&minCarbs="
                + (int) (carbs * minMult)
                + "&minFat="
                + (int) (fat * minMult)
                + "&minProtein="
                + (int) (protein * minMult)

                /* MEAL TYPE */
                + "&number=1&offset=0&ranking=1&type="
                + mealType;

        // Debug
        System.err.println(searchURL);


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
    public Food[] designMealPlan(int calories, int carbs, int protein, int fat,
                                 String[] intolerances, int day) {
        this.date = day;
        //Design first choices
        designMeal(calories * calorieBreakfastMult, carbs, protein, fat, intolerances, "breakfast");
        designMeal(calories * calorieLunchMult, carbs, protein, fat, intolerances, "main+course");
        designMeal(calories * calorieDinnerMult, carbs, protein, fat, intolerances, "main+course");

        // Design second choices
        designMeal(calories * calorieBreakfastMult, carbs, protein, fat, intolerances, "breakfast");
        designMeal(calories * calorieLunchMult, carbs, protein, fat, intolerances, "main+course");
        designMeal(calories * calorieDinnerMult, carbs, protein, fat, intolerances, "main+course");
        return meals;
    }

    /**
     * Wrapper for full designMealPlan method, for current compatibility.
     * Uses hardcoded values for carbs, protein, fat, intolerances.
     *
     * @param calories  Target calories per day
     * @param day       The day to generate a plan for
     * @return          The generated meals in an array
     */
    public Food[] designMealPlan(int calories, int day) {
        // Recommended values from random sites on the internet...
        int carbs = 150;
        int protein = 48;
        int fats = 50;

        return designMealPlan(calories, carbs, protein, fats, null, day);
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
