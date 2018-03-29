package com.drohne.wlanserver;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controller implements KeyListener{
	
	private float throttle = 1000;
	private float pitch = 1500;
	private float roll = 1500;
	private float yaw = 1500;
	
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
		case KeyEvent.VK_W: throttle += 50; break;
		case KeyEvent.VK_A: yaw -= 50; break;
		case KeyEvent.VK_S: throttle-=50; break;
		case KeyEvent.VK_D: yaw+=50; break;
		case KeyEvent.VK_I: pitch+=50; break;
		case KeyEvent.VK_J:roll-=50; break;
		case KeyEvent.VK_K: pitch-=50; break;
		case KeyEvent.VK_L: roll+=50; break;
		default: break;
		
		}
		yaw = norm((int)yaw);
		throttle = norm((int)throttle);
		pitch = norm((int)pitch);
		roll = norm((int)roll);
		
		Window.instance.setThrottle((int)throttle);
		Window.instance.setPitch((int)pitch);
		Window.instance.setRoll((int)roll);
		Window.instance.setYaw((int)yaw);
		
		if (arg0.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			throttle = 1000;
			System.out.println(throttle);
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		
		/*switch(arg0.getKeyCode()) {
		case KeyEvent.VK_W: w = false; break;
		case KeyEvent.VK_A: a = false; yaw = 1500; break;
		case KeyEvent.VK_S: s = false; break;
		case KeyEvent.VK_D: d = false; yaw = 1500;break;
		case KeyEvent.VK_I: i = false; pitch = 1500;break;
		case KeyEvent.VK_J: j = false; roll = 1500;break;
		case KeyEvent.VK_K: k = false; pitch = 1500;break;
		case KeyEvent.VK_L: l = false; roll = 1500;break;
		default: break;
		}*/
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
			}
	
	public void update(long timePassed) {
		//float incr = (float)(timePassed*0.0000001);
		/*float incr = 0.1f;
		if (w) {throttle += incr;}
		if (a) {yaw -= incr;}
		if (s) {throttle -= incr;}
		if (d) {yaw += incr;}
		if (i) {pitch += incr;}
		if (j) {roll -= incr;}
		if (k) {pitch -= incr;}
		if (l) {roll += incr;}
		
		yaw = norm((int)yaw);
		throttle = norm((int)throttle);
		pitch = norm((int)pitch);
		roll = norm((int)roll);
		
		Window.instance.setThrottle((int)throttle);
		Window.instance.setPitch((int)pitch);
		Window.instance.setRoll((int)roll);
		Window.instance.setYaw((int)yaw);*/
	}
	
	private int norm(int i) {
		if (i < 1000)
			return 1000;
		if (i > 2000)
			return 2000;
		return i;
	}
	
	public int getThrottle() {return (int)throttle;}
	public int getPitch() {return (int)pitch;}
	public int getRoll() {return (int)roll;}
	public int getYaw() {return (int)yaw;}
}
