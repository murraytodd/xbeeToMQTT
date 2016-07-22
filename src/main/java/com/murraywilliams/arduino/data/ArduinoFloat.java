package com.murraywilliams.arduino.data;

public abstract class ArduinoFloat extends ArduinoData {
	
	public abstract double[] getValues();
	
	public abstract double[] getValues(Enum<?> type) throws IllegalArgumentException;
	
}
