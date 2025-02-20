///////////////////////////////////////////////////////////////////////////////////////
//Terms of use
///////////////////////////////////////////////////////////////////////////////////////
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//THE SOFTWARE.
///////////////////////////////////////////////////////////////////////////////////////
//Safety note
///////////////////////////////////////////////////////////////////////////////////////
//Always remove the propellers and stay away from the motors unless you
//are 100% certain of what you are doing.
///////////////////////////////////////////////////////////////////////////////////////

#include <Wire.h>                          //Include the Wire.h library so we can communicate with the gyro.
#include <EEPROM.h>                        //Include the EEPROM.h library so we can store information onto the EEPROM
#include <MPU9250.h>                       //Library Gyro

MPU9250 myIMU;
#define adcScale 4.88758f

//#define debug;
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//PID gain and limit settings
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
float pid_p_gain_roll = 1.7;               //Gain setting for the roll P-controller (1.3) 0.9
float pid_i_gain_roll = 0;              //Gain setting for the roll I-controller (0.05)
float pid_d_gain_roll = 30;                //Gain setting for the roll D-controller (15)
int pid_max_roll = 400;                    //Maximum output of the PID-controller (+/-)

float pid_p_gain_pitch = pid_p_gain_roll;  //Gain setting for the pitch P-controller.
float pid_i_gain_pitch = pid_i_gain_roll;  //Gain setting for the pitch I-controller.
float pid_d_gain_pitch = pid_d_gain_roll;  //Gain setting for the pitch D-controller.
int pid_max_pitch = pid_max_roll;          //Maximum output of the PID-controller (+/-)

float pid_p_gain_yaw = 3.0;                //Gain setting for the pitch P-controller. //4.0
float pid_i_gain_yaw = 0.02;               //Gain setting for the pitch I-controller. //0.02
float pid_d_gain_yaw = 0;                //Gain setting for the pitch D-controller.
int pid_max_yaw = 400;                     //Maximum output of the PID-controller (+/-)

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Declaring global variables
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
byte last_channel_1, last_channel_2, last_channel_3, last_channel_4;
//byte eeprom_data[36];   //Keine EEProm Data in unserem Fall
byte highByte, lowByte;
int receiver_input_channel_1, receiver_input_channel_2, receiver_input_channel_3, receiver_input_channel_4;
int last_received_1, last_received_2, last_received_3, last_received_4;
int counter_channel_1, counter_channel_2, counter_channel_3, counter_channel_4, loop_counter;
int esc_1, esc_2, esc_3, esc_4;
int throttle, battery_voltage;
int cal_int, start, gyro_address;
int receiver_input[5];
unsigned long timer_channel_1, timer_channel_2, timer_channel_3, timer_channel_4, esc_timer, esc_loop_timer;
unsigned long timer_1, timer_2, timer_3, timer_4, current_time;
unsigned long loop_timer;
double gyro_pitch, gyro_roll, gyro_yaw;
double gyro_axis[4], gyro_axis_cal[4];
float pid_error_temp;
float pid_i_mem_roll, pid_roll_setpoint, gyro_roll_input, pid_output_roll, pid_last_roll_d_error;
float pid_i_mem_pitch, pid_pitch_setpoint, gyro_pitch_input, pid_output_pitch, pid_last_pitch_d_error;
float pid_i_mem_yaw, pid_yaw_setpoint, gyro_yaw_input, pid_output_yaw, pid_last_yaw_d_error;
//SPI Kommunikation
byte datenplatzhalter[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}; //Daten zur Übermittlung an den Raspberry PI
byte controllerInputs[8]; //Inputs vom Controller
int looptime = 1;
int timeout = 0;
float Vcc; //Errechnen der Spannungen


//Flugdaten
int startLuftdruck;

