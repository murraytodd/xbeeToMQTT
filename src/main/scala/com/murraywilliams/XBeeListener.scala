package com.murraywilliams

import com.digi.xbee.api.listeners.IDataReceiveListener
import com.digi.xbee.api.listeners.IPacketReceiveListener
import com.digi.xbee.api.models.XBeeMessage
import com.digi.xbee.api.packet.XBeePacket
import com.digi.xbee.api.packet.GenericXBeePacket
import com.digi.xbee.api.packet.UnknownXBeePacket
import com.digi.xbee.api.packet.XBeeAPIPacket
import com.murraywilliams.xbee.NodeIdentifierPacket
import org.eclipse.paho.client.mqttv3.MqttMessage
import com.murraywilliams.arduino.data.ArduinoMCP9808
import com.murraywilliams.arduino.data.ArduinoConversion

class XBeeListener extends IDataReceiveListener with IPacketReceiveListener {
  import MQTTClient.mqttSend

  private[this] val logger = org.log4s.getLogger  
  
  val nodeIds = scala.collection.mutable.Map[String, String]()
  
  def updateNodeTable(nodeId : String, addr64 : String) = {
    // first, check to see if it was already registered
    val matchingKeys = nodeIds.filter(t => t._2==nodeId && t._1!=addr64).map(_._1)
    if (matchingKeys.nonEmpty) {
      logger.error(s"""Unexpected NodeId '${nodeId}' reporting from address $addr64 when same name once reported from ${matchingKeys.head} !""")
      matchingKeys.foreach(key => nodeIds.remove(key))
    }
    nodeIds(addr64) = nodeId
  }
  
  def dataReceived(msg : XBeeMessage) = {
    val addr64 = msg.getDevice.get64BitAddress.toString
    val nodeId = nodeIds.getOrElse(addr64, "")
    logger.warn(s"A message was received from '${nodeId}' at ${addr64}")
    logger.warn(msg.getDataString)
    val data = msg.getData
    val colonIdx = data.indexOf(':')
    if (colonIdx != -1) {
      val payload = data.slice(colonIdx,data.length)
      val header = new String(data.slice(0,colonIdx))
      if (header=="Temps") {
        val tempReadingsF = (new ArduinoMCP9808(payload)).getValues(ArduinoMCP9808.DataTypes.TempF)
        val json= s"""{ "sensor" : "MCP9808", "scale" : "F", "values" : [ ${tempReadingsF.mkString(", ")} ] }"""
        mqttSend("readings",json)
      } else if (header.endsWith("F")) {
        val readings = ArduinoConversion.readFloatArray(payload)
        val readingsString = readings.map(_.formatted("%.4f")).mkString(", ")
        val sensor = header.substring(0, header.length-1)
        val json = s"""{ "sensor" : "${sensor}", "values" : [ ${readingsString} ] }"""
        mqttSend("readings",json)
      } else {
        val hexString = payload.map("%02x".format(_)).mkString
        mqttSend("readings",s"""{ "sender" : ${header}, "payload" : ${hexString}""")
      }
    } else {
      mqttSend("raw", msg.getData)
    }
  }
  
  def packetReceived(packet : XBeePacket) = {
    packet match {
      case genPkt : GenericXBeePacket => {
        logger.warn(s"Received generic packet with Frame Type value ${genPkt.getFrameTypeValue.toHexString}")
      }
      case unknown : UnknownXBeePacket if (unknown.getFrameTypeValue==0x95) => {
        val nodeId = new NodeIdentifierPacket(unknown)
        val nodeName = nodeId.getNodeIdentifier
        val addr16 = nodeId.getSourceNetAddr16
        val addr64 = nodeId.getSourceNetAddr64
        logger.debug(s"Detected node '${nodeName}' for device with 64-bit address ${addr64} and 16-bit address ${addr16}")
        mqttSend("discovery", s"""{ "nodeId" : "${nodeName}", "addr64" : "${addr64}", "addr16" : "${addr16}" }""")
        updateNodeTable(nodeName, addr64.toString)
        
      }
      case unknown : UnknownXBeePacket => {
        logger.error(s"Received unknown xbee packet with Frame Type ${unknown.getFrameTypeValue.toHexString} that was not ${0x95}")
      }
      case apiPkt : XBeeAPIPacket => {
        logger.info(s"Received XBeeAPIPacket with frame type value ${apiPkt.getFrameTypeValue.toHexString}")
      }
    }
  }
}
