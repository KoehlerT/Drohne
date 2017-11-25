#include <Servo.h>
#define mot_pin 11

Servo m;
void setup() {
  // put your setup code here, to run once:
  m.attach(mot_pin);
  delay(5);
  m.write(40);
  delay(500);
  //m.write(90);//0-180
}

void loop() {
  // put your main code here, to run repeatedly:
  
}
