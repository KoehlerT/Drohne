package main;
import java.util.Random;
public class Randomnumber {
	private Randomnumber() {}
	
	private static Random r;
	
	static{
		r = new Random();
	}
	
	public static int randomisieren() {
		return r.nextInt(20);
	}
}
