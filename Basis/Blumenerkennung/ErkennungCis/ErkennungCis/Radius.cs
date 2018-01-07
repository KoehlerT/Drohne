using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ErkennungCis
{
    static class Radius
    {
#region Konstanten
        private static readonly int incr = 5;
        private static readonly int anzTest = 32;
        private static readonly int maxAbweichung = 10;

        private static float[] sin = new float[anzTest];
        private static float[] cos = new float[anzTest];
        #endregion
        private static int iorC = 0;

        static Radius()
        {
            //Sinus und Cosinus vorrechnen
            for (int i = 0; i < anzTest; i++)
            {
                sin[i] = (float)Math.Sin(((2 * Math.PI) / anzTest) * i) * incr;
                cos[i] = (float)Math.Cos(((2 * Math.PI) / anzTest) * i) * incr;
            }
        }


        public static Blume[] blume(byte[][] bild, Point[] mittelpunkte)
        {
            LinkedList<Blume> erg = new LinkedList<Blume>();
            DateTime start = DateTime.Now;
            foreach (Point mittelpunkt in mittelpunkte)
            {
                
                UPunkt[] umr = Umriss(bild, mittelpunkt);
                if (umr == null)
                    continue;
                //showUmr(umr);
                int rad = radius(umr);
                if (rad == 0)
                    continue;
                erg.AddFirst(new Blume(mittelpunkt,rad,entfernung(rad)));
            }
            Debug.WriteLine("Blumen validiert und Radius/Rntfernungsrechnungen in {0} ms", DateTime.Now.Subtract(start).TotalMilliseconds);
            return erg.ToArray<Blume>();
        }

        private static UPunkt[] Umriss(byte[][] bild,  Point mittelpunkt)
        {
            iorC = 0;
            UPunkt[] erg = new UPunkt[anzTest];
            for (int i = 0; i < anzTest; i++)
            {
                erg[i] = ende(bild, mittelpunkt, i);
                if (iorC > 3)
                {
                    //Debug.WriteLine("ungültige Blume entdeckt");
                    return null;
                }
            }
            return erg;
        }

        private static UPunkt ende(byte[][] bild,Point mittelpunkt, int ang)
        {
            //Punkt wandert von mittelpunkt nach außen, bis ein Weißer Punkt gefunden wurde
            int x = mittelpunkt.X;
            int y = mittelpunkt.Y;
            bool schwarz = true;
            while (schwarz)
            {
                x += (int)cos[ang];
                y += (int)sin[ang];

                //indexoutofrangecheck
                if (x >= bild.Length || x < 0 || y >= bild[0].Length || y < 0)
                {
                    iorC++;
                    return new UPunkt(false);
                }
                if (bild[x][y] >= 150)
                    schwarz = false;
            }
            return new UPunkt(x,y);
        }

        private static int radius(UPunkt[] umriss)
        {
            //Array von verschiedenen Radien
            LinkedList<int> rad = new LinkedList<int>();
            int insg = 0;
            for (int i = 0; i < anzTest/2; i++)
            {
                UPunkt p1 = umriss[i];
                UPunkt p2 = umriss[i + anzTest / 2];
                int r = p1.dist(p2) / 2;
                rad.AddFirst(r);
                insg += r;
            }
            //Durchschnittlicher Radius
            int dRad = insg / (anzTest/2);
            //Abweichungen
            int maxAbw = 0;
            foreach (int r in rad)
            {
                int abw = Math.Abs(r - dRad);
                maxAbw = Math.Max(abw, maxAbw);
            }
            //Durchschnittl. Abweichung:
            if (maxAbw >= maxAbweichung)
            {
                //Debug.WriteLine("Bume UNGÜLTIG!");
                return 0;
            }
            //Konvertieren in Ellipse? Perfekte Kreise?
            return dRad;
        }

        //Definition von Konstanten bei Berechnung der Distanz
        private static readonly float radSchw = 3.5f; //Radius des Blumeninneren in cm
        private static readonly float fokusl = 0.14f; //Fokuslänge der Kamera in cm
        private static readonly float gr = 0.36736f; //Breite des Kamerasensors in cm
        private static readonly int aufl = 640; //Auflösung des Bildes in Px (Breite)
        private static readonly float fakt = gr/aufl;

        private static int entfernung(int rad)
        {
            // s = (d*f)/a
            // a = rad * fakt
            return (int)((radSchw*fokusl)/(rad*fakt));
        }

        private static void showUmr(UPunkt[] ps)
        {
            Form1 f = Form1.getInstance();
            foreach (UPunkt p in ps)
            {
                if (p.valid)
                    f.drawPoint(p.x, p.y);
            }
        }
    }

}
