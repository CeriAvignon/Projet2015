package fr.univavignon.courbes.inter.simpleimpl.data;

import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ServerProfileHandler;
import fr.univavignon.courbes.network.*;
import fr.univavignon.courbes.network.simpleimpl.Client;

public class ServerProfile implements ServerProfileHandler{
	@Override
	public boolean fetchProfile(Profile profile)
	{
		ClientCommunication clientCommunication = new Client();
		return clientCommunication.addProfile(profile);
	}
}
