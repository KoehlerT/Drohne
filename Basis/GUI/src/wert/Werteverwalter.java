package wert;

import java.awt.Color;

public interface Werteverwalter {
		//name anzeigen k�nnen
		public String getname();
		//kritisch �bergeben
		public boolean getkritisch();
		//Farbe �bergeben
		public Color getColor();
		//Inhalt �bergeben
		public String getString();	

}
