package fr.univavignon.courbes.network.groupe20.test;

import java.util.List;

import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ClientProfileHandler;
import fr.univavignon.courbes.inter.ErrorHandler;
import fr.univavignon.courbes.network.groupe20.client.Client;

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
				for(Profile p : profiles)
					System.out.println(p.profileId+" "+p.userName);
			}
		};
		
		//Instance de la classe client
		Client c = new Client();
		//Définition dl'adresse du serveur et le port
		c.setIp("localhost");c.setPort(1117);
		//Lancement du client
		c.launchClient();
		//---------------Test du profile début------------->
		//Création du profil à envoyer
		Profile p = new Profile();
		p.profileId = 1;p.userName = "test";
		//-------------------Gestion de profile DÉBUT----------------------//
		//Ajouter un profile
		boolean b = c.addProfile(p);
		if(b == true)
			System.out.println("Profile accépter : "+p.profileId);
		else
			System.out.println("Profile refuser : "+p.profileId);
		//Supprimer un profil
		c.removeProfile(p);
		//-------------------Gestion de profile FIN------------------------//
		
		
	}
}
