package com.drohne.main;
import com.drohne.rf.*;

public class StartBase {

	private static RFModule antenna;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Start");
		antenna = new RFModule();
		antenna.pwrUp();
		
		while (true) {
			antenna.receive();
			//break;
		}
	}

}
