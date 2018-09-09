package hardware.communication;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.pi4j.io.spi.*;

import main.Daten;
import main.Info;
import utility.FlyingMode;

import com.koehlert.arduinonative.*;
import com.koehlert.serialcomm.Communicator;

public class Arduino {
	
	private Communicator comm;
	
	static{
		System.load("/lib/drohne/libSerialComm.so");
	}
	
	//Variablen---------------------------------------
	
	private byte[] toSend = new byte[10]; //Controller input bytes
	private byte[] received = new byte[10];
	private int length = 10;
	//private Spi arduino;
	
	//Kosntruktor
	Arduino(){
		//arduino = new Spi();
		comm = new Communicator(length, length);
		
		for (int i = 0; i < toSend.length; i++) {
			toSend[i] = (byte)i;
		}
	}
	
	//Public Methoden --------------------------------------
	
	public void sendControllerInputs() {
		int throttle = Daten.getThrottle();
		int pitch = Daten.getPitch();
		int roll = Daten.getRoll();
		int yaw = Daten.getYaw();
		
		FlyingMode mode = Daten.getFlyingMode();
		if (mode == FlyingMode.FORCESTOP) {
			throttle = 1000;
			yaw = 1000;
		}
		
		//System.out.println(throttle+" "+pitch+" "+roll+" "+yaw);
		
		writeToArray(toSend,throttle,0);
		writeToArray(toSend,pitch,2);
		writeToArray(toSend,roll,4);
		writeToArray(toSend,yaw,6);
		
		toSend[8] = Daten.getContArd();
		//toSend[9] = (byte) 0;
		
		comm.setTransmitBuffer(toSend);
		//System.out.println("COMM");
		comm.communicate();
		comm.getReceiveBuffer(received);
		
		//Datapackager.printBinaryArray(received);
		
		Datapackager.untangleArduinoReceived(received);
	}
	
	
	//Private Methoden -------------------------------------------------
	
	private void writeToArray(byte[] arr, int val, int ind) {
		byte lb = (byte)(val & 0xFF); //Lower Byte value
		byte hb = (byte)((val >> 8)& 0xFF); //Higher Byte value
		arr[ind] = lb;
		arr[ind+1] = hb;
	}
	
	public void closeConnection() {
		comm.close();
	}

}
