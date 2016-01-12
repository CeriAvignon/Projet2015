package fr.univavignon.courbes.network.groupe20.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ClientProfileHandler;
import fr.univavignon.courbes.inter.ErrorHandler;
import fr.univavignon.courbes.network.groupe20.client.Client;
/*
 * Cette classe permet à l'interface UI de savoir comment les fonctions côter Client fonctionnent
 *
 */
public class ClientTest {
	public static void main(String[] args) {
		//Instance de classe ErrorHandler créer par IU
		ErrorHandler error = new ErrorHandler() {
			@Override
			public void displayError(String errorMessage) {
				System.out.println(errorMessage);
			}
		};
		
		//Instance de la classe Server ServerProfileHandler
		ClientProfileHandler profileHandler = new ClientProfileHandler() {
			@Override
			public void updateProfiles(List<Profile> profiles) {
				System.out.println("=====================Affichage est dans la méthode updateProfiles() dans la classe de type ClientProfileHandler IU==========================");
				System.out.println("la méthode updateProfiles() : est exsécuté lors de l'envoie des profiles côtés Serveur");
				System.out.println("les joueurs qui participent : ");
				for(Profile p : profiles)
					System.out.println(p.profileId+" "+p.userName);
				System.out.println("============================================================================================================================================");
			}
		};
		
		final Client c = new Client();
		//Définition dl'adresse du serveur et le port
		c.setIp("localhost");c.setPort(1117);
		c.setErrorHandler(error);
		c.setProfileHandler(profileHandler);
		//Lancement du client
		c.launchClient();
		//--------------------------------------Test du profile DEBUT-------------------------------------------------->
		//Création des profiles
		Profile p1 = new Profile();
		p1.profileId = 6;p1.userName = "test1";
		Profile p2 = new Profile();
		p2.profileId = 7;p2.userName = "test2";
		/*
		//-------------------Gestion de profile DÉBUT----------------------//
		//Ajouter un profile
		boolean b = c.addProfile(p1);
		if(b == true)
			System.out.println("Profile accépter : "+p1.profileId);
		else
			System.out.println("Profile refuser : "+p1.profileId);
		//Envoie du profil
		b = c.addProfile(p2);
		if(b == true)
			System.out.println("Profile accépter : "+p2.profileId);
		else
			System.out.println("Profile refuser : "+p2.profileId);
		//Supprimer un profil
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.removeProfile(p2);//Suppression du profil p2
		System.out.println("supprimé");
		
		//--------------------------------------Gestion de profil FIN-------------------------------------------------->
		*/
		//--------------------------------------Test de récupération des PointThreshold Début-------------------------------------------------->
		/*
		
		//Attente  de 5 seconde avant la réception du point envoyer du serveur , si on le reçoit on l'affiche si non null 
		try {Thread.sleep(5000);} catch (InterruptedException e) {e.printStackTrace();}
		System.out.println("Point : "+c.retrievePointThreshold());
		//--------------------------------------Test de récupération des PointThreshold FIN-------------------------------------------------->
		 */
		/*
		//--------------------------------------Test Réception de l'envoie du boards Début-------------------------------------------------->
		try {Thread.sleep(10000);} catch (InterruptedException e) {e.printStackTrace();}
		Board board= c.retrieveBoard();
			if(board != null){
			System.out.println("Snakes reçu -----> ");
			for(int i = 0;i<board.snakes.length;i++)
				System.out.print(board.snakes[i].playerId+" ");
			System.out.println("");	
			System.out.println("Dimension -----> ");	
			System.out.print("Height : "+board.height+" ");	
			System.out.println("Width: "+board.width);	
		}else
			System.out.println("rien reçu");
			//--------------------------------------Test Réception de l'envoie du boards FIN-------------------------------------------------->
	*/
		Map<Integer, Direction> commande = new HashMap<Integer, Direction>();
		while(true){
			commande.put(0, Direction.RIGHT);
			commande.put(1, Direction.LEFT);
			c.sendCommands(commande);
		}
	}
}
