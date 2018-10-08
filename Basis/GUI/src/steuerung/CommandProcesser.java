package steuerung;

import java.awt.Color;

import main.ControlWordHandler;
import panels.KonsolenFenster;

public class CommandProcesser {
	
	
	public static void processCommand(String command) {
		if (command.startsWith("fm:")) {
			flightModeChange(command);
		}
	}
	
	private static void flightModeChange(String command) {
		String number = command.substring(3);
		System.out.println("Number "+number);
		try {
			int n = Integer.parseInt(number);
			if (n < 0 ||  n > 16)
				KonsolenFenster.addText("Ungültiges Programm", Color.BLACK);
			n += 16; //Flight mode: 0x1?
			ControlWordHandler.getInstance().addSendingWord((byte)n);
		}catch(NumberFormatException e) {
			KonsolenFenster.addText("Falsches Zahlenformat", Color.BLACK);
		}
	}
}
