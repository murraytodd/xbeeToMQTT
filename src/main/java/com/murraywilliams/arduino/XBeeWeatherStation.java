package com.murraywilliams.arduino;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.digi.xbee.api.ZigBeeDevice;
import com.digi.xbee.api.exceptions.XBeeException;

public class XBeeWeatherStation {	
	
	private static Logger log = LoggerFactory.getLogger(XBeeWeatherStation.class);
	
	public static SensorCoordinatorListener listener = new SensorCoordinatorListener();
	
	public static void main(String[] args) {
		
		ZigBeeDevice boss = new ZigBeeDevice(args[0], 9600);
		
		try {
			boss.open();
			log.warn("Starting XBee Coordinator v2.0");
			log.info(" with 64bit address: " + boss.get64BitAddress().toString());
			log.info(" and 16bit address: " + boss.get16BitAddress().toString());
			log.info(" and NI: " + boss.getNodeID());
		} catch (XBeeException e) {
			log.error("Error Opening XBee device: " + e.getMessage());
			System.exit(-1);
		}
		
		boss.addDataListener(listener);
		boss.addPacketListener(listener);
		
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
