package fr.univavignon.courbes.network.groupe20.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Snake;
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
		//Instance de la classe ServerProfileHandler
		ServerProfileHandler profileHandler = new ServerProfileHandler() {
			List<Profile>  profiles = new CopyOnWriteArrayList<Profile>();
			@Override
			public boolean fetchProfile(Profile profile) {
				if(profiles.size() < 2){
					profiles.add(profile);
					return true;
				}
				else
					return false;
			}
		};
		//Instance de la classe Server
		final Server s = new Server();
		//Définition du port du serveur
		s.setPort(1117);
		//Ajout de l'instance ErrorHandler pour l'utiliser coter serveur
		s.setErrorHandler(error);
		//Ajout de l'instance ServerProfileHandler pour l'utiliser coter serveur
		s.setProfileHandler(profileHandler);
		//Lancement du serveur pour accepter des clients
		s.launchServer();
		
		
		/*
		//--------------------------------------Test du profile DEBUT-------------------------------------------------->
		//Création des profiles qui sont dans le serveur
		List<Profile> profiles = new ArrayList<>();
		Profile p1 = new Profile();
		p1.profileId = 0;p1.userName = "serveurProfile1";
		Profile p2 = new Profile();
		p2.profileId = 1;p2.userName = "serveurProfile2";
		//Ajout des profiles à la list qu'on a envoyé
		profiles.add(p1);profiles.add(p2);
		//Envoie de la list des profiles du serveur à tous les clients connéctés aprés 20 seconde
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		s.sendProfiles(profiles);
		System.out.println("Envoier des profils");
		//--------------------------------------Test du profile FIN-------------------------------------------------->
		*/
		
		
		
		/*
		//--------------------------------------Test d'envoie des PointThreshold Début-------------------------------------------------->
		try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true)
					//Envoie des points au clients
					s.sendPointThreshold(15);
			}
		}).start();
		//--------------------------------------Test d'envoie des PointThreshold FIN-------------------------------------------------->
		 */
		
		
		
		 //--------------------------------------Test SendBoard DÉBUT et retreive commande-------------------------------------------------->
		try {Thread.sleep(5000);} catch (InterruptedException e) {e.printStackTrace();}
		 Board board = new Board();
		 Snake s1 = new Snake();
		 
		 s1.playerId = 0;s1.profileId = 1;
		 Snake s2 = new Snake();
		 s2.playerId = 1;s2.profileId = 2;
		 Snake s3 = new Snake();
		 s3.playerId = 2;s3.profileId = 3;//On va toujours avoir NONE parce que le client n'envoie pas les actions de ce joueur
		 
		 Snake [] tabSnake = {s1,s2,s3};
		 board.snakes = tabSnake;
		 
		 board.height = 150;board.width=100;
		 //Envoie du board
		 s.sendBoard(board);
		 System.out.println("Envoie du board");
		 /*
		  * Pour traiter les commandes reçu , on doit connaitre les playerId des joueurs ,
		  * et ma classe Server le connait quand on envoie la  : "board" ,
		  * ça veut dire les joueurs qui participent dans une manche
		  */
		//Réception des commandes envoyer par les joueurs 
		 new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					Map<Integer, Direction> map = s.retrieveCommands();
					for(Iterator i=map.keySet().iterator();i.hasNext();){
			            Object key=i.next();
			            
			            	System.out.println(key + "=" + map.get(key));
			        }
				}
				
			}
		}).start();
		//--------------------------------------Test SendBoard FIN-------------------------------------------------->
	
		
		
	}
	
}
