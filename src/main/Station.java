package main;

/**
 * Class that creates an object that represents a station. It contains of the name of the
 * city at which that station is and the distance of it from another station.
 * <p>
 * It utilizes a String and Integer to efficiently store the aforementioned values.
 */
public class Station {
	private String name;
	private int dist;
	
	/**
	 * Constructor that initializes the station with the given name and distance.
	 * <p>
	 * @param name represents the name of the city at which the station is
	 * @param dist represents the distance of the station from another
	 */
	public Station(String name, int dist) {
		this.name = name;
		this.dist = dist;
	}
	
	/**
	 * Method that gives the name of the city at which the station is.
	 * <p>
	 * @return name which represents the name of the city at which the station is
	 */
	public String getCityName() {
		return this.name;
	}
	
	/**
	 * Method that sets the city name to a given name.
	 * <p>
	 * @param cityName that will represent the city of the station
	 */
	public void setCityName(String cityName) {
		this.name = cityName;
	}
	
	/**
	 * Method that gives the distance of the station from another.
	 * <p>
	 * @return dist which is the distance of the station
	 */
	public int getDistance() {
		return this.dist;
	}
	
	/**
	 * Method that sets the distance of the station from another
	 * <p>
	 * @param distance of the station to another
	 */
	public void setDistance(int distance) {
		this.dist = distance;
	}
	
	/**
	 * Method that determines if the given instance of a station has the same
	 * distance and city name of the one being compared to it.
	 * <p>
	 * @param obj which represents the station to be compared to
	 * @return if it's true that the station is equal to the one being
	 * compared to
	 */
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
	
	/**
	 * Method that converts the obj of Station into a string in the format of (city_name, distance).
	 * <p>
	 * @return a string of the station in the format of (city_name, distance)
	 */
	@Override
	public String toString() {
		return "(" + this.getCityName() + ", " + this.getDistance() + ")";
	}

}
