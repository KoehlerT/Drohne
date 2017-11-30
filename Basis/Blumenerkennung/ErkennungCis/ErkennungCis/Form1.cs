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
    public partial class Form1 : Form
    {
        double scl;
        int deltaY;
        Image img;

        public Form1()
        {
            InitializeComponent();
            
        }

        private void btn_opem_Click(object sender, EventArgs e)
        {
            string dokumente = Environment.GetFolderPath(Environment.SpecialFolder.MyDocuments);
            Debug.WriteLine(dokumente + "\\GitHub\\Drohne\\Basis\\Blumenbilder");
            if (Directory.Exists(dokumente + "\\GitHub\\Drohne\\Basis\\Blumenbilder"))
            {
                openFileDialog.InitialDirectory = dokumente + "\\GitHub\\Drohne\\Basis\\Blumenbilder";
            }
            openFileDialog.Filter = "JPG (*.jpg)|*.JPG|All files (*.*)|*.*";
            if (openFileDialog.ShowDialog() == DialogResult.OK)
            {
                img = Image.FromFile( openFileDialog.FileName);
                showImage();
                new erkenner(img, this);
            }
        }

        public void showImage()
        {
            Graphics g = image_Box.CreateGraphics();
            scl = 600/(double)img.Width;
            deltaY = (int)((600 - img.Height * scl) / 2);
            g.DrawImage(img, 0, deltaY, (int)(img.Width*scl), (int)(img.Height*scl));
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
    }
}
