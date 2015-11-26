/**
 * 
 */
package bikescheme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 *  
 * Docking Station.
 * 
 * @author pbj
 *
 */
public class DStation implements StartRegObserver {
    public static final Logger logger = Logger.getLogger("bikescheme");

    private String instanceName;
    private int eastPos;
    private int northPos;
    
    private DSTouchScreen touchScreen;
    private CardReader cardReader; 
    private KeyIssuer keyIssuer;
    private List<DPoint> dockingPoints;
    private KeyReader keyReader;
 
    /**
     * 
     * Construct a Docking Station object with touch screen, card reader
     * and key issuer interface devices and a connection to a number of
     * docking points.
     * 
     * If the instance name is <foo>, then the Docking Points are named
     * <foo>.1 ... <foo>.<numPoints> . 
     * 
     * @param instanceName
     */
    public DStation(
            String instanceName,
            int eastPos,
            int northPos,
            int numPoints) {
        
     // Construct and make connections with interface devices
        
        this.instanceName = instanceName;
        this.eastPos = eastPos;
        this.northPos = northPos;
        
        touchScreen = new DSTouchScreen(instanceName + ".ts");
        touchScreen.setObserver(this);
        
        cardReader = new CardReader(instanceName + ".cr");
        
        keyIssuer = new KeyIssuer(instanceName + ".ki");
        
        keyReader = new KeyReader(instanceName + ".kr");
        
        dockingPoints = new ArrayList<DPoint>();
        
        for (int i = 1; i <= numPoints; i++) {
            DPoint dp = new DPoint(instanceName + "." + i, i - 1);
            dockingPoints.add(dp);
        }
    }
       
    void setDistributor(EventDistributor d) {
        touchScreen.addDistributorLinks(d); 
        cardReader.addDistributorLinks(d);
        keyReader.addDistributorLinks(d);
        for (DPoint dp : dockingPoints) {
            dp.setDistributor(d);
        }
    }
    
    void setCollector(EventCollector c) {
        touchScreen.setCollector(c);
        cardReader.setCollector(c);
        keyIssuer.setCollector(c);
        for (DPoint dp : dockingPoints) {
            dp.setCollector(c);
        }
    }
    
    /** 
     * Dummy implementation of docking station functionality for 
     * "register user" use case.
     * 
     * Method called on docking station receiving a "start registration"
     * triggering input event at the touch screen.
     * 
     * @param personalInfo
     */
    public void startRegReceived(String personalInfo) {
        logger.fine("Starting on instance " + getInstanceName());
        
        cardReader.requestCard();  // Generate output event
        logger.fine("At position 1 on instance " + getInstanceName());
        
        cardReader.checkCard();    // Pull in non-triggering input event
        logger.fine("At position 2 on instance " + getInstanceName());
        
        keyIssuer.issueKey(); // Generate output event
        
    }
    
    public void viewActivityReceived(String keyID){
        logger.fine("JINHONGLU"+getInstanceName());
        String[] userInfoArray = 
                // "HireTime"  "HireDS" "ReturnDS" "RentTime"
            {  "08:00",      "A",  "A",  "80"};

        List<String> userInfoData = Arrays.asList(userInfoArray);
        touchScreen.showUserActivity(userInfoData);;
        
    }
    
    public String getInstanceName() {
        return instanceName;
    }
    
    public int getEastPos() {
        return eastPos;
    }
    
    public int getNorthPos() {
        return northPos;
    }
    
    //Return number of DPoints Modified
    public int getNoDPoints(){
        return dockingPoints.size();
    }
    
    //Return number of occupied DPoints Modified
    public int getOccupiedDPoints(){
        int occupiedNumber = 0;
        for (DPoint p : dockingPoints){
            if (p.isOccupied()) occupiedNumber++;
        }
        
        return occupiedNumber;
    }
    
    public void setPointsObserver(BikeRentObserver a, BikeReturnObserver o){
        for (DPoint p : dockingPoints){
            p.setRentObserver(a);
            p.setReturnObserver(o);
        }
    }
    
 

}
