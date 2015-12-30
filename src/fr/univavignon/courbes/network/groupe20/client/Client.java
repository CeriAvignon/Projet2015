package fr.univavignon.courbes.network.groupe20.client;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
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
import fr.univavignon.courbes.network.ClientCommunication;
import fr.univavignon.courbes.network.groupe20.ProfileReponse;
public class Client implements ClientCommunication {
	
	Socket client;
	private String ip;
	private int port;
	 List<ProfileReponse> addProfil = new CopyOnWriteArrayList<ProfileReponse>();
	 Integer point = null;
	 Board board = null;
	
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
	public void launchClient() {
		try {
			client = new Socket(this.ip, this.port);
			new ServiceServer(this);
		} catch (IOException e) {e.printStackTrace();}
	}
	@Override
	public void closeClient() {
		// TODO Auto-generated method stub
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
		this.sendObject(profile, (byte)2);
	}
	
	@Override
	public Integer retrievePointThreshold() {
		Integer nbr = point;
		point = null;
		return nbr;
	}
	@Override
	public Board retrieveBoard() {
		Board nBoard = board;
		board = null;
		return nBoard;
	}
	@Override
	public void sendCommands(Map<Integer, Direction> commands) {
		this.sendObject(commands);
    }
	@Override
	public String retrieveText() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void sendText(String message) {
		// TODO Auto-generated method stub
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
		} catch (IOException e) {e.printStackTrace();}
		
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
		    
		} catch (IOException e) {e.printStackTrace();}
		
		
	}
	
		public static void main(String[] args) throws InterruptedException {
			final Client c = new Client();
			c.setIp("localhost");
			c.setPort(1117);
			c.launchClient();
			Map< Integer, Direction> map = new HashMap<Integer, Direction>();
			map.put(1, Direction.RIGHT);
			while(true)
				c.sendCommands(map);
		}
	}


