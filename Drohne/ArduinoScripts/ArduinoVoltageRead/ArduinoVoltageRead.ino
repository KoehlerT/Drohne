#define con (5.0/1023.0)

void setup() {
  // put your setup code here, to run once:
  Serial.begin(57600);
  analogReference(DEFAULT);
}

void loop() {
  // put your main code here, to run repeatedly:

  resetCursor();
  printRelevant();
  delay(100);
}



void resetCursor(){
    Serial.write(27);       // ESC command
    Serial.print("[2J");    // clear screen command
    Serial.write(27);
    Serial.print("[H");     // cursor to home command
}


void printAll(){
    for (int i = 0; i <=7; i++){
      Serial.print("A");
      Serial.print(i);
      Serial.print(": ");
      Serial.print(analogRead(i)*con);
      Serial.println(" V");
    }
}

void printRelevant(){
    Serial.print("3.3V: ");
    Serial.print(readVoltage(0));
    Serial.println(" V");
    Serial.print("5V: ");
    Serial.print(readVoltage(3));
    Serial.println(" V");
    Serial.print("Battery: ");
    Serial.print(readVoltage(2)*readVoltage(3));
    Serial.println(" V");
    Serial.print("Current: ");
    Serial.print(getAmp());
    Serial.println(" A");

    Serial.print("Time: ");
    long start = micros();
    readVoltage(0);
    long dur = micros() - start;
    Serial.print(dur/1000.0f);
    Serial.println("ms");
}

float getAmp(){
    float amp = readVoltage(1);
    float vcc = readVoltage(3);
    return (amp - (vcc*0.5f)) * (30/(vcc*0.5f));
}

long readVcc() {
  long result;
  // Read 1.1V reference against AVcc
  ADMUX = _BV(REFS0) | _BV(MUX3) | _BV(MUX2) | _BV(MUX1);
  delay(2); // Wait for Vref to settle
  ADCSRA |= _BV(ADSC); // Convert
  while (bit_is_set(ADCSRA,ADSC));
  result = ADCL;
  result |= ADCH<<8;
  result = 1125300L / result; // Back-calculate AVcc in mV
  return result;
}

float readVoltage(int pin){
    unsigned int ADCValue;
    float Voltage;
    float Vcc;

    Vcc = readVcc()/1000.0;
    ADCValue = analogRead(pin);
    return (ADCValue / 1024.0) * Vcc;
}
