import com.github.kevinsawicki.http.HttpRequest;

/* Engine tester.
 * For internal temporary use.
 * 
 * @author	Tejas Badadare
 * @version	1
 */
public class MealEngineTester {

	public static void main(String[] args) {
		MealEngine test = new MealEngine();
		int hundred = 100;
		String request = HttpRequest.get("http://google.com", true, 'q', "baseball gloves", "size", hundred).body();
		System.out.println(request.toString());


	}

}