package fr.univavignon.courbes.network.groupe20.server;

import java.net.Socket;

public class ProfileAction {
	private byte[] data;
	private Socket client;
	private byte action;
	
	
	
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