using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace ErkennungCis
{
    class erkenner
    {
        #region singleton
        static erkenner inst = null;
        public static erkenner getInstance()
        {
            return inst;
        }
        #endregion

        #region Konstanten
        private static readonly float kontr = 3; //Weiß/ Schwarz schablone e.g 255/10 = 25 ...
        private static readonly int anzChecks = 16;
        private static float[] sin = new float[anzChecks];
        private static float[] cos = new float[anzChecks];

        //Clustering
        private static readonly int nachbarsch = 20; //nachbarschaftswert für Clustering. Wie weit müssen Punkte aneinander liegen
        private static readonly int minpts = 0; //Wie viele Punkte braucht es mindestens für ein cluster (-1)

        static erkenner()
        {
            for (int i = 0; i < anzChecks; i++)
            {
                double a = (2 * Math.PI*i) / anzChecks;
                sin[i] = (float)Math.Sin(a);
                cos[i] = (float)Math.Cos(a);
            }
        }
        #endregion



        Bitmap grayscale;
        Form1 form;
        byte[][] pixel;

        public erkenner(Image img, Form1 form)
        {
            inst = this;
            this.form = form;

            DateTime start = DateTime.Now;
            Bitmap bmp = new Bitmap(img);
            pixel = new byte[img.Width][];
            for (int i = 0; i < pixel.Length; i++)
                pixel[i] = new byte[img.Height];

            //Sehr problematische konversation von bitmap zu array
            for (int y = 0; y < bmp.Height; y++)
                for (int x = 0; x < bmp.Width; x++)
                    pixel[x][y] = gray(bmp.GetPixel(x, y));

            Debug.WriteLine("Bild -> Arr: {0} ms", DateTime.Now.Subtract(start).TotalMilliseconds);
            form.showImage(getGray());
            //Hauptblock
            
            start = DateTime.Now;
            DateTime startkompl = DateTime.Now;
            erkannt[] erg= raster();
            Debug.WriteLine("{0} Ergebnisse in {1}ms", erg.Length, DateTime.Now.Subtract(start).TotalMilliseconds);
            Point[] mittelpunkte = cluster(erg);
            Blume[] blumen = Radius.blume(pixel, mittelpunkte);
            Debug.WriteLine("Blumenerkennung ausgeführt in {0} ms", DateTime.Now.Subtract(startkompl).TotalMilliseconds);
            maleErgebnisse(blumen);
            
        }

        private erkannt[] raster()
        {
            //Kaskade. Es wird ein Gitter über das Bild gelegt und dann abgesucht
            int width = pixel.Length;
            int height = pixel[0].Length;
            List<erkannt> blumen = new List<erkannt>();
            int a = (int)(0.02f * width);
            for (int y = 0; y < height; y+=a)
            {
                for (int x = 0; x < width; x+=a)
                {
                    //form.drawPoint(x, y);
                    //Prüfe, ob hier ein Blumenmittelpunkt sein kann!
                    //Eventuell prüfen ob schwarz ist und dann umgebung beobachten
                    for (int g = 0; g < 10; g++) //G ist der maximale grad der checks
                    {
                        int r = (int)(5 * Math.Pow(1.5, g));
                        erkannt bl = blumigkeit(x, y, r);
                        if (bl.blumigkeit >= 0.7)
                            blumen.Add(bl);
                    }
                }
            }
            return blumen.ToArray();
        }

        byte gray(Color c) {
            //Für mehr kontrast, wird das ganze mit einer sigmoid funktion gestreckt.
            byte gray = (byte)((c.R + c.G + c.B) / 3f); //- Linear, wenn direkt returnt
            //Schlechter (Grün-) Filter
            if (c.R < 100)
                gray = 0;
            return (byte)(255*(1/(1+Math.Pow(Math.E,-0.025*(gray-127)))));//Sigmoid
        }

        public erkannt blumigkeit(int x, int y, float r, bool debug = false)
        {
            //x,y die koordinaten des Pixels
            //cheks: die anzahl der checks um das Pixel
            // r: radius

            //Muster: schwarzes Pixel in der Mitte, Weißes Pixel weiter außen
            int s = 0;

            for (int i = 0; i < anzChecks; i ++)
            {
                //Berechnen der x und y werte der Pixel
                bool res = schablone(i, r, new Point(x,y));
                if (res)
                {
                    //form.drawLine(x, y, (int)(Math.Cos(a) * r + x), (int)(Math.Sin(a) * r + y));
                    s++;
                }
                    
                
            }

            Debug.WriteLineIf(debug,String.Format("K({0}|{1}) Blumigkeit = {2:0.00} für r={3}", x, y,s/(float)anzChecks,r));
            return new erkannt(x,y,(int)r,s/(float)anzChecks);
        }

        private bool schablone(int check, float r, Point mitte)
        {
            //Check := nummer des checks

            //Perfekte welt: 0-0.4*r = Schwarz 0.4*r - r = weiß
            float schwarz = 0.2f * r; float weiß = 0.9f * r; //definieren die abstände von der Mite rel. zum Radius
            float weiß2 = 0.6f * r;

            double wx = cos[check] * weiß + mitte.X;
            double wy = sin[check] * weiß + mitte.Y;

            if (wx < 0 || wx >= pixel.Length || wy >= pixel[0].Length || wy < 0)
                return false;//Check ob raus

            byte pw = pixel[(int)wx][(int)wy];
            //Andere Pixel frei
            
            byte ps = colorAt(mitte, check, schwarz);
            ps = (ps == (byte)0) ? (byte)1 : ps;
            byte pw2 = colorAt(mitte, check, weiß2);

            if (((float)pw / (float)ps >= kontr) && ((float)pw2 / (float)ps >= kontr))
            {
                return true;
            }

            return false;
        }

        private byte colorAt(Point mitte, int c, float r)
        {
            float x = cos[c] * r + mitte.X; //Oftes Sinusberechnen!
            float y = sin[c] * r + mitte.Y;
            return pixel[(int)x][(int)y];
        }

        private Point[] cluster(erkannt[] erks)
        {
            //DBSCAN Clustering SIEHE https://de.wikipedia.org/wiki/DBSCAN
            //Problem: wir müssen anzahl der Cluster geben
            Debug.WriteLine("Clustering startet");
            Point[] punkte = new Point[erks.Length];
            for (int i = 0; i < erks.Length; i++)
            {
                //Anzeigen
                erkannt e = erks[i];
                //form.fillEll(e.x, e.y, (int)(e.blumigkeit * 2), Brushes.Orange);
                punkte[i] = new Point(e.x, e.y);
            }
            Point[] blumen = Cluster.cluster(punkte, nachbarsch, minpts);
            Debug.WriteLine("Clustering beendet. {0} Blumen gefunden", blumen.Length);
            return blumen;

        }


        //Helfermethoden

        public static double Rad2Deg(double a)
        {
            return (a / Math.PI) * 180.0;
        }

        private void maleErgebnisse(Blume[] erg)
        {
            foreach (Blume b in erg)
            {
                //Mittelpunkt einzeichnen
                form.drawPoint(b.mitte.X, b.mitte.Y);
                form.drawEll(b.mitte.X, b.mitte.Y, b.rad);
                form.showText(b.mitte.X, b.mitte.Y, String.Format("entf: {0}cm", b.entfernung));
            }
        }

        private Bitmap getGray()
        {
            if (grayscale != null)
                return grayscale;
            grayscale = new Bitmap(pixel.Length, pixel[0].Length);
            for (int y = 0; y < grayscale.Height; y++)
            {
                for (int x = 0; x < grayscale.Width; x++)
                {
                    int val = pixel[x][y];
                    grayscale.SetPixel(x, y, Color.FromArgb(val,val,val));
                }
            }
            return grayscale;
        }
    }
}
