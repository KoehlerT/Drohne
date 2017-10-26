using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net.Sockets;
using System.IO;
using System.Diagnostics;
using System.Globalization;

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

    }
}
