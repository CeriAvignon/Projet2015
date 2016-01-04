package fr.univavignon.courbes.userInterface;


public class Player {

	private String email, timeZone, country, userName, password;
	
	public Player(){	
	}
	
	public void Player(String email, String country, String userName, String password, String timeZone){
		this.email = email;
		this.timeZone = timeZone;
		this.country = country;
		this.userName = userName;
		this.password = password;
		System.out.println(email);
	}
	
	
	//Getters
	public String getEmail() { return email; }
	public String getTimeZone() { return timeZone; }
	public String getCountry() { return country; }
	public String getUserName() { return userName; }
	public String getPassword() { return password; }
	
	
	//Setters
	public void setEmail(String email){this.email = email;}
	public void setTimeZone(String timeZone){this.timeZone = timeZone;}
	public void setCountry(String country){this.country = country;}
	public void setUserName(String userName){this.userName = userName;}
	public void setPassword(String password){this.password = password;}
	
	
	public void sendProfil(){
		
	}
	
}

