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

/**
 * Class that manipulates two maps, one (shortRoutes) that handles the shortest 
 * distances between stations according to Westside and another (stations) that
 * shows how each station is interconnected. Additionally, it shows the amount of time
 * it takes to get to these stations from Westside. Other than that, it has a method
 * that helps with the sorting of a stack.
 * <p>
 * The combination of HashTableSC and ArrayList data structures 
 * ensures efficient storage, retrieval, and manipulation of station 
 * data and shortest route information, making the algorithm well-suited 
 * for handling large datasets and frequent queries.
 */
public class TrainStationManager {
	
	private Map<String, List<Station>> stations = new HashTableSC<>(1, new SimpleHashFunction<>());
	private String[] parts;
	private String src_city;
	private String stationString;
	private String dest_city;
	private int distance;
	private Map<String, Station> shortRoutes = new HashTableSC<>(1, new SimpleHashFunction<>());
	
	/** Constructor: Reads the file given by station_file and populates the stations map 
	 * <p>
	 * Efficiently populates the `stations` map using 
	 * two ArrayLists for storing the stations connected to each city. ArrayLists are efficient 
	 * for this task because they provide constant-time access to elements by index, which is 
	 * essential for quickly accessing and updating the list of connected stations for each city. 
	 * Additionally, ArrayLists dynamically resize to accommodate any number of stations, 
	 * ensuring efficient memory utilization without the need for manual resizing. 
	 * By using ArrayLists, the algorithm ensures fast access to station data and dynamic
	 *  scalability to handle varying numbers of stations, making it well-suited for 
	 *  processing large datasets of train station information.
	 * <p>
	 * @param station_file which represents the file from where the information of stations
	 * will be retrieved from.
	 */
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
	
	/** Method: Calculates the shortest route from “Westside” to every other station following 
	 * the logic given in the “Shortcuts to Victory” section. It populates
	 * the shortest route map. 
	 * <p>
	 * The algorithm uses three main data structures: a map (`shortRoutes`), a stack (`toVisit`), 
	 * and a set (`visited`). The `shortRoutes` map is efficiently initialized by 
	 * iterating through the keys of the `stations` map and setting the value for 
	 * each key to a new `Station` object with a maximum integer distance, which 
	 * ensures that the shortest route can be updated later. The `toVisit` stack 
	 * is implemented as a linked stack (`LinkedStack`), which efficiently supports 
	 * push and pop operations, making it suitable for maintaining the order of stations 
	 * to visit. The `visited` set, implemented as a hash set (`HashSet`), efficiently 
	 * checks for membership of visited stations, ensuring that stations are not 
	 * revisited unnecessarily. 
	 * <p>
	 * These data structures are efficient for this algorithm because they provide fast
	 *  insertion, retrieval, and membership checking operations, which are essential 
	 *  for finding the shortest distance from "Westside" to every other station. 
	 *  Additionally, the linked stack and hash set implementations offer O(1) time 
	 *  complexity for most operations, ensuring optimal performance.
	 */
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
		
//		List<Station> valuesWestside = this.stations.get("Westside"); // Stores stations connected to the starting point (Westside)
//		
//		for(Station s : valuesWestside) {
//			if(!visited.isMember(s)) { // If the station isn't in the 'visited' set...
//				sortStack(s, toVisit); // ... it adds each station connected to Westside into the toVisit stack
//				
//				/* Initializes the values of the stations connected to the starting point */
//				this.shortRoutes.put(s.getCityName(), new Station("Westside", s.getDistance()));
//			}
//		}
		
