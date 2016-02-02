package fr.univavignon.courbes.inter.groupe13;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;

/**
 * @author zach
 * Main menu in which the user can: 
 * - select the game type
 * - view the currently existing profiles or the statistics
 */
public class Menu extends Window{

	private static final long serialVersionUID = 1L;
	private int width, height;

	/**
	 * Initialise les variables tempWidth et tempWidth puis affiche le menu qui
	 * permet de choisir les joueurs et leurs touches
	 * 
	 * @param width Largeur de la fenêtre.
	 * @param height Hauteur de la fenêtre.
	 */
	public Menu(int width, int height) {
		super(width, height);
		this.width = width;
		this.height = height;
		menuPlayer();
	}

	/*
	 * affciche le menu qui permet de choisir les joueurs (en local) et leurs
	 * touches
	 */
	public void menuPlayer() {
		this.setLayout(new GridLayout(6, 1));

		JButton localGame = new JButton("Partie locale");
		JButton serverGame = new JButton("Créer une partie réseau");
		JButton clientGame = new JButton("Rejoindre une partie réseau");
		JButton profils = new JButton("Profils");
		JButton stats = new JButton("Statistiques");
		JButton quit = new JButton("Quitter");
		
		localGame.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				setVisible(false);
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				int width = (int)screenSize.getWidth();
				int height= (int)screenSize.getHeight();			
				new Game(width, height);
			}
		});
		
		serverGame.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				setVisible(false);
				new Network(width, height, false);
				
			}
		});
		
		clientGame.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				setVisible(false);
				new Network(width, height, true);
				
			}
		});

		quit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Menu.this.dispatchEvent(new WindowEvent(Menu.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		profils.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				DisplayProfileFrame df = new DisplayProfileFrame(Menu.this);
				Menu.this.setVisible(false);
			}
		});

		this.add(localGame);
		this.add(clientGame);
		this.add(serverGame);
		this.add(profils);
		this.add(stats);
		this.add(quit);

		this.setVisible(true);

	}

}


