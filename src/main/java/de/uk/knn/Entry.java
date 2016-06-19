package de.uk.knn;

import java.util.ArrayList;
import java.util.List;

public class Entry implements Comparable<Entry>{

	private List<Double> data = new ArrayList<Double>();
	private String type;
	
	private double distance;

	public List<Double> getData() {
		return data;
	}

	public void setData( List<Double> data2) {
		this.data = data2;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	@Override
	public int compareTo(Entry entry) {
		
		return Double.compare(getDistance(), entry.getDistance());
	}
	
	
	
}
