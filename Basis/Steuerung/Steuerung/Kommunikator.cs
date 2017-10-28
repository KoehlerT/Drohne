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
        public Kommunikator(byte[] ip)
        {
            TcpClient client = new TcpClient("Drohne", 5892);
            sr = new StreamReader(client.GetStream());
            sw = new StreamWriter(client.GetStream());

            sw.WriteLine("Hi_Drohne");
            sw.Flush();

            /*string data = sr.ReadLine();
            while (data!= null)
            {
                Debug.WriteLine(data);
                data = sr.ReadLine();
            }*/
            //client.Close();
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
            StreamWriter fw = new StreamWriter("bild.txt");
            LinkedList<Byte> bytearray = new LinkedList<byte>();
            char[] bytes = new char[370000];
            System.Threading.Thread.Sleep(500);
            while (sr.Peek()>=0)
            {
                sr.Read(bytes, c, 1);
                fw.Write(bytes[c]);
                if ((c % 5000) == 0)
                {
                    System.Threading.Thread.Sleep(500);
                    Debug.WriteLine((c / 1000f) + "KB Übertragen");
                }
                
                c++;                
            }
            fw.Close();
            Debug.WriteLine((c/1000f) + "KB Daten Übertragen in "+DateTime.Now.Subtract(start).TotalMilliseconds+"ms");
            return;
        }

    }
}