//package fr.univavignon.courbes.inter.groupe13;
//
//import java.awt.*;
//import java.awt.event.*;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.*;
//
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//
//
//
//public class Menu extends Window implements KeyListener{
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//	private int tempWidth, tempHeight;
//	private JTextField userName;
//	private JPasswordField password;
//	private Map<Integer,int[]> commandsPlayer;
//	private JButton bLeft, bRight;
//	private int leftRight[];
//	private JLabel textPlayer1,textPlayer2,textPlayer3,textPlayer4;
//	/*
//	 * Initialise les variables tempWidth et tempWidth puis affiche le menu qui permet de choisir les joueurs et leurs touches
//	 * @param width     Largeur de la fenêtre.
//	 * @param height    Hauteur de la fenêtre.
//	 */
//	public Menu(int width, int height) 
//	{
//		super(width, height);	
//		tempWidth = width;
//		tempHeight = height;
//		leftRight= new int[2];
//		commandsPlayer = new HashMap<>();
//		MenuPlayer();	
//	}
//	
//	/*
//	 * Initialise les variables tempWidth, tempWidth et commandsPlayer puis affiche le menu qui permet de choisir le type de partie
//	 * @param width     Largeur de la fenêtre.
//	 * @param height    Hauteur de la fenêtre.
//     * @param _commandsPlayer la liste des id des joueurs et de leurs touches 
//	 */
//	
//	public Menu(int width, int height, Map<Integer,int[]> _commandsPlayer) 
//	{
//		super(width, height);	
//		tempWidth = width;
//		tempHeight = height;
//		commandsPlayer = new HashMap<>();
//		commandsPlayer = _commandsPlayer;
//		MenuGame();
//	}
//	
//	 public void keyPressed(KeyEvent keyEvent) {
//   	   if(keyEvent.getSource() == bLeft)
//   	   {
//   		   bLeft.setText(KeyEvent.getKeyText(keyEvent.getKeyCode()));
//   		   leftRight[0]= keyEvent.getKeyCode();
//		  
//   	   }
//   	   if(keyEvent.getSource() == bRight)
//   	   {
//   		   bRight.setText(KeyEvent.getKeyText(keyEvent.getKeyCode()));
//   		   leftRight[1]= keyEvent.getKeyCode();
//   	   }
//	 }
//
//     public void keyReleased(KeyEvent keyEvent) {
//     }
//
//     public void keyTyped(KeyEvent keyEvent) {
//     }
//     
//     /*
//      * affciche le menu qui permet de choisir les joueurs (en local) et leurs touches
//      */
//	public void MenuPlayer()
//	{
//		leftRight[0] =-1;
//		leftRight[1] = -1;
//		GridLayout gl = new GridLayout(1, 3);
//		JPanel panelGL = new JPanel();
//		panelGL.setLayout(gl);
//		gl.setHgap(10);
//		
//		JPanel b1 = new JPanel();
//		b1.setLayout(new BoxLayout(b1, BoxLayout.LINE_AXIS));
//		b1.add(new JLabel("Ajouter un Joueur"));
//	    JPanel b2 = new JPanel();
//	    b2.setLayout(new BoxLayout(b2, BoxLayout.LINE_AXIS));
//	    b2.add(new JLabel("UserName"));
//	    userName = new JTextField();
//	    b2.add(userName);
//	    JPanel b3 = new JPanel();
//	    b3.setLayout(new BoxLayout(b3, BoxLayout.LINE_AXIS));
//	    b3.add(new JLabel("Password"));
//	    password = new JPasswordField();
//	    b3.add(password);
//	    JPanel b4 = new JPanel();
//	    b4.setLayout(new BoxLayout(b4, BoxLayout.LINE_AXIS));
//	    bLeft = new JButton("Bouton : direction gauche");
//	    b4.add(bLeft);
//	    bLeft.addKeyListener(this);
//	    bRight = new JButton("Bouton : direction droite");
//	    b4.add(bRight);
//	    bRight.addKeyListener(this);
//	    JPanel b5 = new JPanel();
//	    b5.setLayout(new BoxLayout(b5, BoxLayout.LINE_AXIS));
//	    JButton bConnection = new JButton("Connexion");
//	    b5.add(bConnection);
//	    bConnection.addActionListener(new bConnectionListener());
//	    JPanel b6 = new JPanel();
//	    b6.setLayout(new BoxLayout(b6, BoxLayout.PAGE_AXIS));
//	    b6.add(b1);
//	    b6.add(b2);
//	    b6.add(b3);
//	    b6.add(b4);
//	    b6.add(b5);
//	    JButton bRegistration = new JButton("S'inscrire");
//	    bRegistration.addActionListener(new bRegistrationListener()); 
//	    JButton bContinue = new JButton("Continuer");
//		bContinue.addActionListener(new bContinueListener());
//		JButton bExit = new JButton("Quitter");
//		bExit.addActionListener(new bExitListener());
//		
//		JPanel panelPlayers = new JPanel();
//		JPanel allPlayers = new JPanel();
//		allPlayers.setLayout(new BoxLayout(allPlayers, BoxLayout.PAGE_AXIS));
//		
//		
//		JPanel player1 = new JPanel();
//		player1.setLayout(new BoxLayout(player1, BoxLayout.LINE_AXIS));
//		textPlayer1 = new JLabel();
//		player1.add(textPlayer1);
//		
//	    JPanel player2 = new JPanel();
//	    player2.setLayout(new BoxLayout(player2, BoxLayout.LINE_AXIS));
//		textPlayer2 = new JLabel();
//		player2.add(textPlayer2);
//		
//		JPanel player3 = new JPanel();
//		player3.setLayout(new BoxLayout(player3, BoxLayout.LINE_AXIS));
//		textPlayer3 = new JLabel();
//		player3.add(textPlayer3);
//		
//		JPanel player4 = new JPanel();
//		player4.setLayout(new BoxLayout(player4, BoxLayout.LINE_AXIS));
//		textPlayer4 = new JLabel();
//		player4.add(textPlayer4);
//		
//		allPlayers.add(player1);
//		allPlayers.add(player2);
//		allPlayers.add(player3);
//		allPlayers.add(player4);
//		panelPlayers.add(allPlayers);
//		
//		panelGL.add(bRegistration);
//		panelGL.add(bContinue);
//		panelGL.add(bExit);
//	    JPanel center = new JPanel(new GridLayout(0,1,10,10));
//	    center.add(panelGL);
//	    center.add(b6);
//	    center.add(panelPlayers);
//	    center.setBorder(new EmptyBorder(40,70,40,70));
//	    JPanel menuPlayer = new JPanel(new BorderLayout());
//	    menuPlayer.add(center, BorderLayout.CENTER);
//	    
//	    
//	    this.setLayout(new FlowLayout(FlowLayout.CENTER));
//	    this.add(menuPlayer);
//	    
//	    this.setVisible(true);
//		
//		
//		
//	}
//	
//	 
//		/*
//		 * Affiche le menu du choix du type de partie
//		 */	 
//	 public void MenuGame()
//	  {
//			JPanel loggedMenu = new JPanel(new BorderLayout());
//			JButton bLocal = new JButton("Démarrer une partie locale");
//			bLocal.addActionListener(new bLocalListener());
//			JButton bAddNetwork = new JButton("Démarrer une partie réseau");
//			bAddNetwork.addActionListener(new bAddNetworkListener());
//			JButton bJoinNetwork = new JButton("Rejoindre une partie réseau");
//			bJoinNetwork.addActionListener(new bJoinNetworkListener());
//			JButton bReturn = new JButton("Retour");
//			bReturn.addActionListener(new bReturnListener());
//		    JPanel center = new JPanel(new GridLayout(0,1,10,10));
//		    center.add(bLocal);
//		    center.add(bAddNetwork);
//		    center.add(bJoinNetwork);
//		    center.add(bReturn);
//		    center.setBorder(new EmptyBorder(40,70,40,70));
//		    loggedMenu.add(center, BorderLayout.CENTER);
//		    this.setLayout(new FlowLayout(FlowLayout.CENTER));
//		    this.add(loggedMenu);
//		    setVisible(true);
//	  }
//	 
//		/*
//		 * Verifie si le couple username, password est trouvable dans le fichier des profiles.
//		 * @param UserName    pseudo écrit par l'utilisateur.
//		 * @param Password    mot de passe écrit par l'utilisateur.
//		 * @return vrai(true) si le couple existe ou faux(false) si le couple est introuvable
//		 */
//	 public boolean login(String UserName, String Password)
//	 {
//		 try {
//		      InputStream is = new FileInputStream(new File("res/profiles/profiles.txt"));
//		      InputStreamReader isr = new InputStreamReader(is);
//			  BufferedReader br = new BufferedReader(isr);
//			  String line;
//		      while ((line=br.readLine())!=null)
//		      {
//		    		  String elem[] = line.split(",");
//		    		  if(elem[3].equals(UserName)) 
//		    		  {
//		    			  
//			    		  if(elem[4].equals(Password)) 
//			    		  {
//
//			    			  commandsPlayer.put( Integer.parseInt(elem[0]), new int[] {leftRight[0],leftRight[1]});
//			    			  int nb = commandsPlayer.size();
//			    			  if(nb==1)
//			    			  {
//			    				  textPlayer1.setText(elem[3] + " | gauche = " + KeyEvent.getKeyText(leftRight[0]) +" , droite = " + KeyEvent.getKeyText(leftRight[1]));
//			    			  }
//			    			  else if(nb == 2)
//			    			  {
//			    				  textPlayer2.setText(elem[3] + " | gauche = " + KeyEvent.getKeyText(leftRight[0]) +" , droite = " + KeyEvent.getKeyText(leftRight[1]));
//			    			  }  
//			    			  else if(nb == 3)
//			    			  {
//			    				  textPlayer3.setText(elem[3] + " | gauche = " + KeyEvent.getKeyText(leftRight[0]) +" , droite = " + KeyEvent.getKeyText(leftRight[1]));
//			    			  } else
//			    			  {
//			    				  textPlayer4.setText(elem[3] + " | gauche = " + KeyEvent.getKeyText(leftRight[0]) +" , droite = " + KeyEvent.getKeyText(leftRight[1]));
//			    			  }
//			    			  
//			    			  br.close();
//			    			  return true;
//			    		  }
//		    		  }
//		      }		
//		      br.close();  
//	    }
//		catch (IOException e) {
//		System.out.println(e.getMessage());
//		}
//		 return false;
//		 
//	 }
//		/*
//		 * Implemente l'action associé au bouton bConnectionListener.
//		 */	 
//		class bConnectionListener implements ActionListener
//		{
//			/*
//			 * Permet de se connecter.
//			 */	
//			public void actionPerformed(ActionEvent arg0) 
//			{ 
//				String stringPassword = String.valueOf(password.getPassword());
//				
//				if(userName.getText().equals("") || stringPassword.equals(""))
//				{
//					Error error = new Error();
//					error.displayError("Il faut remplir l'UserName et le Password pour se connecter.");
//				}
//				else if(leftRight[0] == -1 || leftRight[1] ==-1)
//				{
//					Error error = new Error();
//					error.displayError("Il faut choisir un boutton pour aller à droite ET un bouton pour aller à gauche.");
//				}
//				else
//				{
//					if(login(userName.getText(), stringPassword))
//					{
//						userName.setText("");
//						password.setText("");
//						bLeft.setText("Bouton : direction gauche");
//						bRight.setText("Bouton : direction droite");
//					}
//					else
//					{
//						Error error = new Error();
//						error.displayError("Profil introuvable.");
//					}
//				}
//			}
//		}
//		
//		/*
//		 * Implemente l'action associé au bouton bContinueListener. 
//		 * Permet d'aller au menu du choix du type de partie.
//		 */	 
//		class bContinueListener implements ActionListener
//		{
//			/*
//			 * Affiche la fenêtre d'inscription.
//			 */	
//			public void actionPerformed(ActionEvent arg0) 
//			{  
//				if(commandsPlayer.size()>0)
//				{
//					setVisible(false);
//					new Menu(tempWidth,tempHeight, commandsPlayer);
//					
//				}else{
//					Error error = new Error();
//					error.displayError("Il n'y a aucun joueur");
//				}
//			}
//		}
//		
//		
//		/*
//		 * Implemente l'action associé au bouton bRegistrationListener.
//		 */	 
//		class bRegistrationListener implements ActionListener
//		{
//			/*
//			 * Affiche la fenêtre d'inscription.
//			 */	
//			public void actionPerformed(ActionEvent arg0) 
//			{  
//				setVisible(false);
//				new Registration(tempWidth, tempHeight);
//			}
//		}
//		
//		/*
//		 * Implemente l'action associé au bouton bExitListener.
//		 */	 
//		class bExitListener implements ActionListener
//		{
//			/*
//			 * Permet de quitter le programme.
//			 */	
//			public void actionPerformed(ActionEvent arg0) 
//			{  
//				System.exit(0);
//			}
//		}
//		
//		/*
//		 * Implemente l'action associé au bouton bLocalListener.
//		 */
//		class bLocalListener implements ActionListener
//		{
//			/*
//			 * Affiche la fenétre où se deroulera le jeu.
//			 */	
//			public void actionPerformed(ActionEvent arg0) 
//			{  
//				setVisible(false);
//				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//				int width = (int)screenSize.getWidth();
//				int height= (int)screenSize.getHeight();			
//				new Game(width, height);
//			}
//		}
//		
//
//		
//		/*
//		 * Implemente l'action associé au bouton bAddNetworkListener.
//		 */
//		class bAddNetworkListener implements ActionListener
//		{
//			/*
//			 * Affiche la fenêtre permettant de créer un réseau.
//			 */	
//			public void actionPerformed(ActionEvent arg0) 
//			{ 
//				setVisible(false);
//				new Network(tempWidth, tempHeight, false);
//			}
//		}
//		
//		/*
//		 * Implemente l'action associé au bouton bJoinNetworkListener.
//		 */
//		class bJoinNetworkListener implements ActionListener
//		{
//			/*
//			 * Affiche la fenêtre permettant de rejoindre un réseau.
//			 */	
//			public void actionPerformed(ActionEvent arg0) 
//			{  
//				setVisible(false);
//				new Network(tempWidth, tempHeight, true);
//			}
//		}
//		
//		
//		/*
//		 * Implemente l'action associé au bouton bLogOutListener.
//		 */
//		class bReturnListener implements ActionListener
//		{
//			/*
//			 * Permet de se deconnecter.
//			 */	
//			public void actionPerformed(ActionEvent arg0) 
//			{  
//				setVisible(false);
//				new Menu(tempWidth, tempHeight);
//			}
//		}
//		
//}
