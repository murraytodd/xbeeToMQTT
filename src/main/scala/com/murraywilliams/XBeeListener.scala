package com.murraywilliams

import com.digi.xbee.api.listeners.IDataReceiveListener
import com.digi.xbee.api.listeners.IPacketReceiveListener
import com.digi.xbee.api.models.XBeeMessage
import com.digi.xbee.api.packet.XBeePacket
import com.digi.xbee.api.packet.GenericXBeePacket
import com.digi.xbee.api.packet.UnknownXBeePacket
import com.digi.xbee.api.packet.XBeeAPIPacket
import com.murraywilliams.xbee.NodeIdentifierPacket


class XBeeListener extends IDataReceiveListener with IPacketReceiveListener {
  
  private[this] val logger = org.log4s.getLogger
  
  def dataReceived(msg : XBeeMessage) = {
    logger.warn(s"A message was received from '${msg.getDevice.getNodeID}' at ${msg.getDevice.get64BitAddress}")
    logger.warn(msg.getDataString)
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
