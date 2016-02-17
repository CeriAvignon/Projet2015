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
import fr.univavignon.courbes.common.Round;

/**
 * Interface implémentant les méthodes permettant au Moteur Réseau d'envoyer à l'Interface Utilisateur
 * les données relatives à la configuration des joueurs participant à une partie réseau, <i>côté client</i>.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public interface ClientProfileHandler 
{	
	/**
	 * Méthode utilisée <i>côté client</i> par le Moteur Réseau, pour envoyer à l'Interface 
	 * Utilisateur la liste mise à jour des profils des joueurs participant à la partie en 
	 * cours de configuration.
	 * 
	 * @param profiles
	 * 		Tableau <i>à jour</i> des profils reçus par le Moteur Réseau. Les emplacements vides  
	 * 		sont représentés par des valeurs {@code null}.
	 */
	public void updateProfiles(Profile[] profiles);
	
	/**
	 * Indique au client qu'il faut basculer sur le panel de jeu pour commencer la première manche.
	 * 
	 * @param round 
	 * 		Objet representant la manche et toute sa configuration.
	 */
	public void startGame(Round round);
	
	/**
	 * Indique au client que son profil a été rejeté par le serveur, pour ce qui concerne
	 * la partie en cours de configuration. Autrement dit, il s'est fait kick.
	 */
	public void gotKicked();

	/**
	 * La connexion avec le serveur a été perdue accidentellement.
	 */
	public void connectionLost();
}
