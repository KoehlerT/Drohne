#define HWire WIRE2

//Barometer variables.
//Pressure variables.
float pid_error_gain_altitude, pid_throttle_gain_altitude;
uint16_t C[7];
uint8_t barometer_counter, temperature_counter, average_temperature_mem_location;
int64_t OFF, OFF_C2, SENS, SENS_C1, P;
uint32_t raw_pressure, raw_temperature, temp, raw_temperature_rotating_memory[6], raw_average_temperature_total;
float actual_pressure_slow, actual_pressure_fast, actual_pressure_diff;
float ground_pressure, altitude_hold_pressure;
int32_t dT, dT_C5;

uint8_t parachute_rotating_mem_location;
int32_t parachute_buffer[35], parachute_throttle;
float pressure_parachute_previous;

int32_t pressure_rotating_mem[50], pressure_total_avarage;
uint8_t pressure_rotating_mem_location;
float pressure_rotating_mem_actual;
//uint32_t loop_timer;

float altitude_meter, start_pressure;

uint8_t MS5611_address = 0x77;             //The I2C address of the MS5611 barometer is 0x77 in hexadecimal form.

void barometer_setup(){
  HWire.beginTransmission(MS5611_address);                        //Start communication with the MS5611.
  if (HWire.endTransmission() != 0) {                                               //If the exit status is not 0 an error occurred.

    #ifdef debug
    Serial.print("MS5611 is not responding on address: ");        //Print the error on the screen.
    Serial.println(MS5611_address, HEX);
    #endif
    
    while (1);
  }
  else {                                                          //If the MS5611 is responding normal

    //For calculating the pressure the 6 calibration values need to be polled from the MS5611.
    //These 2 byte values are stored in the memory location 0xA2 and up.
    for (int i = 1; i <= 6; i++) {
      HWire.beginTransmission(MS5611_address);                    //Start communication with the MPU-6050.
      HWire.write(0xA0 + i * 2);                              //Send the address that we want to read.
      HWire.endTransmission();                                    //End the transmission.

      HWire.requestFrom(MS5611_address, 2);                       //Request 2 bytes from the MS5611.
      C[i] = HWire.read() << 8 | HWire.read();                //Add the low and high byte to the C[x] calibration variable.
    }

    OFF_C2 = C[2] * pow(2, 16);                                   //This value is pre-calculated to offload the main program loop.
    SENS_C1 = C[1] * pow(2, 15);                                  //This value is pre-calculated to offload the main program loop.
    #ifdef debug
      Serial.println("Barometer initialated");
    #endif
  }
}

void barometer_getvalue(){
  barometer_counter ++;

  if (barometer_counter == 1) {
    if (temperature_counter == 0) {
      //Get temperature data from MS-5611
      HWire.beginTransmission(MS5611_address);
      HWire.write(0x00);
      HWire.endTransmission();
      HWire.requestFrom(MS5611_address, 3);

      raw_average_temperature_total -= raw_temperature_rotating_memory[average_temperature_mem_location];
      raw_temperature_rotating_memory[average_temperature_mem_location] = HWire.read() << 16 | HWire.read() << 8 | HWire.read();
      raw_average_temperature_total += raw_temperature_rotating_memory[average_temperature_mem_location];
      average_temperature_mem_location++;
      if (average_temperature_mem_location == 5)average_temperature_mem_location = 0;
      raw_temperature = raw_average_temperature_total / 5;
    }
    else {
      //Get pressure data from MS-5611
      HWire.beginTransmission(MS5611_address);
      HWire.write(0x00);
      HWire.endTransmission();
      HWire.requestFrom(MS5611_address, 3);
      raw_pressure = HWire.read() << 16 | HWire.read() << 8 | HWire.read();
    }

    temperature_counter ++;
    if (temperature_counter == 20) {
      temperature_counter = 0;
      //Request temperature data
      HWire.beginTransmission(MS5611_address);
      HWire.write(0x58);
      HWire.endTransmission();
    }
    else {
      //Request pressure data
      HWire.beginTransmission(MS5611_address);
      HWire.write(0x48);
      HWire.endTransmission();
    }
  }
  if (barometer_counter == 2) {
    //Calculate pressure as explained in the datasheet of the MS-5611.
    dT = C[5];
    dT <<= 8;
    dT *= -1;
    dT += raw_temperature;
    OFF = OFF_C2 + ((int64_t)dT * (int64_t)C[4]) / pow(2, 7);
    SENS = SENS_C1 + ((int64_t)dT * (int64_t)C[3]) / pow(2, 8);
    P = ((raw_pressure * SENS) / pow(2, 21) - OFF) / pow(2, 15);

    //Let's use a rotating mem
    pressure_total_avarage -= pressure_rotating_mem[pressure_rotating_mem_location];    //Subtract the current memory position to make room for the new value.
    pressure_rotating_mem[pressure_rotating_mem_location] = P;                          //Calculate the new change between the actual pressure and the previous measurement.
    pressure_total_avarage += pressure_rotating_mem[pressure_rotating_mem_location];    //Add the new value to the long term avarage value.
    pressure_rotating_mem_location++;                                                   //Increase the rotating memory location.
    if (pressure_rotating_mem_location == 20)pressure_rotating_mem_location = 0;        //Start at 0 when the memory location 20 is reached.
    actual_pressure_fast = (float)pressure_total_avarage / 20.0;                        //Calculate the avarage pressure value of the last 20 readings.
    if(millis() < 5000)actual_pressure_slow = actual_pressure_fast;                     //Keep the slow and fast avareges the same for the first 5 seconds.

    actual_pressure_slow = actual_pressure_slow * (float)0.985 + actual_pressure_fast * (float)0.015;
    
    actual_pressure_diff = actual_pressure_slow - actual_pressure_fast;
    if (actual_pressure_diff > 8)actual_pressure_diff = 8;
    if (actual_pressure_diff < -8)actual_pressure_diff = -8;
    if (actual_pressure_diff > 1 || actual_pressure_diff < -1)actual_pressure_slow -= actual_pressure_diff / 6.0;
    actual_pressure = actual_pressure_slow;

    pid_altitude_input = actual_pressure;
  }

  if (barometer_counter == 3) {                                                         //When the barometer counter is 3
    //Time Left to do stuff
    barometer_counter = 0;                                                              //Set the barometer counter to 0 for the next measurements.
    if(millis() > 5500 && start_pressure == 0){
      start_pressure = actual_pressure;
    }
    calculateHeightMeter();

    #ifdef debug
      printAlt();
    #endif
  }
  
}

#ifdef debug
void printAlt(){
  Serial.print(altitude_meter);
  Serial.println("M");
}
#endif

void calculateHeightMeter(){
  float pressure_diff = actual_pressure - start_pressure;
  altitude_meter = pressure_diff * (float)0.117;
  altitude_meter *= -1;
}

