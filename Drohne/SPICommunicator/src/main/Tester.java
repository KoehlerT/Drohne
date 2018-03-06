package main;

import java.io.IOException;

public class Tester {

	private static byte[] data = new byte[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		try{
			Communicator c = new Communicator(data.length, 4, 200000);
			while (true){
				byte[] res = c.TransmitData(data);
				printArr(res);
				Thread.sleep(10);

			}
		}catch(IOException | InterruptedException e){
			
		}
		
	}
	
	static void printArr(byte[] arr){
		for (byte b : arr){
			System.out.print((byte)(b&0xFF) + ", ");
		}
		System.out.println();
	}

}
