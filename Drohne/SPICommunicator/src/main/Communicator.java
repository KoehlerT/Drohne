package main;
import java.io.IOException;

import com.pi4j.io.spi.*;


public class Communicator {
	/*Danke: SpiExample Pi4j
	 * */
	private SpiDevice device = null;
	
	public Communicator(){
		try {
			//CS0 = Pin 24 => Serial Select
			device = SpiFactory.getInstance(SpiChannel.CS0, SpiDevice.DEFAULT_SPI_SPEED, SpiDevice.DEFAULT_SPI_MODE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public byte[] Send(char send) throws IOException{
		byte[] arr = new byte[]{(byte)send,'B','C','D','E','F','G'};
		byte[] res = new byte[arr.length];
		for (int i = 0; i<arr.length;i++){
			res[i] = device.write(arr[i])[0];
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return res;
		//return device.write(send);
	}
}
