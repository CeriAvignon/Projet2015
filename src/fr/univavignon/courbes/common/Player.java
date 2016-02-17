package fr.univavignon.courbes.common;

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

import java.io.Serializable;

/**
 * Représente un joueur, non pas dans le cadre général du jeu comme {@link Profile}, 
 * mais dans celui d'une partie. La classe contient le profil associé au joueur, mais
 * aussi le score courant du joueur dans la partie.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class Player implements Serializable
{	/** Numéro de série (pour {@code Serializable}) */
	private static final long serialVersionUID = 1L;
	
	////////////////////////////////////////////////////////////////
	////	IDENTIFICATION
	////////////////////////////////////////////////////////////////
	/** Profil du joueur dans le jeu, en général */
	public Profile profile;
	/** Numéro unique du joueur dans la partie en cours */
	public int playerId;

	////////////////////////////////////////////////////////////////
	////	RESEAU
	////////////////////////////////////////////////////////////////
	/** Indique si le joueur est local ({@code true}) ou distant ({@code false}) */
	public boolean local;
	
	////////////////////////////////////////////////////////////////
	////	SCORE
	////////////////////////////////////////////////////////////////
	/** Score total du joueur dans la partie courante, <i>avant</i> la manche courante */
	public int totalScore;
	/** Score partiel du joueur pour la manche courante */
	public int roundScore;
	
	////////////////////////////////////////////////////////////////
	////	COMMANDES
	////////////////////////////////////////////////////////////////
	/** Numéro de la touche utilisée par le joueur pour aller à gauche */
	public int leftKey;
	/** Numéro de la touche utilisée par le joueur pour aller à droite */
	public int rightKey;
	
	////////////////////////////////////////////////////////////////
	////	TEXTE
	////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		result.append(playerId + ". " + profile.userName);
		if(!local)
			result.append(" (Remote)");
		else
			result.append(" (L=" + leftKey + " R=" + rightKey + ")");
		result.append(" " + roundScore + "/" + totalScore);
		return result.toString();
	}
}
