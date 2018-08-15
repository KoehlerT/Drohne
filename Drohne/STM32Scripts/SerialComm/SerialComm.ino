//Serial2

void setup() {
  // put your setup code here, to run once:

  Serial.begin(9600);
  Serial1.begin(9600);
  delay(200);
  Serial.println("Hallo Welt");
  Serial1.println("Hallo Raspberry Pi");
}

void loop() {
  // put your main code here, to run repeatedly:

  if (Serial1.available()){
    char rd = (char)Serial1.read();
    Serial.print(rd);
    Serial1.print(rd);
  }

  if (Serial.available()){
    Serial1.print(Serial.read());
  }

  /*Serial1.println("Hallo Raspberry Pi");

  delay(10);*/
  
}
