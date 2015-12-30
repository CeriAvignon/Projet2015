package fr.univavignon.courbes.network.groupe20.client;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
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
import fr.univavignon.courbes.network.groupe20.interTest.ClientProfileHandlerr;
public class ServiceServer {
	List<byte[]> tabByte = new CopyOnWriteArrayList<byte[]>();
	ClientProfileHandlerr cHandler = new ClientProfileHandlerr();
	
	public ServiceServer(final Client c){
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
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
						 
						 e.printStackTrace();
					}
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					for(byte[] b : tabByte){
						ByteArrayInputStream bytesIn = new ByteArrayInputStream(b);
					    ObjectInputStream ois;
						try {
							ois = new ObjectInputStream(bytesIn);
						    Object obj = ois.readObject();
						    if(obj instanceof ProfileReponse){
						    	ProfileReponse p = (ProfileReponse)obj;
						    		c.addProfil.add(p);
						    }else if(obj instanceof ArrayList){
						    	List<Profile> profile = (List<Profile>) obj;
						    	cHandler.updateProfiles(profile);
						    }else if(obj instanceof Integer){
						    	c.point = (Integer) obj;
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
