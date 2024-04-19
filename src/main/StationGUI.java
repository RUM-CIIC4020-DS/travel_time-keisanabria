package main;

import javax.swing.*;
import java.awt.*;

import data_structures.*;
import main.*;

import interfaces.List;
import interfaces.Map;

/**
 * Class that creates a GUI that showcases the stations to 
 * depart from and their respective arrival times. Additionally, 
 * it creates an interactive multiple choice bar, which when clicked 
 * shows the stations that can be visited and depending on which station the users click, 
 * the routes that have to be taken to arrive to the said station 
 * are shown on screen.
 * <p>
 * The algorithm efficiently utilizes data structures like `HashTableSC`, 
 * `ArrayList`, `JComboBox`, `JTextArea`, and `JScrollPane`. These structures 
 * offer quick access, dynamic resizing, and smooth user interaction, ensuring 
 * efficient storage, retrieval, and presentation of station-related information 
 * in the graphical user interface (GUI).
 */
public class StationGUI extends JFrame {
    private TrainStationManager manager;
    
    /**
     * Constructor that accesses TrainStationManager with the desired stationFile 
     * and uses the information inside this file to create the GUI.
     * <p>
     * @param stationFile that will be used to initialize the GUI
     */
    public StationGUI(String stationFile) {
        manager = new TrainStationManager(stationFile);
        initializeGUI();
    }
    
