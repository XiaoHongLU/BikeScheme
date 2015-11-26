package bikescheme;

public class Bike {
    private String ID;
    private boolean rentStatus;

    public Bike(String bikeID) {
        this.ID = bikeID;
        rentStatus = false;
    }

    public void rentBike() {
        if (rentStatus == false)
            rentStatus = true;
        else
            System.out.println("WE DONT HAVE THIS BIKE IN STOCK.");
    }

    public void returnBike() {
        if (rentStatus == true)
            rentStatus = false;
        else
            System.out.println("ALREADY INSTOCK.");
    }

    public boolean isRentStatus() {
        return rentStatus;
    }

    public void setRentStatus(boolean rentStatus) {
        this.rentStatus = rentStatus;
    }

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

}