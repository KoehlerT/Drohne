using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.IO;
using System.Diagnostics;

namespace ErkennungCis
{
    /*Größter Teil des codes abgeschireben von::
     * https://www.bwinf.de/fileadmin/user_upload/BwInf/0_2016/35/2._Runde/L%C3%B6sungshinweise/loesungshinweise352.pdf
     * dritte aufgabe: Kreiscodes
     * Sigmoid funktion usw:
     * https://en.wikipedia.org/wiki/Sigmoid_function
     */

    public partial class Form1 : Form
    {
        double scl;
        int deltaY;

        public Form1()
        {
            InitializeComponent();
            
        }

        private void btn_opem_Click(object sender, EventArgs e)
        {
            string dokumente = Environment.GetFolderPath(Environment.SpecialFolder.MyDocuments);
            Debug.WriteLine(dokumente + "\\GitHub\\Drohne\\Basis\\Blumenbilder\\klein");
            if (Directory.Exists(dokumente + "\\GitHub\\Drohne\\Basis\\Blumenbilder\\klein"))
            {
                openFileDialog.InitialDirectory = dokumente + "\\GitHub\\Drohne\\Basis\\Blumenbilder\\klein";
            }
            openFileDialog.Filter = "JPG (*.jpg)|*.JPG|All files (*.*)|*.*";
            if (openFileDialog.ShowDialog() == DialogResult.OK)
            {
                Image img = Image.FromFile( openFileDialog.FileName);
                showImage(new Bitmap(img));
                new erkenner(img, this);
            }
        }

        public void showImage(Bitmap bmp)
        {
            Graphics g = image_Box.CreateGraphics();
            scl = 600/(double)bmp.Width;
            deltaY = (int)((600 - bmp.Height * scl) / 2);
            g.DrawImage(bmp, 0, deltaY, (int)(bmp.Width*scl), (int)(bmp.Height*scl));
        }

        public void drawPoint(int x, int y)
        {
            Graphics g = image_Box.CreateGraphics();
            Rectangle rect = new Rectangle((int)(x*scl)-2,(int)(y*scl)-2+deltaY,4,4);
            g.FillEllipse(Brushes.Orange, rect);
        }
        public void drawLine(int x1, int y1, int x2, int y2)
        {
            Graphics g = image_Box.CreateGraphics();
            g.DrawLine(Pens.Aqua, (float) (x1 * scl),(float)( y1 * scl)+deltaY, (float)(x2 * scl), (float)(y2 * scl)+deltaY);
        }

        public void drawEll(int mx, int my, int r)
        {
            Graphics g = image_Box.CreateGraphics();
            g.DrawEllipse(Pens.Aqua, (int)((mx - r)*scl), (int)((my - r)*scl+deltaY), 2 * r, 2 * r);
        }

        private void image_Box_MouseClick(object sender, MouseEventArgs e)
        {
            int x = e.X; int y = e.Y;
            //x und y zu array indexe
            erkannt max = new erkannt();
            for (int g = 0; g < 10; g++) //G ist der maximale grad der checks
            {
                int r = (int)(5 * Math.Pow(1.5, g));
                erkannt erk = erkenner.getInstance().blumigkeit((int)(x / scl),(int) ((y - deltaY) / scl), 8, r, true);
                if (max.blumigkeit < erk.blumigkeit)
                    max = erk;
            }
            Debug.WriteLine(max.ToString());
            if (max.blumigkeit >= 0.7f)
                drawEll(max.x, max.y, max.r);
            
        }
    }
}
