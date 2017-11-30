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
        Image img;
        Form1 form;
        float[][] pixel;

        public erkenner(Image img, Form1 form)
        {
            this.form = form;
            this.img = img;

            DateTime start = DateTime.Now;
            Bitmap bmp = new Bitmap(img);
            pixel = new float[img.Width][];
            for (int i = 0; i < pixel.Length; i++)
                pixel[i] = new float[img.Height];

            //Sehr problematische konversation von bitmap zu array
            for (int y = 0; y < bmp.Height; y++)
                for (int x = 0; x < bmp.Width; x++)
                    pixel[x][y] = gray(bmp.GetPixel(x, y));

            Debug.WriteLine("Bild -> Arr: {0} ms", DateTime.Now.Subtract(start).TotalMilliseconds);
            schablone();
        }

        private void schablone()
        {
            int width = pixel.Length;
            int height = pixel[0].Length;
            //Raster absuchen
            //Rastergröße Startet bei 100 wird kleiner bis 10.
            for (int a = 100; a >= 10; a-=10)
            {
                for (int y = 0; y < height; y+=a)
                {
                    for (int x = 0; x < width; x+=a)
                    {
                        form.drawPoint(x, y);
                        //Prüfe, ob hier ein Blumenmittelpunkt sein kann!
                        //Eventuell prüfen ob schwarz ist und dann umgebung beobachten
                    }
                }
                //Clear image
                Thread.Sleep(500);
                form.showImage();
            }
        }

        float gray(Color c) { return (c.R + c.G + c.B) / 3f; }
        
    }
}
