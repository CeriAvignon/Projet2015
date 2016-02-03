package fr.univavignon.courbes.inter.groupe13;

import fr.univavignon.courbes.common.Profile;

public class PrintableProfile {
	
	private Profile profile;
		
	@Override
	public String toString(){
		return profile.userName;
	}

	public Profile getProfile() {
		return profile;
	}
	public void setProfile(Profile profile) {
		this.profile = profile;
	}

}
