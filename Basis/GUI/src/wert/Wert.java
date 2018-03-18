package wert;

public class Wert {
	private String name;
	private boolean kritisch;
	public Wert(String n, Boolean krit) {
		name = n;
		kritisch = krit;
	}
	
	public Wert() {
		
		
	}
	
	public String getname() {return name;}
	public boolean getkritisch() {return kritisch;}
	public void setname(String s) {name = s;}
	public void setkritisch(boolean b) {kritisch = b;}
}
