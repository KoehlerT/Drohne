package utility;

import main.Info;

public class Blume {
	private int x; // X-Koordinate Bild 
	private int y; //Y- Koordinate Bild 
	private int rad; //Radius der Blume in Pixeln
	private int dist; //Distanz zur Drohne in cm
	public Blume(int x, int y, int radius, int distanz) {
		this.x = x;
		this.y = y;
		this.rad = radius;
		this.dist = distanz;
	}
	
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getrad() {
		return rad;
	}
	public int getDist() {
		return dist;
	}
	
	public float relX() {
		return (float)((float)x/(float)Info.pictureWidth);
	}
	
	public float relY() {
		return (float)((float)y/(float)Info.pictureHeight);
	}
}
