package fr.univavignon.courbes.network.groupe20.server;

import java.net.Socket;
/**
 * La classe {@link ProfileAction} est appelé lorsque le client veut envoyer une demande d'ajout ou suppression au serveur 
 * 
 */

public class ProfileAction {
	/**
	 * contient un objet de type profil sérialisé 
	 */
	private byte[] data;
	
	/**
	 * le client destinataire de la reponse 
	 */
	private Socket client;
	
	/**
	 * action est égale à 1 si le client demande l'ajout d'un profil , 
	 * si action est égale à 2 le client demande la suppression d'un profil
	 */
	private byte action;
	
	
	//Getter et Setter
	
	byte getAction() {
		return action;
	}
	 void setAction(byte action) {
		this.action = action;
	}
	byte[] getData() {
		return data;
	}
	 void setData(byte[] data) {
		this.data = data;
	}
	Socket getClient() {
		return client;
	}
	 void setClient(Socket client) {
		this.client = client;
	}
	
}