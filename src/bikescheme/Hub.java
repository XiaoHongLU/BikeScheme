/**
 * 
 */
package bikescheme;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.ArrayList;

/**
 * 
 * Hub system.
 *
 * 
 * @author pbj
 *
 */
public class Hub implements AddDStationObserver, BikeRentObserver, BikeReturnObserver {
    public static final Logger logger = Logger.getLogger("bikescheme");

    private HubTerminal terminal;
    private HubDisplay display;
    private Map<String, DStation> dockingStationMap;
    // Create a map that links user to a specific key <String keyID, User>
    private Map<String, User> keyToUserMap;
    private Map<String, String> bikeToKeyMap;
    // Create a map that links a bike to a specific key <String bikeID, String keyID>
    private Map<String, Bike> bikeMap;

    /**
     * 
     * Construct a hub system with an operator terminal, a wall display and
     * connections to a number of docking stations (initially 0).
     * 
     * Schedule update of the hub wall display every 5 minutes with docking
     * station occupancy data.
     * 
     * @param instanceName
     */
    public Hub() {

        // Construct and make connections with interface devices
        terminal = new HubTerminal("ht");
        terminal.setObserver(this);
        display = new HubDisplay("hd");
        dockingStationMap = new HashMap<String, DStation>();
        bikeMap = new HashMap<String, Bike>();
        bikeToKeyMap = new HashMap<String, String>();
        keyToUserMap = new HashMap<String, User>();
        
        
        
        // Schedule timed notification for generating updates of
        // hub display.
        
        
        

        // The idiom of an anonymous class is used here, to make it easy
        // for hub code to process multiple timed notification, if needed.

        Clock.getInstance().scheduleNotification(new TimedNotificationObserver() {

            /**
             * Generate display of station occupancy data.
             */
            @Override
            public void processTimedNotification() {
                logger.fine("");
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                ArrayList<String> occupancyList = new ArrayList<String>();
                // "DSName","East","North","Status","#Occupied","#DPoints"
                for (DStation a : dockingStationMap.values()) {
                    float occupancy = (float) (a.getOccupiedDPoints() / a.getNoDPoints());
                    String status;
                    if ((occupancy >= 0.85) || (occupancy <= 0.15)) {
                        if (occupancy >= 0.85)
                            status = "HIGH";
                        else
                            status = "LOW";
                        occupancyList.add(a.getInstanceName());
                        occupancyList.add(Integer.toString(a.getEastPos()));
                        occupancyList.add(Integer.toString(a.getNorthPos()));
                        occupancyList.add(status);
                        occupancyList.add(Integer.toString(a.getOccupiedDPoints()));
                        occupancyList.add(Integer.toString(a.getNoDPoints()));
                    }
                }
                ;
                display.showOccupancy(occupancyList);
            }

        }, Clock.getStartDate(), 0, 5);

    }

    public void setDistributor(EventDistributor d) {

        // The clock device is connected to the EventDistributor here, even
        // though the clock object is not constructed here,
        // as no distributor is available to the Clock constructor.
        Clock.getInstance().addDistributorLinks(d);
        terminal.addDistributorLinks(d);
    }

    public void setCollector(EventCollector c) {
        display.setCollector(c);
        terminal.setCollector(c);
    }

    /**
     * 
     */
    @Override
    public void addDStation(String instanceName, int eastPos, int northPos, int numPoints) {
        logger.fine("");

        DStation newDStation = new DStation(instanceName, eastPos, northPos, numPoints);
        dockingStationMap.put(instanceName, newDStation);

        // Now connect up DStation to event distributor and collector.

        EventDistributor d = terminal.getDistributor();
        EventCollector c = display.getCollector();

        newDStation.setDistributor(d);
        newDStation.setCollector(c);
        newDStation.setPointsObserver(this, this);
        
    }
    
    /**
     * 
     * @param bikeID
     */
    @Override
    public void bikeRent(String keyID, String bikeID, String DStationName, int DPointNo){
        logger.fine("");
        bikeToKeyMap.put(bikeID, keyID);
        bikeMap.get(bikeID).rentBike();
    }
    
    /**
     * 
     */
    @Override
    public void bikeReturn(String bikeID, String DStationName, int DPointNo){
        logger.fine("");
        if (!this.bikeMap.containsKey(bikeID)) {
            Bike newBike = new Bike(bikeID);
            bikeMap.put(bikeID, newBike);
        } else {
            bikeMap.get(bikeID).returnBike();
            bikeToKeyMap.remove(bikeID);
        }
    }

    public DStation getDStation(String instanceName) {
        return dockingStationMap.get(instanceName);
    }

}
