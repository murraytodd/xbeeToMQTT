package com.murraywilliams.arduino.data;

import java.util.Arrays;

import org.junit.Test;

import com.digi.xbee.api.utils.HexUtils;

public class ArduinoMCP9808Test {

	byte[] bytes = HexUtils.hexStringToByteArray("54656D70733A50C250C251C252C253C253C253C254C255C257C2");
	
	@Test
	public void parseMCP9808data() {
		// Loading bytes withe known header "Temps:" to cause auto-detection.
		ArduinoData data = ArduinoData.parseByteArray(bytes);
		assert(data instanceof ArduinoFloat);
		ArduinoFloat floatData = (ArduinoFloat) data;
		assert(floatData.getValues()[0] == 37.0);
	}
	
	@Test
	public void fromRaw() {
		// Loading only the sensor bytes (without header)
		ArduinoMCP9808 data = new ArduinoMCP9808(Arrays.copyOfRange(bytes, 6, bytes.length));
		assert(data.getValues()[9] == 37.4375);
	}
}
