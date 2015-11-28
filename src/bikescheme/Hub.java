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
public class Hub implements AddDStationObserver, HubInterface {
    public static final Logger logger = Logger.getLogger("bikescheme");

    private HubTerminal terminal;
    private HubDisplay display;
    private Map<String, DStation> dockingStationMap;
    // Create a map that links user to a specific key <String keyID, User user>
    private Map<String, User> keyToUserMap;
    // Create a map that links bike to a specific key <String bikeID, String
    // keyID>
    private Map<String, String> bikeToKeyMap;
    // Create a map that links a bike to a specific key <String bikeID, String
    // keyID>
    private Map<String, Bike> bikeMap;
    // Create an arraylist of fault bike;
    private ArrayList<Bike> faultBikes;

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
        faultBikes = new ArrayList<Bike>();
        
        newUser("STAFF", "admin", "****");

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
                ArrayList<String> occupancyList = showOccupancy(0, 0);
                display.showOccupancy(occupancyList);
            }

        }, Clock.getStartDate(), 0, 5);
        
        Clock.getInstance().scheduleNotification(new TimedNotificationObserver() {
            /**
             * 
             */
            @Override
            public void processTimedNotification() {
                logger.fine("Charging User");
            }
        }, Clock.getStartDate(), 24, 0);

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
    public ArrayList<String> showOccupancy(int east, int north){
    ArrayList<String> occupancyList = new ArrayList<String>();
    // "DSName","East","North","Status","#Occupied","#DPoints"
    for (DStation a : dockingStationMap.values()) {
        float occupancy = ((float) a.getOccupiedDPoints() / a.getNoDPoints());
        String status = "";
            if (occupancy >= 0.85)
                status = "HIGH";
            if (occupancy <= 0.15)
                status = "LOW";
            occupancyList.add(a.getInstanceName());
            occupancyList.add(Integer.toString(a.getEastPos()-east));
            occupancyList.add(Integer.toString(a.getNorthPos()-north));
            occupancyList.add(status);
            occupancyList.add(Integer.toString(a.getOccupiedDPoints()));
            occupancyList.add(Integer.toString(a.getNoDPoints()));
    }
    return occupancyList;
    }
    
    /**
     * 
     * 
     */
    @Override
    public ArrayList<String> findFreePoints(DStation s){
        logger.fine("");
        return showOccupancy(s.getEastPos(), s.getNorthPos());
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

        newDStation.setHub(this);
        ;

    }

    /**
     * 123456789
     * 
     * @param bikeID
     */
    @Override
    public void bikeTakeOut(String keyID, String bikeID, DPoint d) {
        logger.fine("");
        if (this.keyToUserMap.containsKey(keyID)){
            bikeToKeyMap.put(bikeID, keyID);
            logger.fine(bikeID + " is linked to " + bikeToKeyMap.get(bikeID));
            bikeMap.get(bikeID).rentBike();
            keyToUserMap.get(keyID).startTrip(d.getStationName());
        } else if (keyID == "admin") {
            logger.fine(bikeID + " is removed.");
            bikeMap.remove(bikeID);
        } else {
            logger.warning(keyID + " Is Not A Valid Key!");
        }
    }

    /**
     * §1234567890
     */
    public void bikeDocked(String bikeID, DPoint d) {
        logger.fine("");
        if (!this.bikeMap.containsKey(bikeID)) {
            Bike newBike = new Bike(bikeID);
            bikeMap.put(bikeID, newBike);
        } else {
            bikeMap.get(bikeID).returnBike();
            logger.fine(bikeID);
            keyToUserMap.get(bikeToKeyMap.get(bikeID)).endTrip(d.getStationName());
            bikeToKeyMap.remove(bikeID);
        }
    }

    /**
     * §1234567890
     */
    public void newUser(String info, String keyID, String card) {
        User u = new User(info, keyID, card);
        logger.fine(keyID);
        this.keyToUserMap.put(keyID, u);
    }

    /**
     * 123
     * 
     * @param instanceName
     * @return
     */
    public ArrayList<String> userActivity(String keyID) {
        ArrayList<String> ac = new ArrayList<String>();
        User u = this.keyToUserMap.get(keyID);
        logger.fine(keyToUserMap.get(keyID).getKeyNumber());
        for (Trip t : u.getTrips()) {
            ac.add(Clock.format(t.getStart()).split(" ")[1]);
            ac.add(t.getStartStation());
            ac.add(t.getEndStation());
            ac.add(Integer.toString(t.getLength()));
        }
        return ac;
    }

    /**
     * 
     */
    public boolean containsBike(String bikeID) {
        if (bikeMap.containsKey(bikeID))
            return true;
        else
            return false;
    }

    
    /**
     * 
     */
    public void fault(DPoint dp) {
        logger.fine("");
        
        faultBikes.add(bikeMap.get(dp.getBikeID()));
    }
    
    
    /**
     * 
     * @param instanceName
     * @return
     */
    public DStation getDStation(String instanceName) {
        return dockingStationMap.get(instanceName);
    }

}
