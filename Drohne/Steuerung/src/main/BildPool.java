package main;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import utility.*;

public class BildPool {
	
	/*Funktion eines Pools ist, M�ll (Object Garbage) zu vermeiden
	 * Es Existeren nur 10 Bildobjekte, die immer geupdatet werden
	 * So wird nie 1 Bildobjekt weckgeworfen.
	 * Funktion �hnlich der "Wickelschlange", nur das keine Objekte geupdatet werden, die noch benutzt werden
	 * */
	
	private static Bild[] pool = new Bild[10]; //Poolgr��e. Achtung Speicherverbrauch. 10 Elemente -> ca. 1,7MB
	private static int aktuelles = 0; //Das aktuelle Bild
	//Liste aller Klassen, die von neuen Bildern benachrichtigt werden wollen
	private static List<BildNotify> interessierte = new LinkedList<BildNotify>(); 
	
	static {
		//Konstruktor f�r Statische methoden
		for (int i = 0; i < pool.length; i++) {
			pool[i] = new Bild();
		}
	}
	
	public static void addBild(byte[][] bildInfo, int aufnZeit) {
		Date d = new Date();
		if (pool[aktuelles+1].locked()) { //N�chstes Bild ist reserviert
			System.out.println("Das zu �berschreibende Bild wird noch gebraucht\n"
					+ "Die Aufnahme ist schneller als die Bearbeitung");
			return;
		}
		
		aktuelles = (aktuelles+1)%pool.length; //Aktuelles wird geupdatet
		pool[aktuelles].changeData(bildInfo, aufnZeit, d); //Bild wird gupdatet
		
		benachrichtigen();
	}
	
	public static Bild getBild() {
		return pool[aktuelles]; //N�chstes Bild vermitteln.
	}
	
	public static void Abo(BildNotify klasse) {
		interessierte.add(klasse); //Hier dr�cken Klassen ihr Interesse an neuen Informationen aus
	}
	
	public static void benachrichtigen() {
		//Jede interessierte Klasse (hat BildNotify implementiert) wird benachrichtigt
		for (BildNotify b : interessierte) {
			b.neuesBild();
		}
	}
}
