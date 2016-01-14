package fr.univavignon.courbes.userInterface;


/**
* Classe récupérant les informations du profil d'un utilisateur
* @author Florian DEMOL - Alexis MASSIA
*/


public class Profile {

	public static String email;
	public static String timeZone;
	public static String country;
	public static String userName;
	public static String password;
	public static int id;
	
	
	/**
	 * Méthode récupérant les informations de l'utilisateur connecté
	 * @param _email = email de l'utilisateur
	 * @param _country = pays de l'utilisateur
	 * @param _userName = pseudo de l'utilisateur
	 * @param _password = mot de passe de l'utilisateur
	 * @param _timeZone = l'heure
	 * @param _id = son id
	 */
	
	public void Player(String _email, String _country, String _userName, String _password, String _timeZone, int _id){
		email = _email;
		timeZone = _timeZone;
		country = _country;
		userName = _userName;
		password = _password;
		id = _id;
	}
	
	
	public void sendProfil(){
		
	}
	
}
