package fr.univavignon.courbes.network.groupe20;

import java.io.Serializable;
import fr.univavignon.courbes.common.Profile;

public class ProfileReponse implements Serializable{
	/**
	 * Numéro de série (pour {@code Serializable})
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Profile profile;
	private boolean action;
	
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
