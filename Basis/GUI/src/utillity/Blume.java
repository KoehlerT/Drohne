package utillity;

public class Blume {
	
	private float x;
	private float y;
	private int dist;
	
	public Blume(float x, float y, int dist) {
		this.x = x;
		this.y = y;
		this.dist = dist;
	}
	
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public int getDist() {
		return dist;
	}

	@Override
	public String toString() {
		return "("+x+"|"+y+"): "+dist+"cm";
	}
}
