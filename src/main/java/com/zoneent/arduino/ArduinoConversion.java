package com.zoneent.arduino;

import java.util.Arrays;

public class ArduinoConversion {
	public static float readFloat(byte[] dataStream) throws IllegalArgumentException {
		if (dataStream.length!=4) 
			throw new IllegalArgumentException("Byte array length (" + dataStream.length + ") must be 4.");
		int bits = (( 0xFF & dataStream[3] ) << 24) | (( 0xFF & dataStream[2]) << 16) | 
				((0xFF & dataStream[1]) << 8) | (0xFF & dataStream[0]);
		return Float.intBitsToFloat(bits);
	}
	
	public static float readFloat(byte[] dataStream, int offset) throws IllegalArgumentException {
		if ((offset + 4) > dataStream.length) 
			throw new IllegalArgumentException("Offset reads past the end of the byte array.");
		return readFloat(new byte[] { dataStream[3 + offset] , dataStream[2 + offset], 
				dataStream[1 + offset], dataStream[offset] });
	}
	
	public static float[] readFloatArray(byte[] dataStream) throws IllegalArgumentException {
		if ((dataStream.length % 4) != 0) 
			throw new IllegalArgumentException("Byte array length (" + dataStream.length + ") must be a multiple of 4.");
		float[] values = new float[(dataStream.length / 4)];
		for (int i=0; i<values.length; i++) {
			values[i] = readFloat(dataStream, i*4);
		}
		return values;
	}
	
	public static float[] readFloatArray(byte[] dataStream, int offset) {
		if (((dataStream.length - offset) % 4) != 0)
			throw new IllegalArgumentException("Byte array length after offset (" + (dataStream.length - offset) + ") must be a multiple of 4.");
		return readFloatArray(Arrays.copyOfRange(dataStream, offset, dataStream.length));
	}
	
	public static float[] readFloatArray(byte[] dataStream, int offset, int count) {
		if ((dataStream.length - offset) < (count * 4))
			throw new IllegalArgumentException("Byte array length past offset (" + (dataStream.length - offset) + ") not sufficient to parse " + count + " 4-byte floats.");
		return readFloatArray(Arrays.copyOfRange(dataStream, offset, offset + (count * 4)));
	}
	
	public static int readUnsignedInt(byte lsb, byte msb) {
		return ((msb & 0xFF) << 8) | (lsb & 0xFF);
	}
	
	public static int[] readUnsignedInts(byte[] source) throws IllegalArgumentException {
		if ((source.length % 2) != 0)
			throw new IllegalArgumentException("Byte array must have an even number of elements.");
		int[] values = new int[source.length / 2];
		for (int i=0; i<values.length; i++)
			values[i] = readUnsignedInt(source[i * 2], source[(i * 2) + 1]);
		return values;
	}
	
	public static int[] readUnsignedInts(byte[] source, int offset) throws IllegalArgumentException {
		return readUnsignedInts(Arrays.copyOfRange(source, offset, source.length));
	}
	

}
