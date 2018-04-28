package hardware;

import main.Data;

public class Datapackager {
	
	public void untangleReceivedPackage(byte[] received) {
		
	}
	
	public byte[] getTransmitPackage() {
		byte[] toSend = new byte[8];
		
		//toSend[0] = Data.getControlWord(); //1. Kontrol-Word
		toSend[0] = (byte)42;
		//2. Kontroller Inputs
		setContollerInputs(toSend);
		
		
		//Stopbytes
		toSend[toSend.length-2] = (byte)0b01001010;
		toSend[toSend.length-1] = (byte)0b10010110;
		
		return toSend;
	}
	
	private void setContollerInputs(byte[] buffer) {
		int throttle = Data.getCont_throttle().getWert();
		int pitch = Data.getCont_pitch().getWert();
		int roll = Data.getCont_roll().getWert();
		int yaw = Data.getCont_yaw().getWert();
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
	
	
}
