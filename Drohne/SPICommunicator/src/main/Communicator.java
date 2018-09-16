package main;
import java.io.IOException;

import com.pi4j.io.spi.*;

/**
 * Communicator ist eine Klasse zum Kommunizieren mit dem Arduino. 
 * 
 * */
public class Communicator {
	/*Danke: SpiExample Pi4j
	 */
	private SpiDevice device = null;
	private byte[] res;
	private int wait;
	/**
	 * Communicator instanziiert ein neues Communicator objekt, das mit dem Raspberry Pi Kommuniziert.
	 * Bitte keine 2 Objekte, da sonst Daten auf den Leitungen vermischt werden.
	 *@param streamLength gibt die Länge der Rückgabe an. Muss so groß sein, wie das zu sendende Bytearray
	 *@param packageWait gibt die Zeit in ms an, wie lange nach jedem Packet gewartet werden soll. Empfolen 4
	 *@param speed gibt die Tacktfrequenz des Buses in Hz an. 200kHz Empfohlen
	 * @throws IOException Wenn das SPI Gerät nciht eingrichtet werden konnte
	 * */
	public Communicator(int streamLength, int packageWait, int speed) throws IOException{
		res = new  byte[streamLength];
		wait = packageWait;
		//CS0 = Pin 24 => Serial Select
		//Normale Geschwindigkeit: 1MHz besser: 200kHz
		device = SpiFactory.getInstance(SpiChannel.CS0, speed, SpiDevice.DEFAULT_SPI_MODE);
	}
	/**Übermittelt daten vom Raspberry PI zum arduino
	 * @param ein Array an Bites, das übermittelt werden soll
	 * @return ein Array der länge des parameters, mit der Antwort des Arduinos
	 * */
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
			Thread.sleep(wait);//Warte an Übermittlung
		}catch (InterruptedException | IOException e){
			System.out.println("Problem bim Senden");
		}
		return res;
	}
	
	private void shakeHands(){
		//Spamt Requests bis ein Acknowledge
		while (sendPackage((byte)'R') != (byte)'A');
	}
}
