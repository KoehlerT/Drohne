package hardware.communication;

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
		
		//toSend[9] = (byte) 0;
		toSend = Datapackager.getArduinoSend(toSend);
		
		comm.setTransmitBuffer(toSend);
		//System.out.println("COMM");
		comm.communicate();
		comm.getReceiveBuffer(received);
		
		//Datapackager.printBinaryArray(received);
		
		Datapackager.untangleArduinoReceived(received);
	}
	
	
	//Private Methoden -------------------------------------------------
	
	
	
	public void closeConnection() {
		comm.close();
	}

}
