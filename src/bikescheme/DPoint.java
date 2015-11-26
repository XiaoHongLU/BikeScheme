/**
 * 
 */
package bikescheme;

import java.util.logging.Logger;

/**
 *  
 * Docking Point for a Docking Station.
 * 
 * @author pbj
 *
 */
public class DPoint implements KeyInsertionObserver,BikeDockingObserver {
    public static final Logger logger = Logger.getLogger("bikescheme");

    private KeyReader keyReader; 
    private OKLight okLight;
    private BikeSensor bikeSensor;
    private BikeLock bikeLock;
    private String instanceName;
    private int index;
    private String stationName;
    //This status indicates if it is occupied;
    private boolean occupiedStatus;
    private BikeReturnObserver returnObserver;
    private BikeRentObserver rentObserver;
    private String bikeID;
 
    /**
     * 
     * Construct a Docking Point object with a key reader and green ok light
     * interface devices.
     * 
     * @param instanceName a globally unique name
     * @param index of reference to this docking point  in owning DStation's
     *  list of its docking points.
     */
    
    public DPoint(String instanceName, int index) {

     // Construct and make connections with interface devices
        
        keyReader = new KeyReader(instanceName + ".kr");
        keyReader.setObserver(this);
        okLight = new OKLight(instanceName + ".ok");
        // NEW PARAMETERS
        bikeSensor = new BikeSensor(instanceName + ".bs");
        bikeSensor.setObserver(this);
        bikeLock = new BikeLock (instanceName + ".bl");
        occupiedStatus = false;
        
        this.instanceName = instanceName;
        this.index = index;
    }
     //q23  
    public void setDSName(String s){
        this.stationName = s;
    }
    
    public void setDistributor(EventDistributor d) {
        bikeSensor.addDistributorLinks(d);
        keyReader.addDistributorLinks(d); 
    }
    
    public void setCollector(EventCollector c) {
        bikeLock.setCollector(c);
        okLight.setCollector(c);
    }
    
    public String getInstanceName() {
        return instanceName;
    }
    public int getIndex() {
        return index;
    }
    
    public void setReturnObserver (BikeReturnObserver o) {
        returnObserver = o;
    }
    
    public void setRentObserver (BikeRentObserver o) {
        rentObserver = o;
    }
    /** 
     * Implementation of docking point functionality on key insertion.
     * 
     * Key Insertion triggers unlocking the bike and flashes the light.
     */
    public void keyInserted(String keyId) {
        logger.fine(getInstanceName());
        bikeLock.unlock();
        okLight.flash();
        logger.fine("123");
        rentObserver.bikeRent(keyId, bikeID, stationName, index);
    }
    
    /**
     * Hello
     */
    public void bikeDocked(String bikeID){
        logger.fine(getInstanceName());
        okLight.flash();
        bikeLock.lock();
        this.bikeID = bikeID;
        returnObserver.bikeReturn(bikeID, stationName, index);
    }
    
    /**
     * Occupy()
     */
    public void occupy(){
        if (this.occupiedStatus == false)
            this.occupiedStatus = true;
        else { System.out.println("WARNING: ALREADY OCCUPIED.");};
    }
    /**
     * If occupied return True else return False.
     * 
     */
    public boolean isOccupied(){
        return this.occupiedStatus;
    }
 

}
