package fr.univavignon.courbes.network.simpleimpl;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Round;
import fr.univavignon.courbes.common.UpdateInterface;

/**
 * Classe permettant d'enregistrer les objets susceptibles d'être envoyés sur le réseau.
 * L'utilisation d'une unique classe Network pour le client et le serveur permet d'assurer que les objets sont envoyés de la même façon coté client et serveur.
 * @author zach
 *
 */
public class Network{
	static public int port = 54555;

	/**
	 * Enregistre les objets enregistrés sur le réseau
	 * @param endPoint
	 */
	static public void register (EndPoint endPoint){
		Kryo kryo = endPoint.getKryo();
		kryo.register(String.class);
		kryo.register(Direction.class);
		kryo.register(Profile.class);
		kryo.register(Profile[].class);
		kryo.register(UpdateInterface.class);
		kryo.register(Integer.class);
		kryo.register(Round.class);
	}
}

