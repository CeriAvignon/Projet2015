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
 * Interface implémentant les méthodes permettant au MR de demander à l'IU d'afficher 
 * des messages d'erreur. Par exemple, cela peut être fait dans la console texte, ou 
 * bien sous forme de fenêtre de type pop-up.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public interface ErrorHandler 
{	/**
	 * Demande à l'interface utilisateur d'afficher le message indiqué
	 * en paramètre. La méthode est essentiellement destinée au Moteur
	 * Réseau, afin de faire remonter des problèmes du client vers le
	 * serveur, et inversement.
	 * 
	 * @param errorMessage
	 * 		Le message d'erreur à afficher.
	 */
	public void displayError(String errorMessage);
}
