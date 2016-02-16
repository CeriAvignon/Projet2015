package fr.univavignon.courbes.network.groupe20.client;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.network.groupe20.ProfileReponse;
import fr.univavignon.courbes.network.groupe20.server.Server;
public class ServiceServer {
	/**
     *List de type tableau de {@link Byte} où l'on sauvegarde tous les Tableaux de Bytes reçu depuis le Serveur défini
     *dans le constructeur
	 */
	List<byte[]> tabByte = new CopyOnWriteArrayList<byte[]>();
	
	/**
	 * le 1er thread permet de traiter toutes les requettes envoyées par le Serveur entré en paramétre dans le constructeur qui seront par la suite 
	 * sauvegardés dans une collection de tableau de {@link Byte}
	 * 
	 * le 2éme thread  permet de parcourir la collection "tabByte" de tableau de type {@link Byte} pour désérialiser les objets reçu ,
	 *  qui seront par la suite sauvegardés dans les collections  ou des variables  de la classe {@link Server} en fonction de leurs types : 
	 *  
	 *   + dans la collection Server.board si l'objet reçu est de type {@link Board}.
	 *   + dans la collection Server.addProfil si l'objet reçu est de type {@link ProfileReponse}.
	 *   + dans la variable Server.Point si l'objet reçu est de type {@link Integer}
	 *   + Appel la méthode profileHandler.updateProfiles(Profiles) si l'objet est de type ArrayList
	 *  
	 * 
	 * */
	public ServiceServer(final Client c){
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(c.etatClient){
					DataInputStream dis;
					try {
						dis = new DataInputStream(c.client.getInputStream());
						int nbrByte = dis.readInt();
						if(nbrByte > 0 && nbrByte < 1024){
					    byte[] data = new byte[nbrByte];
					    dis.read(data);
					    tabByte.add(data);
					   }
					 } catch (IOException e) {
						c.etatClient = false;
						c.errorHandler.displayError("Serveur déconnécté");
					}
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(c.etatClient){
					for(byte[] b : tabByte){
						ByteArrayInputStream bytesIn = new ByteArrayInputStream(b);
					    ObjectInputStream ois;
						try {
							ois = new ObjectInputStream(bytesIn);
						    Object obj = ois.readObject();
						    if(obj instanceof Board){
						    	c.board = (Board)obj;
						    }else if(obj instanceof ProfileReponse){
						    	ProfileReponse p = (ProfileReponse)obj;
						    		c.addProfil.add(p);
						    }else if(obj instanceof ArrayList){
						    	List<Profile> profile = (List<Profile>) obj;
						    	c.profileHandler.updateProfiles(profile);
						    }else if(obj instanceof Integer){
						    	c.setPoint((Integer) obj);
						    }
							
						   ois.close();
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						tabByte.remove(b);
					}
				}
			}
		}).start();
	}
}
