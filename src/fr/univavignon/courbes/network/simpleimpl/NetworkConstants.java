package fr.univavignon.courbes.network.simpleimpl;

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

/**
 * Cette classe contient uniquement des constantes utilisées par le moteur réseau.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class NetworkConstants
{	/** Le client indique au serveur qu'il se déconnecte, ou inversement */
	public static final String ANNOUNCE_DISCONNECTION = "ANNOUNCE_DISCONNECTION";
	/** Le serveur indique au client que son profil a été rejeté et ne participera pas à la partie (le message implique aussi une déconnexion) */
	public static final String ANNOUNCE_REJECTED_PROFILE = "ANNOUNCE_REJECTED_PROFILE";
	
//	/** Message indiquant que la partie va commencer */
//	public static final String ANNOUNCE_GAME_START = "ANNOUNCE_GAME_START";
	
	/** Le client requiert que le serveur lui envoie les profils actuellement sélectionnés pour la partie en cours de configuration */
	public static final String REQUEST_PROFILES = "REQUEST_PROFILES";
}
