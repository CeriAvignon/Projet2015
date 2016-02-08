package fr.univavignon.courbes.inter.simpleimpl.communication;

import java.util.List;

import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ClientProfileHandler;
import fr.univavignon.courbes.network.ServerCommunication;
import fr.univavignon.courbes.network.simpleimpl.Server;

public class ClientProfileHandlerImpl implements ClientProfileHandler{

	@Override
	public void updateProfiles(List<Profile> profiles) {
		
		ServerCommunication serverCommunication = new Server();
		serverCommunication.sendProfiles(profiles);
	}
}
