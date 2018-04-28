package steuerung;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.Data;

public class ManuManager implements KeyListener{

	private int throttle = 1000;
	private int roll = 1000;
	private int pitch = 1000;
	private int yaw = 1000;
	
	public static final int thrInt = 50;
	public static final int tiltInt = 20;
	
	private void updateValues() {
		Data.setCont_pitch(pitch);
		Data.setCont_roll(roll);
		Data.setCont_throttle(throttle);
		Data.setCont_yaw(yaw);
	}
	
	private void clampValues() {
		throttle = clamp(throttle);
		roll = clamp(roll);
		pitch = clamp(pitch);
		yaw = clamp(yaw);
	}
	
	private int clamp(int i) {
		if (i < 1000)
			i = 1000;
		if (i > 2000)
			i = 2000;
		return i;
	}
	
	//Key Listener
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		//int key = e.getKeyCode();
		//System.out.println(key+" :KEY");
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (!Data.getManuMode())
			return;
		
		
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_W: throttle +=thrInt; break;
		case KeyEvent.VK_S: throttle -=thrInt; break;
		case KeyEvent.VK_A: yaw +=tiltInt; break;
		case KeyEvent.VK_D: yaw -=tiltInt; break;
		case KeyEvent.VK_I: pitch +=tiltInt; break;
		case KeyEvent.VK_K: pitch -=tiltInt; break;
		case KeyEvent.VK_J: roll +=tiltInt; break;
		case KeyEvent.VK_L: roll -=tiltInt; break;
		}
		clampValues();
		updateValues();
	}

}
