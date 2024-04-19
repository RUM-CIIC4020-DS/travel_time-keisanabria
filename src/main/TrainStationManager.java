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
import data_structures.SinglyLinkedList;
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
		findShortestDistance();
	}
	
	/* Method: Calculates the shortest route from “Westside” to every other station following 
	 * the logic given in the “Shortcuts to Victory” section. It populates
	 * the shortest route map. */
	private void findShortestDistance() {

		/* Initializes the shortRoutes map according to the instructions */
		List<String> stationsKeys = this.stations.getKeys();
		for(String str : stationsKeys) {
			this.shortRoutes.put(str, new Station("Westside", Integer.MAX_VALUE));
		}
		
		Stack<Station> toVisit = new LinkedStack<Station>();
		HashSet<Station> visited = new HashSet<Station>();
		
		/* Initiates "Westside" into the map and toVisit, then 'visited' */
		this.shortRoutes.put("Westside", new Station("Westside", 0));
		sortStack(new Station("Westside", 0), toVisit);
		visited.add(new Station("Westside", 0));
		
		List<Station> valuesWestside = this.stations.get("Westside"); // Stores stations connected to the starting point (Westside)
		
		for(Station s : valuesWestside) {
			if(!visited.isMember(s)) { // If the station isn't in the 'visited' set...
				sortStack(s, toVisit); // ... it adds each station connected to Westside into the toVisit stack
				
				/* Initializes the values of the stations connected to the starting point */
				this.shortRoutes.put(s.getCityName(), new Station("Westside", s.getDistance()));
			}
		}
		
		while(!toVisit.isEmpty()) {
			Station temp = toVisit.pop(); // Next station to visit
			
			List<Station> tempValues = this.stations.get(temp.getCityName()); // Stores that stations' connections
			
			for(Station t : tempValues) { /* Iterates through each 
			* station that is connected to the station that is being visited */
				
				if(t == null) {
					break;
				}
				
				/* Variable: stores the sum of t's distance with the shortest distance 
				 * of temp that is already in the shortRoutes */
				int addition = t.getDistance() + this.shortRoutes.get(temp.getCityName()).getDistance();
				
				if(addition < this.shortRoutes.get(t.getCityName()).getDistance()) {
					/* Replaces the shortest distance of 't' in shortRoutes 
					 * with a station of city_name being 'temp' */
					this.shortRoutes.put(t.getCityName(), new Station(temp.getCityName(), addition)); 
				}
				
				/* If the station isn't connected to the starting point and it hasn't been visited, it
				is added to the toVisit stack */
				if(!visited.isMember(t)) {
					sortStack(t, toVisit);
				}
			}
		}
	}

	/* Method: Receives a Stack that needs to remain sorted and the station we want to add. */
	public void sortStack(Station station, Stack<Station> stackToSort) {
		// Created algorithm to keep the stack sorted (Reference: "How do we keep the stack sorted?" section)
	
	    Stack<Station> tempStack = new LinkedStack<Station>();
	    
        // Pop elements from the original stack and push them onto the temporary stack
        // until the original stack is empty or the top element is greater than the station to be added.
        while (!stackToSort.isEmpty() && stackToSort.top().getDistance() < station.getDistance()) {
            tempStack.push(stackToSort.pop());
        }

        // Push the station to be added onto the temporary stack.
        tempStack.push(station);

        // Pop elements from the temporary stack and push them back onto the original stack.
        while (!tempStack.isEmpty()) {
            stackToSort.push(tempStack.pop());
        }
	}
	
	/* Method: Returns a Map where the key is the station name, and the value is 
	 * the time it takes to reach that station. */
	public Map<String, Double> getTravelTimes() {
		// 2.5 minutes per kilometer (use the shortest distance for this)
		// 15 min per station (between Westside and the destination)
		throw new UnsupportedOperationException();
	}


	public Map<String, List<Station>> getStations() {
		return this.stations;
	}


	public void setStations(Map<String, List<Station>> cities) {
		this.stations = cities;
	}


	public Map<String, Station> getShortestRoutes() {
//		findShortestDistance();
		return this.shortRoutes;
	}


	public void setShortestRoutes(Map<String, Station> shortestRoutes) {
		this.shortRoutes = shortestRoutes;
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
	
	// ONLY FOR DEBUGGING - REMOVE THE METHOD BELOW BEFORE SUBMITTING!
	public static void main(String[] args) {
	    TrainStationManager manager = new TrainStationManager("stations.csv");
	    // Assuming you have loaded data into shortRoutes somewhere in your code
	    Map<String, Station> shortRoutes = manager.getShortestRoutes();
	    System.out.println("Shortest Routes:");
	    List<String> shortRoutesKeys = shortRoutes.getKeys();
	    List<Station> shortRoutesValues = shortRoutes.getValues();
	    
	    // Debugging statements
	    System.out.println("Number of keys: " + shortRoutesKeys.size());
	    System.out.println("Number of values: " + shortRoutesValues.size());
	    
	    // Print contents of shortRoutesValues
	    for (Station station : shortRoutesValues) {
	        System.out.println(station);
	    }
	    
	    for (int i = 0; i < shortRoutesKeys.size() ; i++) {
	        Station station = shortRoutesValues.get(i);
	        System.out.println(shortRoutesKeys.get(i) + ": " + station.getCityName() + " - " + station.getDistance());
	    }
	}


}