    /**
     * Method that sets up the GUI for displaying train station information.
     * It first configures the main frame by setting the title, size, 
     * and default close operation. Then, it creates a main panel with a border 
     * layout to organize the components. Within the main panel, 
     * it creates a text area to display station information, including departure and 
     * arrival times. This information is obtained from the `manager` object, which is 
     * an instance of `TrainStationManager`. The departure and arrival times are retrieved 
     * using the `getDepartureTime()` and `getArrivalTime()` methods, respectively.
     * <p>
     * Additionally, a combo box is created to allow users to select a station 
     * and view its route. When a station is selected, the `actionPerformed` method 
     * is triggered, displaying a message dialog with the route obtained from the 
     * `traceRoute()` method. 
     * <p>
     * Finally, the main panel is added to the frame, and the GUI is set to visible.
     * <p>
     * The algorithm efficiently utilizes Swing GUI components such as `JPanel`, `JTextArea`, 
     * `JScrollPane`, and `JComboBox` for displaying station information and route selection. 
     * Specifically, a `JTextArea` is used to display station information with a scrollable pane, 
     * enabling the efficient presentation of potentially large amounts of text. Additionally, 
     * a `JComboBox` allows users to select stations from a dropdown menu, providing a convenient 
     * and space-efficient interface for route selection.
     */
    private void initializeGUI() {
        setTitle("Train Station Information");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Display station information
        JTextArea stationInfoArea = new JTextArea();
        stationInfoArea.setEditable(false);
        stationInfoArea.append("Station Departure Arrival\n");
        interfaces.Map<String, Double> travelTimes = manager.getTravelTimes();
        for (String station : travelTimes.getKeys()) {
            if (!station.equals("Westside")) { // Exclude "Westside"
                String departureTime = getDepartureTime(station);
                String arrivalTime = getArrivalTime(station, travelTimes.get(station));
                stationInfoArea.append(station + " " + departureTime + " " + arrivalTime + "\n");
            }
        }

        JScrollPane scrollPane = new JScrollPane(stationInfoArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Display route for selected station
        JComboBox<String> stationComboBox = new JComboBox<>();
        for (String station : manager.getShortestRoutes().getKeys()) {
                stationComboBox.addItem(station);
        }
        stationComboBox.addActionListener(e -> {
            String selectedStation = (String) stationComboBox.getSelectedItem();
            String route = manager.traceRoute(selectedStation);
            JOptionPane.showMessageDialog(this, "Route: " + route);
        });

        mainPanel.add(stationComboBox, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }
    
    /**
     * Method that shows the time to depart from a given station.
     * <p>
     * The algorithm efficiently utilizes a `HashTableSC` data structure to store departure 
     * times for various stations. This type of hash table provides constant-time average-case 
     * performance for basic operations like insertion and retrieval, ensuring efficient storage
     * and retrieval of departure times based on station names.
     * <p>
     * @param station to get the departure time from
     * @return time at which the train will depart from the given station (in HH:MM PM/AM format)
     */
    private String getDepartureTime(String station) {
        
        Map<String, String> departureTimes = new HashTableSC<String, String>(1, new SimpleHashFunction<String>());
        /* The following will initialize departureTimes with its keys & values */
        departureTimes.put("Bugapest", "9:35am");
        departureTimes.put("Dubay", "10:30am");
        departureTimes.put("Berlint", "8:25pm");
        departureTimes.put("Mosbull", "6:00pm");
        departureTimes.put("Cayro", "6:40am");
        departureTimes.put("Bostin", "10:25am");
        departureTimes.put("Los Angelos", "12:30pm");
        departureTimes.put("Dome", "1:30pm");
        departureTimes.put("Takyo", "3:35pm");
        departureTimes.put("Unstabul", "4:45pm");
        departureTimes.put("Chicargo", "7:25am");
        departureTimes.put("Loondun", "2:00pm");
        
        // Check if the station exists in the departure times map
        if (departureTimes.containsKey(station)) {
            // If the station exists, return its departure time
            return departureTimes.get(station);
        } else {
            // If the station is not found, return a default departure time
            return "N/A";
        }
    }
    
    /**
     * Method that shows the calculated time to arrive at the given station. It sums up
     * the travel time and the departure time to obtain the value, whilst converting
     * it from minutes to hours for the purpose of fulfilling the format of AM/PM.
     * <p>
     * The algorithm efficiently uses basic string manipulation and arithmetic 
     * operations to calculate the arrival time. It leverages the simplicity 
     * and flexibility of string splitting and integer manipulation to extract 
     * and process the departure time components. By directly computing the arrival 
     * time in minutes and adjusting it based on the departure period, it avoids 
     * unnecessary data structure overhead, resulting in a streamlined and efficient 
     * implementation.
     * <p>
     * @param station that the method will calculate the arrival time for
     * @param travelTime represents the amount of minutes it takes
     * to get there from the starting point (Westside)
     * @return time at which the user will arrive to the given station (in HH:MM PM/AM format)
     */
    private String getArrivalTime(String station, double travelTime) {
        String departureTime = getDepartureTime(station);

        // Splitting the departure time string to extract hours, minutes, and period (AM/PM)
        String[] timeParts = departureTime.split(":");
        int departureHours = Integer.parseInt(timeParts[0]);
        int departureMinutes = Integer.parseInt(timeParts[1].substring(0, 2)); // Extracting minutes
        String departurePeriod = timeParts[1].substring(2); // Extracting AM/PM 

        // Calculating the departure time in minutes
        int departureInMinutes = departureHours * 60 + departureMinutes;

        // Calculating the arrival time in minutes
        int arrivalInMinutes = departureInMinutes + (int) (travelTime);

        // Adjusting arrival hours and period if necessary
        int arrivalHours = arrivalInMinutes / 60;
        int arrivalMinutes = arrivalInMinutes % 60;
        String arrivalPeriod = departurePeriod; // Default to departure period

        if (arrivalHours >= 12) {
            arrivalPeriod = "pm"; // After 12:00 PM
            if (arrivalHours > 12) {
                arrivalHours %= 12; // Convert to 12-hour format
            }
        } else if (arrivalHours == 0) {
            arrivalHours = 12; // 12:00 AM
        } else {
            arrivalPeriod = "am"; // Before 12:00 PM
        }

        // Constructing the arrival time string
        return arrivalHours + ":" + (arrivalMinutes < 10 ? "0" : "") + arrivalMinutes + "" + arrivalPeriod;
    }
    
    /**
     * Main method to execute the GUI.
     * <p>
     * @param args represents an array of strings 
     * that holds the command-line arguments passed 
     * to the program when it is executed.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StationGUI("stations.csv"));
    }
}
