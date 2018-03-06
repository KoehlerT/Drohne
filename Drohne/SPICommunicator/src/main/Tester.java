package main;

import java.io.IOException;

public class Tester {

	private static byte[] data = new byte[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Communicator c = new Communicator(data.length);
		while (true){
			byte[] res = c.TransmitData(data);
			printArr(res);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	static void printArr(byte[] arr){
		for (byte b : arr){
			System.out.print((byte)(b&0xFF) + ", ");
		}
		System.out.println();
	}

}
