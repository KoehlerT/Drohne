package com.koehlert.flowerflyer.target;

import com.koehlert.flowerflyer.main.*;


/**Gibt geschwinigkeiten an, um auf die Blume zuzusteuern
 * Ein PID-Regler wird benutzt
 * Geschwindigkeiten sollen normalisiert auf 1 - -1 errechnet werden
 * Geschwindigkeiten: Oben (+) Unten (-) [Altitude]
 * 					  Rechts(+) Links (-) [Roll]
 * 					  Hinten(+) Vorne (-) [Pitch]
 * 
 * 					Die Achse Links(+) und Rechts (-) (Drehung) Wird weggelassen!
 * */
public class SpeedController {
	static float p_gain_alt = 1;
	static float i_gain_alt = 0;
	static float d_gain_alt = 0;
	static int max_alt = 1;
	
	static float p_gain_roll = 1;
	static float i_gain_roll = 0;
	static float d_gain_roll = 0;
	static int max_roll = 1;
	
	static float p_gain_pitch = 1;
	static float i_gain_pitch = 0;
	static float d_gain_pitch = 0;
	static int max_pitch = 1;
	
	static float pid_i_mem_alt, pid_alt_setpoint, pid_alt_input, pid_output_alt, pid_last_alt_d_error;
	static float pid_i_mem_roll, pid_roll_setpoint, pid_roll_input, pid_output_roll, pid_last_roll_d_error;
	static float pid_i_mem_pitch, pid_pitch_setpoint, pid_pitch_input, pid_output_pitch, pid_last_pitch_d_error;
	
	static float pid_error_temp = 0;
	
	static Vector3 vel = new Vector3(0,0,0);
	
	static{
		pid_alt_setpoint = 0;
		pid_roll_setpoint = 0;
		pid_pitch_setpoint = 30f/60f;
	}
	
	public static Vector3 calculatePid(Location target){
		//Set Inputs
		pid_roll_input = target.x;
		pid_alt_input = target.y;
		pid_pitch_input = Math.min(target.dist,60)/60f;
		if (pid_pitch_input > 1) pid_pitch_input = 1;
		//pid_pitch_input *=-1;
		
		//Altitude calculations
		pid_error_temp = pid_alt_input - pid_alt_setpoint;
		pid_i_mem_alt += i_gain_alt * pid_error_temp;
		if (pid_i_mem_alt > max_alt) pid_i_mem_roll = max_alt;
		else if (pid_i_mem_alt < max_alt*-1) pid_i_mem_roll = max_alt*-1;
		
		pid_output_alt = p_gain_alt * pid_error_temp + pid_i_mem_alt + d_gain_alt * (pid_error_temp - pid_last_alt_d_error);
		if (pid_output_alt > max_alt) pid_output_alt = max_alt;
		else if (pid_output_alt < max_alt*-1) pid_output_alt = max_alt*-1;
		
		pid_last_alt_d_error = pid_error_temp;
		
		//Roll calculations
		pid_error_temp = pid_roll_input - pid_roll_setpoint;
		pid_i_mem_roll += i_gain_roll * pid_error_temp;
		if (pid_i_mem_roll > max_roll) pid_i_mem_roll = max_roll;
		else if (pid_i_mem_roll < max_roll*-1) pid_i_mem_roll = max_roll*-1;
		
		pid_output_roll = p_gain_roll * pid_error_temp + pid_i_mem_roll + d_gain_roll * (pid_error_temp - pid_last_roll_d_error);
		if (pid_output_roll > max_roll) pid_output_roll = max_roll;
		else if (pid_output_roll < max_roll*-1) pid_output_roll = max_roll*-1;
		
		pid_last_roll_d_error = pid_error_temp;
		
		//Pitch calculations
		pid_error_temp = pid_pitch_input - pid_pitch_setpoint;
		pid_i_mem_pitch += i_gain_pitch * pid_error_temp;
		if (pid_i_mem_pitch > max_pitch) pid_i_mem_pitch = max_pitch;
		else if (pid_i_mem_pitch < max_pitch*-1) pid_i_mem_pitch = max_pitch*-1;
		
		pid_output_pitch = p_gain_pitch * pid_error_temp + pid_i_mem_pitch + d_gain_pitch * (pid_error_temp - pid_last_pitch_d_error);
		if (pid_output_pitch > max_pitch) pid_output_pitch = max_pitch;
		else if (pid_output_pitch < max_pitch*-1) pid_output_pitch = max_pitch*-1;
		
		pid_last_roll_d_error = pid_error_temp;
		
		vel.x = pid_output_roll;
		vel.y = pid_output_pitch * -1;
		vel.z = pid_output_alt;
		
		return vel;
		
	}
	
}
