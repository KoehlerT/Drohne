package wert;

import java.awt.Color;
//siehe Klasse doubleWert 
public class intWert implements Werteverwalter {
	int minimum; 
	int maximum; 
	int wert;
	String name;
	Boolean kritisch;
	Color color;
	public intWert(String n, Boolean krit, int min, int max, int w) {
		name = n;
		kritisch = krit;
		minimum = min;
		maximum = max;
		wert = w;
	}
	public int getMinimum() {
		return minimum;
	}
	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}
	public int getMaximum() {
		return maximum;
	}
	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}
	public int getWert() {
		return wert;
	}
	public void setWert(int wert) {
		this.wert = wert;
		if(istUnnormal()) {
			color = Color.red;			
		}else {
			color = Color.BLACK;			
		}
	}

	public String toString() {
		return this.name;		
	}

	public String getname() {
		return name;
	}

	public boolean getkritisch() {
		return kritisch;
	}

	public Color getColor() {
		return color;
	}

	public String getString() {
		return String.valueOf(wert);
	}
	private Boolean istUnnormal() {
		if(this.wert > this.maximum || this.wert < minimum) {
			return true;
		} 
		else {return false;
		}
		
	}
}
