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
import data_structures.ArrayList;
import main.Station;
import data_structures.LinkedStack;
import data_structures.HashSet;

public class TrainStationManager {
	
	private Map<String, List<Station>> stations = new HashTableSC<>(1, new SimpleHashFunction<>());
	private String[] parts;
	private String src_city;
	private String stationString;
	private String dest_city;
	private int distance;
	private Map<String, Station> shortRoutes = new HashTableSC<>(1, new SimpleHashFunction<>()); 
	private Stack<Station> sortedStack = new LinkedStack<Station>();
	private Stack<Station> tempStack = new LinkedStack<Station>();
	private Stack<Station> toVisit = new LinkedStack<Station>();
	private HashSet<Station> visited = new HashSet<Station>();
	
	/* Method: Reads the file given by station_file and populates the stations map */
	public TrainStationManager(String station_file) {
		
		try (BufferedReader reader = new BufferedReader(new FileReader("inputFiles/" + station_file))) {
            String line;
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                if(lineNum != 1) { // Condition: if it is the first line, it skips it
                	
                	parts = line.split(",", 2);
                	src_city = parts[0];
                	stationString = parts[1];
                	dest_city = stationString.split(",")[0];
                	distance = Integer.parseInt(stationString.split(",")[1]);
                	
                	Station destStation = new Station(dest_city, distance); // Variable that stores the station obj of the dest_city
                	Station srcStation = new Station(src_city, distance); // Variable that stores the station obj of the src_city
                	List<Station> srcValue = new ArrayList<Station>(); // Variable that stores the value of the src_city key
                	List<Station> destValue = new ArrayList<Station>(); // Variable that stores the value of the dest_city key
                	
                	if(stations.containsKey(src_city) && !((stations.get(src_city)).contains(destStation))) {
                	/* Condition: checks if the map contains the key & if the value doesn't contain the destStation */
                		srcValue = stations.get(src_city);
                		srcValue.add(destStation);
                		stations.put(src_city, srcValue);
                	} if(stations.containsKey(dest_city) && !((stations.get(dest_city)).contains(srcStation))) { 
                	/* Condition: checks if the dest_city is a key in the map as well & if the value doesn't contain the srcStation */
                		destValue = stations.get(dest_city);
                		destValue.add(srcStation);
                		stations.put(dest_city, destValue);
                	} if(!stations.containsKey(src_city)) {
                		srcValue.add(destStation);
                		stations.put(src_city, srcValue);
                	} if(!stations.containsKey(dest_city)) {
                		destValue.add(srcStation);
                		stations.put(dest_city, destValue);
                	}
                }
                lineNum++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		/* Calls findShortestDistance() for "Westside" so that the shortest
		routes will be in reference to Westside. */
	}
	
	/* Method: Calculates the shortest route from “Westside” to every other station following 
	 * the logic given in the “Shortcuts to Victory” section. It populates
	 * the shortest route map. */
	private void findShortestDistance() {
		List<String> stationsKeys = stations.getKeys();
		for(int i = 0; i < stationsKeys.size(); i++) {
			shortRoutes.put(stationsKeys.get(i), new Station("Westside", Integer.MAX_VALUE));
		}
		/* The above initializes the shortRoutes map according to the instructions */
		
		/* Creates a sorted stack of the stations according to their distances */
		List<Integer> stationsValues = stations.getValues();
		for(int j = 0; j < stationsKeys.size(); j++) {
			sortStack(stationsKeys.get(j), toVisit);
		}
	}

	/* Method: Receives a Stack that needs to remain sorted and the station we want to add. */
	public void sortStack(Station station, Stack<Station> stackToSort) {
		// Created algorithm to keep the stack sorted (Reference: "How do we keep the stack sorted?" section)
		stackToSort.push(station);
		
		/* Stacks for comparison */
		Stack<Station> firstStack = new LinkedStack<Station>();
		Stack<Station> secondStack = new LinkedStack<Station>();
		
		secondStack.push(stackToSort.pop());
		
		// Populating secondStack whilst stackToSort has no duplicates
		// Function: Sorts stackToSort into secondStack
		while(!stackToSort.isEmpty()) {
			if(stackToSort.top().getDistance() > secondStack.top().getDistance()) {
				secondStack.push(stackToSort.pop());
			} else {
				while(!secondStack.isEmpty() && stackToSort.top().getDistance() < secondStack.top().getDistance()) {
					firstStack.push(secondStack.pop());
				}
				secondStack.push(stackToSort.pop());
				while(!firstStack.isEmpty()) { // Restore secondStack back with all objs in order
					secondStack.push(firstStack.pop());
				}
			}
		}
		
		// Puts all values in sorted stack into stackToSort
		while(!secondStack.isEmpty()) {
			stackToSort.push(secondStack.pop());
		}
	}
	
	/* Method: Returns a Map where the key is the station name, and the value is 
	 * the time it takes to reach that station. */
	public Map<String, Double> getTravelTimes() {
		// 2.5 minutes per kilometer (use the shortest distance for this)
		// 15 min per station (between Westside and the destination)
		
	}


	public Map<String, List<Station>> getStations() {
		return this.stations;
	}


	public void setStations(Map<String, List<Station>> cities) {
		
	}


	public Map<String, Station> getShortestRoutes() {
		return null;
		
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