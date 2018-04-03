package main;
//import java.util.*;
import javax.swing.*;

import panels.Frame;
import wert.doubleWert;
import wert.intWert;
import wert.vektorenWert;
public class Exec {
	static JLabel l = new JLabel();
	static intWert b = new intWert("Geschwindigkeit",false,0,15,5);
	public static void main(String[] args) {
		Frame.addtoll(new intWert("Geschwindigkeit",true,0,20,Randomnumber.randomisieren()));
		Frame.addtoll(new intWert("Höhe",true,0,20,Randomnumber.randomisieren()));
		Frame.addtoll(new intWert("Status",false,0,20,Randomnumber.randomisieren()));
		Frame.addtoll(new intWert("Batterieladung in %",true,0,20,Randomnumber.randomisieren()));
		Frame.addtoll(new doubleWert("Spannung",false,0,20,Randomnumber.randomisieren()+0.6));
		Frame.addtoll(new vektorenWert("Ort",true,0,0,0,20,20,20,Randomnumber.randomisieren(),Randomnumber.randomisieren(),Randomnumber.randomisieren()));
		new Frame();
	}
}
 