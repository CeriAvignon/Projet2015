package fr.univavignon.courbes.network.groupe15;

import java.net.*;
import java.io.*;

import java.util.List;
import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.network.ServerCommunication;

/**
 * 
 * @author uapv1504059
 *
 */

public class Server implements ServerCommunication {

	/**
	 * @param args Port de connexion
	 * @throws IOException Gestion des exceptions
	 */
	public static void main(String[] args) throws IOException {
        
        if (args.length != 1) {
            System.err.println("Usage: java KnockKnockServer <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

        try ( 
            ServerSocket serverSocket = new ServerSocket(portNumber);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out =
                new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        ) {
        
            String inputLine, outputLine;
            
            // Initiate conversation with client
            Client kkp = new Client();
            outputLine = kkp.processInput(null);
            out.println(outputLine);

            while ((inputLine = in.readLine()) != null) {
                outputLine = kkp.processInput(inputLine);
                out.println(outputLine);
                if (outputLine.equals("Bye."))
                    break;
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

	@Override
	public void launchServer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeServer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendPlayers(List<Profile> profiles) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendPointThreshold(int pointThreshold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendBoard(Board board) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<Integer, Direction> retrieveCommands() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendText(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] retrieveText() {
		// TODO Auto-generated method stub
		return null;
	}

}
