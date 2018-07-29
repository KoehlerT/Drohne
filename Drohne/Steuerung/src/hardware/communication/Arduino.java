package hardware.communication;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.pi4j.io.spi.*;

import main.Daten;
import main.Info;
import utility.FlyingMode;

import com.koehlert.arduinonative.*;

public class Arduino {
	
	private native byte[] spi(byte[] send);
	private native int init();
	
	static{
		System.load("/lib/drohne/libarduinocom.so");
	}
	
	//Variablen---------------------------------------
	
	private byte[] toSend = new byte[9]; //Controller input bytes
	private byte[] received = new byte[9];
	//private Spi arduino;
	
	//Kosntruktor
	Arduino() throws IOException{
		//arduino = new Spi();
		init();
	}
	
	//Public Methoden --------------------------------------
	
	public void sendControllerInputs() {
		int throttle = Daten.getCont_throttle();
		int pitch = Daten.getCont_pitch();
		int roll = Daten.getCont_roll();
		int yaw = Daten.getCont_yaw();
		
		FlyingMode mode = Daten.getFlyingMode();
		if (mode == FlyingMode.FORCEDOWN)
			throttle = 1000;
		if (mode == FlyingMode.FORCESTOP) {
			throttle = 1000;
			yaw = 1000;
		}
		
		System.out.println(throttle+" "+pitch+" "+roll+" "+yaw);
		
		writeToArray(toSend,throttle,0);
		writeToArray(toSend,pitch,2);
		writeToArray(toSend,roll,4);
		writeToArray(toSend,yaw,6);
		
		toSend[8] = (byte) 0;
		//toSend[9] = (byte) 0;
		
		received = spi(toSend);
		Datapackager.untangleArduinoReceived(received);
	}
	
	
	//Private Methoden -------------------------------------------------
	
	private void writeToArray(byte[] arr, int val, int ind) {
		byte lb = (byte)(val & 0xFF); //Lower Byte value
		byte hb = (byte)((val >> 8)& 0xFF); //Higher Byte value
		arr[ind] = lb;
		arr[ind+1] = hb;
	}

}
