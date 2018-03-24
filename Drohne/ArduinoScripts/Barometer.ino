#include <Wire.h>
#include <Adafruit_BMP085.h>
double Hoehe = 0;
double Temperatur = 0;
double HoeheMittelwert = 0;
int Druck;
Adafruit_BMP085 bmp;
 
void setup() 
{
  Serial.begin(9600);
  if (!bmp.begin()) 
  {
  Serial.println("BMP180 sensor not found");
  while (1) {}
  }
}
 
void loop() {
  HoeheMittelwert = 0;
    for(int x = 0; x<5; x++){
    HoeheMittelwert = HoeheMittelwert + bmp.readAltitude(bmp.readSealevelPressure());
    }
    HoeheMittelwert = HoeheMittelwert/5;
    Temperatur = bmp.readTemperature();
    Hoehe = bmp.readAltitude(bmp.readPressure());
    Druck = bmp.readPressure();
    Serial.print("Luftdruck = ");
    Serial.print(Druck);
    Serial.println(" p");

    Serial.print("Luftdruck bei NN = ");
    Serial.print(bmp.readSealevelPressure());
    Serial.println(" p");
    
    Serial.print("Temperatur = ");
    Serial.print(Temperatur);
    Serial.println(" *C");

    Serial.print("Höhe = ");
    Serial.print(Hoehe);
    Serial.println(" Meter");
    
    Serial.print("Höhe im Durchschnitt = ");
    Serial.print(HoeheMittelwert);
    Serial.println(" Meter");
    Serial.println();
    
    
    delay(5000);
}
