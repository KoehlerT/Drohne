package bilderkennung;

final class Einstellungen {
	//Raster/ Schablonenerkennung
	static final float rasterGröße = 0.02f; //Vielfaches der Breite, die als Rasterabstand benutzt wird
	static final int schablonenChecks = 16; //Anzahl der Schablonenprüfungen pro Pixel
	static final float kontrast = 3.0f; //Minimaler Quotient aus Weiß/Schwarz (Farben: 0-255)
	static final float erfolgreich = 0.70f; //% der Checks, die beim Schablonen-prüfen erfolgreich sein sollen DEF=0.7----VAR
	
	//Clustering
	static final int dichte = 40; //Wie Dicht müssen Punkte im gleichen Cluster liegen? DEF=20
	static final int minPts = 0; //Wie viele Punkte muss ein Cluster mindestens Umfassen?
	
	//Radiusberechnung
	static final int incr = 5; //Schritt von einem Test zum nächsten
	static final int anzTest = 32; //Anzahl der Radiustests (Vielfaches von2 !!!);
	static final int maxAbw = 10; //Maximalabweichung vom Durchschnittsradius
	static final int weiß = 150; //Minimalwert, dass ein Pixel "weiß" genannt wird
	
	//Kameraeigenschaften zur Distanzerkennung
	static final float radSchw = 3.5f; //Radius des Blumeninneren in cm
	static final float fokus = 0.14f; //Fokuslänge der Kamera in cm
	static final float gr = 0.36736f; //Breite des Kamerasensors in cm
	static final int aufl = 640; //Auflösung des Bildes in Px (breite)
	static final float faktor = gr/aufl; //Vorgerechneter Faktor für entfernungsbemessung
	
	
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
