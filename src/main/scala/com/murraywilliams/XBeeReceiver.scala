package com.murraywilliams

import com.digi.xbee.api.ZigBeeDevice
import com.typesafe.config.ConfigFactory

object XBeeReceiver {
  
  val conf = ConfigFactory.load
  
  private[this] val logger = org.log4s.getLogger
  
  lazy val xbeeDevice = {
    val port = conf.getString("xbee.port")
    val speed = conf.getInt("xbee.baud")
    val device = new ZigBeeDevice(port, speed)
    device.open
    logger.info(s"""Connected to Xbee device \"${device.getNodeID.trim}\" """ + 
	    s"with 64bit address ${device.get64BitAddress.toString} " +
      s"and 16bit address ${device.get16BitAddress.toString} ")
    device
  }
  
  def main(args: Array[String]): Unit = {
    try {
      val listener = new XBeeListener
      xbeeDevice.addDataListener(listener)
      xbeeDevice.addPacketListener(listener)
     
      while (true) Thread sleep 1000
    } finally {
      xbeeDevice.close
    }
  }
}
