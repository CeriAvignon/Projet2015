package fr.univavignon.courbes.inter.groupe13;

import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ServerProfileHandler;
import fr.univavignon.courbes.network.*;
/* ajouter l'import pour votre groupe (physics et graphics)
 * 
 * import fr.univavignon.courbes.network.groupeXX.*;
 * 
 * Penser a instancier le moteur reseau partie client ligne 17
 */

public class ServerProfile implements ServerProfileHandler{
	@Override
	public boolean fetchProfile(Profile profile)
	{
		ClientCommunication clientCommunication = new NOM_DE_LA_CLASSE_CLIENT();
		return clientCommunication.addProfile(profile);
	}
}
