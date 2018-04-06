package hardware;

import com.pi4j.io.gpio.*;

import main.Daten;
import main.Info;

public class Ultrasonic {
	
	//Danke an: https://github.com/oksbwn/Ultrasonic-Sesnor-With-Raspberry-Pi/blob/master/Code/src/Main.java
	
	private final Pin trigPinNum = RaspiPin.GPIO_04; //GPIO 16 = 4
	private final Pin echoPinNum = RaspiPin.GPIO_05; //GPIO 18 = 5
	
	private GpioPinDigitalInput echoPin;
	private GpioPinDigitalOutput trigPin;
	
	private float oldDistance = 0f;
	
	public Ultrasonic() {
		trigPin = GpioFactory.getInstance().provisionDigitalOutputPin(trigPinNum);
		echoPin = GpioFactory.getInstance().provisionDigitalInputPin(echoPinNum,PinPullResistance.PULL_DOWN);
	}
	
	
	public void measureDistanceDm() throws InterruptedException {
		trigPin.high();
		Thread.sleep((long) 0.01);
		trigPin.low();
		
		//Wait until the ECHO Pin gets High
		while(echoPin.isLow());
		long startTime = System.nanoTime();
		//Wait until the ECHO Pin gets Low
		while(echoPin.isHigh());
		long endTime = System.nanoTime();
		
		//Distance
		float newDistance = (float)(endTime - startTime) * Info.SoundSpeed;
		
		//Noise Filter
		float distance = (newDistance);
		if (oldDistance > 0)
			distance = distance * 0.5f + oldDistance*0.5f;
		oldDistance = distance;
		
		Daten.setDistanceUltrasonic(distance);
	}
}
