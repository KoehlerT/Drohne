package bilderkennung;

import java.awt.Point;

public class UPunkt {
	//Speichert einen Punkt für die Umrissberechnubng
	//Enthält daten bezüglich Validität des Punktes
	private byte x;
	private byte y;
	private boolean valid;
	
	public UPunkt(int x, int y) {
		this.x = (byte)x;
		this.y = (byte)y;
		this.valid = true;
	}
	
	public UPunkt(boolean v) {
		this.valid = v;
	}
	
	public Point getP() {
		return new Point(x,y);
	}
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean valid() {
		return valid;
	}
	
	public int dist(UPunkt p2) {
		return (int)Math.sqrt(Math.pow(p2.x-x, 2)+Math.pow(p2.y+y, 2));
	}
}
