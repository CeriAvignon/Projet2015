package fr.univavignon.courbes.network.groupe20;

import java.io.Serializable;
import fr.univavignon.courbes.common.Profile;

/**
 *cette classe nous permet d'envoyer la reponse (de la part du serveur) suite à la demande du profil(Client)
 **/
public class ProfileReponse implements Serializable{
	/**
	 * Numéro de série (pour {@code Serializable})
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 *profil de type {@link Profile} qui a éffectué la demande d'ajout ou de suppréssion
	 **/
	private Profile profile;
	/**
	 * action de type {@link Boolean} , 
	 * 
	 * <code>true</code> : s'il est accépté.
	 * <code>false</code> : s'il est refusé
	 **/
	private boolean action;
	
	//Getter et setter
	public Profile getProfile() {
		return profile;
	}
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	public boolean isAction() {
		return action;
	}
	public void setAction(boolean action) {
		this.action = action;
	}
}
