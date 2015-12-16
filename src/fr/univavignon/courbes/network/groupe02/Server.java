package fr.univavignon.courbes.network.groupe02;

import java.util.List;
import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.network.ServerCommunication;

import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.InetSocketAddress;

/**
 * 
 * 
 * @author Marie et Mary    
 * 
 * On fait la classe serveur
 *
 */
public class Server implements ServerCommunication
{
	int port = 2345;
	String ip;
	ServerSocket server = null;
	
	@Override
	public void launchServer() 
	{

	}

	@Override
	public void closeServer() 
	{
		
	}

	@Override
	public void sendPlayers(List<Profile> profiles) 
	{
		
	}

	@Override
	public void sendPointThreshold(int pointThreshold) 
	{
		
	}

	@Override
	public void sendBoard(Board board) 
	{
		
	}

	@Override
	public Map<Integer, Direction> retrieveCommands() 
	{
		return null;
	}

	@Override
	public void sendText(String message) 
	{
		return;
	}

	@Override
	public String[] retrieveText() 
	{
		return null;
	}

	@Override
	public String getIp() 
	{
		return this.ip;
	}

	@Override
	public int getPort() 
	{
		return this.port;
	}

	@Override
	public void setPort(int port) 
	{
		this.port = port;
	}
	
}