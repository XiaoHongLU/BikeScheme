package bikescheme;

public class User {

	private String userAccount;
	private String keyNumber;
	private String card;
	
	public User(String userAccount, String keyNumber, String card) {
	    this.userAccount = userAccount;
	    this.keyNumber = keyNumber;
	    this.card = card;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
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
