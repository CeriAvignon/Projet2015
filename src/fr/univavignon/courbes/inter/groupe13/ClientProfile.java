package fr.univavignon.courbes.inter.groupe13;

import java.util.List;

import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ClientProfileHandler;
import fr.univavignon.courbes.network.ServerCommunication;
/* ajouter l'import pour votre groupe (physics et graphics)
 * 
 * import fr.univavignon.courbes.network.groupeXX.*;
 * 
 * Penser a instancier le moteur reseau partie reseau ligne 20
 */

public class ClientProfile implements ClientProfileHandler{

	@Override
	public void updateProfiles(List<Profile> profiles) {
		
		ServerCommunication serverCommunication = null;// TODO new NOM_DE_LA_CLASSE_SERVER();
		serverCommunication.sendProfiles(profiles);
	}
}
