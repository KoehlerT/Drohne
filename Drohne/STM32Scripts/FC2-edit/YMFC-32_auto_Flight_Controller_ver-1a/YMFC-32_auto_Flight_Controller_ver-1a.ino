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
//#define deb

#ifdef deb
  #define DEBUGa(x) Serial.println(x);
  #define DEBUGb(x,y) Serial.print(x); Serial.println(y);
#else
  #define DEBUGa(x) 
  #define DEBUGb(x,y)
#endif

#define HWire WIRE
#include <EEPROM.h>
#include <Wire.h>                          //Include the Wire.h library so we can communicate with the gyro.
#include <SPI.h>

#define SPI2_NSS_PIN PB12
TwoWire HWire (1, I2C_FAST_MODE);          //Initiate I2C port 2 at 400kHz.
SPIClass SPI_2(2);

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//PID gain and limit settings
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
float pid_p_gain_roll = 1.3;               //Gain setting for the pitch and roll P-controller (default = 1.3).
float pid_i_gain_roll = 0.04;              //Gain setting for the pitch and roll I-controller (default = 0.04).
float pid_d_gain_roll = 18.0;              //Gain setting for the pitch and roll D-controller (default = 18.0).
int pid_max_roll = 400;                    //Maximum output of the PID-controller (+/-).

float pid_p_gain_pitch = pid_p_gain_roll;  //Gain setting for the pitch P-controller.
float pid_i_gain_pitch = pid_i_gain_roll;  //Gain setting for the pitch I-controller.
float pid_d_gain_pitch = pid_d_gain_roll;  //Gain setting for the pitch D-controller.
int pid_max_pitch = pid_max_roll;          //Maximum output of the PID-controller (+/-).

float pid_p_gain_yaw = 4.0;                //Gain setting for the pitch P-controller (default = 4.0).
float pid_i_gain_yaw = 0.02;               //Gain setting for the pitch I-controller (default = 0.02).
float pid_d_gain_yaw = 0.0;                //Gain setting for the pitch D-controller (default = 0.0).
int pid_max_yaw = 400;                     //Maximum output of the PID-controller (+/-).

//During flight the battery voltage drops and the motors are spinning at a lower RPM. This has a negative effecct on the
//altitude hold function. With the battery_compensation variable it's possible to compensate for the battery voltage drop.
//Increase this value when the quadcopter drops due to a lower battery voltage during a non altitude hold flight.
float battery_compensation = 40.0;         

float pid_p_gain_altitude = 1.4;           //Gain setting for the altitude P-controller (default = 1.4).
float pid_i_gain_altitude = 0.2;           //Gain setting for the altitude I-controller (default = 0.2).
float pid_d_gain_altitude = 0.75;          //Gain setting for the altitude D-controller (default = 0.75).
int pid_max_altitude = 400;                //Maximum output of the PID-controller (+/-).

float gps_p_gain = 2.7;                    //Gain setting for the GPS P-controller (default = 2.7).
float gps_d_gain = 6.5;                    //Gain setting for the GPS D-controller (default = 6.5).

float declination = 3.08;                   //Set the declination between the magnetic and geographic north.

int16_t manual_takeoff_throttle = 1550;    //Enter the manual hover point when auto take-off detection is not desired (between 1400 and 1600).
int16_t motor_idle_speed = 1100;           //Enter the minimum throttle pulse of the motors when they idle (between 1000 and 1200). 1170 for DJI

uint8_t gyro_address = 0x68;               //The I2C address of the MPU-6050 is 0x68 in hexadecimal form.
uint8_t MS5611_address = 0x77;             //The I2C address of the MS5611 barometer is 0x77 in hexadecimal form.
uint8_t compass_address = 0x1E;            //The I2C address of the HMC5883L is 0x1E in hexadecimal form.

float low_battery_warning = 10.5;          //Set the battery warning at 10.5V (default = 10.5V).

#define STM32_board_LED PC13               //Change PC13 if the LED on the STM32 is connected to another output.

