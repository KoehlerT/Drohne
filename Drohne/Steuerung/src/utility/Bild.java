package utility;

import java.util.Date;

public class Bild {
	private int aufnahmeDauer = 0;
	private Date aufnahmeZeit;
	private byte[][] imageData;
	private int referenced = 0;
	
	public Bild() {
		
	}
	public void changeData(byte[][] data, int dauer, Date zeit) {
		referenced = 0;
		imageData = data;
		aufnahmeDauer = dauer;
		aufnahmeZeit = zeit;
	}
	public Boolean locked() {
		return referenced > 0; //Returnt true, wenn das Bild noch gebraucht wird
	}
	
	public void incref() {
		referenced ++;
	}
	public void decref() {
		referenced --;
	}
	
	public byte[][] getImageData() {return imageData;}
	public int getDauer() {return aufnahmeDauer;}
	public Date getzeit() {return aufnahmeZeit;}
}
