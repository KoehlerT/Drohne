///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//In this file the timers for reading the receiver pulses and for creating the output ESC pulses are set.
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//More information can be found in these two videos:
//STM32 for Arduino - Connecting an RC receiver via input capture mode: https://youtu.be/JFSFbSg0l2M
//STM32 for Arduino - Electronic Speed Controller (ESC) - STM32F103C8T6: https://youtu.be/Nju9rvZOjVQ
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void timer_setup(void) {
  //TIMER2_BASE->CCER |= TIMER_CCER_CC1P;    //Detect falling edge.
  TIMER3_BASE->CR1 = TIMER_CR1_CEN | TIMER_CR1_ARPE;
    TIMER3_BASE->CR2 = 0;
    TIMER3_BASE->SMCR = 0;
    TIMER3_BASE->DIER = 0;
    TIMER3_BASE->EGR = 0;
    TIMER3_BASE->CCMR1 = (0b110 << 4) | TIMER_CCMR1_OC1PE |(0b110 << 12) | TIMER_CCMR1_OC2PE;
    TIMER3_BASE->CCMR2 = (0b110 << 4) | TIMER_CCMR2_OC3PE |(0b110 << 12) | TIMER_CCMR2_OC4PE;
    TIMER3_BASE->CCER = TIMER_CCER_CC1E | TIMER_CCER_CC2E | TIMER_CCER_CC3E | TIMER_CCER_CC4E;
    TIMER3_BASE->PSC = 71;
    TIMER3_BASE->ARR = 5000; //4000??
    TIMER3_BASE->DCR = 0;
    TIMER3_BASE->CCR1 = 1000;

    TIMER3_BASE->CCR1 = 1000;
    TIMER3_BASE->CCR2 = 1000;
    TIMER3_BASE->CCR3 = 1000;
    TIMER3_BASE->CCR4 = 1000;
    pinMode(PB1, PWM);
    pinMode(PB0, PWM);
    pinMode(PA7, PWM);
    pinMode(PA6, PWM);
}