//Tuning parameters/settings is explained in this video: https://youtu.be/ys-YpOaA2ME
#define variable_1_to_adjust takeoff_throttle   //Change dummy_float to any setting that you want to tune.
#define variable_2_to_adjust throttle   //Change dummy_float to any setting that you want to tune.
#define variable_3_to_adjust dummy_float   //Change dummy_float to any setting that you want to tune.

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Declaring global variables
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//int16_t = signed 16 bit integer
//uint16_t = unsigned 16 bit integer

uint8_t last_channel_1, last_channel_2, last_channel_3, last_channel_4;
uint8_t check_byte, flip32, start;
uint8_t error, error_counter, error_led;
uint8_t flight_mode, flight_mode_counter, flight_mode_led;
uint8_t takeoff_detected, manual_altitude_change;
uint8_t telemetry_send_byte, telemetry_bit_counter, telemetry_loop_counter;
uint8_t channel_select_counter;
uint8_t level_calibration_on;

uint32_t telemetry_buffer_byte;

int16_t esc_1, esc_2, esc_3, esc_4;
int16_t manual_throttle;
int16_t throttle, takeoff_throttle, cal_int;
int16_t temperature, count_var;
int16_t acc_x, acc_y, acc_z;
int16_t gyro_pitch, gyro_roll, gyro_yaw;

int32_t channel_1_start, channel_1, pid_roll_setpoint_base;
int32_t channel_2_start, channel_2, pid_pitch_setpoint_base;
int32_t channel_3_start, channel_3;
int32_t channel_4_start, channel_4;
int32_t channel_5_start, channel_5;
int32_t channel_6_start, channel_6;
int32_t measured_time, measured_time_start;
int32_t acc_total_vector, acc_total_vector_at_start;
int32_t gyro_roll_cal, gyro_pitch_cal, gyro_yaw_cal;
int16_t acc_pitch_cal_value;
int16_t acc_roll_cal_value;

int32_t acc_z_average_short_total, acc_z_average_long_total, acc_z_average_total ;
int16_t acc_z_average_short[26], acc_z_average_long[51];

uint8_t acc_z_average_short_rotating_mem_location, acc_z_average_long_rotating_mem_location;

int32_t acc_alt_integrated;

uint32_t loop_timer, error_timer, flight_mode_timer;

float roll_level_adjust, pitch_level_adjust;
float pid_error_temp;
float pid_i_mem_roll, pid_roll_setpoint, gyro_roll_input, pid_output_roll, pid_last_roll_d_error;
float pid_i_mem_pitch, pid_pitch_setpoint, gyro_pitch_input, pid_output_pitch, pid_last_pitch_d_error;
float pid_i_mem_yaw, pid_yaw_setpoint, gyro_yaw_input, pid_output_yaw, pid_last_yaw_d_error;
float angle_roll_acc, angle_pitch_acc, angle_pitch, angle_roll, angle_yaw;
float battery_voltage, dummy_float;

//Compass variables
uint8_t compass_calibration_on, heading_lock;
int16_t compass_x, compass_y, compass_z;
int16_t compass_cal_values[6];
float compass_x_horizontal, compass_y_horizontal, actual_compass_heading;
float compass_scale_y, compass_scale_z;
int16_t compass_offset_x, compass_offset_y, compass_offset_z;
float course_a, course_b, course_c, base_course_mirrored, actual_course_mirrored;
float course_lock_heading, heading_lock_course_deviation;


//Pressure variables.
float pid_error_gain_altitude, pid_throttle_gain_altitude;
uint16_t C[7];
uint8_t barometer_counter, temperature_counter, average_temperature_mem_location;
int64_t OFF, OFF_C2, SENS, SENS_C1, P;
uint32_t raw_pressure, raw_temperature, temp, raw_temperature_rotating_memory[6], raw_average_temperature_total;
float actual_pressure, actual_pressure_slow, actual_pressure_fast, actual_pressure_diff;
float ground_pressure, altutude_hold_pressure;
int32_t dT, dT_C5;
//Altitude PID variables
float pid_i_mem_altitude, pid_altitude_setpoint, pid_altitude_input, pid_output_altitude, pid_last_altitude_d_error;
uint8_t parachute_rotating_mem_location;
int32_t parachute_buffer[35], parachute_throttle;
float pressure_parachute_previous;
int32_t pressure_rotating_mem[50], pressure_total_avarage;
uint8_t pressure_rotating_mem_location;
float pressure_rotating_mem_actual;

