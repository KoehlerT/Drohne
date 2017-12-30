using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace ErkennungCis
{
    class Cluster
    {

        public static Point[] cluster(Point[] punkte, int dichte, int minpts = 0)
        {
            int cID = 0;
            Dictionary<Point, int> labels = new Dictionary<Point, int>();
            //Punkte Labeln
            foreach (Point p in punkte)
            {
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
                foreach (Point n in neighbors)
                {
                    if (labels[n] == -1)
                        labels[n] = cID;
                    if (labels[n] == -2)
                        continue;
                    labels[n] = cID;
                    var nn = RangeQuery(punkte, n, dichte);
                    if (nn.Count >= minpts)
                    {
                        mergeLists(neighbors, nn);
                    }
                }

            }

            //Ergebnis fertig machen
            //Pro Cluster ein Punkt

            return null;
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
