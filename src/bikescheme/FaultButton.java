package bikescheme;

public class FaultButton extends AbstractInputDevice{

    private FaultButtonPressedObserver observer;
    
    public FaultButton(String instanceName) {
        super(instanceName);
    }
    
    public void setObserver(FaultButtonPressedObserver o) {
        observer = o;
    }

    @Override
    public void receiveEvent(Event e) {
        
        if (e.getMessageName().equals("dockBike") 
                && e.getMessageArgs().size() == 0) {
            
            faultBike();
            
        } else {
            super.receiveEvent(e);
        }
    }
    
    public void faultBike(){
        logger.fine(getInstanceName());
        
        observer.faultButtonPressed();
    }

}