//Loop exeed calculation variables
int8_t loop_rotating_memory[100];
uint8_t loop_rot_mem_loc;
float averageExeeds;
uint8_t currendExeeds;

//GPS variables
uint8_t read_serial_byte, incomming_message[100], number_used_sats, fix_type;
uint8_t waypoint_set, latitude_north, longiude_east ;
uint16_t message_counter;
int16_t gps_add_counter;
int32_t l_lat_gps, l_lon_gps, lat_gps_previous, lon_gps_previous;
int32_t lat_gps_actual, lon_gps_actual, l_lat_waypoint, l_lon_waypoint;
float gps_pitch_adjust_north, gps_pitch_adjust, gps_roll_adjust_north, gps_roll_adjust;
float lat_gps_loop_add, lon_gps_loop_add, lat_gps_add, lon_gps_add;
uint8_t new_line_found, new_gps_data_available, new_gps_data_counter;
uint8_t gps_rotating_mem_location;
int32_t gps_lat_total_avarage, gps_lon_total_avarage;
int32_t gps_lat_rotating_mem[40], gps_lon_rotating_mem[40];
int32_t gps_lat_error, gps_lon_error;
int32_t gps_lat_error_previous, gps_lon_error_previous;
uint32_t gps_watchdog_timer;

//Adjust settings online
uint32_t setting_adjust_timer;
uint16_t setting_click_counter;
uint8_t previous_channel_6;
float adjustable_setting_1, adjustable_setting_2, adjustable_setting_3;

