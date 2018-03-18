package wert;

public class intWert extends Wert {
	int minimum; 
	int maximum; 
	int wert;
	public intWert(String n, Boolean krit, int min, int max, int w) {
		super(n, krit);
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
	}
	@Override
	public String toString() {
		return this.getname();		
	}
}
