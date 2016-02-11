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
 * Représente un joueur, non pas dans le cadre d'une partie comme {@link Player}, mais dans
 * le cadre plus général du jeu. Autrement dit, il s'agit d'un profil de joueur, qui pourra 
 * ensuite être sélectionné pour jouer dans une partie donnée.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class Profile implements Serializable, Comparable<Profile>
{	/** Numéro de série (pour {@code Serializable}) */
	private static final long serialVersionUID = 1L;
	
//	/**
//	 * Crée un nouveau profil, vide. TODO
//	 */
//	public Profile()
//	{
//		//
//	}
//	
//	/**
//	 * Crée un nouveau profil qui est une copie de
//	 * celui passé en paramètre.
//	 *  
//	 * @param profile
//	 * 		Profil à recopier.
//	 */
//	public Profile(Profile profile)
//	{	this.profileId = profile.profileId;
//		this.eloRank = profile.eloRank;
//		this.email = profile.email;
//		this.userName = profile.userName;
//		this.password = profile.password;
//		this.country = profile.country;
//	}
	
	/** Numéro unique du profil dans la BD du jeu */
	public int profileId;
	
	/** Rang ELO du profil relativement à toutes les parties qu'il a jouées */
	public int eloRank;
	
	/** Adresse email associée au profil */
	public String email;
	/** Nom d'utilisateur associé au profil */
	public String userName;
	/** Mot de passe associé au profil */
	public String password;
	/** Pays associé au profil */
	public String country;
	
	@Override
	public int compareTo(Profile profile)
	{	int result = profileId - profile.profileId;
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{	boolean result = false;
		if(this==obj)
			result = true;
		else if(obj!=null && obj instanceof Profile)
		{	Profile profile = (Profile) obj;
			result = compareTo(profile)==0;
		}
		return result;
	}
	
	@Override
	public String toString()
	{	return userName;
	}
}