package com.drohne.wlanserver;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame{
	
	static public Window instance;
	
	JPanel panel;
	
	JLabel ping;
	JLabel throttle;
	JLabel pitch;
	JLabel roll;
	JLabel yaw;
	
	public Window(Controller c) {
		instance = this;
		this.setTitle("Controller");
		this.setVisible(true);
		this.setFocusable(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500, 200);
		this.setResizable(false);
		this.addKeyListener(c);
		addLabels();
	}
	
	private void addLabels() {
		panel = new JPanel();
		this.add(panel);
		Box vbox = Box.createVerticalBox();
		ping = new JLabel("Ping: 1ms",SwingConstants.CENTER);
		throttle = new JLabel("Throttle: 1000us",SwingConstants.CENTER);
		pitch = new JLabel("Pitch: 1000us",SwingConstants.CENTER);
		roll = new JLabel("Roll: 1000us",SwingConstants.CENTER);
		yaw = new JLabel("Yaw: 1000us",SwingConstants.CENTER);
		vbox.add(ping);
		vbox.add(throttle);
		vbox.add(yaw);
		vbox.add(roll);
		vbox.add(pitch);
		panel.add(vbox);
	}
	
	public void setPing(int i) {
		ping.setText("Ping: "+i+"ms");
	}
	public void setThrottle(int i) {
		throttle.setText("Throttle: "+i+"us");	
	}
	public void setPitch(int i) {
		pitch.setText("Pitch: "+i+"us");
	}
	public void setRoll(int i) {
		roll.setText("Roll: "+i+"us");
	}
	public void setYaw(int i) {
		yaw.setText("Yaw: "+i+"us");
	}
}
