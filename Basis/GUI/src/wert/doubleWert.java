package wert;

public class doubleWert extends Wert {
	double minimum; 
	double maximum; 
	double wert;
	public doubleWert(String n, Boolean krit, double min, double max, double w) {
		super(n, krit);
		minimum = min;
		maximum = max;
		wert = w;
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
	}

	@Override
	public String toString() {
		return this.getname();		
	}
}
