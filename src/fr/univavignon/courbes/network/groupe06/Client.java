package fr.univavignon.courbes.network.groupe06;

import fr.univavignon.courbes.network.ClientCommunication;

import java.util.List;
import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;

public class Client implements ClientCommunication {

	protected String ip;
	protected int port;
	
	@Override
	public String getIp() {
		
		return this.ip;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeClient() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Profile> retrieveProfiles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer retrievePointThreshold() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Board retrieveBoard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendCommands(Map<Integer, Direction> commands) {
		// TODO Auto-generated method stub
		
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

}
