package wert;

import java.awt.Color;

public class vektorenWert implements Werteverwalter {
	int minimum1;
	int maximum1;
	int minimum2;
	int maximum2;
	int minimum3;
	int maximum3;
	int wert1;
	int wert2;
	int wert3;
	String name;
	Boolean kritisch;
	Color color;

	public vektorenWert(String n, Boolean krit, int min1, int min2, int min3, int max1, int max2, int max3, int w1,
			int w2, int w3) {
		name = n;
		kritisch = krit;
		minimum1 = min1;
		minimum2 = min2;
		minimum3 = min3;
		maximum1 = max1;
		maximum2 = max2;
		maximum3 = max3;
		wert1 = w1;
		wert2 = w2;
		wert3 = w3;
	}

	public int[] getMinimum() {
		int[] minimum = new int[3];
		minimum[0] = minimum1;
		minimum[1] = minimum2;
		minimum[2] = minimum3;
		return minimum;
	}
	public int[] getMaximum() {
		int[] maximum = new int[3];
		maximum[0] = maximum1;
		maximum[1] = maximum2;
		maximum[2] = maximum3;
		return maximum;
	}

	public int getMinimum1() {
		return minimum1;
	}

	public void setMinimum1(int minimum1) {
		this.minimum1 = minimum1;
	}

	public int getMaximum1() {
		return maximum1;
	}

	public void setMaximum1(int maximum1) {
		this.maximum1 = maximum1;
	}

	public int getMinimum2() {
		return minimum2;
	}

	public void setMinimum2(int minimum2) {
		this.minimum2 = minimum2;
	}

	public int getMaximum2() {
		return maximum2;
	}

	public void setMaximum2(int maximum2) {
		this.maximum2 = maximum2;
	}

	public int getMinimum3() {
		return minimum3;
	}

	public void setMinimum3(int minimum3) {
		this.minimum3 = minimum3;
	}

	public int getMaximum3() {
		return maximum3;
	}

	public void setMaximum3(int maximum3) {
		this.maximum3 = maximum3;
	}

	public int getWert1() {
		return wert1;
	}

	public void setWert1(int wert1) {
		this.wert1 = wert1;
	}

	public int getWert2() {
		return wert2;
	}

	public void setWert2(int wert2) {
		this.wert2 = wert2;
	}

	public int getWert3() {
		return wert3;
	}

	public void setWert3(int wert3) {
		this.wert3 = wert3;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getKritisch() {
		return kritisch;
	}

	public void setKritisch(Boolean kritisch) {
		this.kritisch = kritisch;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setWert(int wert) {
		this.wert1 = wert;
		if (istUnnormal()) {
			color = Color.red;
		} else {
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
		return String.valueOf(wert1 + " " + wert2 + " " + wert3);
	}

	private Boolean istUnnormal() {
		if (this.wert1 > this.maximum1 || this.wert1 < minimum1) {
			return true;
		} else if (this.wert2 > this.maximum2 || this.wert2 < minimum2) {
			return true;
		} else if (this.wert3 > this.maximum3 || this.wert3 < minimum3) {
			return true;
		} else {
			return false;
		}

	}
}
