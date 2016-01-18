package fr.univavignon.courbes.inter;

import fr.univavignon.courbes.common.Profile;

/**
 * Interface implémentant les méthodes permettant au Moteur Réseau d'envoyer à l'Interface Utilisateur
 * les données relatives à la configuration des joueurs participant à une partie réseau, <i>côté serveur</i>.
 * <br/>
 * Chaque binôme de la composante Interface Utilisateur doit définir une classe implémentant 
 * cette interface, puis l'instancier. Notez qu'il est possible pour une classe d'implémenter
 * plusieurs interfaces simultanément.
 */
public interface ServerProfileHandler 
{	
	/**
	 * Méthode utilisée <i>côté serveur</i> par le Moteur Réseau, pour envoyer à l'Interface 
	 * Utilisateur une demande d'inscription d'un joueur, à la partie réseau en cours de 
	 * configuration. Le serveur peut refuser certains joueurs, par exemple si la partie est 
	 * complète (plus de place libre). La méthode renvoie un booléen indiquant si le joueur
	 * a été accepté ({@code true}) ou rejeté ({@code false}).
	 * 
	 * @param profile
	 * 		Le profil du joueur distant que l'on veut rajouter à la partie en cous de configuration.
	 * @return
	 * 		Un booléen indiquant si le profil a été accepté ({@code true}) ou rejeté ({@code false}). 
	 */
	public boolean fetchProfile(Profile profile);
}
