package fr.univavignon.courbes.inter.groupe13;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ErrorHandler;
import fr.univavignon.courbes.network.*;
import fr.univavignon.courbes.network.groupe06.Client;
import fr.univavignon.courbes.network.groupe06.Server;

public class Network extends Window {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private JTextField inputIP, inputPort, bPort ;
	private int tempWidth, tempHeight;
	private Map<Integer,int[]> commandsPlayer;
	private ServerCommunication serverCommunication;
	private ClientCommunication clientCommunication;
	private JButton bClose, bOpen;
	private JLabel port;
	private boolean start;
	/*
	 * Initialise les variables tempWidth, tempWidth et commandsPlayer  et affiche le menu pour creer un reseau ou pour ajouter un reseau
	 * @param width     Largeur de la fenêtre.
	 * @param height    Hauteur de la fenêtre.
	 * @param join true:menu rejoindre reseau, false: menu creer reseau.
	 * @param _commandsPlayer la liste des id des joueurs et de leurs touches 
	 */
	public Network(int width, int height, boolean join) 
	{
		super(width, height);	
		tempWidth = width;
		tempHeight = height;
		commandsPlayer = new HashMap<>();
//		commandsPlayer = _commandsPlayer;
		start = false;
		if(join)
			joinNetwork();
		else
			addNetwork();
	}
	
	/*
	 * Affiche le menu permettant de rejoindre un reseau
	 */
	public void joinNetwork()
	{
		clientCommunication = new Client();
		JPanel joinNetwork = new JPanel(new BorderLayout());
		JLabel ip = new JLabel("Adresse ip du serveur : ");
		inputIP = new JTextField();
		JLabel port = new JLabel("Port du serveur : ");
		inputPort = new JTextField();
	    JPanel center = new JPanel(new GridLayout(0,1,10,10));
	    center.add(ip);
	    center.add(inputIP);
	    center.add(port);
	    center.add(inputPort);
	    center.setBorder(new EmptyBorder(40,70,40,70));
	    JButton bReturn = new JButton("Retour");
	    bReturn.addActionListener(new bReturnListener());
	    JButton bConnection = new JButton("connexion");
	    bConnection.addActionListener(new bConnectionListener());
	    joinNetwork.add(center, BorderLayout.CENTER);
	    this.setLayout(new FlowLayout(FlowLayout.CENTER));
	    this.add(joinNetwork);
	    this.add(bConnection);
	    this.add(bReturn);
	    setVisible(true);
	    
	    Thread thread = new Thread();
	    thread.start();
	}
	
	/*
	 * Attend une reponse du reseau
	 */
	public void run()
	{
		
	    int threshold =0;
		while(true){
	        try{
	        	threshold = clientCommunication.retrievePointThreshold();
	        	System.out.println("coucou >" + threshold + "<");
	        	if(threshold>0)
	        	{
	    			setVisible(false);
	    			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    			int width = (int)screenSize.getWidth();
	    			int height= (int)screenSize.getHeight();
	        		new Game(width, height);	
	        	}
	            Thread.sleep(200);
	        }catch(InterruptedException e){}
		}
	}
	/*
	 * Affiche le menu permettant de créer un reseau
	 */
	public void addNetwork()
	{
		serverCommunication = new Server();
		ErrorHandler error = new Error();
		serverCommunication.setErrorHandler(error);
		
		
		JPanel joinNetwork = new JPanel(new BorderLayout());
		JPanel information = new JPanel(); 
		JLabel ip = new JLabel("Votre adresse ip : " + serverCommunication.getIp());
		port = new JLabel(" / Port actuel : " + serverCommunication.getPort());
		JPanel panelPort = new JPanel(); 
		JLabel newPort = new JLabel("Changer le port :");
		bPort = new JTextField("",6);
	    JButton switchPort = new JButton("Modifier le port");
	    switchPort.addActionListener(new switchPortListener());
	    JPanel center = new JPanel(new GridLayout(0,1,10,10));
	    information.add(ip);
	    information.add(port);
	    center.add(information);
	    panelPort.add(newPort);
	    panelPort.add(bPort);
	    panelPort.add(switchPort);
	    
	    center.add(panelPort);
	    center.setBorder(new EmptyBorder(40,20,40,20));
	    
	    bOpen = new JButton("Ouvrir le serveur");
	    bOpen.addActionListener(new bOpenListener());
	    bClose = new JButton("Fermer le serveur");
	    bClose.addActionListener(new bCloseListener());
	    JButton bPlay = new JButton("Lancer la partie");
	    bPlay.addActionListener(new bPlayListener());
	    JButton bReturn = new JButton("Retour");
	    bReturn.addActionListener(new bReturnListener());
	    joinNetwork.add(center, BorderLayout.CENTER);
	    this.setLayout(new FlowLayout(FlowLayout.CENTER));
	    this.add(joinNetwork);
	    
	    
	    center.add(bOpen);
	    center.add(bClose);
	    center.add(bPlay);
	    center.add(bReturn);
	    bClose.setVisible(false);
	    setVisible(true);
		
	}
	
