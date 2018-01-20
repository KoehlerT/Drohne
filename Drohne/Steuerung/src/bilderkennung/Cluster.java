package bilderkennung;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/*Labels:	-2: Undefined
 * 			-1: Noise
 * 			0+: Cluster ID
 * */

class Cluster {
	
	private Cluster() {}//Privater Konstruktor -> Keine Instanzen
	
	static Point[] cluster(Point[] points) {
		int cId = 0;
		Map<Point, Integer> labels = new HashMap<Point, Integer>();
		// Markiere Punkte
		for (Point p : points) {
			if (!labels.containsKey(p)) {
				labels.put(p, -2);
			}
		}
		
		for (Point p : points) {
			if (labels.get(p) != -2)
				continue;
			
			LinkedList<Point> neighbours = RangeQuery(points, p);
			//Noise Check
			if (neighbours.size() < Einstellungen.minPts) {
				labels.replace(p, -1);
				continue;
			}
			
			cId ++; //Neues Cluster
			labels.replace(p, cId);
			neighbours.remove(p);
			//Expandiere Cluster
			for (int i = 0; i < neighbours.size(); i++) {
				Point n = neighbours.get(i);
				if (labels.get(n) == -1)
					labels.replace(p, cId);
				if (labels.get(n) != -2)
					continue;
				labels.replace(n, cId);
				LinkedList<Point> n2 = RangeQuery(points, n);
				if (n2.size() >= Einstellungen.minPts) {
					mergeLists(neighbours, n2);
				}
			}
			
		}
		
		return durchschnitt(labels, points, cId);
		
	}
	
	private static LinkedList<Point> RangeQuery(Point[] punkte, Point mitte){
		LinkedList<Point> res = new LinkedList<Point>();
		for (Point p : punkte) {
			if (mitte == p)
				continue;
			if (dist(mitte, p) <= Einstellungen.dichte) {
				res.addFirst(p);
			}
		}
		return res;
	}
	
	private static double dist(Point m, Point p) {
		return Math.sqrt(Math.pow(p.x-m.x, 2)+Math.pow(p.y+m.y, 2));
	}
	
	private static void mergeLists(LinkedList<Point> s, LinkedList<Point> d) {
		s.addAll(d);
	}
	
	private static Point[] durchschnitt(Map<Point, Integer> lbl, Point[] pts, int size) {
		Point[] cluster = new Point[size];
		byte[] zähler = new byte[size];
		for (int i = 0; i< size; i++) {
			cluster[i] = new Point(0,0);
			zähler[i] = 0;
		}
		//Jeden Punkt clsuter hinzufügen
		for (Point p : pts) {
			int l = lbl.get(p)-1;
			if (l < 0)
				continue;
			cluster[l].x += p.x;
			cluster[l].y += p.y;
			zähler[l]++;
		}
		//Durchscnitt errechnen
		for (int i = 0; i < cluster.length; i++) {
			cluster[i].x /= zähler[i];
			cluster[i].y /= zähler[i];
		}
		return cluster;
	}
}
