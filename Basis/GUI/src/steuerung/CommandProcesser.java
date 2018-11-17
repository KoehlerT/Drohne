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
		if (command.toLowerCase() == "help") {
			System.out.println("0: Forcestop\n1: Manual\n"
					+ "2: Forcedown\n3: Automatic\n4: Altitude Hold\n"
					+ "5: Calibrate Level\n6: Calibrate Compass\n"
					+ "7: Set Takeoff Throttle\n8: Follow");
		}
		
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
