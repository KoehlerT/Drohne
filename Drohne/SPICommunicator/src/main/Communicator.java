package main;
import java.io.IOException;

import com.pi4j.io.spi.*;


public class Communicator {
	/*Danke: SpiExample Pi4j
	 * */
	private SpiDevice device = null;
	private byte[] res;
	public Communicator(int streamLength){
		res = new  byte[streamLength];
		try {
			//CS0 = Pin 24 => Serial Select
			//Normale Geschwindigkeit: 1MHz besser: 100kHz
			device = SpiFactory.getInstance(SpiChannel.CS0, 100000, SpiDevice.DEFAULT_SPI_MODE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public byte[] TransmitData(byte[] data){
		if (data.length > res.length)
			return null; //Problem: der Receive Buffer ist nicht groß genug für das Ergebnis!
		//Handshake
		shakeHands();
		//Transmission
		for (int i = 0; i < data.length; i++){
			res[i] = sendPackage(data[i]);
		}
		return res ;
	}
	
	
	private byte sendPackage(byte send){
		byte res = 0;
		try{
			res = device.write(send)[0];
			Thread.sleep(5);//Warte an Übermittlung
		}catch (InterruptedException | IOException e){
			System.out.println("Problem bim Senden");
		}
		return res;
	}
	
	private void shakeHands(){
		//Spamt Requests bis ein Acknowledge
		while (sendPackage((byte)'R') != (byte)'A');
		sendPackage((byte)0);
	}
}
