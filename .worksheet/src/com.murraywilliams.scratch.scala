package com.murraywilliams

import com.digi.xbee.api.utils.HexUtils
import com.murraywilliams.arduino.data.ArduinoConversion

object scratch {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(197); 
  val data = HexUtils.hexStringToByteArray("6666EE41");System.out.println("""data  : Array[Byte] = """ + $show(data ));$skip(45); 
  println(ArduinoConversion.readFloat(data))}
}
