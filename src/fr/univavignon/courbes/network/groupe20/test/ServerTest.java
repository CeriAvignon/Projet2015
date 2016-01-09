package fr.univavignon.courbes.network.groupe20.test;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ErrorHandler;
import fr.univavignon.courbes.inter.ServerProfileHandler;
import fr.univavignon.courbes.network.groupe20.server.Server;

public class ServerTest {
	public static void main(String[] args) {
		//Instance de classe ErrorHandler créer par IU
		ErrorHandler error = new ErrorHandler() {
			@Override
			public void displayError(String errorMessage) {
				System.out.println(errorMessage);
			}
		};
		//Instance de la classe Server ServerProfileHandler
		ServerProfileHandler profileHandler = new ServerProfileHandler() {
			List<Profile>  profiles = new CopyOnWriteArrayList<Profile>();
			@Override
			public boolean fetchProfile(Profile profile) {
				if(profiles.size() >= 3){
					profiles.add(profile);
					return true;
				}
				else
					return false;
			}
		};
		//Instance de la classe Server
		Server s = new Server();
		//Définition du port du serveur
		s.setPort(1117);
		//Ajout de l'instance ErrorHandler pour l'utiliser coter serveur
		s.setErrorHandler(error);
		//Ajout de l'instance ServerProfileHandler pour l'utiliser coter serveur
		s.setProfileHandler(profileHandler);
		//Lancement du serveur pour accepter des clients
		s.launchServer();
		
		
		
		
		
		
	}
}