//Transmission Variables
char programm_code;
char receive[10];
char transmit[10];
float used_power,altitude_meter;
uint32_t startLoop = 0;
uint16_t loopDuration = 0;
uint8_t nextPackage = true;
unsigned long CommunicationDuration;
unsigned long HSDuration;
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Setup routine
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void setup() {
  esc_1 = 1000; esc_2 = 1000; esc_3 = 1000; esc_4 = 1000; 
  pinMode(5, INPUT_ANALOG);                                     //This is needed for reading the analog value of port A4.
  //Port PB3 and PB4 are used as JTDO and JNTRST by default.
  //The following function connects PB3 and PB4 to the
  //alternate output function.
  afio_cfg_debug_ports(AFIO_DEBUG_SW_ONLY);                     //Connects PB3 and PB4 to output function.

  pinMode(SPI2_NSS_PIN, INPUT);
  pinMode(PA2, INPUT);
  SPI_2.beginTransactionSlave(SPISettings(18000000, MSBFIRST, SPI_MODE0, DATA_SIZE_8BIT));
  
  adjustable_setting_3 = 100;
  #ifdef deb
    Serial.begin(230400);
    delay(200);
    Serial.println("Hello World");
    adjustable_setting_3 = 200;
  #endif

  pinMode(PB3, OUTPUT);                                         //Set PB3 as output for green LED.
  pinMode(PB4, OUTPUT);                                         //Set PB4 as output for red LED.
  pinMode(STM32_board_LED, OUTPUT);                             //This is the LED on the STM32 board. Used for GPS indication.
  digitalWrite(STM32_board_LED, HIGH);                          //Turn the LED on the STM32 off. The LED function is inverted. Check the STM32 schematic.

  green_led(LOW);                                               //Set output PB3 low.
  red_led(HIGH);                                                //Set output PB4 high.

  pinMode(PB0, OUTPUT);                                         //Set PB0 as output for telemetry TX.

  //EEPROM emulation setup
  DEBUGa("EEPROM")
  EEPROM.PageBase0 = 0x801F000;
  EEPROM.PageBase1 = 0x801F800;
  EEPROM.PageSize  = 0x400;

  //Takeoff Throttle
  manual_takeoff_throttle = EEPROM.read(0x18) | (EEPROM.read(0x19)<<8);
  if (manual_takeoff_throttle == 0)
    manual_takeoff_throttle = 1500;

  
  //Serial.begin(57600);                                        //Set the serial output to 57600 kbps. (for debugging only)
  //delay(250);                                                 //Give the serial port some time to start to prevent data loss.

  DEBUGa("Timers");
  timer_setup();                                                //Setup the timers for the receiver inputs and ESC's output.
  delay(50);                                                    //Give the timers some time to start.

  gps_setup();                                                  //Set the baud rate and output refreshrate of the GPS module.

  //Check if the MPU-6050 is responding.
  HWire.begin();                                                //Start the I2C as master
  HWire.beginTransmission(gyro_address);                        //Start communication with the MPU-6050.
  error = HWire.endTransmission();                              //End the transmission and register the exit status.
  DEBUGb("MPU",error)
  while (error != 0) {                                          //Stay in this loop because the MPU-6050 did not responde.
    getRaspberryInfo();
    error = 1;                                                  //Set the error status to 1.
    error_signal();                                             //Show the error via the red LED.
    delay(4);                                                   //Simulate a 250Hz refresch rate as like the main loop.
    break;
  }

  //Check if the compass is responding.
  HWire.begin();                                                //Start the I2C as master
  HWire.beginTransmission(compass_address);                     //Start communication with the HMC5883L.
  error = HWire.endTransmission();                              //End the transmission and register the exit status.
  DEBUGb("Compass",error)
  while (error != 0) {                                          //Stay in this loop because the HMC5883L did not responde.
    getRaspberryInfo();
    error = 2;                                                  //Set the error status to 2.
    error_signal();                                             //Show the error via the red LED.
    delay(4);                                                   //Simulate a 250Hz refresch rate as like the main loop.
    break;
  }

  //Check if the MS5611 barometer is responding.
  HWire.begin();                                                //Start the I2C as master
  HWire.beginTransmission(MS5611_address);                      //Start communication with the MS5611.
  error = HWire.endTransmission();                              //End the transmission and register the exit status.
  DEBUGb("MS5611",error)
  while (error != 0) {                                          //Stay in this loop because the MS5611 did not responde.
    getRaspberryInfo();
    error = 3;                                                  //Set the error status to 2.
    error_signal();                                             //Show the error via the red LED.
    delay(4);                                                   //Simulate a 250Hz refresch rate as like the main loop.
    break;
  }

  gyro_setup();                                                 //Initiallize the gyro and set the correct registers.
  setup_compass();                                              //Initiallize the compass and set the correct registers.
  read_compass();                                               //Read and calculate the compass data.
  angle_yaw = actual_compass_heading;                           //Set the initial compass heading.

  //Create a 5 second delay before calibration.
  DEBUGa("Calibration")
  for (count_var = 0; count_var < 1250; count_var++) {          //1250 loops of 4 microseconds = 5 seconds.
    if (count_var % 125 == 0) {                                 //Every 125 loops (500ms).
      digitalWrite(PB4, !digitalRead(PB4));                     //Change the led status.
    }
    delay(4);                                                   //Simulate a 250Hz refresch rate as like the main loop.
  }
  count_var = 0;                                                //Set start back to 0.
  calibrate_gyro();                                             //Calibrate the gyro offset.

  //Wait until the receiver is active.
  DEBUGa("Receiver")
  while (channel_1 < 990 || channel_2 < 990 || channel_3 < 990 || channel_4 < 990)  {
    getRaspberryInfo();
    error = 4;                                                  //Set the error status to 4.
    error_signal();                                             //Show the error via the red LED.
    delay(4);                                                   //Delay 4ms to simulate a 250Hz loop
  }
  error = 0;                                                    //Reset the error status to 0.


  //When everything is done, turn off the led.
  red_led(LOW);                                                 //Set output PB4 low.

  //Load the battery voltage to the battery_voltage variable.
  //The STM32 uses a 12 bit analog to digital converter.
  //analogRead => 0 = 0V ..... 4095 = 3.3V
  //The voltage divider (1k & 10k) is 1:11.
  //analogRead => 0 = 0V ..... 4095 = 36.3V
  //36.3 / 4095 = 112.81.
  //The variable battery_voltage holds 1050 if the battery voltage is 10.5V.
  DEBUGa("voltage")
  battery_voltage = (float)analogRead(5) / 112.81;

  //For calculating the pressure the 6 calibration values need to be polled from the MS5611.
  //These 2 byte values are stored in the memory location 0xA2 and up.
  DEBUGa("pressure")
  for (start = 1; start <= 6; start++) {
    HWire.beginTransmission(MS5611_address);                    //Start communication with the MPU-6050.
    HWire.write(0xA0 + start * 2);                              //Send the address that we want to read.
    HWire.endTransmission();                                    //End the transmission.

    HWire.requestFrom(MS5611_address, 2);                       //Request 2 bytes from the MS5611.
    C[start] = HWire.read() << 8 | HWire.read();                //Add the low and high byte to the C[x] calibration variable.
  }

  OFF_C2 = C[2] * pow(2, 16);                                   //This value is pre-calculated to offload the main program loop.
  SENS_C1 = C[1] * pow(2, 15);                                  //This value is pre-calculated to offload the main program loop.

  //The MS5611 needs a few readings to stabilize.
  for (start = 0; start < 100; start++) {                       //This loop runs 100 times.
    read_barometer();                                           //Read and calculate the barometer data.
    delay(4);                                                   //The main program loop also runs 250Hz (4ms per loop).
  }
  actual_pressure = 0;                                          //Reset the pressure calculations.

  //Before starting the avarage accelerometer value is preloaded into the variables.
  DEBUGa("accelerometer")
  for (start = 0; start <= 24; start++)acc_z_average_short[start] = acc_z;
  for (start = 0; start <= 49; start++)acc_z_average_long[start] = acc_z;
  acc_z_average_short_total = acc_z * 25;
  acc_z_average_long_total = acc_z * 50;
  start = 0;

  if (motor_idle_speed < 1000)motor_idle_speed = 1000;          //Limit the minimum idle motor speed to 1000us.
  if (motor_idle_speed > 1200)motor_idle_speed = 1200;          //Limit the maximum idle motor speed to 1200us.

  DEBUGa("Finished Setup")

  flight_mode = 1;
  
  loop_timer = micros();                                        //Set the timer for the first loop.
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Main program loop
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void loop() {
  getRaspberryInfo();
  //Some functions are only accessible when the quadcopter is off.
  if (start == 0) {
    //For compass calibration move both sticks to the top right.
    if (channel_1 > 1900 && channel_2 < 1100 && channel_3 > 1900 && channel_4 > 1900)calibrate_compass();
    //Level calibration move both sticks to the top left.
    if (channel_1 < 1100 && channel_2 < 1100 && channel_3 > 1900 && channel_4 < 1100)calibrate_level();
    //Change settings
    if (channel_6 >= 1900 && previous_channel_6 == 0) {
      previous_channel_6 = 1;
      if (setting_adjust_timer > millis())setting_click_counter ++;
      else setting_click_counter = 0;
      setting_adjust_timer = millis() + 1000;
      if (setting_click_counter > 3) {
        setting_click_counter = 0;
        change_settings();
      }
    }
    if (channel_6 < 1900)previous_channel_6 = 0;
  }

  heading_lock = 0;
  if (channel_6 > 1200)heading_lock = 1;                                           //If channel 6 is between 1200us and 1600us the flight mode is 2

  //flight_mode = 1;                                                                 //In all other situations the flight mode is 1;
  if (channel_5 >= 1200 && channel_5 < 1600)flight_mode = 2;                       //If channel 6 is between 1200us and 1600us the flight mode is 2
  if (channel_5 >= 1600 && channel_5 < 2100)flight_mode = 3;                       //If channel 6 is between 1600us and 1900us the flight mode is 3

  flight_mode_signal();                                                            //Show the flight_mode via the green LED.
  error_signal();                                                                  //Show the error via the red LED.
  gyro_signalen();                                                                 //Read the gyro and accelerometer data.
  read_barometer();                                                                //Read and calculate the barometer data.
  read_compass();                                                                  //Read and calculate the compass data.

  if (gps_add_counter >= 0)gps_add_counter --;

  read_gps();

  //65.5 = 1 deg/sec (check the datasheet of the MPU-6050 for more information).
  gyro_roll_input = (gyro_roll_input * 0.7) + (((float)gyro_roll / 65.5) * 0.3);   //Gyro pid input is deg/sec.
  gyro_pitch_input = (gyro_pitch_input * 0.7) + (((float)gyro_pitch / 65.5) * 0.3);//Gyro pid input is deg/sec.
  gyro_yaw_input = (gyro_yaw_input * 0.7) + (((float)gyro_yaw / 65.5) * 0.3);      //Gyro pid input is deg/sec.


  ////////////////////////////////////////////////////////////////////////////////////////////////////
  //This is the added IMU code from the videos:
  //https://youtu.be/4BoIE8YQwM8
  //https://youtu.be/j-kE0AMEWy4
  ////////////////////////////////////////////////////////////////////////////////////////////////////

  //Gyro angle calculations
  //0.0000611 = 1 / (250Hz / 65.5)
  angle_pitch += (float)gyro_pitch * 0.0000611;                                    //Calculate the traveled pitch angle and add this to the angle_pitch variable.
  angle_roll += (float)gyro_roll * 0.0000611;                                      //Calculate the traveled roll angle and add this to the angle_roll variable.
  angle_yaw += (float)gyro_yaw * 0.0000611;                                        //Calculate the traveled yaw angle and add this to the angle_yaw variable.
  if (angle_yaw < 0) angle_yaw += 360;                                             //If the compass heading becomes smaller then 0, 360 is added to keep it in the 0 till 360 degrees range.
  else if (angle_yaw >= 360) angle_yaw -= 360;                                     //If the compass heading becomes larger then 360, 360 is subtracted to keep it in the 0 till 360 degrees range.

  //0.000001066 = 0.0000611 * (3.142(PI) / 180degr) The Arduino sin function is in radians and not degrees.
  angle_pitch -= angle_roll * sin((float)gyro_yaw * 0.000001066);                  //If the IMU has yawed transfer the roll angle to the pitch angel.
  angle_roll += angle_pitch * sin((float)gyro_yaw * 0.000001066);                  //If the IMU has yawed transfer the pitch angle to the roll angel.

  angle_yaw -= course_deviation(angle_yaw, actual_compass_heading) / 1200.0;       //Calculate the difference between the gyro and compass heading and make a small correction.
  if (angle_yaw < 0) angle_yaw += 360;                                             //If the compass heading becomes smaller then 0, 360 is added to keep it in the 0 till 360 degrees range.
  else if (angle_yaw >= 360) angle_yaw -= 360;                                     //If the compass heading becomes larger then 360, 360 is subtracted to keep it in the 0 till 360 degrees range.


  //Accelerometer angle calculations
  acc_total_vector = sqrt((acc_x * acc_x) + (acc_y * acc_y) + (acc_z * acc_z));    //Calculate the total accelerometer vector.

  if (abs(acc_y) < acc_total_vector) {                                             //Prevent the asin function to produce a NaN.
    angle_pitch_acc = asin((float)acc_y / acc_total_vector) * 57.296;              //Calculate the pitch angle.
  }
  if (abs(acc_x) < acc_total_vector) {                                             //Prevent the asin function to produce a NaN.
    angle_roll_acc = asin((float)acc_x / acc_total_vector) * 57.296;               //Calculate the roll angle.
  }

  angle_pitch = angle_pitch * 0.9996 + angle_pitch_acc * 0.0004;                   //Correct the drift of the gyro pitch angle with the accelerometer pitch angle.
  angle_roll = angle_roll * 0.9996 + angle_roll_acc * 0.0004;                      //Correct the drift of the gyro roll angle with the accelerometer roll angle.

  pitch_level_adjust = angle_pitch * 15;                                           //Calculate the pitch angle correction.
  roll_level_adjust = angle_roll * 15;                                             //Calculate the roll angle correction.

  vertical_acceleration_calculations();                                            //Calculate the vertical accelration.

  pid_roll_setpoint_base = channel_1;                                              //Normally channel_1 is the pid_roll_setpoint input.
  pid_pitch_setpoint_base = channel_2;                                             //Normally channel_2 is the pid_pitch_setpoint input.
  //When the heading_lock mode is activated the roll and pitch pid setpoints are heading dependent.
  //At startup the heading is registerd in the variable course_lock_heading.
  //First the course deviation is calculated between the current heading and the course_lock_heading is calculated.
  //Based on this deviation the pitch and roll controls are calculated so the responce is the same as on startup.
  if (heading_lock == 1) {
    heading_lock_course_deviation = course_deviation(angle_yaw, course_lock_heading);
    pid_roll_setpoint_base = 1500 + ((float)(channel_1 - 1500) * cos(heading_lock_course_deviation * 0.017453)) + ((float)(channel_2 - 1500) * cos((heading_lock_course_deviation - 90) * 0.017453));
    pid_pitch_setpoint_base = 1500 + ((float)(channel_2 - 1500) * cos(heading_lock_course_deviation * 0.017453)) + ((float)(channel_1 - 1500) * cos((heading_lock_course_deviation + 90) * 0.017453));
  }

  if (flight_mode >= 3 && waypoint_set == 1) {
    pid_roll_setpoint_base += gps_roll_adjust;
    pid_pitch_setpoint_base += gps_pitch_adjust;
  }

  //Because we added the GPS adjust values we need to make sure that the control limits are not exceded.
  if (pid_roll_setpoint_base > 2000)pid_roll_setpoint_base = 2000;
  if (pid_roll_setpoint_base < 1000)pid_roll_setpoint_base = 1000;
  if (pid_pitch_setpoint_base > 2000)pid_pitch_setpoint_base = 2000;
  if (pid_pitch_setpoint_base < 1000)pid_pitch_setpoint_base = 1000;

  calculate_pid();                                                                 //Calculate the pid outputs based on the receiver inputs.

  start_stop_takeoff();                                                            //Starting, stopping and take-off detection

  //The battery voltage is needed for compensation.
  //A complementary filter is used to reduce noise.
  //1410.1 = 112.81 / 0.08.
  battery_voltage = battery_voltage * 0.92 + ((float)analogRead(5) / 1410.1);

  //Turn on the led if battery voltage is to low. Default setting is 10.5V
  if (battery_voltage > 6.0 && battery_voltage < low_battery_warning && error == 0)error = 1;


  //The variable base_throttle is calculated in the following part. It forms the base throttle for every motor.
  if (takeoff_detected == 1 && start == 2) {                                         //If the quadcopter is started and flying.
    throttle = channel_3 + takeoff_throttle;                                         //The base throttle is the receiver throttle channel + the detected take-off throttle.
    if (flight_mode >= 2) {                                                          //If altitude mode is active.
      throttle = 1500 + takeoff_throttle + pid_output_altitude + manual_throttle;    //The base throttle is the receiver throttle channel + the detected take-off throttle + the PID controller output.
    }
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////////
  //Creating the pulses for the ESC's is explained in this video:
  //https://youtu.be/Nju9rvZOjVQ
  ////////////////////////////////////////////////////////////////////////////////////////////////////

  if (start == 2) {                                                                //The motors are started.
    if (throttle > 1800) throttle = 1800;                                          //We need some room to keep full control at full throttle.
    esc_1 = throttle - pid_output_pitch + pid_output_roll - pid_output_yaw;        //Calculate the pulse for esc 1 (front-right - CCW).
    esc_2 = throttle + pid_output_pitch + pid_output_roll + pid_output_yaw;        //Calculate the pulse for esc 2 (rear-right - CW).
    esc_3 = throttle + pid_output_pitch - pid_output_roll - pid_output_yaw;        //Calculate the pulse for esc 3 (rear-left - CCW).
    esc_4 = throttle - pid_output_pitch - pid_output_roll + pid_output_yaw;        //Calculate the pulse for esc 4 (front-left - CW).

    if (battery_voltage < 12.40 && battery_voltage > 6.0) {                        //Is the battery connected?
      esc_1 += (12.40 - battery_voltage) * battery_compensation;                   //Compensate the esc-1 pulse for voltage drop.
      esc_2 += (12.40 - battery_voltage) * battery_compensation;                   //Compensate the esc-2 pulse for voltage drop.
      esc_3 += (12.40 - battery_voltage) * battery_compensation;                   //Compensate the esc-3 pulse for voltage drop.
      esc_4 += (12.40 - battery_voltage) * battery_compensation;                   //Compensate the esc-4 pulse for voltage drop.
    }

    if (esc_1 < motor_idle_speed) esc_1 = motor_idle_speed;                        //Keep the motors running.
    if (esc_2 < motor_idle_speed) esc_2 = motor_idle_speed;                        //Keep the motors running.
    if (esc_3 < motor_idle_speed) esc_3 = motor_idle_speed;                        //Keep the motors running.
    if (esc_4 < motor_idle_speed) esc_4 = motor_idle_speed;                        //Keep the motors running.

    if (esc_1 > 2000)esc_1 = 2000;                                                 //Limit the esc-1 pulse to 2000us.
    if (esc_2 > 2000)esc_2 = 2000;                                                 //Limit the esc-2 pulse to 2000us.
    if (esc_3 > 2000)esc_3 = 2000;                                                 //Limit the esc-3 pulse to 2000us.
    if (esc_4 > 2000)esc_4 = 2000;                                                 //Limit the esc-4 pulse to 2000us.
  }

  else {
    esc_1 = 1000;                                                                  //If start is not 2 keep a 1000us pulse for ess-1.
    esc_2 = 1000;                                                                  //If start is not 2 keep a 1000us pulse for ess-2.
    esc_3 = 1000;                                                                  //If start is not 2 keep a 1000us pulse for ess-3.
    esc_4 = 1000;                                                                  //If start is not 2 keep a 1000us pulse for ess-4.
  }


  TIMER3_BASE->CCR1 = esc_1;                                                       //Set the throttle receiver input pulse to the ESC 1 output pulse.
  TIMER3_BASE->CCR2 = esc_2;                                                       //Set the throttle receiver input pulse to the ESC 2 output pulse.
  TIMER3_BASE->CCR3 = esc_3;                                                       //Set the throttle receiver input pulse to the ESC 3 output pulse.
  TIMER3_BASE->CCR4 = esc_4;                                                       //Set the throttle receiver input pulse to the ESC 4 output pulse.
  TIMER3_BASE->CNT = 5000;                                                         //This will reset timer 4 and the ESC pulses are directly created.

  send_telemetry_data();                                                           //Send telemetry data to the ground station.
  //DEBUGb("takeoff detected: ", takeoff_detected);
  #ifdef deb
    //printEscs();
    //printSignals();
    //DEBUGa(throttle);
  #endif
  //! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! !
  //Because of the angle calculation the loop time is getting very important. If the loop time is
  //longer or shorter than 4000us the angle calculation is off. If you modify the code make sure
  //that the loop time is still 4000us and no longer! More information can be found on
  //the Q&A page:
  //! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! !
  loopDuration = micros() - loop_timer;
  
  currendExeeds -= loop_rotating_memory[loop_rot_mem_loc];
  if (micros() - loop_timer > 4050){
    error = 2;
    loop_rotating_memory[loop_rot_mem_loc] = 1;
    #ifdef deb
      printComm();
    #endif
  }                                      //Output an error if the loop time exceeds 4050us.
  else {loop_rotating_memory[loop_rot_mem_loc] = 0;}
  currendExeeds += loop_rotating_memory[loop_rot_mem_loc];
  loop_rot_mem_loc++;                                                                         //Increase the rotating memory location.
  if (loop_rot_mem_loc == 100)loop_rot_mem_loc = 0;
  
  while (micros() - loop_timer < 4000);                                            //We wait until 4000us are passed.
  loop_timer = micros();                                                           //Set the timer for the next loop.
}

#ifdef deb
  void printEscs(void){
    DEBUGb("ESC 1: ", esc_1);
    DEBUGb("ESC 2: ", esc_2);
    DEBUGb("ESC 3: ", esc_3);
    DEBUGb("ESC 4: ", esc_4);
  }

  void printSignals(void){
    DEBUGb("Throttle: ", throttle)
    DEBUGb("ptich: ", pid_output_pitch)
    DEBUGb("Roll: ", pid_output_roll)
    DEBUGb("Yaw: ", pid_output_yaw)
  }

  void printComm(){
    DEBUGb("Comm: ",CommunicationDuration)
    DEBUGb("HS: ", HSDuration)
  }
#endif
