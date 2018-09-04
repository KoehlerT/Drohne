package utility;

import java.util.Date;

public class Bild {
	/*Objekte dieser Klasse halten informationen über die einzelnen Bilder
	 * */
	
	private float aufnahmeDauer = 0;
	private Date aufnahmeZeit;
	private byte[][] imageData;
	private int referenced = 0;
	
	public Bild() {
		
	}
	public void changeData(byte[][] data, float dauer, Date zeit) {
		//Bilddaten werden geupdatet. Wichtig für den BildPool zum Funktionieren
		//Anstatt das ein neues Objekt erstellt wird, wird das alte mit neuen Daten ausgestattet
		referenced = 0;
		imageData = data;
		aufnahmeDauer = dauer;
		aufnahmeZeit = zeit;
	}
	public Boolean locked() {
		return referenced > 0; //Returnt true, wenn das Bild noch gebraucht wird
	}
	
	
	/*Referenzensystem, angelehnt an das Garbage System von python.
	 * referenced zählt, wie oft das Objekt gerade gebraucht wird
	 * */
	public synchronized void incref() {
		referenced ++; //Objekt wird von einer Funktion Benutzt
	}
	public synchronized void decref() {
		referenced --;//Funktion ist fertig und entlässt das Objekt
	}
	
	//Getter für die anderen Informationen GEHEIMNIS!!
	public byte[][] getImageData() {return imageData;} 
	public float getDauer() {return aufnahmeDauer;}
	public Date getzeit() {return aufnahmeZeit;}
}