		while(!toVisit.isEmpty()) {
			Station currentStation = toVisit.pop(); // Next station to visit
			
			List<Station> neighbors = this.stations.get(currentStation.getCityName()); // Stores that stations' connections
			
			for(Station s : neighbors) { /* Iterates through each 
			* station that is connected to the station that is being visited */
				
				if(s == null) { // Edge case
					break;
				}
				
				if(!visited.isEmpty() && visited.isMember(currentStation)) {
					break;
				}
				
				/* Variable: stores the sum of the distance of variable 's' with the shortest distance 
				 * of currentStation that is already in the shortRoutes */
				int addition = s.getDistance() + this.shortRoutes.get(currentStation.getCityName()).getDistance();
				
				if(addition < this.shortRoutes.get(s.getCityName()).getDistance()) {
					/* Replaces the shortest distance of 's' in shortRoutes 
					 * with a station of city_name being 'currentStation' */
					this.shortRoutes.put(s.getCityName(), new Station(currentStation.getCityName(), addition)); 
				}
				
				/* If the station isn't connected to the starting point and it hasn't been visited, it
				is added to the toVisit stack */
				if(!visited.isMember(s)) {
					sortStack(s, toVisit);
				}
			}
			
			visited.add(currentStation);

		}
	}

	/** Method: Receives a Stack that needs to remain sorted and the station we want to add.
	 * <p>
	 * It is sorted from shortest to longest.
	 * <p>
	 * It efficiently uses a temporary stack 
	 * implemented using a linked list. This approach allows for constant-time 
	 * insertion and deletion operations, ensuring the efficient sorting of 
	 * the stack. By iterating through the original stack once and transferring 
	 * elements back in the correct order after adding the new station,
	 *  the algorithm effectively maintains the sorted order, making it a suitable 
	 *  choice for sorting stacks with minimal overhead. 
	 * <p>
	 * @param station that will be sorted into the given stack
	 * @param stackToSort represents the stack that will be sorted according to the station
	 * to be added to it
	 */
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
	
	/** A method that shows the amount of time that it takes to get to each station in the
	 * 'stations' map.
	 * <p>
	 * The algorithm efficiently calculates travel times between 
	 * stations using a hash table to store the results and lists 
	 * to iterate over the shortest routes. The hash table provides 
	 * fast key-based operations, while the lists ensure ordered traversal 
	 * through the shortest routes for accurate time calculations. By 
	 * combining these data structures, the algorithm achieves both efficiency 
	 * and accuracy in calculating travel times.
	 * <p>
	 * @return a map where the key is the station name, and the value is 
	 * the time it takes to reach that station. 
	 */
	public Map<String, Double> getTravelTimes() {
		// 2.5 minutes per kilometer (use the shortest distance for this)
		// 15 min per station (between Westside and the destination)
		
		Map<String, Double> travelTimes = new HashTableSC<>(1, new SimpleHashFunction<>());
		
		List<Station> shortRoutesVals = shortRoutes.getValues();
		List<String> shortRoutesKeys = shortRoutes.getKeys();
		
		Double mult = 1.0; // Initialization of variable that'll store the multiplication
		for(int i = 0; i < shortRoutesVals.size(); i++) {
			Station s = shortRoutesVals.get(i);
			mult = s.getDistance() * 2.5;
			
			String shortRouteCity = s.getCityName();
			while(!shortRouteCity.equals("Westside")) {
				mult += 15;
				shortRouteCity = shortRoutes.get(shortRouteCity).getCityName();
			}
			travelTimes.put(shortRoutesKeys.get(i), mult);
		}
		
		return travelTimes;
	}
	
	/**
	 * Method that gets the map that represents the connection between each station.
	 * <p>
	 * @return the global map of 'stations'
	 */
	public Map<String, List<Station>> getStations() {
		return this.stations;
	}
	

	/**
	 * Method that sets the map that represents the connection between each station.
	 * <p>
	 * @param cities which represents the map that will replace the current
	 * 'stations' map.
	 */
	public void setStations(Map<String, List<Station>> cities) {
		this.stations = cities;
	}
	
	
	/**
	 * Method that gets the map that represents the 
	 * shortest route from Westside to the other stations.
	 * <p>
	 * @return the global map of 'shortRoutes'
	 */
	public Map<String, Station> getShortestRoutes() {
		return this.shortRoutes;
	}
	

	/**
	 * Method that sets the map that represents the 
	 * shortest route from Westside to the other stations.
	 * <p>
	 * @param shortestRoutes which represents the map that will replace the current
	 * 'shortRoutes' map.
	 */
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
	
	/* CODE BELOW IS SOLELY FOR DEBUGGING PURPOSES! */
	
//    public static void main(String[] args) {
//        TrainStationManager manager = new TrainStationManager("personalTester.csv");
//        
//        // Print stations map
//        System.out.println("Stations Map:");
//        Map<String, List<Station>> stationsMap = manager.getStations();
//        for (String city : stationsMap.getKeys()) {
//            System.out.println(city + ": " + stationsMap.get(city));
//        }
//        
//        // Print shortest routes map
//        System.out.println("\nShortest Routes:");
//        Map<String, Station> shortRoutes = manager.getShortestRoutes();
//        List<String> shortRoutesKeys = shortRoutes.getKeys();
//        for (String key : shortRoutesKeys) {
//            Station station = shortRoutes.get(key);
//            System.out.println(key + ": " + station.getCityName() + " - " + station.getDistance() + " km");
//        }
//        
//        // Print travel times calculations
//        System.out.println("\nCalculating Travel Times:");
//        List<Station> shortRoutesValues = manager.getShortestRoutes().getValues();
//        
//        // Calculate and print travel times
//        Map<String, Double> travelTimes = manager.getTravelTimes();
//        for (int i = 0; i < shortRoutesKeys.size(); i++) {
//            Station station = shortRoutesValues.get(i);
//            double time = 0.0;
//            String stationName = shortRoutesKeys.get(i);
//            int distance = station.getDistance();
//            
//            System.out.print(stationName + ": ");
//            
//            // Calculate time based on distance
//            double timePerKm = 2.5;
//            double timePerStation = 15.0;
//            time = distance * timePerKm;
//            System.out.print("Distance: " + distance + " km, ");
//            
//            // Add additional time for non-Westside stations
//            if (!stationName.equals("Westside")) {
//                time += timePerStation;
//                System.out.print("Additional time: " + timePerStation + " minutes, ");
//            }
//            
//            // Print total travel time for the station
//            System.out.println("Total Travel Time: " + time + " minutes");
//            
//            // Update travel times map
//            travelTimes.put(stationName, time);
//        }
//        
//        // Print travel times map
//        System.out.println("\nTravel Times:");
//        for (String station : travelTimes.getKeys()) {
//            System.out.println(station + ": " + travelTimes.get(station) + " minutes");
//        }
//    }



}