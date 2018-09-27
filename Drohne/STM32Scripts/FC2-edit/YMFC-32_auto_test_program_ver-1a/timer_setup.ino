
#define HWire WIRE
//
//In this file the timers for reading the receiver pulses and for creating the output ESC pulses are set.
//More information can be found in these two videos:
//STM32 for Arduino - Connecting an RC receiver via input capture mode: https://youtu.be/JFSFbSg0l2M
//STM32 for Arduino - Electronic Speed Controller (ESC) - STM32F103C8T6: https://youtu.be/Nju9rvZOjVQ
//
void timer_setup(void) {

//A test is needed to check if the throttle input is active and valid. Otherwise the ESC's might start without any warning.
  loop_counter = 0;
  while ((channel_3 > 2100 || channel_3 < 900) && warning == 0) {
    getRaspberryInfo();
    delay(100);
    loop_counter++;
    if (loop_counter == 40) {
      Serial.println(F("Waiting for a valid receiver channel-3 input signal"));
      Serial.println(F(""));
      Serial.println(F("The input pulse should be between 1000 till 2000us"));
      Serial.print(F("Current channel-3 receiver input value = "));
      Serial.println(channel_3);
      Serial.println(F(""));
      Serial.println(F("Is the receiver connected and the transmitter on?"));
      Serial.println(F("For more support and questions: www.brokking.net"));
      Serial.println(F(""));
      Serial.print(F("Waiting for another 5 seconds."));
    }
    if (loop_counter > 40 && loop_counter % 10 == 0)Serial.print(F("."));

      if (loop_counter == 90) {
      Serial.println(F(""));
      Serial.println(F(""));
      Serial.println(F("The ESC outputs are disabled for safety!!!"));
      warning = 1;
    }
  }
  if (warning == 0) {
    TIMER3_BASE->CR1 = TIMER_CR1_CEN | TIMER_CR1_ARPE;
    TIMER3_BASE->CR2 = 0;
    TIMER3_BASE->SMCR = 0;
    TIMER3_BASE->DIER = 0;
    TIMER3_BASE->EGR = 0;
    TIMER3_BASE->CCMR1 = (0b110 << 4) | TIMER_CCMR1_OC1PE |(0b110 << 12) | TIMER_CCMR1_OC2PE;
    TIMER3_BASE->CCMR2 = (0b110 << 4) | TIMER_CCMR2_OC3PE |(0b110 << 12) | TIMER_CCMR2_OC4PE;
    TIMER3_BASE->CCER = TIMER_CCER_CC1E | TIMER_CCER_CC2E | TIMER_CCER_CC3E | TIMER_CCER_CC4E;
    TIMER3_BASE->PSC = 71;
    TIMER3_BASE->ARR = 4000;
    TIMER3_BASE->DCR = 0;
    TIMER3_BASE->CCR1 = 1000;

    TIMER3_BASE->CCR1 = channel_3;
    TIMER3_BASE->CCR2 = channel_3;
    TIMER3_BASE->CCR3 = channel_3;
    TIMER3_BASE->CCR4 = channel_3;
    pinMode(PB1, PWM);
    pinMode(PB0, PWM);
    pinMode(PA7, PWM);
    pinMode(PA6, PWM);
  }
}
