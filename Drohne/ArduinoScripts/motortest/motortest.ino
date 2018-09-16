#include <Servo.h>
#define mot_pin 8

Servo m;
void setup() {
  // put your setup code here, to run once:
  m.attach(mot_pin);
  delay(5);
  //m.write(40);//Arm esc
  m.writeMicroseconds(1000);
  Serial.begin(9600);
  Serial.println("Val: 1000-2000:");
}

void loop() {
  // put your main code here, to run repeatedly:
  int val;
  if (Serial.available()) {
    val = Serial.parseInt();
    m.writeMicroseconds(val);
  }
}
