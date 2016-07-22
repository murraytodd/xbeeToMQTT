package com.zoneent.arduino;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.digi.xbee.api.exceptions.InvalidPacketException;
import com.digi.xbee.api.listeners.IDataReceiveListener;
import com.digi.xbee.api.listeners.IPacketReceiveListener;
import com.digi.xbee.api.models.XBee16BitAddress;
import com.digi.xbee.api.models.XBeeMessage;
import com.digi.xbee.api.packet.UnknownXBeePacket;
import com.digi.xbee.api.packet.XBeeAPIPacket;
import com.digi.xbee.api.packet.XBeePacket;
import com.digi.xbee.api.utils.ByteUtils;
import com.digi.xbee.api.utils.HexUtils;
import com.zoneent.arduino.data.ArduinoData;
import com.zoneent.arduino.data.ArduinoFloat;
import com.zoneent.arduino.data.ArduinoMCP9808;

public class SensorCoordinatorListener implements IPacketReceiveListener , IDataReceiveListener {

	private static Logger log = LoggerFactory.getLogger(SensorCoordinatorListener.class);
	
	HashMap<String, XBee16BitAddress> names = new HashMap<String, XBee16BitAddress>();
	HashMap<String, Date> lastEntries = new HashMap<String, Date>();
	HashMap<String, StationData[]> caches = new HashMap<String, StationData[]>();
	
	public void registerDevice(String name, XBee16BitAddress address) {
		String key = null;
		for (String n : names.keySet()) {
			if (names.get(n).equals(address)) key = n;
		}
		if (key!=null) names.remove(key);
		names.put(name, address);
	}
	
	public String getName(XBee16BitAddress address) {
		for (String n : names.keySet()) {
			if (names.get(n).equals(address)) return(n);
		}
		return address.toString();
	}
	
	public void logData(StationData data) {
		System.out.println(data.getTimestamp() + " : " + data);
	}
	
	public void register(String stationName, StationData[] data) {
		log.debug("register from " + stationName + " with " + data.length + " items.");
		log.debug("caches" + (caches.containsKey(stationName) ? " has " : " hasn't ") + " an entry.");
		log.debug("lastEntries" + (lastEntries.containsKey(stationName) ? " has " : " hasn't ") + " an entry.");
		if (caches.containsKey(stationName) && lastEntries.containsKey(stationName)) {
			log.info("Stored original data found. Processing...");
			StationData[] lastArray = caches.get(stationName);
			long now = (new Date()).getTime();
			long ending = lastEntries.get(stationName).getTime();
			long beginning = (2 * ending) - now;
			long inc = (now - ending) / data.length;
			for (int i=0; i<data.length; i++) {
				lastArray[i].setTimestamp(new Date(beginning + (inc * i)));
				lastArray[i].setStationName(stationName);
				logData(lastArray[i]);
			}
			caches.remove(stationName);
		} 
		if (lastEntries.containsKey(stationName)) {
			log.info("New data delivered. Processing...");
			long now = (new Date()).getTime();
			long then = lastEntries.get(stationName).getTime();
			long inc = (now - then) / data.length;
			for (int i=0; i<data.length; i++) {
				data[i].setTimestamp(new Date(then + (inc * i)));
				data[i].setStationName(stationName);
				logData(data[i]);
			}
			lastEntries.put(stationName, new Date(now));
		} else {
			log.info("First batch of data delivered, holding until we know the time intervals.");
			caches.put(stationName, data);
			lastEntries.put(stationName, new Date());
		}
	}
	
	public void dataReceived(XBeeMessage msg) {
		
		String stationName = null;
		if ((msg.getDevice()!=null) && (msg.getDevice().getNodeID()!=null)) {
			stationName = msg.getDevice().getNodeID();
		} else {
			getName(msg.getDevice().get16BitAddress());
		}
		
		log.info("dataReceived called with msg from " + stationName + " <" + msg.getDevice().get16BitAddress().toString() +
				":" + msg.getDevice().get64BitAddress().toString() + ">");
		
		byte[] data = msg.getData();
		log.debug(HexUtils.prettyHexString(data) + " : (" +
				ByteUtils.byteArrayToString(data).substring(0, 5) + "...)");

		ArduinoData parsedData = ArduinoData.parseByteArray(data);
		if ((parsedData!=null) && (parsedData instanceof ArduinoFloat)) {
			StationData[] sd = new StationData[parsedData.getSize()];
			double[] parsedValues = ((ArduinoFloat) parsedData).getValues();
			for (int i=0; i<sd.length; i++) {
				sd[i] = new StationData(stationName,parsedData.getLabel(),parsedValues[i]);
			}
			register(stationName, sd);
		} else {
			log.warn(ByteUtils.byteArrayToString(data));
		}
	}

	public void packetReceived(XBeePacket receivedPacket) {
		log.debug("PacketReceived called with type " + receivedPacket.getClass().getCanonicalName());
		if (receivedPacket instanceof UnknownXBeePacket) {
			UnknownXBeePacket unknownPacket = (UnknownXBeePacket) receivedPacket;
			if (unknownPacket.getFrameTypeValue()==0x95) {
				try {
					NodeIdentifierPacket ni = new NodeIdentifierPacket(unknownPacket);
					this.registerDevice(ni.getNodeIdentifier(), ni.getSourceNetAddr16());
					log.info("NodeIdentification call from " + ni.getNodeIdentifier());
					log.debug(" with address <" + ni.getSourceNetAddr64() + "> <" + ni.getSourceNetAddr16() + ">");
				} catch (InvalidPacketException e) {
					log.error("Error getting node identifier: " + e.getMessage());
				}
			} else {
				log.warn("Uknown frame with FrameTypeID = " + unknownPacket.getFrameTypeValue());
			}

		} else if (receivedPacket instanceof XBeeAPIPacket) {
			XBeeAPIPacket apiPacket = (XBeeAPIPacket) receivedPacket;
			log.info("Packet API frame type is " + apiPacket.getFrameType());
		} else {
			log.error("No idea what's going on.");
		}
	}

}
