package bikescheme;

/**
 * @author Keqi & Jinhong
 *
 */
public class Bike {
	private String ID;
	private boolean rentStatus;

	public Bike(String bikeID) {
		this.ID = bikeID;
		rentStatus = false;
	}
	
	public void rentBike(){
		if (rentStatus == false) rentStatus = true;
		else System.out.println("WTF ARE YOU DOING? WE DONT HAVE THIS IN STOCK.");
	}
	
	
	public void returnBike(){
		if (rentStatus == true) rentStatus = false;
		else System.out.println("WTF ARE YOU DOING? WE ALREADY HAVE IT. ARE YOU SENDING US A BOMB???");
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