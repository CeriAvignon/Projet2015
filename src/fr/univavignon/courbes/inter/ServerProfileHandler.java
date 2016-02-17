package fr.univavignon.courbes.inter;

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

import fr.univavignon.courbes.common.Profile;

/**
 * Interface implémentant les méthodes permettant au Moteur Réseau d'envoyer à l'Interface Utilisateur
 * les données relatives à la configuration des joueurs participant à une partie réseau, <i>côté serveur</i>.
 * 
 * @author	L3 Info UAPV 2015-16
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
	 * 		Le profil du joueur distant que l'on veut rajouter à la partie en cours de configuration.
	 * @param index
	 * 		Numéro du client hébergeant le joueur distant.
	 */
	public void fetchProfile(Profile profile, int index);
	
	/**
	 * La connexion avec le client dont le numéro est indiqué a été perdue, soit 
	 * volontairement de sa part, soit accidentellement.
	 * 
	 * @param index
	 * 		Numéro du client dont on a perdu la connexion.
	 */
	public void connectionLost(int index);
}
