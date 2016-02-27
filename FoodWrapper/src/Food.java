
public class Food {
	public String name;
	public double metric_serving_amount;
	public double calories;
	public double protein;
	public double carbohydrate;
	public double fat;
	public double saturated_fat;
	public double trans_fat;
	public double cholesterol;
	public double sodium;
	public double potassium;
	public double fiber;
	public double sugar;
	public double vitamin_a;
	public double vitamin_c;
	public double calcium;
	public double iron;
	
	public Food(String name) {
		this.name = name;
		//Use API to instantiate fields, also case where food not found
	}
	
	public String categorize() {
		if(protein / metric_serving_amount > 6.7 && fat / metric_serving_amount > 5.0) {
			return "Meat";
		}
		else if() {
			
		}
		return "Other";
	}

}
