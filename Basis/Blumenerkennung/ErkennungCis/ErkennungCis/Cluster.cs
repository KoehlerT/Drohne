using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using System.Diagnostics;

namespace ErkennungCis
{
    class Cluster
    {

        public static Point[] cluster(Point[] punkte, int dichte, int minpts = 0)
        {
            DateTime start = DateTime.Now;
            int cID = 0;
            Dictionary<Point, int> labels = new Dictionary<Point, int>();
            //Punkte Labeln
            foreach (Point p in punkte)
            {
                if (!labels.ContainsKey(p))
                    labels.Add(p, -2);
            }
            //Lables: -2 = Undefined
            //Lables: -1 = Noise
            //Lables: 0+ = Clusterzug.


            foreach (Point p in punkte)
            {
                if (labels[p] != -2)
                    continue;
                var neighbors = RangeQuery(punkte, p, dichte);
                if (neighbors.Count <= minpts)
                {
                    labels[p] = -1; //Noise
                    continue;
                }
                cID++; //Neues Cluster
                labels[p] = cID; //p neues Cluster
                neighbors.Remove(p);
                for (int i = 0; i < neighbors.Count; i++)
                {
                    Point n = neighbors.ElementAt<Point>(i);
                    if (labels[n] == -1)
                        labels[n] = cID;
                    if (labels[n] != -2)
                        continue;
                    labels[n] = cID;
                    var nn = RangeQuery(punkte, n, dichte);
                    if (nn.Count >= minpts)
                    {
                        mergeLists(neighbors, nn);
                        //Debug.WriteLine("neighbours: " + neighbors.Count+ " CID: "+cID);
                    }
                }

            }

            //Ergebnis fertig machen
            //Pro Cluster ein Punkt
            // Durchschnittlichen Mittelpunkt berechnen
            Point[] punkt = new Point[cID]; //So viele Punkte wie Cluster
            byte[] zähler = new byte[cID]; //o viele Zähler wie Cluster
            //Arrays mit Leerwerten befüllen
            for (int i = 0; i < cID; i++)
            {
                punkt[i] = Point.Empty;
                zähler[i] = 0;
            }
            //Durchschnitt aufaddieren
            foreach (Point p in punkte)
            {
                int lbl = labels[p]-1;
                if (lbl < 0)
                    continue;
                punkt[lbl].X += p.X;
                punkt[lbl].Y += p.Y;
                zähler[lbl]++;

            }

            //Durchschnitt teilen
            for (int i = 0; i < cID; i++)
            {
               punkt[i].X /= zähler[i];
                punkt[i].Y /= zähler[i];
            }
            Debug.WriteLine("{0} Cluster in {1}ms",punkt.Length, DateTime.Now.Subtract(start).TotalMilliseconds);
            return punkt;
        }

        private static LinkedList<Point> RangeQuery(Point[] points, Point mitte, int minD)
        {
            LinkedList<Point> ps = new LinkedList<Point>();
            foreach (Point p in points)
            {
                if (mitte == p)
                    continue;
                if (dist(mitte, p)<=minD)
                {
                    ps.AddFirst(p);
                }
            }
            return ps;
        }

        private static double dist(Point p1, Point p2)
        {
             return Math.Sqrt(Math.Pow(p2.X - p1.X, 2) + Math.Pow(p2.Y - p1.Y, 2));
        }

        private static void mergeLists(LinkedList<Point> main, LinkedList<Point> append)
        {
            foreach (Point p in append)
            {
                main.AddLast(p);
            }
        }


    }
}
