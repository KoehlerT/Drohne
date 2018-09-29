package hardware;

import main.Data;
import panels.KonsolenFenster;
import utillity.Blume;
import utillity.FlyingMode;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import main.ControlWordHandler;

public class Datapackager {
	
	
	public static void untangleReceivedPackage(byte[] received) {
		/*byte receivedControl = received[1];
		ControlWordHandler.getInstance().addReceivedControlWord(receivedControl);*/
		//printBinaryArray(received);
		GetReceivedDrone.untangleReceived(received);
		
	}
	
	public static byte[] getTransmitPackage() {
		byte[] toSend = new byte[8];
		
		toSend[0] = ControlWordHandler.getInstance().getNextSendingWord(); //1. Kontrol-Word
		//2. Kontroller Inputs
		setContollerInputs(toSend);
		
		
		//Stopbytes
		toSend[toSend.length-2] = (byte)0b01001010;
		toSend[toSend.length-1] = (byte)0b10010110;
		
		return toSend;
	}
	
	private static void setContollerInputs(byte[] buffer) {
		int throttle = Data.getCont_throttle().getWert();
		int roll = Data.getCont_roll().getWert();
		int pitch = Data.getCont_pitch().getWert();
		int yaw = Data.getCont_yaw().getWert();
		//Force Stop/Down
		FlyingMode mode = Data.getFlyingMode();
		if (mode == FlyingMode.FORCESTOP) {
			yaw = 1000;
			throttle = 1000;
		}
			
		
		
		//10bits Pro Control
		buffer[1] = (byte) (throttle-1000);
		buffer[2] = (byte) (roll-1000);
		buffer[3] = (byte) (pitch-1000);
		buffer[4] = (byte) (yaw-1000);
		//Von jedem Control noch die übrigen 2 MSBs
		byte MSBs = (byte)((throttle-1000) >> 8);
		MSBs = (byte)((MSBs << 2) | ((roll-1000) >> 8));
		MSBs = (byte)((MSBs << 2) | ((pitch-1000) >> 8)); 
		MSBs = (byte)((MSBs << 2) | ((yaw-1000) >> 8)); 
		buffer[5] = MSBs;
	}
	
	private static void printBinaryArray(byte[] bin) {
		for (int i = 0; i < bin.length; i++) {
			byte content = bin[i];
			String str = String.format("%8s", Integer.toBinaryString(content & 0xFF)).replace(' ', '0');
			System.out.println("Array ["+i+"]: "+str);
		}
	}
	
	
}
