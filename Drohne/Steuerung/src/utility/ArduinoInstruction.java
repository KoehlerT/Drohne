package utility;

public class ArduinoInstruction {
	
	static ArduinoInstruction inst = new ArduinoInstruction();
	public static ArduinoInstruction getInst() {return inst;}
	
	private ArduinoInstruction() {}
	
	private byte control;
	private byte[] data = new byte[9];
	private boolean isActive = false;
	
	public synchronized byte getControl() {return control;}
	public synchronized byte[] getData() {return data;}
	
	public synchronized void enable() {isActive = true;}
	public synchronized void disable() {isActive = false;}
	
	public synchronized boolean enabled() {return isActive;}
	
	public synchronized void setData(byte[] data) { //Create Copy (Thread save???)
		for (int i = 0; i < 9; i++) {
			this.data[i] = data[i];
		}
	}
	public synchronized void setControl(byte c) {control = c;}
	
}
