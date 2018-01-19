package utility;

public class Blume {
	private int x; //X-Koordinate Bild
	private int y; //Y- Koordinate Bild
	private int rad; //Radius der Blume in Pixeln
	private int dist; //Distanz zur Drohne in cm
	public Blume(int x, int y, int radius, int distanz) {
		this.x = x;
		this.y = y;
		this.rad = radius;
		this.dist = distanz;
	}
}
