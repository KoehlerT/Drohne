package main;
import java.awt.Color;

//import java.util.*;
import javax.swing.*;

import hardware.HardwareCommunicator;
import panels.*;
import panels.KonsolenFenster;
import wert.doubleWert;
import wert.intWert;
import wert.vektorenWert;
@SuppressWarnings("unused")
public class Exec {
	
	static Frame frame;
	static HardwareCommunicator communicator;
	
	public static void main(String[] args) {
		System.out.println("RUN");
		frame = new Frame();
		communicator = new HardwareCommunicator();
		
		KonsolenFenster.addText("Halol, 1&1", Color.red);
		KonsolenFenster.addText("Ich bin ein echter Gangster", Color.orange);
		Bildanzeige.kreiszeichnen(Bildanzeige.getBil(), 100, 100, 30);
		Bildanzeige.distAnzeige(Bildanzeige.getBil(), 100, 130, 3.5);
		System.out.println("RUNNING");
		//loop();
		communicator.send(new byte[] {(byte)'A',(byte)'B',(byte)'C'});
	}
	
	
	public static void loop() {
		while (true) {
			frame.update();
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
 