using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ErkennungCis
{
    struct erkannt
    {
        public int x, y;
        public float blumigkeit;
        public int r;
        public erkannt(int x, int y, int r, float b)
        {
            this.x = x; this.y = y; this.r = r; this.blumigkeit = b;
        }

        public override string ToString()
        {
            return String.Format("An P({0}|{1}) bl={2:0.00} mit r={3}", x, y, blumigkeit, r);
        }
    }

    struct Blume
    {
        public Point mitte;
        public int rad;
        public int entfernung;
        public Blume(Point mitte, int rad, int entfernung)
        {
            this.mitte = mitte; this.rad = rad; this.entfernung = entfernung;
        }
        public Blume(int x, int y, int rad, int entfernung)
        {
            this.mitte = new Point(x,y); this.rad = rad; this.entfernung = entfernung;
        }
    }

    struct UPunkt
    {
        public int x;
        public int y;
        public bool valid;
        public UPunkt(int x, int y, bool valid = true)
        {
            this.x = x; this.y = y; this.valid = valid;
        }
        public UPunkt(bool valid)
        {
            this.valid = valid; x = -1; y = -1;
        }

        public int dist(UPunkt p2)
        {
            return (int)Math.Sqrt(Math.Pow(p2.x - x, 2) + Math.Pow(p2.y - y, 2));
        }
    }

    class Helper
    {
    }
}
