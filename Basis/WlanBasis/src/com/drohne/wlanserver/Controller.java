package com.drohne.wlanserver;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controller implements KeyListener{
	
	private int throttle = 1000;
	private int pitch = 1500;
	private int roll = 1500;
	private int yaw = 1500;
	
	private Boolean w = false;
	private Boolean a = false;
	private Boolean s = false;
	private Boolean d = false;
	private Boolean i = false;
	private Boolean j = false;
	private Boolean k = false;
	private Boolean l = false;
	
	public Controller() {
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getKeyCode()) {
		case KeyEvent.VK_W: w = true; break;
		case KeyEvent.VK_A: a = true; break;
		case KeyEvent.VK_S: s = true; break;
		case KeyEvent.VK_D: d = true; break;
		case KeyEvent.VK_I: i = true; break;
		case KeyEvent.VK_J: j = true; break;
		case KeyEvent.VK_K: k = true; break;
		case KeyEvent.VK_L: l = true; break;
		default: break;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		
		switch(arg0.getKeyCode()) {
		case KeyEvent.VK_W: w = false; break;
		case KeyEvent.VK_A: a = false; yaw = 1500; break;
		case KeyEvent.VK_S: s = false; break;
		case KeyEvent.VK_D: d = false; yaw = 1500;break;
		case KeyEvent.VK_I: i = false; pitch = 1500;break;
		case KeyEvent.VK_J: j = false; roll = 1500;break;
		case KeyEvent.VK_K: k = false; pitch = 1500;break;
		case KeyEvent.VK_L: l = false; roll = 1500;break;
		default: break;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void update(long timePassed) {
		int incr = (int)(timePassed/2000);
		if (w) {throttle += incr;}
		if (a) {yaw -= incr;}
		if (s) {throttle -= incr;}
		if (d) {yaw += incr;}
		if (i) {pitch += incr;}
		if (j) {roll -= incr;}
		if (k) {pitch -= incr;}
		if (l) {roll += incr;}
		
		yaw = norm(yaw);
		throttle = norm(throttle);
		pitch = norm(pitch);
		roll = norm(roll);
		
		Window.instance.setThrottle(throttle);
		Window.instance.setPitch(pitch);
		Window.instance.setRoll(roll);
		Window.instance.setYaw(yaw);
	}
	
	private int norm(int i) {
		if (i < 1000)
			return 1000;
		if (i > 2000)
			return 2000;
		return i;
	}
	
	public int getThrottle() {return throttle;}
	public int getPitch() {return pitch;}
	public int getRoll() {return roll;}
	public int getYaw() {return yaw;}
}
