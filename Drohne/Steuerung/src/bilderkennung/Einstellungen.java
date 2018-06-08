package bilderkennung;

final class Einstellungen {
	//Raster/ Schablonenerkennung
	static final float rasterGr��e = 0.02f; //Vielfaches der Breite, die als Rasterabstand benutzt wird
	static final int schablonenChecks = 16; //Anzahl der Schablonenpr�fungen pro Pixel
	static final float kontrast = 3.0f; //Minimaler Quotient aus Wei�/Schwarz (Farben: 0-255)
	static final float erfolgreich = 0.70f; //% der Checks, die beim Schablonen-pr�fen erfolgreich sein sollen DEF=0.7----VAR
	
	//Clustering
	static final int dichte = 40; //Wie Dicht m�ssen Punkte im gleichen Cluster liegen? DEF=20
	static final int minPts = 0; //Wie viele Punkte muss ein Cluster mindestens Umfassen?
	
	//Radiusberechnung
	static final int incr = 5; //Schritt von einem Test zum n�chsten
	static final int anzTest = 32; //Anzahl der Radiustests (Vielfaches von2 !!!);
	static final int maxAbw = 10; //Maximalabweichung vom Durchschnittsradius
	static final int wei� = 150; //Minimalwert, dass ein Pixel "wei�" genannt wird
	
	//Kameraeigenschaften zur Distanzerkennung
	static final float radSchw = 3.5f; //Radius des Blumeninneren in cm
	static final float fokus = 0.14f; //Fokusl�nge der Kamera in cm
	static final float gr = 0.36736f; //Breite des Kamerasensors in cm
	static final int aufl = 640; //Aufl�sung des Bildes in Px (breite)
	static final float faktor = gr/aufl; //Vorgerechneter Faktor f�r entfernungsbemessung
	
	
	//Sinus und Cosinus vorberechnen
	static float[] sin = new float[schablonenChecks];
	static float[] cos = new float[schablonenChecks];

	static {
		for (int i = 0; i < schablonenChecks; i++) {
			double a = (2*Math.PI*i)/schablonenChecks;
			sin[i] = (float)Math.sin(a);
			cos[i] = (float)Math.cos(a);
		}
	}
}
