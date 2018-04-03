package wert;

import java.awt.Color;

public class doubleWert implements Werteverwalter {
	double minimum; 
	double maximum; 
	double wert;
	String name;
	Boolean kritisch;
	Color color;
	public doubleWert(String n, Boolean krit, double min, double max, double w) {
		name = n;
		kritisch = krit;
		minimum = min;
		maximum = max;
		wert = w;
		color = Color.BLACK;
	}

	public double getMinimum() {
		return minimum;
	}

	public void setMinimum(double minimum) {
		this.minimum = minimum;
	}

	public double getMaximum() {
		return maximum;
	}

	public void setMaximum(double maximum) {
		this.maximum = maximum;
	}

	public double getWert() {
		return wert;
	}

	public void setWert(double wert) {
		this.wert = wert;
		if(istUnnormal()) {
			color = Color.red;			
		} else {
			color = Color.BLACK;			
		}
	}

	@Override
	public String toString() {
		return this.getname();		
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
	private Boolean istUnnormal() {
		if(this.wert > this.maximum || this.wert < minimum) {
			return true;
		} 
		else {return false;
		}
		
	}
	
	public String getString() {
		return String.valueOf(this.wert);
	}
}
