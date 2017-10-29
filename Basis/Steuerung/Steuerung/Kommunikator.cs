using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net.Sockets;
using System.IO;
using System.Diagnostics;
using System.Globalization;
using System.Drawing;

namespace Steuerung
{
    class Kommunikator
    {
        StreamReader sr;
        StreamWriter sw;
        Stream s;
        public Kommunikator(byte[] ip)
        {
            TcpClient client = new TcpClient("Drohne", 5892);
            sr = new StreamReader(client.GetStream(), Encoding.UTF8, false, 65536);
            sw = new StreamWriter(client.GetStream());
            s = client.GetStream();

            sw.WriteLine("Hi_Drohne");
            sw.Flush();

            Debug.WriteLine("HI");
        }

        public float getTemp()
        {
            sw.Write("temp");
            sw.Flush();
            string empf = sr.ReadLine();
            //temp=35.8'C
            string zahl = empf.Split('=')[1].Split('\'')[0];
            float temp = float.Parse(zahl, new CultureInfo("us-US").NumberFormat);
            return temp;
        }

        public void getBild()
        {
            DateTime start = DateTime.Now;
            sw.Write("bild");
            sw.Flush();
            int c = 0;
            Debug.Write("Bild wird geladen: ");

            StreamWriter fw = new StreamWriter("bild.bmp");
            LinkedList<char> chars = new LinkedList<char>();

            char[] bytes = new char[1024];
            int data = s.ReadByte();
            //sr.Read(bytes, 0, 1024);
            while (data >= 0)
            {
                fw.Write((char)data);
                data = s.ReadByte();
                c++;
            }

            fw.Close();
            Debug.WriteLine((c/1000f) + "KB Daten Übertragen in "+DateTime.Now.Subtract(start).TotalMilliseconds+"ms");
            return;
        }

    }
}
