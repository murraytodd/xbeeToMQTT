package com.murraywilliams.arduino.data;

import java.util.Arrays;
import java.util.Date;

import com.digi.xbee.api.utils.ByteUtils;

public abstract class ArduinoData {
	
	public abstract int getSize();
	
	protected Date dates[] = null;
	
	protected String typeString = "Unknown";
	
	public Date[] setDates(long start, long end) {
		int count = this.getSize();
		long interval = (end - start) / count;
		dates = new Date[count];
		for (int i=0; i<count; i++) {
			dates[i] = new Date( start + (interval * i));
		}
		return this.dates;
	}
	
	public Date[] setDates(Date start, Date end) {
		return setDates(start.getTime(), end.getTime());
	}
	
	public static ArduinoData parseByteArray(byte[] data) {
		String l = label(data);
		if (l.equals("Temps:") || (l.equals("MCP9808:")))
			return new ArduinoMCP9808(data);
		return null;
	}
	
	public static int offset(byte[] data) {
		for (int i=0; i<data.length; i++)
			if (data[i]==0x3A) return i+1;
		return 0;
	}
	
	public static String label(byte[] data) {
		int o = offset(data);
		return (o==0) ? "" : ByteUtils.byteArrayToString(Arrays.copyOfRange(data, 0, o));
	}
	
	public abstract String getLabel();
	
	public abstract void printByDate();
	
	public abstract String toJSON();
}
