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

        private readonly float kontr = 3; //Weiß/ Schwarz schablone e.g 255/10 = 25 ...

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
            start = DateTime.Now;
            erkannt[] erg= raster();
            Debug.WriteLine("{0} Ergebnisse in {1}ms", erg.Length, DateTime.Now.Subtract(start).TotalMilliseconds);
            maleErgebnisse(erg);
            Point[] blumen = cluster(erg);
            
        }

        private erkannt[] raster()
        {
            //Kaskade. Es wird ein Gitter über das Bild gelegt und dann abgesucht
            int width = pixel.Length;
            int height = pixel[0].Length;
            List<erkannt> blumen = new List<erkannt>();
            //Raster absuchen
            //Rastergröße Startet bei 30% wird kleiner bis 1%.
            for (int a = (int)(0.3f*width); a >= (int)(0.02f*width); a-=(int)(a*0.5f))
            {
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
                            erkannt bl = blumigkeit(x, y, 16, r); //Erkennt ob eine Blume da ist, oder nicht.
                            //Verbesserungsvorschalg. blumigkeit nur float, erkannt objekt erst bei approoveter blume
                            if (bl.blumigkeit >= 0.7)
                                blumen.Add(bl);
                        }
                    }
                }
                //Clear image
                 //Thread.Sleep(500);
                //form.showImage(getGray());
            }
            return blumen.ToArray() ;
        }

        byte gray(Color c) {
            //Für mehr kontrast, wird das ganze mit einer sigmoid funktion gestreckt.
            byte gray = (byte)((c.R + c.G + c.B) / 3f); //- Linear, wenn direkt returnt
            //Schlechter (Grün-) Filter
            if (c.R < 100)
                gray = 0;
            return (byte)(255*(1/(1+Math.Pow(Math.E,-0.025*(gray-127)))));//Sigmoid
        }

        public erkannt blumigkeit(int x, int y, int checks, float r, bool debug = false)
        {
            //x,y die koordinaten des Pixels
            //cheks: die anzahl der checks um das Pixel
            // r: radius

            //Muster: schwarzes Pixel in der Mitte, Weißes Pixel weiter außen
            int s = 0;

            for (int i = 0; i < checks; i ++)
            {
                //Berechnen der x und y werte der Pixel
                double a = (2 * Math.PI * i)/checks; //Winkel in dem geprüft wird !Bogenmaß! //TODO: Konstante
                bool res = schablone(a, r, new Point(x,y));
                if (res)
                {
                    //form.drawLine(x, y, (int)(Math.Cos(a) * r + x), (int)(Math.Sin(a) * r + y));
                    s++;
                }
                    
                
            }

            Debug.WriteLineIf(debug,String.Format("K({0}|{1}) Blumigkeit = {2:0.00} für r={3}", x, y,s/(float)checks,r));
            return new erkannt(x,y,(int)r,s/(float)checks);
        }

        private bool schablone(double a, float r, Point mitte)
        {
            //Perfekte welt: 0-0.4*r = Schwarz 0.4*r - r = weiß
            float schwarz = 0.2f * r; float weiß = 0.9f * r; //definieren die abstände von der Mite rel. zum Radius
            float weiß2 = 0.6f * r;

            double wx = Math.Cos(a) * weiß + mitte.X;
            double wy = Math.Sin(a) * weiß + mitte.Y;

            if (wx < 0 || wx >= pixel.Length || wy >= pixel[0].Length || wy < 0)
                return false;//Check ob raus / Verbessern... unnötiges testen im prinzip

            byte pw = pixel[(int)wx][(int)wy];
            //Andere Pixel frei
            
            byte ps = colorAt(mitte, a, schwarz);
            ps = (ps == (byte)0) ? (byte)1 : ps;
            byte pw2 = colorAt(mitte, a, weiß2);

            if (((float)pw / (float)ps >= kontr) && ((float)pw2 / (float)ps >= kontr))
            {
                //form.drawLine(mitte.X, mitte.Y, (int)wx, (int)wy);
                return true;
            }

            return false;
            //Debug.WriteLineIf(debug,String.Format("Prüfe Pixel S({0:0.0}|{1:0.0}) = {2}  und W({3:0.0}|{4:0.0}) = {5}", sx, sy, ps, wx, wy,pw));
        }

        private byte colorAt(Point mitte, double a, float r)
        {
            double x = Math.Cos(a) * r + mitte.X; //Oftes Sinusberechnen!
            double y = Math.Sin(a) * r + mitte.Y;
            return pixel[(int)x][(int)y];
        }

        private Point[] cluster(erkannt[] erks)
        {
            //DBSCAN Clustering SIEHE https://de.wikipedia.org/wiki/DBSCAN
            //Problem: wir müssen anzahl der Cluster geben
            Debug.WriteLine("Clustering startet");
            Thread.Sleep(1000);
            form.showImage(getGray()); //Clear image
            Point[] punkte = new Point[erks.Length];
            for (int i = 0; i < erks.Length; i++)
            {
                //Anzeigen
                erkannt e = erks[i];
                //form.fillEll(e.x, e.y, (int)(e.blumigkeit * 2), Brushes.Orange);
                punkte[i] = new Point(e.x, e.y);
            }
            Point[] blumen = Cluster.cluster(punkte, 10);
            Debug.WriteLine("Clustering beendet. {0} Blumen gefunden", blumen.Length);
            foreach (Point p in blumen)
            {
                form.fillEll(p.X, p.Y, (int)(10), Brushes.Blue);
            }
            return blumen;

        }


        //Helfermethoden

        public static double Rad2Deg(double a)
        {
            return (a / Math.PI) * 180.0;
        }

        private void maleErgebnisse(erkannt[] erg)
        {
            foreach (erkannt e in erg)
            {
                form.drawEll(e.x, e.y, e.r);
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
