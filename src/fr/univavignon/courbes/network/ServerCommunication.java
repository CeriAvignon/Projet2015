package fr.univavignon.courbes.network;

/*
 * Courbes
 * Copyright 2015-16 L3 Info UAPV 2015-16
 * 
 * This file is part of Courbes.
 * 
 * Courbes is free software: you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation, 
 * either version 2 of the License, or (at your option) any later version.
 * 
 * Courbes is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Courbes. If not, see <http://www.gnu.org/licenses/>.
 */

import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Round;
import fr.univavignon.courbes.common.UpdateInterface;
import fr.univavignon.courbes.inter.ErrorHandler;
import fr.univavignon.courbes.inter.ServerGameHandler;
import fr.univavignon.courbes.inter.ServerProfileHandler;

/**
 * Ensemble de méthodes permettant à l'Interface Utilisateur côté serveur
 * de communiquer avec l'Interface Utilisateur côté client, via le Moteur
 * Réseau.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public interface ServerCommunication
{	
	/**
     * Renvoie l'adresse IP de ce serveur, que les clients doivent
     * utiliser pour se connecter à lui. Cette valeur n'est pas 
     * modifiable, elle dépend directement du système.
     *
     * @return 
     * 		Une chaîne de caractères qui correspond à l'adresse IP du serveur.
     */
	public String getIp();
	
	/**
     * Renvoie le port utilisé par le serveur pour accepter les connexions
     * de la part des clients. (à préciser <i>avant</i> de se connecter, 
     * bien sûr).
     *
     * @return 
     * 		Un entier qui correspond au port du serveur.
     */
	public int getPort();

	/**
     * Modifie le port utilisé par le serveur pour accepter les connexions
     * de la part des clients. 
     * <br/>
     * Cette valeur est à modifier avant d'utiliser {@link #launchServer}.
     * 
     * @param port
     * 		Le nouveau port du serveur.
     */
	public void setPort(int port);
	
	/**
     * Permet à l'Interface Utilisateur d'indiquer au Moteur Réseau l'objet
     * à utiliser pour prévenir d'une erreur lors de l'exécution.
     * 
     * @param errorHandler
     * 		Un objet implémentant l'interface {@code ErrorHandler}.
     */
	public void setErrorHandler(ErrorHandler errorHandler);
	
	/**
     * Permet à l'Interface Utilisateur d'indiquer au Moteur Réseau l'objet
     * à utiliser pour prévenir d'une modification des joueurs lors de la
     * configuration d'une partie.
     * 
     * @param profileHandler
     * 		Un objet implémentant l'interface {@code ServerProfileHandler}.
     */
	public void setProfileHandler(ServerProfileHandler profileHandler);
	
	/**
     * Permet à l'Interface Utilisateur d'indiquer au Moteur Réseau l'objet
     * à utiliser pour transmettre les données relatives à la partie en cours.
     * 
     * @param gameHandler
     * 		Un objet implémentant l'interface {@code ServerGameHandler}.
     */
	public void setGameHandler(ServerGameHandler gameHandler);

	/**
     * Permet de créer un serveur pour que les clients puissent s'y connecter.
     * <br/>
     * Cette méthode doit être appelée par l'Interface Utilisateur lorsque
     * l'utilisateur décide de créer une partie réseau.
     */
	public void launchServer();

	/**
     * Permet de stopper le serveur et ainsi déconnecter tous les clients.
     * <br/>
     * Cette méthode doit être appelée par l'Interface Utilisateur lorsque
     * l'utilisateur décide d'arrêter une partie réseau en cours.
     */
	public void closeServer();

	/**
	 * Change le nombre de joueurs distants autorisés pour la configuration
	 * en cours. Un réduction du nombre de joueurs peut impliquer d'en déconnecter
	 * certains.
	 * 
	 * @param clientNumber
	 * 		Nouveau nombre de joueurs distants.
	 */
	public void setClientNumber(int clientNumber);
	
	/**
	 * Envoie la liste des profils des joueurs de la manche à tous les 
	 * clients connectés à ce serveur.
	 * <br/>
	 * Cette méthode est invoquée par l'Interface Utilisateur de manière
	 * à ce que le serveur transmette au client l'identité des joueurs 
	 * participant à une partie.
	 * 
	 * @param profiles
	 * 		Tableau <i>à jour</i> des profils participants à la partie. Les emplacements
	 * 		vides sont représentés par des valeurs {@code null}.
	 */
	public void sendProfiles(Profile[] profiles);
	
	/**
	 * Envoie la limite de points à atteindre pour gagner la partie,
	 * à tous les clients connectés à ce serveur.
	 * 
	 * @param pointThreshold
	 * 		Limite de point courante de la partie.
	 */
	public void sendPointThreshold(int pointThreshold);

	/**
     * Permet au serveur d'envoyer des informations sur l'évolution de 
     * la manche en cours, à tous les clients connectés au serveur.
     * 
     * @param updateData
     * 		Etat courant de l'aire de jeu.
     */
	public void sendUpdate(UpdateInterface updateData);
	
	/**
	 * Indique que la manche est sur le point de démarrer, cloturant
	 * ainsi la phase de configuration du jeu.
	 * 
	 * @param round
	 * 		L'objet représentant la partie qui va commencer.
	 */
	public void sendRound(Round round);
	
	/**
	 * Fait le nécessaire pour déconnecter le client correspondant
	 * à l'index passé en paramètre.
	 * 
	 * @param index
	 * 		Index du client à déconnecter.
	 */
	public void kickClient(int index);
	
	/**
     * Permet au serveur de recevoir les commandes envoyés par les clients. La méthode
     * renvoie un tableau de directions (une par client), ou null si rien n'a été reçu. 
     *
     * @return 
     * 		Un tableau contenant les directions choisies par chaque joueur traité par
     * 		un client. Si un client ne renvoie rien, les valeurs manquantes doivent
     * 		être remplacées par des valeurs {@link Direction#NONE}.
     */
	public Direction[] retrieveCommands();
	
	/**
	 * Effectue les opérations nécessaires à la préparatin du Moteur Réseau
	 * pour la manche suivante (par ex. vider des buffers, etc.).
	 */
	public void finalizeRound();
}
