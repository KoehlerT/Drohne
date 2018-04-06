package main;
import java.awt.Color;

//import java.util.*;
import javax.swing.*;

import panels.*;
import panels.KonsolenFenster;
import wert.doubleWert;
import wert.intWert;
import wert.vektorenWert;
@SuppressWarnings("unused")
public class Exec {
	public static void main(String[] args) {
		Frame.addtoll(new intWert("Geschwindigkeit",true,0,20,Randomnumber.randomisieren()));
		Frame.addtoll(new intWert("Höhe",true,0,20,Randomnumber.randomisieren()));
		Frame.addtoll(new intWert("Status",false,0,20,Randomnumber.randomisieren()));
		Frame.addtoll(new intWert("Batterieladung in %",true,0,20,Randomnumber.randomisieren()));
		Frame.addtoll(new doubleWert("Spannung",false,0,20,Randomnumber.randomisieren()+0.6));
		Frame.addtoll(new vektorenWert("Ort",true,0,0,0,20,20,20,Randomnumber.randomisieren(),Randomnumber.randomisieren(),Randomnumber.randomisieren()));
		new Frame();
		KonsolenFenster.addText("Halol, 1&1", Color.red);
		KonsolenFenster.addText("Ich bin ein echter Gangster", Color.orange);
		Bildanzeige.kreiszeichnen(Bildanzeige.getBil(), 100, 100, 30);
		Bildanzeige.distAnzeige(Bildanzeige.getBil(), 100, 130, 3.5);
	}
}
 