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

/**
 * Interface implémentant les méthodes permettant au Moteur Réseau d'envoyer à l'Interface Utilisateur
 * les données relatives à la connection du client à une partie distante.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public interface ClientConnectionHandler 
{	
	/**
	 * Indique au client que le serveur ne l'a pas accepté dans sa partie en cours de configuration.
	 */
	public void gotRefused();
	
	/**
	 * Indique au client que le serveur l'a accepté dans sa partie en cours de configuration. 
	 */
	public void gotAccepted();
}
