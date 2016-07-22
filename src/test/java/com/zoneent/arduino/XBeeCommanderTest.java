package com.zoneent.arduino;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.digi.xbee.api.utils.ByteUtils;
import com.digi.xbee.api.utils.HexUtils;
import com.zoneent.arduino.data.ArduinoData;
import com.zoneent.arduino.data.ArduinoMCP9808;
import com.zoneent.arduino.data.ArduinoMCP9808.DataTypes;

public class XBeeCommanderTest {

	int[] data = { 0x54, 0x65, 0x6D, 0x70, 0x73, 0x3A, 0xA8, 0xC1, 0xA8, 0xC1, 0xA7, 0xC1, 0xAA, 0xC1 };
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void processTempString() {
		String tempHexString = "54656D70733A41C148C147C145C144C148C14BC14DC14EC149C1";
		byte[] data = HexUtils.hexStringToByteArray(tempHexString);
		ArduinoData tempset = ArduinoData.parseByteArray(data);
		System.out.println(tempset);
	
	}
	
	@Test
	public void testUnwrap() {
		byte[] source = new byte[] { (byte) 219, 15, 73, 64 }; // Sent from Arduino pi = atan(1) * 4.0
		Collections.reverse(Arrays.asList(source));
		for (int i=0; i<source.length; i++) {
			System.out.print(Integer.toHexString(0xFF & source[i]));
			System.out.print(i < 3 ? " " : "\n");
		}
		float pi = ArduinoConversion.readFloat(source);
		Assert.assertEquals(Math.atan(1) * 4.0, pi, 0.0001);
		System.out.println(pi);
	}
	
	@Test
	public void test() {
		float temps[] = new float[(data.length-6)/2];
		for (int i=0; i<temps.length; i++) {
			int t = (data[7+(i*2)] << 8) + (data[6+(i*2)]);
			System.out.println("Raw form is " + Integer.toHexString(t));
			float temp = t & 0x0FFF;
			temp /= 16.0;
			if ((t & 0x1000) != 0) temp -= 256;
			System.out.println(temp + " C");
			System.out.println(((temp * 9.0 / 5.0) + 32) + " F");
		}
		System.out.println(Arrays.toString(temps));
	}

}
