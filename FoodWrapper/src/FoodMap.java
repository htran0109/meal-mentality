import java.util.HashMap;
public class FoodMap {
	public final static int mapSize = 500; 
	public static HashMap foods;
	public static void main(String[] args) {
		createFoods();
	}
	
	public static void createFoods() {
		foods = new HashMap(mapSize);
		
	}
}
