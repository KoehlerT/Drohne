using System;
using System.Collections.Generic;
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
    class Helper
    {
    }
}
