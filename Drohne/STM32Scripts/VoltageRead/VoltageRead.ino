
void setup() {
  // put your setup code here, to run once:
  pinMode(5,INPUT);
  pinMode(6,INPUT);
  pinMode(7,INPUT);
  Serial.begin(57600);
  delay(200);
  Serial.println("Hello World");
}

void loop() {
  // put your main code here, to run repeatedly:
  Serial.print("Voltage Main: ");
  Serial.print((float)analogRead(6)/112.81);
  Serial.println("V");
  Serial.print("Amperage: ");
  float amp = (((analogRead(5) * (3.3/(float)4095))/1.65)*60-30);
  Serial.print(amp);
  Serial.println("A");

  unsigned long st = micros();
  analogRead(7);
  st = micros() - st;

  Serial.print("Duration: ");
  Serial.print(st);
  Serial.println("us");
  
  delay(100);
}
