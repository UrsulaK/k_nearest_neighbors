package de.uk.knn;

public class Movie implements Comparable<Movie>{
    
	
	private String type;
	private int kicks;
	private int kisses;	
	private double distance;
	
	public Movie(String type, int kicks, int kisses) {
		super();
		this.type = type;
		this.kicks = kicks;
		this.kisses = kisses;
	}
	public Movie(int kicks, int kisses) {
		super();
		this.kicks = kicks;
		this.kisses = kisses;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getKicks() {
		return kicks;
	}
	public void setKicks(int kicks) {
		this.kicks = kicks;
	}
	public int getKisses() {
		return kisses;
	}
	public void setKisses(int kisses) {
		this.kisses = kisses;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	@Override
	public int compareTo(Movie o) {
		return Double.compare(this.distance, o.distance);
	}
}
