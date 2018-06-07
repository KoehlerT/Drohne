package hardware.communication;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.pi4j.io.spi.*;

import main.Daten;
import main.Info;


public class Arduino {
	
	public native int init();
	public native byte[] spi(byte[] data);
	
	//Native Library
	static {
		String path = "/lib/drohne/libarduinocom.so";
		Path inputPath = Paths.get(path);
		System.out.println(inputPath.isAbsolute());
		System.out.println(inputPath.toFile().exists());
		
		System.load(inputPath.toAbsolutePath().toString());
	}
	
	//Variablen---------------------------------------
	
	private byte[] toSend = new byte[9]; //Controller input bytes
	private SpiDevice device;
	
	//Kosntruktor
	Arduino() throws IOException{
		device = SpiFactory.getInstance(SpiChannel.CS1,Info.SpiSpeed,SpiDevice.DEFAULT_SPI_MODE);
		init();
	}
	
	//Public Methoden --------------------------------------
	
	public void sendControllerInputs() {
		int throttle = Daten.getCont_throttle();
		int pitch = Daten.getCont_pitch();
		int roll = Daten.getCont_roll();
		int yaw = Daten.getCont_yaw();
		
		writeToArray(toSend,throttle,0);
		writeToArray(toSend,pitch,2);
		writeToArray(toSend,roll,4);
		writeToArray(toSend,yaw,6);
		
		toSend[8] = (byte) 0;
		//toSend[9] = (byte) 0;
		
		spi(toSend);
		
		//sendAndReceive(toSend);
	}
	
	
	//Private Methoden -------------------------------------------------
	private void shakeHands() {
		while (sendPackage((byte)'R') != (byte)'A');
	}
	
	private byte sendPackage(byte send){
		byte res = 0;
		try{
			res = device.write(send)[0];
			delayMicros(100);
			//Thread.sleep(Info.SpiWait);//Warte an Übermittlung
		}catch (IOException e){
			System.out.println("Problem bim Senden");
			e.printStackTrace();
		}
		return res;
	}
	
	private byte[] sendAndReceive(byte[] send) {
		byte[] recv = new byte[send.length];
		System.out.println("Shaking");
		shakeHands();
		for (int i = 0; i < send.length; i++) {
			recv[i] = sendPackage(send[i]);
		}
		
		Datapackager.untangleArduinoReceived(recv);
		return recv;
	}
	
	private void writeToArray(byte[] arr, int val, int ind) {
		byte lb = (byte)(val & 0xFF); //Lower Byte value
		byte hb = (byte)((val >> 8)& 0xFF); //Higher Byte value
		arr[ind] = lb;
		arr[ind+1] = hb;
	}
	
	private void delayMicros(int delay) {
		long startTime = System.nanoTime();
		while(System.nanoTime()-startTime < (long)(delay*1000L));
	}

}
