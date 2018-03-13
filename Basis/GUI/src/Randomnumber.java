import java.util.Random;
public class Randomnumber {
	public Randomnumber() {		
	}
	public static int randomisieren() {
		Random r = new Random();
		int x = (int) r.nextInt(100);
		return x; 
		
	}
}