//ADS read


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Setup routine
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void setup(){
#ifdef debug
  Serial.begin(57600);
  Serial.println("HI");
#endif
  //Read EEPROM for fast access data.
  /*for(start = 0; start <= 35; start++)eeprom_data[start] = EEPROM.read(start);
  gyro_address =  0x68;                           //Store the gyro address in the variable.*/

  Wire.begin();                                                //Start the I2C as master.
  myIMU.initMPU9250();

  //Arduino (Atmega) pins default to inputs, so they don't need to be explicitly declared as inputs.
  SPCR |= _BV(SPE);                                            //Configure SPI Slave
  DDRD |= B11101000;                                           //Configure digital poort 3,5,6 Output
  DDRB |= B00010001;                                           //Configure digital port 12 (MISO) Output. 13 Input! 8 Output

  //Use the led on the Arduino for startup indication.
  digitalWrite(8,HIGH);                                       //Turn on the warning led.

  //Check the EEPROM signature to make sure that the setup program is executed
  //while(eeprom_data[33] != 'J' || eeprom_data[34] != 'M' || eeprom_data[35] != 'B')delay(10);

  set_gyro_registers();                                        //Set the specific gyro registers. + Konfiguriere I2C Geräte
  //Was tut die Forschleife? Kalibrieren!?
  for (cal_int = 0; cal_int < 1250 ; cal_int ++){              //Wait 5 seconds before continuing.
    PORTD |= B11101000;                                        //Set digital poort 3,5,6,7 HIGH
    delayMicroseconds(1000);                                   //Wait 1000us.
    PORTD &= B00010111;                                        //Set digital poort 3,5,6,7 low.
    delayMicroseconds(3000);                                   //Wait 3000us.
  }

  //Let's take multiple gyro data samples so we can determine the average gyro offset (calibration).
  for (cal_int = 0; cal_int < 2000 ; cal_int ++){              //Take 2000 readings for calibration.
    if(cal_int % 15 == 0)digitalWrite(8, !digitalRead(8));   //Change the led status to indicate calibration.
    gyro_signalen();                                           //Read the gyro output.
    gyro_axis_cal[1] += gyro_axis[1];                          //Ad roll value to gyro_roll_cal.
    gyro_axis_cal[2] += gyro_axis[2];                          //Ad pitch value to gyro_pitch_cal.
    gyro_axis_cal[3] += gyro_axis[3];                          //Ad yaw value to gyro_yaw_cal.
    //We don't want the esc's to be beeping annoyingly. So let's give them a 1000us puls while calibrating the gyro.
    pulse(1000);
    delay(3);                                                  //Wait 3 milliseconds before the next loop.
  }
  //Now that we have 2000 measures, we need to devide by 2000 to get the average gyro offset.
  gyro_axis_cal[1] /= 2000;                                    //Divide the roll total by 2000.
  gyro_axis_cal[2] /= 2000;                                    //Divide the pitch total by 2000.
  gyro_axis_cal[3] /= 2000;                                   //Divide the yaw total by 2000.
#ifdef debug
  printCal();
#endif
  //Keine Interrupts
  /*PCICR |= (1 << PCIE0);                                       //Set PCIE0 to enable PCMSK0 scan.
  PCMSK0 |= (1 << PCINT0);                                     //Set PCINT0 (digital input 8) to trigger an interrupt on state change.
  PCMSK0 |= (1 << PCINT1);                                     //Set PCINT1 (digital input 9)to trigger an interrupt on state change.
  PCMSK0 |= (1 << PCINT2);                                     //Set PCINT2 (digital input 10)to trigger an interrupt on state change.
  PCMSK0 |= (1 << PCINT3);                                     //Set PCINT3 (digital input 11)to trigger an interrupt on state change.*/

  //Wait until the receiver is active and the throttle is set to the lower position.
  while(receiver_input_channel_3 < 990 || receiver_input_channel_3 > 1020 || receiver_input_channel_4 < 1400){
    receive();                                                 //Receive new Controller inputs
    convert_integer(0,receiver_input_channel_3,last_received_3);             //Convert the actual receiver signals for throttle to the standard 1000 - 2000us
    convert_integer(6,receiver_input_channel_4,last_received_4);             //Convert the actual receiver signals for yaw to the standard 1000 - 2000us
    start ++;                                                  //While waiting increment start whith every loop.
    //We don't want the esc's to be beeping annoyingly. So let's give them a 1000us puls while waiting for the receiver inputs.
    pulse(1000);

    delay(3);                                                  //Wait 3 milliseconds before the next loop.
    if(start == 125){                                          //Every 125 loops (500ms).
      digitalWrite(8, !digitalRead(8));                      //Change the led status.
      start = 0;                                               //Start again at 0.
    }
    //break;
  }
  start = 0;                                                   //Set start back to 0.

  //Load the battery voltage to the battery_voltage variable.
  //65 is the voltage compensation for the diode.
  //12.6V equals ~5V @ Analog 0.
  //12.6V equals 1023 analogRead(0).
  //1260 / 1023 = 1.2317.
  //The variable battery_voltage holds 1050 if the battery voltage is 10.5V. centiVolt
  battery_voltage = getBatteryVoltage();


  //Read Reverence voltage
  prepeareVoltage();
  delay(2);
  readVcc();

  //When everything is done, turn off the led.
  digitalWrite(8,LOW);                                        //Turn off the warning led.
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Main program loop
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void loop(){
  //if(start == 2){ Serial.print(gyro_roll,0); Serial.print(";"); Serial.print(gyro_pitch,0); Serial.print(";"); Serial.println(gyro_yaw,0); }


  /*receiver_input_channel_1 = convert_receiver_channel(1);      //Convert the actual receiver signals for pitch to the standard 1000 - 2000us.
  receiver_input_channel_2 = convert_receiver_channel(2);      //Convert the actual receiver signals for roll to the standard 1000 - 2000us.
  receiver_input_channel_3 = convert_receiver_channel(3);      //Convert the actual receiver signals for throttle to the standard 1000 - 2000us.
  receiver_input_channel_4 = convert_receiver_channel(4);      //Convert the actual receiver signals for yaw to the standard 1000 - 2000us.*/

  //prepeare Voltage readings
  prepeareVoltage();

  //Get Controller Inputs via SPI#
  receive();
  convert_integer(4,receiver_input_channel_1,last_received_1);
  convert_integer(2,receiver_input_channel_2,last_received_2);
  convert_integer(0,receiver_input_channel_3,last_received_3);
  convert_integer(6,receiver_input_channel_4,last_received_4);

  //Let's get the current gyro data and scale it to degrees per second for the pid calculations.
  gyro_signalen();

  gyro_roll_input = (gyro_roll_input * 0.8) + ((gyro_roll) * 0.2);            //Gyro pid input is deg/sec.
  gyro_pitch_input = (gyro_pitch_input * 0.8) + ((gyro_pitch) * 0.2);         //Gyro pid input is deg/sec.
  gyro_yaw_input = (gyro_yaw_input * 0.8) + ((gyro_yaw) * 0.2);               //Gyro pid input is deg/sec.

  //For starting the motors: throttle low and yaw left (step 1).
  if(receiver_input_channel_3 < 1050 && receiver_input_channel_4 < 1050){start = 1;digitalWrite(8,HIGH);}
  //When yaw stick is back in the center position start the motors (step 2).
  if(start == 1 && receiver_input_channel_3 < 1050 && receiver_input_channel_4 > 1450){
    start = 2;
    digitalWrite(8,LOW);
    //Reset the pid controllers for a bumpless start.
    pid_i_mem_roll = 0;
    pid_last_roll_d_error = 0;
    pid_i_mem_pitch = 0;
    pid_last_pitch_d_error = 0;
    pid_i_mem_yaw = 0;
    pid_last_yaw_d_error = 0;
  }
  //Stopping the motors: throttle low and yaw right.
  if(start == 2 && receiver_input_channel_3 < 1050 && receiver_input_channel_4 > 1950)start = 0;
  //Stopping the motors at timeout
  if (start == 2 && timeout > 500){start = 0;} //500: 2s

  //The PID set point in degrees per second is determined by the roll receiver input.
  //In the case of deviding by 3 the max roll rate is aprox 164 degrees per second ( (500-8)/3 = 164d/s ).
  pid_roll_setpoint = 0;
  //We need a little dead band of 16us for better results.
  if(receiver_input_channel_1 > 1508)pid_roll_setpoint = (receiver_input_channel_1 - 1508)/3.0;
  else if(receiver_input_channel_1 < 1492)pid_roll_setpoint = (receiver_input_channel_1 - 1492)/3.0;

  //The PID set point in degrees per second is determined by the pitch receiver input.
  //In the case of deviding by 3 the max pitch rate is aprox 164 degrees per second ( (500-8)/3 = 164d/s ).
  pid_pitch_setpoint = 0;
  //We need a little dead band of 16us for better results.
  if(receiver_input_channel_2 > 1508)pid_pitch_setpoint = (receiver_input_channel_2 - 1508)/3.0;
  else if(receiver_input_channel_2 < 1492)pid_pitch_setpoint = (receiver_input_channel_2 - 1492)/3.0;

  //The PID set point in degrees per second is determined by the yaw receiver input.
  //In the case of deviding by 3 the max yaw rate is aprox 164 degrees per second ( (500-8)/3 = 164d/s ).
  pid_yaw_setpoint = 0;
  //We need a little dead band of 16us for better results.
  if(receiver_input_channel_3 > 1050){ //Do not yaw when turning off the motors.
    if(receiver_input_channel_4 > 1508)pid_yaw_setpoint = (receiver_input_channel_4 - 1508)/3.0;
    else if(receiver_input_channel_4 < 1492)pid_yaw_setpoint = (receiver_input_channel_4 - 1492)/3.0;
  }
  //PID inputs are known. So we can calculate the pid output.
  calculate_pid();

  //Read Voltages

  if (looptime < 3000){ //nur wenn Zeit ist!
      readVcc();
      readVoltages();
  }

  //The battery voltage is needed for compensation.
  //A complementary filter is used to reduce noise.
  //0.09853 = 0.08 * 1.2317.
  battery_voltage = battery_voltage * 0.92 + (getBatteryVoltage()*0.08);

  //Turn on the led if battery voltage is too low.
  if(battery_voltage < 1030 && battery_voltage > 600)digitalWrite(8, HIGH);

  throttle = receiver_input_channel_3;                                      //We need the throttle signal as a base signal.

  if (start == 2){                                                          //The motors are started.
    if (throttle > 1800) throttle = 1800;                                   //We need some room to keep full control at full throttle.
    esc_1 = throttle - pid_output_pitch + pid_output_roll - pid_output_yaw; //Calculate the pulse for esc 1 (front-right - CCW)
    esc_2 = throttle + pid_output_pitch + pid_output_roll + pid_output_yaw; //Calculate the pulse for esc 2 (rear-right - CW)
    esc_3 = throttle + pid_output_pitch - pid_output_roll - pid_output_yaw; //Calculate the pulse for esc 3 (rear-left - CCW)
    esc_4 = throttle - pid_output_pitch - pid_output_roll + pid_output_yaw; //Calculate the pulse for esc 4 (front-left - CW)

    if (battery_voltage < 1240 && battery_voltage > 800){                   //Is the battery connected?
      esc_1 += esc_1 * ((1240 - battery_voltage)/(float)3500);              //Compensate the esc-1 pulse for voltage drop.
      esc_2 += esc_2 * ((1240 - battery_voltage)/(float)3500);              //Compensate the esc-2 pulse for voltage drop.
      esc_3 += esc_3 * ((1240 - battery_voltage)/(float)3500);              //Compensate the esc-3 pulse for voltage drop.
      esc_4 += esc_4 * ((1240 - battery_voltage)/(float)3500);              //Compensate the esc-4 pulse for voltage drop.
    }

    if (esc_1 < 1200) esc_1 = 1200;                                         //Keep the motors running.
    if (esc_2 < 1200) esc_2 = 1200;                                         //Keep the motors running.
    if (esc_3 < 1200) esc_3 = 1200;                                         //Keep the motors running.
    if (esc_4 < 1200) esc_4 = 1200;                                         //Keep the motors running.

    if(esc_1 > 2000)esc_1 = 2000;                                           //Limit the esc-1 pulse to 2000us.
    if(esc_2 > 2000)esc_2 = 2000;                                           //Limit the esc-2 pulse to 2000us.
    if(esc_3 > 2000)esc_3 = 2000;                                           //Limit the esc-3 pulse to 2000us.
    if(esc_4 > 2000)esc_4 = 2000;                                           //Limit the esc-4 pulse to 2000us.
  }

  else{
    esc_1 = 1000;                                                           //If start is not 2 keep a 1000us pulse for ess-1.
    esc_2 = 1000;                                                           //If start is not 2 keep a 1000us pulse for ess-2.
    esc_3 = 1000;                                                           //If start is not 2 keep a 1000us pulse for ess-3.
    esc_4 = 1000;                                                           //If start is not 2 keep a 1000us pulse for ess-4.
  }

  //Clear serial
  /*Serial.write(27);       // ESC command
    Serial.print("[2J");    // clear screen command
    Serial.write(27);
    Serial.print("[H");     // cursor to home command*/


  //All the information for controlling the motor's is available.
  //The refresh rate is 250Hz. That means the esc's need there pulse every 4ms.
#ifdef debug
  //printSensors();
  //printESCs();
  printDirs();
  Serial.print(";");Serial.print(micros()-loop_timer);
  Serial.print(";");Serial.print(start);
  Serial.println();
#endif
  looptime = micros()-loop_timer;
  while(micros() - loop_timer < 4000);                                      //We wait until 4000us are passed.
  loop_timer = micros();                                                    //Set the timer for the next loop.

  PORTD |= B11101000;                                        //Set digital poort 3,5,6,7 HIGH
  timer_channel_1 = esc_1 + loop_timer;                                     //Calculate the time of the faling edge of the esc-1 pulse.
  timer_channel_2 = esc_2 + loop_timer;                                     //Calculate the time of the faling edge of the esc-2 pulse.
  timer_channel_3 = esc_3 + loop_timer;                                     //Calculate the time of the faling edge of the esc-3 pulse.
  timer_channel_4 = esc_4 + loop_timer;                                     //Calculate the time of the faling edge of the esc-4 pulse.

  while(PORTD >= 16){                                                       //Stay in this loop until output 4,5,6 and 7 are low.
    esc_loop_timer = micros();                                              //Read the current time.
    if(timer_channel_1 <= esc_loop_timer)PORTD &= B11110111;                //Set digital output 3 to low if the time is expired. Vorne Rechts
    if(timer_channel_2 <= esc_loop_timer)PORTD &= B10111111;                //Set digital output 6 to low if the time is expired. Hinten Rechts
    if(timer_channel_3 <= esc_loop_timer)PORTD &= B01111111;                //Set digital output 7 to low if the time is expired. Hinten Links
    if(timer_channel_4 <= esc_loop_timer)PORTD &= B11011111;                //Set digital output 5 to low if the time is expired. Vorne Links
  }
}

#ifdef debug
void printCIs(){
    Serial.print("Throttle: ");Serial.print(receiver_input_channel_3); Serial.print(" Roll: ");Serial.print(receiver_input_channel_2);Serial.print(" Pitch: "); Serial.print(receiver_input_channel_1); Serial.print(" Yaw: "); Serial.print(receiver_input_channel_4);
}

void printESCs(){
    Serial.print(esc_1); Serial.print(" ");Serial.print(esc_2);Serial.print(" ");Serial.print(esc_3);Serial.print(" ");Serial.print(esc_4);
}

void printSensors(){
    Serial.print(gyro_pitch_input); Serial.print(";");Serial.print(gyro_roll_input);Serial.print(";");Serial.print(gyro_yaw_input);
}

void printDirs(){
    Serial.print("Right Wing ");
    if (gyro_roll_input > 0){
        Serial.print("DOWN");
    }else{
        Serial.print("UP");
    }
    Serial.print(";");
    Serial.print("Nose ");
    if (gyro_pitch_input > 0){
        Serial.print("UP");
    }else{
        Serial.print("DOWN");
    }
    Serial.print(";");
    Serial.print("Nose ");
    if (gyro_yaw_input > 0){
        Serial.print("LEFT");
    }else{
        Serial.print("RIGHT");
    }
    Serial.print(";");
}

void printCal(){
    Serial.print("Calibration: ");Serial.print(gyro_axis_cal[1]);Serial.print(" ");Serial.print(gyro_axis_cal[2]);Serial.print(" ");Serial.println(gyro_axis_cal[3]);
}

#endif
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//This routine is called every time input 8, 9, 10 or 11 changed state
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//Hier stand der Code für seinen Receiver

//For Accurate Voltage readings registers are set
void prepeareVoltage(){
    // Read 1.1V reference against AVcc
    ADMUX = _BV(REFS0) | _BV(MUX3) | _BV(MUX2) | _BV(MUX1);
    //delay(2); // Wait for Vref to settle
}

void readVcc(){
    ADCSRA |= _BV(ADSC); // Convert
    while (bit_is_set(ADCSRA,ADSC));
    long vcc = ADCL;
    vcc |= ADCH<<8;
    vcc = 1125300L / vcc; // Back-calculate AVcc in mV
    Vcc = vcc/1000.0f;
}

float readVoltage(int pin){
    unsigned int ADCValue = analogRead(pin);
    return (ADCValue / 1024.0f) * Vcc;
}

void readVoltages(){
    //readVcc();
    for (int v = 0; v < 4; v++) {
        int voltage = (readVoltage(v)*1000);
        datenplatzhalter[v*2] = (byte)(voltage &0x00FF);
        datenplatzhalter[v*2+1] =(byte)(voltage >> 8);
    }
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Subroutine for reading the gyro
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void gyro_signalen(){
  //Read the L3G4200D or L3GD20H
  //Read the MPU-6050

myIMU.readGyroData(myIMU.gyroCount);
gyro_axis[1] = (float)myIMU.gyroCount[0]*myIMU.gRes;                                 // Takes the Informations and gives it as deg/s
gyro_axis[2] = (float)myIMU.gyroCount[1]*myIMU.gRes;                                // X and Y are switched, because of the mounting on the board
gyro_axis[3] = (float)myIMU.gyroCount[2]*myIMU.gRes;

gyro_axis[1] = -gyro_axis[1];
gyro_axis[2] = -gyro_axis[2];//Meines erachtens nach falsch, funktioniert aber besser!!!
gyro_axis[3] = -gyro_axis[3];//Meines erachtens nach falsch, funktioniert aber besser!!!

  if(cal_int == 2000){
    gyro_axis[1] -= gyro_axis_cal[1];                            //Only compensate after the calibration
    gyro_axis[2] -= gyro_axis_cal[2];                            //Only compensate after the calibration
    gyro_axis[3] -= gyro_axis_cal[3];                            //Only compensate after the calibration
  }

  if (cal_int == 2000){ //Only calculate if not calibrating
      //datenplatzhalter 10 Elemente!
      //Read Voltages
      //Bis index 7
      //float altitude RasPI
      datenplatzhalter[8] = (byte)looptime;
      datenplatzhalter[9] = (byte)(looptime >> 8);


  }
  gyro_pitch = gyro_axis[1];
  gyro_roll = gyro_axis[2];
  gyro_yaw = gyro_axis[3];
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Subroutine for calculating pid outputs
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//The PID controllers are explained in part 5 of the YMFC-3D video session:
//www.youtube.com/watch?v=JBvnB0279-Q

void calculate_pid(){
  //Roll calculations
  pid_error_temp = gyro_roll_input - pid_roll_setpoint;
  pid_i_mem_roll += pid_i_gain_roll * pid_error_temp;
  if(pid_i_mem_roll > pid_max_roll)pid_i_mem_roll = pid_max_roll;
  else if(pid_i_mem_roll < pid_max_roll * -1)pid_i_mem_roll = pid_max_roll * -1;

  pid_output_roll = pid_p_gain_roll * pid_error_temp + pid_i_mem_roll + pid_d_gain_roll * (pid_error_temp - pid_last_roll_d_error);
  if(pid_output_roll > pid_max_roll)pid_output_roll = pid_max_roll;
  else if(pid_output_roll < pid_max_roll * -1)pid_output_roll = pid_max_roll * -1;

  pid_last_roll_d_error = pid_error_temp;

  //Pitch calculations
  pid_error_temp = gyro_pitch_input - pid_pitch_setpoint;
  pid_i_mem_pitch += pid_i_gain_pitch * pid_error_temp;
  if(pid_i_mem_pitch > pid_max_pitch)pid_i_mem_pitch = pid_max_pitch;
  else if(pid_i_mem_pitch < pid_max_pitch * -1)pid_i_mem_pitch = pid_max_pitch * -1;

  pid_output_pitch = pid_p_gain_pitch * pid_error_temp + pid_i_mem_pitch + pid_d_gain_pitch * (pid_error_temp - pid_last_pitch_d_error);
  if(pid_output_pitch > pid_max_pitch)pid_output_pitch = pid_max_pitch;
  else if(pid_output_pitch < pid_max_pitch * -1)pid_output_pitch = pid_max_pitch * -1;

  pid_last_pitch_d_error = pid_error_temp;

  //Yaw calculations
  pid_error_temp = gyro_yaw_input - pid_yaw_setpoint;
  pid_i_mem_yaw += pid_i_gain_yaw * pid_error_temp;
  if(pid_i_mem_yaw > pid_max_yaw)pid_i_mem_yaw = pid_max_yaw;
  else if(pid_i_mem_yaw < pid_max_yaw * -1)pid_i_mem_yaw = pid_max_yaw * -1;

  pid_output_yaw = pid_p_gain_yaw * pid_error_temp + pid_i_mem_yaw + pid_d_gain_yaw * (pid_error_temp - pid_last_yaw_d_error);
  if(pid_output_yaw > pid_max_yaw)pid_output_yaw = pid_max_yaw;
  else if(pid_output_yaw < pid_max_yaw * -1)pid_output_yaw = pid_max_yaw * -1;

  pid_last_yaw_d_error = pid_error_temp;
}

//This part converts the actual receiver signals to a standardized 1000 – 1500 – 2000 microsecond value.
//The stored data in the EEPROM is used.

//Hier stand Code zum Umwandeln von Inputs in Integer

//Instead: Receive Controller Inputs via SPI
void receive(){
    int s = 0;
    if ((SPSR & (1<<SPIF)) != 0){ //Hat sich der Register verändert?
        //Serial.println("Received");
      if (SPDR == (byte)'R'){
        SPDR = (byte)'A';
        s = 0;
        timeout = 0; //Wieder Daten angekommen
      }else{
        return;//Sollte nicht eintreffen, aber ohne Handshake keine Datenübermittlung
      }
      //Alle Daten werden verschickt
      while (s < sizeof(datenplatzhalter)){
        if ((SPSR & (1<<SPIF)) != 0){
          SPDR = datenplatzhalter[s];
          //Erstes Byte Nutzlos, da noch R von Raspberry
          if (s > 0){
            controllerInputs[s-1] = SPDR;
          }
          s++;
          //Empfangen = SPDR
        }
      }
  }else{
      //Keine Daten wurden verschickt/ Empfangen
      timeout ++;

  }
}


void convert_integer(int startInd, int& var, int& last){
  //Daten müssen doppelt gesendet werden. Sonst ungültig!
    int res = controllerInputs[startInd+1];
    res = (res << 8) | controllerInputs[startInd];

    if (res == 21074){
        //82 82 wurde übermittelt -> 'RR'
        #ifdef debug
            Serial.println("!!!INVALID!!!");
        #endif
        return;
    }

    if (res == last){
      var = clamp(res);
    }else{
      last = res;
    }
}

int clamp(int c){
    if (c < 1000)
        return 1000;
    if (c > 2000)
        return 2000;
    else
        return c;
}

void pulse(int delay){
    PORTD |= B11101000;                                        //Set digital poort 3,5,6,7 HIGH
    delayMicroseconds(delay);                                   //Wait 1000us.
    PORTD &= B00010111;                                        //Set digital poort 3,5,6,7 low.
}

void set_gyro_registers(){
  //Setup the MPU-6050

  //Let's perform a random register check to see if the values are written correct
  //Setup the L3G4200D

  //Let's perform a random register check to see if the values are written correct

  //Setup the L3GD20H

  //Setup MPU9250
  myIMU.getGres();
  myIMU.getAres();

}



int getBatteryVoltage(){
    //int input = ads.readADC_SingleEnded(1);
    return 1100;
    //return input*500; // Input * 5 = Volt, Input*500 = centivolt
}
