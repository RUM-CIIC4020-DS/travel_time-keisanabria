package main;

import interfaces.List;
import interfaces.Map;
import interfaces.Stack;

/* My imports below */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import data_structures.HashTableSC;
import data_structures.SimpleHashFunction;

public class TrainStationManager {
	
	private Map<String, List<Station>> stations = new HashTableSC<>(1, new SimpleHashFunction<>());
	private String[] parts;
	private String src_city;
	private String station;
	// Initiate Map<String, Station> shortRoutes
	
	/* Reads the file given by station_file and populates the stations map */
	public TrainStationManager(String station_file) {
		/* Calls findShortestDistance() for "Westside" so that the shortest
		routes will be in reference to Westside. */
		
		try (BufferedReader reader = new BufferedReader(new FileReader(station_file))) {
            String line;
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                if(lineNum != 1) { // If it is the first line, it skips it
                	// If it contains key (use containsKey method), just add the dest_city to the List<Station>
                	// Else, add the key
                	
                	parts = line.split(",", 2);
                	src_city = parts[0];
                	station = parts[1];
                	if(stations.containsKey( /* dest_city */ )) {
                		
                	}
                	stations.put( , );
                }
                lineNum++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	/* Calculates the shortest route from “Westside” to every other station following 
	 * the logic given in the “Shortcuts to Victory” section. It populates
	 * the shortest route map. */
	private void findShortestDistance() {
				
	}

	/* Receives a Stack that needs to remain sorted and the station we want to add. */
	public void sortStack(Station station, Stack<Station> stackToSort) {
		// Create alg to keep the stack sorted (Reference: "How do we keep the stack sorted?" section)
	}
	
	/*  Returns a Map where the key is the station name, and the value is 
	 * the time it takes to reach that station. */
	public Map<String, Double> getTravelTimes() {
		// 2.5 minutes per kilometer (use the shortest distance for this)
		// 15 min per station (between Westside and the destination)
		
	}


	public Map<String, List<Station>> getStations() {
		
	}


	public void setStations(Map<String, List<Station>> cities) {
		
	}


	public Map<String, Station> getShortestRoutes() {
		
	}


	public void setShortestRoutes(Map<String, Station> shortestRoutes) {
		
	}
	
	/**
	 * BONUS EXERCISE THIS IS OPTIONAL
	 * Returns the path to the station given. 
	 * The format is as follows: Westside->stationA->.....stationZ->stationName
	 * Each station is connected by an arrow and the trace ends at the station given.
	 * 
	 * @param stationName - Name of the station whose route we want to trace
	 * @return (String) String representation of the path taken to reach stationName.
	 */
	public String traceRoute(String stationName) {
		// Remove if you implement the method, otherwise LEAVE ALONE
		throw new UnsupportedOperationException();
	}

}