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

import java.util.List;

import fr.univavignon.courbes.common.Profile;

/**
 * Interface implémentant les méthodes permettant au Moteur Réseau d'envoyer à l'Interface Utilisateur
 * les données relatives à la configuration des joueurs participant à une partie réseau, <i>côté client</i>.
 * <br/>
 * Chaque binôme de la composante Interface Utilisateur doit définir une classe implémentant 
 * cette interface, puis l'instancier. Notez qu'il est possible pour une classe d'implémenter
 * plusieurs interfaces simultanément.
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
	 * 		Liste <i>à jour</i> des profils reçus par le Moteur Réseau (peut être vide si aucun 
	 * 		joueur n'a encore été sélectionné). 
	 */
	public void updateProfiles(List<Profile> profiles);
}