	/*
	 * Implemente l'action associé au bouton bLocalListener.
	 */
	class bPlayListener implements ActionListener
	{
		/*
		 * Affiche la fenétre où se deroulera le jeu.
		 */	
		public void actionPerformed(ActionEvent arg0) 
		{  
			 ArrayList<Profile> lProfile = new ArrayList<Profile>();
			 for ( int key : commandsPlayer.keySet() ) {
				 lProfile.add(searchProfile(key));
			 }
			serverCommunication.sendProfiles(lProfile);
			setVisible(false);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int width = (int)screenSize.getWidth();
			int height= (int)screenSize.getHeight();
			new Game(width, height);
		}
	}
	
	 public Profile searchProfile(int _id)
	 {
		 Profile profile = new Profile();
		 
		 try {
		      InputStream is = new FileInputStream(new File("res/profiles/profiles.txt"));
		      InputStreamReader isr = new InputStreamReader(is);
			  BufferedReader br = new BufferedReader(isr);
			  String line;
		      while ((line=br.readLine())!=null)
		      {
		    		  String elem[] = line.split(",");
		    		  if(elem[0].equals(_id)) 
		    		  {
		    			  	profile.profileId = Integer.parseInt(elem[0]);
		    				profile.score = Integer.parseInt(elem[1]);
		    				profile.email = elem[2];
		    				profile.userName = elem[3];
		    				profile.password = elem[4];
		    				profile.country = elem[5];
		    				profile.timeZone = elem[6];
		    		  }
		      }		
		      br.close();  
	    }
		catch (IOException e) {
		System.out.println(e.getMessage());
		}
		 return profile;
	 }
	
	 
	 /*
		 * Implemente l'action associé au bouton bportListener.
		 */
		class switchPortListener implements ActionListener
		{
			/*
			 * Permet de changer le port utilisé
			 */	
			public void actionPerformed(ActionEvent arg0) 
			{  
				boolean boolPort = Pattern.matches("^[0-9]*$",bPort.getText());
				if(start)
				{
					Error error = new Error();
					error.displayError("Vous devez fermer le serveur avant de changer de port");
				}
				else if(boolPort)
				{
					 serverCommunication.setPort(Integer.parseInt(bPort.getText())); 
					 port.setText(" / Port actuel : "+bPort.getText());
				}
				else
				{
					Error error = new Error();
					error.displayError("Veuillez rentrer un port valide");	
				}
			}
		}
	 
	 
		/*
		 * Implemente l'action associé au bouton bReturnListener.
		 */
		class bReturnListener implements ActionListener
		{
			/*
			 * Permet de revenir en arriere.
			 */	
			public void actionPerformed(ActionEvent arg0) 
			{  
				setVisible(false);
				new Menu(tempWidth, tempHeight, commandsPlayer);
			}
		}
		
		/*
		 * Implemente l'action associé au bouton bOpenListener.
		 */
		class bOpenListener implements ActionListener
		{
			/*
			 * Permet de'ouvrir le serveur.
			 */	
			public void actionPerformed(ActionEvent arg0) 
			{  
				serverCommunication.launchServer();
				bClose.setVisible(true);
				bOpen.setVisible(false);
				start = true;
			}
		}
		/*
		 * Implemente l'action associé au bouton bCloseListener.
		 */
		class bCloseListener implements ActionListener
		{
			/*
			 * Permet de fermer le serveur
			 */	
			public void actionPerformed(ActionEvent arg0) 
			{  
				serverCommunication.closeServer();
				bClose.setVisible(false);
				bOpen.setVisible(true);
				start = false;
			}
		}
		
		/*
		 * Implemente l'action associé au bouton bConnectionListener.
		 */
		class bConnectionListener implements ActionListener
		{
			public void actionPerformed(ActionEvent arg0) 
			{  
						
				boolean boolPort = Pattern.matches("^[0-9]*$",inputPort.getText());
				if(inputIP.getText().equals("") || inputPort.getText().equals("") || !boolPort)
				{
					Error error = new Error();
					error.displayError("Veuillez rentrer un port valide");
				} else
				{
					
					ErrorHandler error = new Error();
					clientCommunication.setErrorHandler(error);
					clientCommunication.setIp(inputIP.getText());
					clientCommunication.setPort(Integer.parseInt(inputPort.getText()));
					
					clientCommunication.launchClient();
					
					
					
					for ( int key : commandsPlayer.keySet() ) {
						if(!clientCommunication.addProfile(searchProfile(key)))
						{
							commandsPlayer.remove(key);
						}
					
					}
				}
			}
		}
		
}
