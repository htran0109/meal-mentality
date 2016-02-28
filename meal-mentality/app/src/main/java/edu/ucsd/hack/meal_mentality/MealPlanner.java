package edu.ucsd.hack.meal_mentality;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MealPlanner {

	public  Food[] meals = new Food[3];
	int index;

	public static final int BREAKFAST = 0;
	public static final int LUNCH = 1;
	public static final int DINNER = 2;

	public MealPlanner(){
		index=0;
	}

	public void designMeal(int calories, String mealType) {
		String searchURL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/searchComplex?maxCalories="
				+ calories
				//+ "&maxCarbs=100&maxFat=100&maxProtein=100&
				+ "minCalories="
				+ calories/2
				//+ "&minCarbs=5&minFat=5&minProtein=5" +
				+ "&number=1&offset=0&ranking=1&type="
				+ mealType;


		new GetHTTP().execute(searchURL);


	}

	public  void designMealPlan(int calories)  {

		designMeal(calories * 20 / 100, "breakfast");
		designMeal(calories * 40 / 100, "main+course");
		designMeal(calories * 40 / 100, "main+course");
		for (Food meal : meals) {

		}
	}


	void processFinish(String response){


		JSONObject myObj = new JSONObject(response);
		JSONArray resultArray = myObj.getJSONArray("results");
		JSONObject firstResult = resultArray.getJSONObject(0);
		String firstTitle = firstResult.getString("title");
		int calorieValue = firstResult.getInt("calories");
		int protein = Integer.parseInt(firstResult.getString("protein")
				.substring(0, firstResult.getString("protein").length() - 1));
		int carbohydrate = Integer.parseInt(firstResult.getString("carbs")
				.substring(0, firstResult.getString("carbs").length() - 1));
		int fat = Integer.parseInt(firstResult.getString("fat").substring(0,
				firstResult.getString("fat").length() - 1));

		System.out.println(myObj);
		System.out.println(firstTitle);

		meals[index++] = new Food(firstTitle, mealType, calorieValue, protein,
				carbohydrate, fat);

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
