package fr.univavignon.courbes.network.interTest;



import java.util.List;

import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ClientProfileHandler;

public class ClientProfileHandlerr implements ClientProfileHandler {

	@Override
	public void updateProfiles(List<Profile> profiles) {
		for(Profile p : profiles)
			System.out.println(p.profileId);
	}

}