#define con (5.0/1023.0)

void setup() {
  // put your setup code here, to run once:
  Serial.begin(57600);
}

void loop() {
  // put your main code here, to run repeatedly:
  Serial.write(27);       // ESC command
  Serial.print("[2J");    // clear screen command
  Serial.write(27);
  Serial.print("[H");     // cursor to home command

  for (int i = 1; i <=7; i++){
    Serial.print("A");
    Serial.print(i);
    Serial.print(": ");
    Serial.print(analogRead(i)*con);
    Serial.println(" V");
  }
  

  delay(500);
}
