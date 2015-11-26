package bikescheme;


import java.util.Date;

public class Trip {
    private String startStation, endStation;
    private Date start;
    private int length;

    public Trip(String station, Date start){
        this.startStation = station;
        this.start = start;
    }
    
    public void endTrip(String station, Date end){
        this.endStation = station;
        this.length = Clock.minutesBetween(start, end);
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
    
    
}
