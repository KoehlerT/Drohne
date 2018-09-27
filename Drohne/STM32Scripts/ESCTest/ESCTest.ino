int pwm = 1000;

void setup() {
  // put your setup code here, to run once:
Serial.begin(230400);
  
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

    TIMER3_BASE->CCR1 = 1000;
    TIMER3_BASE->CCR2 = 1000;
    TIMER3_BASE->CCR3 = 1000;
    TIMER3_BASE->CCR4 = 1000;
    pinMode(PB1, PWM);
    pinMode(PB0, PWM);
    pinMode(PA7, PWM);
    pinMode(PA6, PWM);



}

void loop() {
  if (Serial.available()){
    char c = Serial.read();
    if (c == 'u'){
      pwm+=50;
    }
    if (c == 'd'){
      pwm-=50;
    }

    if (c == 'm'){
      pwm = 2000;
    }
    if (c == 'n'){
      pwm = 1000;
    }
    TIMER3_BASE->CCR1 = pwm;
    TIMER3_BASE->CCR2 = pwm;
    TIMER3_BASE->CCR3 = pwm;
    TIMER3_BASE->CCR4 = pwm;
    Serial.println(pwm);
  }
  
}
