package panels;
import java.awt.Color;

import javax.swing.*;

import main.Data;
import steuerung.ManuManager;
import utillity.FlyingMode;

public class ManuSteu  {
	
	private static ManuSteu instance = null;
	static ManuSteu getInstance() {return instance;}
	
	int c = 0;
	
	//Labels
	private JLabel enabledL;
	private JLabel throttleL;
	private JLabel rollL;
	private JLabel pitchL;
	private JLabel yawL;
	
    public ManuSteu(String s, JDesktopPane p) {
    	instance = this;
    	//Anzeige der Steuermöglichkeiten
    	JInternalFrame j = new JInternalFrame("Key",true,true,true,true);
    	//Actions initalisieren um sie weiter unten verwenden zu können
        p.add(j);
        //Bild festlegen
        j.setSize(250, 250);
        j.setLocation(0, 250);
        j.setTitle(s);
        j.setLayout(null);
        
        //BUTTONS UND LABELS
        enabledL = new JLabel();
        throttleL = new JLabel();
        rollL = new JLabel();
        pitchL = new JLabel();
        yawL = new JLabel();
        
        enabledL.setBounds(0, 5, 250, 20);
        throttleL.setBounds(0, 30, 100, 20);
        rollL.setBounds(0, 50, 100, 20);
        pitchL.setBounds(0, 70, 100, 20);
        yawL.setBounds(0,90,100,20);
        
        j.add(enabledL);
        j.add(throttleL);
        j.add(rollL);
        j.add(pitchL);
        j.add(yawL);
        
        j.addKeyListener(new ManuManager());
        j.setFocusable(true);
        
        j.show();
        updateLabels();
        setColors(false);
        enabledL.setText("Automatischer Modus");
        //enabledL.setBackground(Color.BLACK);
        enabledL.setForeground(Color.RED);
        
    }
    
    public void toggleManu() {
    	FlyingMode mode = Data.getFlyingMode();
    	
    	if (mode == FlyingMode.AUTOMATIC) {
    		Data.setFlyingMode(FlyingMode.MANUAL);
    		enabledL.setText("Manueller Modus");
    	}else {
    		Data.setFlyingMode(FlyingMode.AUTOMATIC);
    		enabledL.setText("Automatischer Modus");
    	}
    	setColors(mode == FlyingMode.AUTOMATIC);
    }
    
    public void updateLabels() {
    	throttleL.setText("Throttle: "+Data.getCont_throttle().getWert());
    	rollL.setText("Roll: "+Data.getCont_roll().getWert());
    	pitchL.setText("Pitch: "+Data.getCont_pitch().getWert());
    	yawL.setText("Yaw: "+Data.getCont_yaw().getWert());
    }
    
    public void setColors(boolean manu) {
    	Color c = (manu)?Color.BLACK:Color.GRAY;
    	
    	throttleL.setForeground(c);
    	rollL.setForeground(c);
    	pitchL.setForeground(c);
    	yawL.setForeground(c);
    }

}
