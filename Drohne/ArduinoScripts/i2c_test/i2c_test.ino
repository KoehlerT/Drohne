#include <Wire.h>
void setup() {
  // put your setup code here, to run once:
  Wire.begin();
  Serial.begin(9600);
  while(!Serial);
  Serial.println("Start");
  Wire.beginTransmission(0x60);
  Serial.println("Transmission begin");
  Wire.endTransmission();
  Serial.println("Transmission end");
  //Serial.println(error);
}

void loop() {
  // put your main code here, to run repeatedly:

}
