package com.murraywilliams

import com.digi.xbee.api.utils.HexUtils
import com.murraywilliams.arduino.data.ArduinoConversion
import com.murraywilliams.arduino.data.ArduinoData

object scratch {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(280); 
  def toHexString(bytes : Array[Byte]) : String = bytes.map("%02x".format(_)).mkString;System.out.println("""toHexString: (bytes: Array[Byte])String""");$skip(57); 

  val value = HexUtils.hexStringToByteArray("6666EE41");System.out.println("""value  : Array[Byte] = """ + $show(value ));$skip(37); val res$0 = 
  ArduinoConversion.readFloat(value);System.out.println("""res0: Float = """ + $show(res$0));$skip(94); val res$1 = 
  ArduinoConversion.readFloatArray(HexUtils.hexStringToByteArray("cdccf041cdccf0410000f041"));System.out.println("""res1: Array[Float] = """ + $show(res$1));$skip(119); val res$2 = 

  ArduinoData.parseByteArray(HexUtils.hexStringToByteArray("4854553231442d462d54656d70463a28a6f0410ce8f04104fef041"));System.out.println("""res2: com.murraywilliams.arduino.data.ArduinoData = """ + $show(res$2));$skip(152); 
                                                  
  val data = HexUtils.hexStringToByteArray("4854553231442d462d54656d70463a28a6f0410ce8f04104fef041");System.out.println("""data  : Array[Byte] = """ + $show(data ));$skip(34); 
  val listener = new XBeeListener;System.out.println("""listener  : com.murraywilliams.XBeeListener = """ + $show(listener ));$skip(35); 
  val colonIdx = data.indexOf(':');System.out.println("""colonIdx  : Int = """ + $show(colonIdx ));$skip(51); 
  val payload = data.slice(colonIdx+1,data.length);System.out.println("""payload  : Array[Byte] = """ + $show(payload ));$skip(23); val res$3 = 
  toHexString(payload);System.out.println("""res3: String = """ + $show(res$3));$skip(44); val res$4 = 
  ArduinoConversion.readFloatArray(payload);System.out.println("""res4: Array[Float] = """ + $show(res$4));$skip(94); val res$5 = 
  ArduinoConversion.readFloatArray(HexUtils.hexStringToByteArray("28a6f0410ce8f04104fef041"));System.out.println("""res5: Array[Float] = """ + $show(res$5));$skip(75); val res$6 = 
  ArduinoConversion.readFloat(HexUtils.hexStringToByteArray("28a6f041"),0);System.out.println("""res6: Float = """ + $show(res$6));$skip(73); val res$7 = 
  ArduinoConversion.readFloat(HexUtils.hexStringToByteArray("0ce8f041"));System.out.println("""res7: Float = """ + $show(res$7));$skip(73); val res$8 = 
  ArduinoConversion.readFloat(HexUtils.hexStringToByteArray("04fef041"));System.out.println("""res8: Float = """ + $show(res$8));$skip(50); 
  val header = new String(data.slice(0,colonIdx));System.out.println("""header  : String = """ + $show(header ));$skip(52); 
  val json = listener.parseReadings(header,payload);System.out.println("""json  : String = """ + $show(json ))}
}

/*
Delivery returned true.
HTU21D-F-TempF::30.08 HTU21D-F-HumF::41.17 MCP9808F::30.44 BMP085-TempF::995.07 BMP085-PresF::30.00
Finished loading 1 of 3 readings.
HTU21D-F-TempF::30.11 HTU21D-F-HumF::41.08 MCP9808F::30.37 BMP085-TempF::995.14 BMP085-PresF::30.00
Finished loading 2 of 3 readings.
HTU21D-F-TempF::30.12 HTU21D-F-HumF::41.02 MCP9808F::30.44 BMP085-TempF::995.12 BMP085-PresF::30.00
Finished loading 3 of 3 readings.
Preparing payloads.
Prepping HTU21D-F-TempF: 30.08 30.11 30.12
[ 48 54 55 32 31 44 2D 46 2D 54 65 6D 70 46 3A 28 A6 F0 41 C E8 F0 41 4 FE F0 41 ]
Prepping HTU21D-F-HumF: 41.17 41.08 41.02
[ 48 54 55 32 31 44 2D 46 2D 48 75 6D 46 3A C8 AC 24 42 D8 56 24 42 88 10 24 42 ]
Prepping MCP9808F: 30.44 30.37 30.44
[ 4D 43 50 39 38 30 38 46 3A 0 80 F3 41 0 0 F3 41 0 80 F3 41 ]
Prepping BMP085-TempF: 995.07 995.14 995.12
[ 42 4D 50 30 38 35 2D 54 65 6D 70 46 3A 7B C4 78 44 F6 C8 78 44 AE C7 78 44 ]
Prepping BMP085-PresF: 30.00 30.00 30.00
[ 42 4D 50 30 38 35 2D 50 72 65 73 46 3A 0 0 F0 41 0 0 F0 41 0 0 F0 41 ]

Prepping HTU21D-F-TempF: 30.08 30.11 30.12
 48 54 55 32 31 44 2D 46 2D 54 65 6D 70 46 3A 28 A6 F0 41 0C E8 F0 41 04 FE F0 41.
4854553231442d462d54656d70463a28a6f0410ce8f04104fef041

HTU21D-F-HumF: 41.17 41.08 41.02
 48 54 55 32 31 44 2D 46 2D 48 75 6D 46 3A C8 AC 24 42 D8 56 24 42 88 10 24 42.
4854553231442d462d48756d463ac8ac2442d856244288102442

MCP9808F: 30.44 30.37 30.44
 4D 43 50 39 38 30 38 46 3A 00 80 F3 41 00 00 F3 41 00 80 F3 41.
4d435039383038463a0080f3410000f3410080f341

BMP085-TempF: 995.07 995.14 995.12
 42 4D 50 30 38 35 2D 54 65 6D 70 46 3A 7B C4 78 44 F6 C8 78 44 AE C7 78 44.
424d503038352d54656d70463a7bc47844f6c87844aec77844

BMP085-PresF: 30.00 30.00 30.00
 42 4D 50 30 38 35 2D 50 72 65 73 46 3A 00 00 F0 41 00 00 F0 41 00 00 F0 41.
424d503038352d50726573463a0000f0410000f0410000f041


*/
