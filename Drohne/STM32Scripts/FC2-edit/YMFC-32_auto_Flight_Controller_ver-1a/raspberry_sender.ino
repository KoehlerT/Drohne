///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//This part sends the telemetry data to the ground station.
//The output for the serial monitor is PB0. Protocol is 1 start bit, 8 data bits, no parity, 1 stop bit.
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void send_telemetry_data(void) {
  if (nextPackage){
    telemetry_loop_counter++; 
    nextPackage = false;
  }
                                                                 //Increment the telemetry_loop_counter variable.
  if (telemetry_loop_counter == 1)telemetry_send_byte = 'J';                                //Send a J as start signature.
  if (telemetry_loop_counter == 2)telemetry_send_byte = 'B';                                //Send a B as start signature.
  if (telemetry_loop_counter == 3) {
    check_byte = 0;
    telemetry_send_byte = error;                              //Send the error as a byte.
  }
  if (telemetry_loop_counter == 4)telemetry_send_byte = flight_mode;                        //Send the flight mode as a byte.
  if (telemetry_loop_counter == 5)telemetry_send_byte = battery_voltage * 10;               //Send the battery voltage as a byte.
  if (telemetry_loop_counter == 6) {
    telemetry_buffer_byte = temperature;                                                    //Store the temperature as it can change during the next loop.
    telemetry_send_byte = telemetry_buffer_byte;                                            //Send the first 8 bytes of the temperature variable.
  }
  if (telemetry_loop_counter == 7)telemetry_send_byte = telemetry_buffer_byte >> 8;         //Send the last 8 bytes of the temperature variable.
  if (telemetry_loop_counter == 8)telemetry_send_byte = angle_roll + 100;                   //Send the roll angle as a byte. Adding 100 prevents negative numbers.
  if (telemetry_loop_counter == 9)telemetry_send_byte = angle_pitch + 100;                  //Send the pitch angle as a byte. Adding 100 prevents negative numbers.
  if (telemetry_loop_counter == 10)telemetry_send_byte = start;                             //Send the error as a byte.
  if (telemetry_loop_counter == 11) {
    if (start == 2) {                                                                       //Only send the altitude when the quadcopter is flying.
      telemetry_buffer_byte = 1000 + ((ground_pressure - actual_pressure) * 8.42);        //Calculate the altitude (cm!) and add 1000 to prevent negative numbers.
    }
    else {
      telemetry_buffer_byte = 1000;                                                         //Send and altitude of 0 meters if the quadcopter isn't flying.
    }
    telemetry_send_byte = telemetry_buffer_byte;                                            //Send the first 8 bytes of the altitude variable.
  }
  if (telemetry_loop_counter == 12)telemetry_send_byte = telemetry_buffer_byte >> 8;        //Send the last 8 bytes of the altitude variable.

  if (telemetry_loop_counter == 13) {
    telemetry_buffer_byte = 1500 + takeoff_throttle;                                        //Store the take-off throttle as it can change during the next loop.
    telemetry_send_byte = telemetry_buffer_byte;                                            //Send the first 8 bytes of the take-off throttle variable.
  }
  if (telemetry_loop_counter == 14)telemetry_send_byte = telemetry_buffer_byte >> 8;        //Send the last 8 bytes of the take-off throttle variable.
  if (telemetry_loop_counter == 15) {
    telemetry_buffer_byte = angle_yaw;                                                      //Store the compass heading as it can change during the next loop.
    telemetry_send_byte = telemetry_buffer_byte;                                            //Send the first 8 bytes of the compass heading variable.
  }
  if (telemetry_loop_counter == 16)telemetry_send_byte = telemetry_buffer_byte >> 8;        //Send the last 8 bytes of the compass heading variable.
  if (telemetry_loop_counter == 17)telemetry_send_byte = heading_lock;                      //Send the heading_lock variable as a byte.
  if (telemetry_loop_counter == 18)telemetry_send_byte = number_used_sats;                  //Send the number_used_sats variable as a byte.
  if (telemetry_loop_counter == 19)telemetry_send_byte = fix_type;                          //Send the fix_type variable as a byte.
  if (telemetry_loop_counter == 20) {
    telemetry_buffer_byte = l_lat_gps;                                                      //Store the latitude position as it can change during the next loop.
    telemetry_send_byte = telemetry_buffer_byte;                                            //Send the first 8 bytes of the latitude position variable.
  }
  if (telemetry_loop_counter == 21)telemetry_send_byte = telemetry_buffer_byte >> 8;        //Send the next 8 bytes of the latitude position variable.
  if (telemetry_loop_counter == 22)telemetry_send_byte = telemetry_buffer_byte >> 16;       //Send the next 8 bytes of the latitude position variable.
  if (telemetry_loop_counter == 23)telemetry_send_byte = telemetry_buffer_byte >> 24;       //Send the next 8 bytes of the latitude position variable.
  if (telemetry_loop_counter == 24) {
    telemetry_buffer_byte = l_lon_gps;                                                      //Store the longitude position as it can change during the next loop.
    telemetry_send_byte = telemetry_buffer_byte;                                            //Send the first 8 bytes of the longitude position variable.
  }
  if (telemetry_loop_counter == 25)telemetry_send_byte = telemetry_buffer_byte >> 8;        //Send the next 8 bytes of the longitude position variable.
  if (telemetry_loop_counter == 26)telemetry_send_byte = telemetry_buffer_byte >> 16;       //Send the next 8 bytes of the longitude position variable.
  if (telemetry_loop_counter == 27)telemetry_send_byte = telemetry_buffer_byte >> 24;       //Send the next 8 bytes of the longitude position variable.

  if (telemetry_loop_counter == 28) {
    telemetry_buffer_byte = adjustable_setting_1 * 100;                                     //Store the adjustable setting 1 as it can change during the next loop.
    telemetry_send_byte = telemetry_buffer_byte;                                            //Send the first 8 bytes of the adjustable setting 1.
  }
  if (telemetry_loop_counter == 29)telemetry_send_byte = telemetry_buffer_byte >> 8;        //Send the next 8 bytes of the adjustable setting 1.
  if (telemetry_loop_counter == 30) {
    telemetry_buffer_byte = throttle;                                     //Store the adjustable setting 1 as it can change during the next loop.
    telemetry_send_byte = telemetry_buffer_byte;                                            //Send the first 8 bytes of the adjustable setting 2.
  }
  if (telemetry_loop_counter == 31)telemetry_send_byte = telemetry_buffer_byte >> 8;        //Send the next 8 bytes of the adjustable setting 2.
  if (telemetry_loop_counter == 32) {
    telemetry_buffer_byte = currendExeeds;                                     //Store the adjustable setting 2 currendExceeds /100 * 100
    telemetry_send_byte = telemetry_buffer_byte;                                            //Send the first 8 bytes of the adjustable setting 3.
  }
  if (telemetry_loop_counter == 33)telemetry_send_byte = telemetry_buffer_byte >> 8;        //Send the next 8 bytes of the adjustable setting 3.


  if (telemetry_loop_counter == 34)telemetry_send_byte = check_byte;                        //Send the check-byte.


  //Data is sent whenever possible
  if (telemetry_loop_counter == 35)telemetry_loop_counter = 0;                             //After 125 loops reset the telemetry_loop_counter variable

  //Send the Telemetry data via the Serial bus to the Raspberry
  // Data is sent multiple times, to prevent data Loss
  if (telemetry_loop_counter <= 34) {
    transmit[0] = telemetry_loop_counter;
    transmit[1] = telemetry_loop_counter;
    transmit[2] = telemetry_loop_counter;
    transmit[3] = telemetry_send_byte;
    transmit[4] = telemetry_send_byte;
    transmit[5] = telemetry_send_byte;
    transmit[6] = (char)(0x76);
    transmit[7] = (char)(0x9A);
    transmit[8] = (byte)loopDuration;
    transmit[9] = (byte) (loopDuration >> 8);
  }
}
