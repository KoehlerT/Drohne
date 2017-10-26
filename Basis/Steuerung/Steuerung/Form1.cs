using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Diagnostics;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Steuerung
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }
        Kommunikator verbindung = new Kommunikator(new byte[] {192,168,178,201});
        private void Form1_Load(object sender, EventArgs e)
        {
            Abfrage.Start();
        }

        private void Abfrage_Tick(object sender, EventArgs e)
        {
            DateTime a = DateTime.Now;
            float temp = verbindung.getTemp();
            lbl_ping.Text = String.Format("Ping: {0:00.0}ms", DateTime.Now.Subtract(a).TotalMilliseconds);
            lbl_temp.Text = String.Format("Temperatur: {0}°C", temp);
        }
    }
}
