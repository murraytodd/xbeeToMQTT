package com.murraywilliams.xbee;

import java.util.Arrays;

import com.digi.xbee.api.exceptions.InvalidPacketException;
import com.digi.xbee.api.models.XBee16BitAddress;
import com.digi.xbee.api.models.XBee64BitAddress;
import com.digi.xbee.api.packet.UnknownXBeePacket;
import com.digi.xbee.api.utils.ByteUtils;
import com.digi.xbee.api.utils.HexUtils;

public class NodeIdentifierPacket {

	private final XBee64BitAddress sourceAddress64;
	
	private final XBee16BitAddress sourceAddress16;
	
	private final XBee64BitAddress sourceNetAddr64;
	
	private final XBee16BitAddress sourceNetAddr16;
	
	private final int receiveOptions;
	
	private final String nodeIdentifier;
	
	private final XBee16BitAddress parentAddress16;
	
	
	
	public NodeIdentifierPacket(UnknownXBeePacket source) throws InvalidPacketException {
		byte[] data = source.getPacketData();
		
		if ((data[0] & 0xFF) != 0x95) throw new InvalidPacketException("API code was " + HexUtils.byteToHexString(data[0]) + " instead of 0x95");
		
		sourceAddress64 = new XBee64BitAddress(Arrays.copyOfRange(data, 1, 9));
		sourceNetAddr16 = new XBee16BitAddress(Arrays.copyOfRange(data, 9, 11));
		
		receiveOptions = data[11] & 0xFF;
		
		sourceAddress16 = new XBee16BitAddress(Arrays.copyOfRange(data,12, 14));
		sourceNetAddr64 = new XBee64BitAddress(Arrays.copyOfRange(data,14, 22));
		
		if (data[22]!=' ') throw new InvalidPacketException("Expected a space at the 25th  offset of the NI frame!");
		
		int NIoffset = 23;
		while (((data[NIoffset] & 0xFF) != 0x00) && (NIoffset < data.length)) NIoffset++;
		nodeIdentifier = ByteUtils.byteArrayToString(Arrays.copyOfRange(data, 23, NIoffset));
		
		parentAddress16 = new XBee16BitAddress(Arrays.copyOfRange(data,NIoffset, NIoffset+2));
		NIoffset += 2;
	}
	
	public XBee64BitAddress getSourceAddress64() {
		return sourceAddress64;
	}

	public XBee16BitAddress getSourceAddress16() {
		return sourceAddress16;
	}

	public XBee64BitAddress getSourceNetAddr64() {
		return sourceNetAddr64;
	}

	public XBee16BitAddress getSourceNetAddr16() {
		return sourceNetAddr16;
	}

	public int getReceiveOptions() {
		return receiveOptions;
	}

	public XBee16BitAddress getParentAddress16() {
		return parentAddress16;
	}

	public String getNodeIdentifier() { return this.nodeIdentifier; }
}
