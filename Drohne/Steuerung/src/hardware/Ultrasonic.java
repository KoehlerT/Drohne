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
		
		long timoutTime = System.nanoTime();
		//Wait until the ECHO Pin gets High
		//System.out.println("34");
		while(echoPin.isLow()) {
			//Echo kommt nie zurück
			if ((System.nanoTime() -timoutTime) > Info.ultrasonicTimeout) {
				System.out.println("Timeout 1");
				return;
			}
		}
		//System.out.println("42");
		long startTime = System.nanoTime();
		//Wait until the ECHO Pin gets Low
		while(echoPin.isHigh()) {
			if ((System.nanoTime() -startTime) > Info.ultrasonicTimeout) {
				//Zu lange, Objekt wird als weiter als s.u. angenommen
				filterNoise(330); //100ms => 330dm
				return;
			}
		}
		long endTime = System.nanoTime();
		
		//Distance
		float newDistance = (float)(endTime - startTime) * Info.SoundSpeed;
		
		//Noise Filter
		filterNoise(newDistance);
		
	}
	
	private void timeout() {
		filterNoise(330); //100ms => 330dm
	}
	
	private void filterNoise(float distance) {
		if (oldDistance > 0)
			distance = distance * 0.5f + oldDistance*0.5f;
		oldDistance = distance;
		
		Daten.setDistanceUltrasonic(distance);
	}
}
