package fr.univavignon.courbes.inter.groupe13;

import java.util.List;

import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ClientProfileHandler;
import fr.univavignon.courbes.network.ServerCommunication;
import fr.univavignon.courbes.network.groupe06.Server;

public class ClientProfile implements ClientProfileHandler{

	@Override
	public void updateProfiles(List<Profile> profiles) {
		
		ServerCommunication serverCommunication = new Server();
		serverCommunication.sendProfiles(profiles);
	}
}
