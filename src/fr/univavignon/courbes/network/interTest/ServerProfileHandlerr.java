package fr.univavignon.courbes.network.interTest;

import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ServerProfileHandler;

public class ServerProfileHandlerr implements ServerProfileHandler {

	@Override
	public boolean fetchProfile(Profile profile) {
		return true;
	}

}
