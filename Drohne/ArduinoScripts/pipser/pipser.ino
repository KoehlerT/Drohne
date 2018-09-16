#include <Wire.h>
#define beeper 11
#define hoehensensor 0x60
void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  Serial.println("Board gestartet");
  Wire.begin();
}

void loop() {
  // put your main code here, to run repeatedly:
  Wire.beginTransmission(hoehensensor);
  delay(500);
  Wire.requestFrom(hoehensensor,2);
  if (Wire.available() == 2){
    int bit1 = Wire.read();
    int bit2 = Wire.read();
    Wire.endTransmission();
    Serial.println(bit1);
    Serial.println(bit2);
  }
}

void beep(){
  analogWrite(beeper, 50);
  delay(100);
  analogWrite(beeper,0);
}

