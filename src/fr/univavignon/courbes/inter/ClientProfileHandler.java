package fr.univavignon.courbes.inter;

import java.util.List;

import fr.univavignon.courbes.common.Profile;

/**
 * Interface implémentant les méthodes permettant au Moteur Réseau d'envoyer à l'Interface Utilisateur
 * les données relatives à la configuration des joueurs participant à une partie réseau, <i>côté client</i>.
 * <br/>
 * Chaque binôme de la composante Interface Utilisateur doit définir une classe implémentant 
 * cette interface, puis l'instancier. Notez qu'il est possible pour une classe d'implémenter
 * plusieurs interfaces simultanément.
 */
public interface ClientProfileHandler 
{	
	/**
	 * Méthode utilisée <i>côté client</i> par le Moteur Réseau, pour envoyer à l'Interface 
	 * Utilisateur la liste mise à jour des profils des joueurs participant à la partie en 
	 * cours de configuration.
	 * 
	 * @param profiles
	 * 		Liste <i>à jour</i> des profils reçus par le Moteur Réseau (peut être vide si aucun 
	 * 		joueur n'a encore été sélectionné). 
	 */
	public void updateProfiles(List<Profile> profiles);
}