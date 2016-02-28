
import java.util.Calendar;

public class Food {
	public String name;
	public int calories;
	public int protein;
	public int carbohydrate;
	public int fat;
	public String timeOfDay;
	public Calendar date;

	public Food(String name, String timeOfDay, int calories, int protein,
			int carbohydrate, int fat, Calendar date) {
		this.timeOfDay = timeOfDay;
		this.name = name;
		this.calories = calories;
		this.protein = protein;
		this.carbohydrate = carbohydrate;
		this.fat = fat;
		this.date = date;
	}

	public Food(String name, String timeOfDay, int calories, int protein,
			int carbohydrate, int fat) {

		this.timeOfDay = timeOfDay;
		this.name = name;
		this.calories = calories;
		this.protein = protein;
		this.carbohydrate = carbohydrate;
		this.fat = fat;
	}

	public String toString() {

		return name + ", a " + timeOfDay + " meal with " + calories
				+ " calories, " + protein + "g protein, " + carbohydrate
				+ "g carbohydrates, and " + fat + "g fat";

	}
}
