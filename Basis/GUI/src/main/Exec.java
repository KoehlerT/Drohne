package main;
import java.awt.Color;

//import java.util.*;
import javax.swing.*;

import hardware.HardwareCommunicator;
import panels.*;
import panels.KonsolenFenster;
import steuerung.ControllerInfo;
import wert.doubleWert;
import wert.intWert;
import wert.vektorenWert;
@SuppressWarnings("unused")
public class Exec {
	
	static Frame frame;
	static HardwareCommunicator communicator;
	static ControllerInfo controller;
	
	public static void main(String[] args) {
		System.out.println("RUN");
		frame = new Frame();
		controller = new ControllerInfo();
		communicator = new HardwareCommunicator();
		
		
		KonsolenFenster.addText("Halol, 1&1", Color.red);
		KonsolenFenster.addText("Ich bin ein echter Gangster", Color.orange);
		KonsolenFenster.addText("Ich bin ein echter Gangster1", Color.orange);
		Bildanzeige.kreiszeichnen(Bildanzeige.getBil(), 100, 100, 30);
		Bildanzeige.distAnzeige(Bildanzeige.getBil(), 100, 130, 3.5);
		System.out.println("RUNNING");
		
		loop();
	}
	
	
	public static void loop() {
		while (true) {
			frame.update();
			Bildanzeige.drawBlumen();
			controller.Update();
			communicator.PrepareAndSend();
			ControlWordHandler.getInstance().evaluateWord();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
 