package fr.univavignon.courbes.network.groupe20.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ServerProfileHandler;
import fr.univavignon.courbes.network.groupe20.ProfileReponse;
import fr.univavignon.courbes.network.groupe20.client.Client;
import fr.univavignon.courbes.network.groupe20.interTest.ServerProfileHandlerr;
/**
 * Cette classe  permet de traiter les requettes (coté serveur) envoyer par le client entrer en paramétre.Et aprés le 
 * traitement il enregistre chaque objet reçu dans sa propre place
 * 
 */
public class ServiceClient {
   /**
     *List de type Byte[] dont on sauvegarde tous les Tableaux de Bytes envoyer par le client en paramétre 
	 */
	List<byte[]> tabByte = new CopyOnWriteArrayList<byte[]>();
	
	/**
     *List de type ProfileAction  dont on sauvegarde tous les Objets de ProfileAction envoyer par le client en paramétre 
	 */
	List<ProfileAction> tabProfile = new CopyOnWriteArrayList<ProfileAction>();
	
	ServerProfileHandlerr sHandler = new ServerProfileHandlerr();
	/**
	 * Lors de l'instance de la classe ServiceClient : 
	 * </br> 
	 *  + le 1er thread permet de traiter toutes les requettes envoyer par le client entrer en paramétre
	 *   et les sauvegarder dans la liste tabByte si l'action égale à 0 ,si non on crée un objet de type 
	 *   {@link ProfileAction} et on le sauvegarde dans la List tabProfile
	 *  </br>
	 *  + le 2éme thread permet de parcourir la List tabProfile de type {@link ProfileAction} pour désérialiser tous
	 *  les objets de type {@link Profile} et savoir l'action voulu de l'envoie du profil. Si c'est un nouvel ajout ou une
	 *  suppression .
	 *      - Si c'est un nouvel ajout ,on appel la méthode fetchProfile de la classe de type {@link ServerProfileHandler}
	 *      si la méthode retourne {@code true} : profil va s'ajouter à la liste profileClients de type {@link Profile} dans la classe {@link Server}
	 *      et il va être envoyer comme quoi il est accépter au {@link Client} entrer en paramétre. Si non ,si  la méthode retourne {@code false}
	 *      il va être envoyer comme quoi il est rejeter.
	 *      </br>
	 *      - Si c'est une suppression,il va être supprimer de la liste profileClients de type {@link Profile} dans la classe {@link Server}
	 *      </br>
	 *      </br>
	 * 
	 * @param s
	 * 			Instance de la classe Server qui nous permet d'accéder au differente variable de cette classe
	 * @param client
	 * 			Instance de la classe Socket qui correspond à un client entrer en paramétre
	 */
	public ServiceClient(final Server s,final Socket client){
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					DataInputStream dis;
					try {
						dis = new DataInputStream(client.getInputStream());
						int nbrByte = dis.readInt();
						byte action = dis.readByte();
						byte[] data = new byte[nbrByte];
						 dis.read(data);
					   if(action == 0)
						  tabByte.add(data);
					   else{
						   ProfileAction pA = new ProfileAction();
						   pA.setClient(client);
						   pA.setData(data);
						   pA.setAction(action);
						   tabProfile.add(pA);
					   }
					}catch (IOException e) {
						System.out.println("Client déconnecter");
						break;
					}
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					for(ProfileAction pA : tabProfile){
						ByteArrayInputStream bytesIn = new ByteArrayInputStream(pA.getData());
					    ObjectInputStream ois;
					    try {
							ois = new ObjectInputStream(bytesIn);
							Object obj = ois.readObject();
								Profile p = (Profile)obj;
								if(pA.getAction() == 1){
									boolean actionAccept = sHandler.fetchProfile(p);
									if(actionAccept == true){	
										s.profileClients.add(p);
										ProfileReponse pR = new ProfileReponse();
										pR.setProfile(p);
										pR.setAction(actionAccept);
										ServiceClient.this.sendObject(pR, pA.getClient());
									}else{
										ProfileReponse pR = new ProfileReponse();
										pR.setProfile(p);
										pR.setAction(actionAccept);
										ServiceClient.this.sendObject(pR, pA.getClient());
									}
									}else{
										for(Profile pr : s.profileClients){
											if(pr.profileId == p.profileId)
												s.profileClients.remove(pr);
										}
									}
								
							tabProfile.remove(pA);
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
		
		
	}
	
	/**
	 * L'envoie d'un objet(aprés la sérialisation de ce dérnier) entrer en paramétre 
	 * vers un client entrer en paramétre
	 * 
	 * @param object
	 * 			object de type {@link Object} à envoyer
	 * @param client
	 * 			client de type {@link Socket}
	 */
	public void sendObject(Object object,Socket client){
		try {
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos;
			oos = new ObjectOutputStream(bos);
			oos.writeObject(object);
			oos.flush();
			oos.close();
			bos.close();
			
			DataOutputStream dos = new DataOutputStream(client.getOutputStream());  
			byte [] data = bos.toByteArray();
			dos.writeInt(data.length);
		    dos.write(data);
		    dos.flush();
		} catch (IOException e) {e.printStackTrace();}
		
	}
}
