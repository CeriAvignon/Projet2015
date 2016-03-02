package fr.univavignon.courbes.network.kryonet;

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
 * Cette classe contient uniquement des constantes utilisées par l'implémentation
 * à base de Kryonet du Moteur Réseau.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class NetworkConstants
{	
//	/** Le client indique au serveur qu'il se déconnecte, ou inversement */
//	public static final String ANNOUNCE_DISCONNECTION = "ANNOUNCE_DISCONNECTION";
	
	/** Le serveur indique au client qu'il peut se connecter à la partie en cours de configuration */
	public static final String ANNOUNCE_ACCEPTED_CONNECTION = "ANNOUNCE_ACCEPTED_CONNECTION";
	/** Le serveur indique au client qu'il ne peut pas se connecter à la partie en cours de configuration */
	public static final String ANNOUNCE_REJECTED_CONNECTION = "ANNOUNCE_REJECTED_CONNECTION";
	/** Le serveur indique au client que son profil a été rejeté et ne participera pas à la partie (le message implique aussi une déconnexion) */
	public static final String ANNOUNCE_REJECTED_PROFILE = "ANNOUNCE_REJECTED_PROFILE";
	
//	/** Message indiquant que la partie va commencer */
//	public static final String ANNOUNCE_GAME_START = "ANNOUNCE_GAME_START";
	
	/** Message indiquant que le client est prêt à commencer la manche */
	public static final String ANNOUNCE_ACKNOWLEDGMENT = "ANNOUNCE_ACKNOWLEDGMENT";
	
//	/** Le client requiert que le serveur lui envoie les profils actuellement sélectionnés pour la partie en cours de configuration */
//	public static final String REQUEST_PROFILES = "REQUEST_PROFILES";
	
	/** Taille du buffer de sortie de Kryonet pour les types simples (défaut : 16384) */
	public static final int WRITE_BUFFER_SIZE = 16384;
	/** Taille du buffer de sortie de Kryonet pour les objets (défaut : 2048) */
	public static final int READ_BUFFER_SIZE = 16384;
}
