package hardware;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class Beeper {
	
	private GpioPinDigitalOutput pin;
	
	Beeper() {
		pin = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_21, "Piepser",PinState.LOW);
	}
	
	synchronized void beep(int pulse) {
		pin.pulse(pulse,true);
	}

}
