package main;

import java.io.IOException;

public class Tester {


	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Communicator c = new Communicator();
		while (true){
			try{
				byte[] recv = c.Send('A');
				System.out.print("Gesendet "+recv.length+": ");
				printArr(recv);
			
				Thread.sleep(1000);
				
			} catch (IOException | InterruptedException e){
				System.out.println("problem");
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
