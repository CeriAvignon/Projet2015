package fr.univavignon.courbes.common;

import java.io.Serializable;

/**
 * Représente un joueur, non pas dans le cadre d'une partie, mais dans
 * le cadre plus général du jeu. Autrement dit, il s'agit d'un profil
 * de joueur, qui pourra ensuite être sélectionné pour jouer dans une
 * partie donnée.
 */
public class Profile implements Serializable
{	/** Numéro de série (pour {@code Serializable}) */
	private static final long serialVersionUID = 1L;
	
	/** Numéro unique du profil dans la BD du jeu */
	public int profileId;
	
	/** Score ELO du profil relativement à toutes les parties qu'il a jouées */
	public int score;
	
	/** Adresse email associée au profil */
	public String email;
	/** Nom d'utilisateur associé au profil */
	public String userName;
	/** Mot de passe associé au profil */
	public String password;
	/** Pays associé au profil */
	public String country;
	/** Fuseau horaire associé au profil */
	public String timeZone;
}