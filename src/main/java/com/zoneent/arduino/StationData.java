package com.zoneent.arduino;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class StationData {

	private String stationName = "unknown";
	private Date timestamp = new Date();
	private HashMap<String, Object> data = new HashMap<String, Object>();
	
	public StationData(String stationName, Date timestamp, HashMap<String,Object> data) {
		this.stationName = stationName;
		this.timestamp = timestamp;
		this.data = data;
	}
	
	public StationData(String stationName, String name, double value) {
		this.stationName = stationName;
		this.data = new HashMap<String, Object>(1);
		this.data.put(name, new Double(value));
	}
	
	public StationData(String stationName, String name, long value) {
		this.stationName = stationName;
		this.data = new HashMap<String, Object>(1);
		this.data.put(name, new Long(value));
	}
	
	public double getDouble() throws Exception {
		if (this.data.size()!=1) throw new Exception("getDouble can only be called on StationData with exactly 1 element.");
		String key = null;
		for (String n : this.data.keySet()) key = n;
		if (! (this.data.get(key) instanceof Double)) throw new Exception("getDouble called on non-Double data.");
		return (Double) this.data.get(key);
	}
	
	public long getLong() throws Exception {
		if (this.data.size()!=1) throw new Exception("getDouble can only be called on StationData with exactly 1 element.");
		String key = null;
		for (String n : this.data.keySet()) key = n;
		if (! (this.data.get(key) instanceof Long)) throw new Exception("getDouble called on non-Double data.");
		return (Long) this.data.get(key);
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public HashMap<String, Object> getData() {
		return data;
	}

	public void setData(HashMap<String, Object> data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		String me = "{ station : '" + stationName +  "', data : { ";
		List<String> keys = new ArrayList<String>(data.keySet());
		java.util.Collections.sort(keys);
		for (int i=0; i<keys.size(); i++) {
			me += "'" + keys.get(i) + "' : '" + data.get(keys.get(i)) + "'";
			if (i < (keys.size() - 1)) me += ", ";
		}
		me += " } }";
		return me;
	}
}
