package fr.univavignon.courbes.network.groupe20.client;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ClientProfileHandler;
import fr.univavignon.courbes.inter.ErrorHandler;
import fr.univavignon.courbes.network.ClientCommunication;
import fr.univavignon.courbes.network.groupe20.ProfileReponse;
public class Client implements ClientCommunication {
	
	Socket client;
	private String ip;
	private int port;
	 List<ProfileReponse> addProfil = new CopyOnWriteArrayList<ProfileReponse>();
	 Integer point = null,pointRetour = null;
	 Board board = null;
	 ErrorHandler errorHandler; 
	 ClientProfileHandler profileHandler;
	 Boolean lancer;
	@Override
	public String getIp() {
		return ip;
	}
	@Override
	public void setIp(String ip) {
		this.ip = ip;
	}
	@Override
	public int getPort() {
		return this.port;
	}
	@Override
	public void setPort(int port) {
		this.port = port;
	}
	
	@Override
	public void setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}
	
	@Override
	public void setProfileHandler(ClientProfileHandler profileHandler) {
		this.profileHandler = profileHandler;
	}
	@Override
	public void launchClient() {
		try {
			lancer = true;
			client = new Socket(this.ip, this.port);
			new ServiceServer(this);
		} catch (IOException e) {
			this.errorHandler.displayError("Aucun serveur avec ces informations");
		}
	}
	@Override
	public void closeClient() {
		lancer = false;
		this.closeClient();
	}
	
	@Override
	public boolean addProfile(Profile profile) {
		boolean b = false;
		boolean ret = false;
		this.sendObject(profile, (byte)1);
		while(b == false)
			for(ProfileReponse p :addProfil)
				if(p.getProfile().profileId == profile.profileId){
					ret = p.isAction();
					addProfil.remove(p);
					b = true;
					break;
				}
		return ret;
	}
	@Override
	public void removeProfile(Profile profile) {
		if(lancer)
			this.sendObject(profile, (byte)2);
	}
	
	@Override
	public Integer retrievePointThreshold() {
		this.pointRetour = this.point;
		this.point = null;
		return pointRetour;
	}
	@Override
	public Board retrieveBoard() {
		Board nBoard = board;
		board = null;
		return nBoard;
	}
	@Override
	public void sendCommands(Map<Integer, Direction> commands) {
		if(lancer)
			this.sendObject(commands);
    }
	
	private void sendObject(Object o){
		
			try {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream oos;
				oos = new ObjectOutputStream(bos);
				oos.writeObject(o);
				oos.flush();
				oos.close();
				bos.close();
				
				DataOutputStream dos = new DataOutputStream(client.getOutputStream());  
				byte [] data = bos.toByteArray();
				dos.writeInt(data.length);
				dos.writeByte(0);
			    dos.write(data);
			    dos.flush();
			} catch (IOException e) {}
		
	}
	
	private void sendObject(Object o,byte b){

			try {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream oos;
				oos = new ObjectOutputStream(bos);
				oos.writeObject(o);
				oos.flush();
				oos.close();
				bos.close();
				
				DataOutputStream dos = new DataOutputStream(client.getOutputStream());  
				byte [] data = bos.toByteArray();
				dos.writeInt(data.length);
				dos.writeByte(b);
			    dos.write(data);
			    dos.flush();
			    
			} catch (IOException e) {}
	}
	public Integer getPoint() {
		return point;
	}
	public void setPoint(Integer point) {
		this.point = point;
	}	
	
	
	
}


