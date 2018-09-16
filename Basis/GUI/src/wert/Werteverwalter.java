package wert;

import java.awt.Color;

public interface Werteverwalter {
		//name anzeigen können
		public String getname();
		//kritisch übergeben
		public boolean getkritisch();
		//Farbe übergeben
		public Color getColor();
		//Inhalt übergeben
		public String getString();	

}
