package panels;
import java.awt.Color;
import java.awt.Component;
import java.util.LinkedList;

import javax.swing.*;

import main.Data;
import utillity.FlyingMode;
import wert.Werteverwalter;
public class Vitalmonitore {
	JInternalFrame critic = new JInternalFrame("kritisch", true, true, true, true);
	JInternalFrame uncritic = new JInternalFrame("unwichtig", true, true, true, true);
	JLabel[] criticLabels;
	JLabel[] uncriticLabels;
	
	public Vitalmonitore(JDesktopPane p) {
		//Neune Fenster+ anzeigen
		p.add(critic);
		p.add(uncritic);
		critic.setSize(250, 250);
		uncritic.setSize(250, 250);
		critic.setLocation(0, 0);
		uncritic.setLocation(250, 0);
		critic.setLayout(null);
		uncritic.setLayout(null);
		critic.show();
		uncritic.show();
		//Array aller kritischen werte zu anzeige umwandeln
		criticLabels = new JLabel[Data.numCrit];
		for (int i = 0; i < criticLabels.length ; i++) {
			criticLabels[i] = new JLabel("");
			criticLabels[i].setForeground(Color.BLACK);
			addtocritic(criticLabels[i],0,i*15);
		}
		
		
		//Array aller unkritischen werte zu anzeige umwandeln
		uncriticLabels = new JLabel[Data.numUnCrit];
		for (int i = 0; i < uncriticLabels.length; i++) {
			uncriticLabels[i] = new JLabel("");
			uncriticLabels[i].setForeground(Color.BLACK);
			addtouncritic(uncriticLabels[i],0,i*15);
		}
	}
	
	
	public void update() {
		//Manuelles eintragen der neuen Werte
		//Controller Inputs
		updateLabel(Data.getCont_throttle(),uncriticLabels[0]);
		updateLabel(Data.getCont_roll(), uncriticLabels[1]);
		updateLabel(Data.getCont_pitch(), uncriticLabels[2]);
		updateLabel(Data.getCont_yaw(), uncriticLabels[3]);
		
		//Statusinformationen
		updateLabel(Data.getTilt(),uncriticLabels[4]);
		updateLabel(Data.getDistUltrasonic(),uncriticLabels[5]);
		updateLabel(Data.getNumGpsSatellites(),uncriticLabels[6]);
		
		updateLabel(Data.getCommunicatorRefresh(),uncriticLabels[7]);
		updateLabel(Data.getHardwareRefresh(),uncriticLabels[8]);
		
		//Kritische Informationen
		updateLabel(Data.getVoltageMain(), criticLabels[0]);
		updateLabel(Data.getVoltage5v(), criticLabels[1]);
		updateLabel(Data.getVoltage3v(),criticLabels[2]);
		updateLabel(Data.getAmperage(),criticLabels[3]);
		
		//GPS
		updateLabel(Data.getLatitude(), criticLabels[4]);
		updateLabel(Data.getLongitude(),criticLabels[5]);
		updateLabel(Data.getAltitude(),criticLabels[6]);
		
		updateLabel(Data.getArduinoRefresh(), criticLabels[7]);
		//Status
		criticLabels[8].setText("Mode: "+Data.getFlyingMode().toString());
	}
	
	private void updateLabel(Werteverwalter wert, JLabel label) {
		label.setText(wert.getname() + ": "+wert.getString());
		label.setForeground(wert.getColor());
	}
	
	public void addtocritic(Component l,int x,int y) {
		l.setBounds(x, y, 250, 30);
		critic.add(l);
	}

	public void addtouncritic(Component l,int x, int y) {
		l.setBounds(x, y, 250, 30);
		uncritic.add(l);
	}
}
