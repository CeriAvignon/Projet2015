package fr.univavignon.courbes.network.groupe20.server;

import java.awt.Window.Type;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ServerProfileHandler;
import fr.univavignon.courbes.network.groupe20.ProfileReponse;
import fr.univavignon.courbes.network.groupe20.client.Client;
/**
 * {@link ServiceClient}  permet de traiter les requettes (coté serveur) envoyées par le client entré en paramètre.
 * <br/>
 * Aprés le traitement de la requette, {@link ServiceClient} enregistre chaque objet reçu dans sa propre collection de type tableau de {@link Byte}
 * 
 */
public class ServiceClient {
   /**
     *List de type tableau de {@link Byte} où l'on sauvegarde tous les Tableaux de Bytes reçu depuis le client défini
     *dans le constructeur
	 */
	List<byte[]> tabByte = new CopyOnWriteArrayList<byte[]>();
	
	/**
     *List de type {@link ProfileAction} où l'on sauvegarde tous les Objets de ProfileAction reçu depuis le client défini
     *dans le constructeur
	 */
	List<ProfileAction> tabProfile = new CopyOnWriteArrayList<ProfileAction>();
	
	/**
	 * Lors de l'instance de la classe {@link ServiceClient}: 
	 * </br> 
	 *  + le 1er thread permet de traiter toutes les requettes envoyées par le client entré en paramétre dans le constructeur.
	 *   , si la variable "action" est égale à 0, le thread les sauvegarde dans la collection "tabByte", si non on crée un objet de type 
	 *   {@link ProfileAction} et on le sauvegarde dans la collection "tabProfile".
	 *  </br>
	 *  + le 2éme thread permet de parcourir la collection "tabProfile" de tableau de type {@link Byte}  pour désérialiser tous
	 *  les objets de type {@link ProfileAction} et savoir l'action, si c'est un ajout ou une suppression :
	 *      - Si c'est un nouvel ajout ,on appel la méthode fetchProfile qui appartient à la classe de type {@link ServerProfileHandler}
	 *      	si la méthode retourne {@code true} : le profil va s'ajouter à la collection profileClients de type {@link Profile} dans la classe {@link Server}
	 *       	et le {@link Client} entré en paramétre va recevoir la reponse "true" s'il est accepté. Si la méthode retourne {@code false}
	 *    	  	le {@link Client} entré en paramétre va recevoir la reponse "false" s'il est rejeté.
	 *      </br>
	 *      - Si c'est une suppression, le profile envoyé {@link Client} va être supprimé de la collection profileClients de type {@link Profile} dans la classe {@link Server}
	 *      </br>
	 *      </br>
	 * 
	 * @param s
	 * 			Instance de la classe Server qui nous permet d'accéder au differente variable de cette classe
	 * @param client
	 * 			Instance de la classe Socket qui correspond à un client entré en paramétre
	 * 	
	 * +le 3ème thread permet de parcourir la collection "tabByte" de tableau de type {@link Byte} pour désérialiser 
	 * tous les autres types objets reçu hors le type {@link ProfileAction}  qui seront par la suite sauvegardés dans
	 * une collection en fonction de leurs types .
	 */
	public ServiceClient(final Server s,final Socket client){
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(s.lancer){
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
						s.errorHandler.displayError("Client déconnecter");
						break;
					}
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(s.lancer){
					for(ProfileAction pA : tabProfile){
						ByteArrayInputStream bytesIn = new ByteArrayInputStream(pA.getData());
					    ObjectInputStream ois;
					    try {
							ois = new ObjectInputStream(bytesIn);
							Object obj = ois.readObject();
								Profile p = (Profile)obj;
								if(pA.getAction() == 1){
									boolean actionAccept = s.profileHandler.fetchProfile(p);
									if(actionAccept == true)	
										s.profileClients.add(p);
									
										ProfileReponse pR = new ProfileReponse();
										pR.setProfile(p);
										pR.setAction(actionAccept);
										ServiceClient.this.sendObject(pR, pA.getClient());
									
									
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
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(s.lancer){
					for(byte[] b : tabByte){
						ByteArrayInputStream bytesIn = new ByteArrayInputStream(b);
					    ObjectInputStream ois;
						try {
							ois = new ObjectInputStream(bytesIn);
							Object obj = ois.readObject();
							if(obj instanceof HashMap){
								s.directions.add((Map<Integer, Direction>)obj);
							}
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
	
	/**
	 * L'envoie d'un objet(aprés la sérialisation de ce dernier) entré en paramétre 
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
