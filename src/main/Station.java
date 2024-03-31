package main;

public class Station {
	private String name;
	private int dist;
	
	public Station(String name, int dist) {
		this.name = name;
		this.dist = dist;
	}
	
	public String getCityName() {
		return this.name;
	}
	public void setCityName(String cityName) {
		this.name = cityName;
	}
	public int getDistance() {
		return this.dist;
	}
	public void setDistance(int distance) {
		this.dist = distance;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Station other = (Station) obj;
		return this.getCityName().equals(other.getCityName()) && this.getDistance() == other.getDistance();
	}
	@Override
	public String toString() {
		return "(" + this.getCityName() + ", " + this.getDistance() + ")";
	}

}
