package bikescheme;

public class User {

	private String name;
	private String keyNumber;
	private String card1;
	
	public User(String name, String keyNumber,String card) {
		this.name = name;
		this.keyNumber = keyNumber;
		this.card1 = card;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKeyNumber() {
		return keyNumber;
	}
	public void setKeyNumber(String keyNumber) {
		this.keyNumber = keyNumber;
	}
	public String getCard() {
		return card;
	}
	public void setCard(String card) {
		this.card = card;
	}
	
	
}
