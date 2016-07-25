package com.murraywilliams.arduino.data;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class ArduinoFloat extends ArduinoData {
	
	protected DecimalFormat formatter = new DecimalFormat("###.0000");
	
	public abstract double[] getValues();
	
	public abstract double[] getValues(Enum<?> type) throws IllegalArgumentException;
	
	public String toJSON() {
		List<String> values = new ArrayList<String>();
		for (double f : getValues()) values.add(formatter.format(f));
		return "{ \"type\" : \"" + this.typeString + "\", \"values\" : [" + String.join(", ",  values) + "] }";
	}
}
