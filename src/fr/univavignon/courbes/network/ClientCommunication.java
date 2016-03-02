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
import fr.univavignon.courbes.common.UpdateInterface;
import fr.univavignon.courbes.inter.ClientConnectionHandler;
import fr.univavignon.courbes.inter.ClientGameHandler;
import fr.univavignon.courbes.inter.ClientProfileHandler;
import fr.univavignon.courbes.inter.ErrorHandler;

/**
 * Ensemble de méthodes permettant à l'Interface Utilisateur côté client
 * de communiquer avec l'Interface Utilisateur côté serveur, via le Moteur
 * Réseau.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public interface ClientCommunication
{	
	/**
     * Renvoie l'adresse IP du serveur auquel le client se connecte
     * (à préciser <i>avant</i> de se connecter, bien sûr).
     *
     * @return 
     * 		Une chaîne de caractères qui correspond à l'adresse IP du serveur.
     */
	public String getIp();

	/**
     * Modifie l'adresse IP du serveur auquel le client va se connecter.
     * <br/>
     * Cette valeur est à modifier avant d'utiliser {@link #launchClient}.
     *
     * @param ip
     * 		La nouvelle IP du serveur.
     */
	public void setIp(String ip);

	/**
     * Renvoie le port du serveur auquel le client se connecte
     * (à préciser <i>avant</i> de se connecter, bien sûr).
     *
     * @return 
     * 		Un entier qui correspond au port du serveur.
     */
	public int getPort();

	/**
     * Modifie le port du serveur auquel le client va se connecter.
     * <br/>
     * Cette valeur est à modifier avant d'utiliser {@link #launchClient}.
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
     * 		Un objet implémentant l'interface {@code ClientProfileHandler}.
     */
	public void setProfileHandler(ClientProfileHandler profileHandler);
	
	/**
     * Permet à l'Interface Utilisateur d'indiquer au Moteur Réseau l'objet
     * à utiliser pour prévenir d'une modification lors de la connexion lors
     * de la configuration d'une partie. 
     * 
     * @param connectionHandler
     * 		Un objet implémentant l'interface {@code ClientProfileHandler}.
     */
	public void setConnectionHandler(ClientConnectionHandler connectionHandler);
	
	/**
     * Permet à l'Interface Utilisateur d'indiquer au Moteur Réseau l'objet
     * à utiliser pour transmettre les données relatives à la partie en cours. 
     * 
     * @param gameHandler
     * 		Un objet implémentant l'interface {@code ClientGameHandler}.
     */
	public void setGameHandler(ClientGameHandler gameHandler);
	
	/**
     * Permet au client de se connecter au serveur dont on a préalablement
     * configuré l'adresse IP et le port. 
     * <br/>
     * Cette méthode doit être appelée par l'Interface Utilisateur lorsque
     * l'utilisateur décide de se connecter à une partie réseau existante.
     * 
     * @return
     * 		{@code true} ssi la connexion a pu être effectuée.
     */
	public boolean launchClient();

 	/**
     * Permet à un client de clore sa connexion avec le serveur.
     */
	public void closeClient();
	
	/**
	 * Indique si le client est actuellement connecté au serveur.
	 * 
	 * @return
	 * 		{@code true} ssi ce client est actuellement connecté au serveur.
	 */
	public boolean isConnected();
	
	/**
-	 * Envoie au serveur le profil d'un joueur désirant participer à la partie
	 * en cours de configuration.
	 *   
	 * @param profile
	 * 		Profil du joueur à ajouter à la partie.
	 */
	public void sendProfile(Profile profile);
	
	/**
	 * Récupère la limite de points à atteindre pour gagner la partie,
	 * limite envoyée par le serveur auquel ce client est connecté.
	 * 
	 * @return pointThreshold
	 * 		Limite de point courante de la partie, ou {@code null} si aucune
	 * 		valeur n'a été envoyée.
	 */
	public Integer retrievePointThreshold();

	/**
     * Permet au client de récupérer des informations sur l'évolution de 
     * la manche en cours, envoyées par le serveur auquel il est connecté.
     * <br/>
     * Cette méthode est appelée par l'Interface Utilisateur à
     * chaque itération d'une manche.
     * 
     * @return
     * 		Etat courant de l'aire de jeu, ou {@code null} si aucune mise à jour
     * 		n'a été envoyée.
     */
	public UpdateInterface retrieveUpdate();
	
	/**
     * Permet au client d'envoyer les commandes générées par les joueurs qu'il gère.
     * Ces commandes sont passées sous forme de map: l'entier correspond à l'ID du joueur
     * <i>sur le serveur</i>, pour la manche en cours, et la direction correspond à la
     * commande générée par le joueur. Si un joueur n'a pas généré de commande, alors la 
     * valeur associée doit être {@link Direction#NONE}.
     *
     * @param command
     * 		Une liste contenant les directions choisies par chaque joueur local au client.
     */
	public void sendCommand(Direction command);
	
	/**
     * Permet au client d'indiquer au serveur qu'il est prêt à commencer la manche.. 
     */
	public void sendAcknowledgment();
	
	/**
	 * Effectue les opérations nécessaires à la préparatin du Moteur Réseau
	 * pour la manche suivante (par ex. vider des buffers, etc.).
	 */
	public void finalizeRound();
}
