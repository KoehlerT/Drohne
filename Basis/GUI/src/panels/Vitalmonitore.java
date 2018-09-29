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
		critic.setSize(250, 500);
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
		updateLabel(Data.getError(),criticLabels[1]);
		updateLabel(Data.getStart(),criticLabels[2]);
		updateLabel(Data.getFlightModeInt(),criticLabels[3]);
		//GPS
		updateLabel(Data.getLatitude(), criticLabels[4]);
		updateLabel(Data.getLongitude(),criticLabels[5]);
		updateLabel(Data.getAltitude(),criticLabels[6]);
		
		updateLabel(Data.getArduinoRefresh(), criticLabels[7]);
		//Status
		criticLabels[8].setText("Mode: "+Data.getFlyingMode().toString());
		
		//Flugcontrols
		updateLabel(Data.getDrone_throttle(),criticLabels[9]);
		updateLabel(Data.getDrone_roll(),criticLabels[10]);
		updateLabel(Data.getDrone_pitch(),criticLabels[11]);
		updateLabel(Data.getDrone_yaw(),criticLabels[12]);
		
		updateLabel(Data.getTakeoffThrottle(),criticLabels[13]);
		updateLabel(Data.getTemoerature(),criticLabels[14]);
		updateLabel(Data.getAngleRoll(),criticLabels[15]);
		updateLabel(Data.getAnglePitch(),criticLabels[16]);
		updateLabel(Data.getAngleYaw(),criticLabels[17]);
		updateLabel(Data.getHeadingLock(),criticLabels[18]);
		updateLabel(Data.getFixType(),criticLabels[19]);
		updateLabel(Data.getSet1(),criticLabels[20]);
		updateLabel(Data.getSet2(),criticLabels[21]);
		updateLabel(Data.getSet3(),criticLabels[22]);
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
