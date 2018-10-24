package hardware;

import java.util.PriorityQueue;
import java.util.Queue;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class Beeper {
	private static Beeper instance;
	public static Beeper getInstance() {return instance;}
	
	private Queue<Integer> pulses = new PriorityQueue<Integer>();
	
	private GpioPinDigitalOutput pin;
	
	Beeper() {
		pin = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_07, "Piepser",PinState.LOW);
		instance = this;
	}
	
	synchronized void beep(int pulse) {
		pin.pulse(pulse,true);
	}
	
	void workBeeps() {
		if (!pulses.isEmpty()) {
			beep(pulses.poll());
		}
	}
	
	public synchronized void addBeep(int duration) {
		pulses.add(duration);
	}

}
