package fr.univavignon.courbes.network.groupe20.test;

import java.util.List;

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
		
		Client c = new Client();
		//Définition dl'adresse du serveur et le port
		c.setIp("localhost");c.setPort(1117);
		c.setErrorHandler(error);
		c.setProfileHandler(profileHandler);
		//Lancement du client
		c.launchClient();
		//---------------Test du profile début------------->
		//Création du profil à envoyer
		Profile p = new Profile();
		p.profileId = 4;p.userName = "test";
		//-------------------Gestion de profile DÉBUT----------------------//
		//Ajouter un profile
		boolean b = c.addProfile(p);
		if(b == true)
			System.out.println("Profile accépter : "+p.profileId);
		else
			System.out.println("Profile refuser : "+p.profileId);
		//Supprimer un profil
		//sc.removeProfile(p);
		
		//-------------------Gestion de profile FIN------------------------//
		
		
	}
}
