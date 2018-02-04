package main;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import utility.*;

public class BildPool {
	
	private static Bild[] pool = new Bild[10]; //Poolgröße. Achtung Speicherverbrauch. 10 Elemente -> 1,7MB
	private static int aktuelles = 0; //Das aktuelle bild
	private static List<BildNotify> interessierte = new LinkedList<BildNotify>();
	
	static {
		for (int i = 0; i < pool.length; i++) {
			pool[i] = new Bild();
		}
	}
	
	public static void addBild(byte[][] bildInfo, int aufnZeit) {
		Date d = new Date();
		if (pool[aktuelles+1].locked()) {
			System.out.println("Das zu überschreibende Bild wird noch gebraucht\n"
					+ "Die Aufnahme ist schneller als die Bearbeitung");
			return;
		}
		aktuelles = (aktuelles+1)%pool.length;
		pool[aktuelles].changeData(bildInfo, aufnZeit, d);
		benachrichtigen();
	}
	
	public static Bild getBild() {
		return pool[aktuelles];
	}
	
	public static void Abo(BildNotify klasse) {
		interessierte.add(klasse);
	}
	
	public static void benachrichtigen() {
		for (BildNotify b : interessierte) {
			b.neuesBild();
		}
	}
}
