package com.murraywilliams.arduino.data;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.murraywilliams.arduino.ArduinoConversion;

public class ArduinoMCP9808 extends ArduinoFloat {

	public enum DataTypes { TempC, TempF }
	
	private double temps[] = null;
	DecimalFormat formatter = new DecimalFormat("###.00");
	
	public ArduinoMCP9808(byte[] data) {
		int offset = 0;
		for (int i=0; i<data.length; i++) {
			if (data[i]==0x3A) offset = i + 1;
		}
		this.temps = getMCP9808TempC(offset == 0 ? data : Arrays.copyOfRange(data, offset, data.length));
	}
	
	@Override
	public double[] getValues() {
		return temps;
	}

	@Override
	public double[] getValues(Enum<?> type) {
		if (type.equals(DataTypes.TempF)) {
			double f[] = new double[temps.length];
			for (int i=0; i<temps.length; i++) f[i] = temps[i] * 9.0 / 5.0 + 32.0;
			return f;
		} else return this.temps;
	}

	public static double[] getMCP9808TempC(byte[] source) throws IllegalArgumentException {
		int[] rawValues = ArduinoConversion.readUnsignedInts(source);
		double[] temps = new double[rawValues.length];
		for (int i=0; i<rawValues.length; i++) {
			temps[i] = (rawValues[i] & 0x0FFF) / 16.0;
			if ((rawValues[i] & 0x1000) != 0) temps[i] -= 256.0;
		}
		return temps;
	}
	
	public static double[] getMCP9808TempF(byte[] source) throws IllegalArgumentException {
		double t[] = getMCP9808TempC(source);
		for (int i=0; i<t.length; i++) {
			t[i] = t[i] * 9.0 / 5.0 + 32.0;
		}
		return t;
	}
	
	public static double[] parseDoubles(byte[] source) {
		ArduinoMCP9808 d = new ArduinoMCP9808(source);
		return d.getValues();
	}
	
	@Override
	public String toString() {
		List<String> values = new ArrayList<String>();
		for (double f : getValues(DataTypes.TempF)){
			values.add(formatter.format(f) + "\u00b0F");
		}
		return String.join(", ", values);
	}

	@Override
	public int getSize() {
		return temps.length;
	}

	@Override
	public void printByDate() {
		double[] ftemps = getValues(DataTypes.TempF);
		for (int i=0; i<temps.length; i++) {
			System.out.print(dates[i] + ", " + formatter.format(ftemps[i]) + "\u00b0F");
		}
		
	}

	@Override
	public String getLabel() {
		return "MCP9808Temp";
		
	}
}
