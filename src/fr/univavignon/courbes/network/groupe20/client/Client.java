package fr.univavignon.courbes.network.groupe20.client;

import java.net.Socket;
import java.util.List;
import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.network.ClientCommunication;

public class Client implements ClientCommunication {
	
	private Socket client;
	private String ip;
	private int port;
	
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
