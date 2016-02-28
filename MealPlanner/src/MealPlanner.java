import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class MealPlanner {

	public static Food[] meals = new Food[3];

	public static void main(String[] args) {
		try {
			designMealPlan(5000);
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Food designMeal(int calories, String mealType)
			throws UnirestException {
		String searchURL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/searchComplex?maxCalories="
				+ calories
				+ "&maxCarbs=100&maxFat=100&maxProtein=100&minCalories=50&minCarbs=5&minFat=5&minProtein=5&number=100&offset=0&ranking=1&type="
				+ mealType;
		HttpResponse<JsonNode> response = Unirest
				.get(searchURL)
				.header("X-Mashape-Key",
						"unjCf6sRtXmshg2zKOJM5hWAm6HZp1kw7qbjsn8tlrzAmAwNnX")
				.asJson();
		System.out.println(response);
		JSONObject myObj = response.getBody().getObject();
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

		return new Food(firstTitle, mealType, calorieValue, protein,
				carbohydrate, fat);

	}

	public static void designMealPlan(int calories) throws UnirestException {

		meals[0] = designMeal(calories * 20 / 100, "breakfast");
		meals[1] = designMeal(calories * 40 / 100, "main+course");
		meals[2] = designMeal(calories * 40 / 100, "main+course");
		for (Food meal : meals) {
			System.out.println(meal);
		}
	}
}
