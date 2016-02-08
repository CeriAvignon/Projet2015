package fr.univavignon.courbes.inter.simpleimpl.game;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.simpleimpl.Window;
import fr.univavignon.courbes.inter.simpleimpl.menus.LocalProfileSelector;
import fr.univavignon.courbes.physics.PhysicsEngine;

public class Game extends Window{
	
	public static final String CONSTANTE_1 = "Round";
	private static final long serialVersionUID = 1L;
	private JPanel gameBoard, dataBoard;
	private int tempWidth, tempHeight;
	private boolean fin;
	private int ids[];
	private Map<Integer, Direction> commands;
	private Map<Integer,int[]> commandsPlayer;
	private ArrayList<LocalProfileSelector> players;
	
	/**
	 * Créer une fenêtre contenant le plateau du jeu et les données du jeu.
	 * @param width  Largeur de la fenêtre.
	 * @param height Hauteur de la fenêtre.
	 */
	public Game(int width, int height, ArrayList<LocalProfileSelector> players) 
	{
		super( width, height);
		tempWidth=width;
		tempHeight=height;
		this.players = players;
		
		commandsPlayer =  new HashMap<>();
		
		/* For each player */
		for(LocalProfileSelector lps : players){
			
			/* Set its keys */
			KeyEvent left = lps.getC_profile().getLeft();
			KeyEvent right = lps.getC_profile().getLeft();
			
			int[] playerKeys = new int[2];
			playerKeys[0] = left.getKeyCode();
			playerKeys[1] = right.getKeyCode();
			
		}
//		commandsPlayer = _commandsPlayer;
		
		JPanel game = new JPanel(new GridBagLayout());
		game.setPreferredSize(new Dimension(100, 100));
		gameBoard = new JPanel(new GridLayout());
		gameBoard.setPreferredSize(new Dimension(100, 100));
		dataBoard = new JPanel(new GridLayout());
	    dataBoard.setPreferredSize(new Dimension(100, 100));
	    GridBagConstraints gbc = new GridBagConstraints(); 
	    gbc.fill = GridBagConstraints.BOTH;
	    gbc.weightx = 0.75;
	    gbc.weighty = 1;
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    game.add(gameBoard, gbc);
	    gbc.fill = GridBagConstraints.BOTH;
	    gbc.weightx = 0.25;
	    gbc.weighty = 1;
	    gbc.gridx = 1;		    
	    game.add(dataBoard, gbc);
	    this.add(game);
	    this.setVisible(true);
	    gameThread thread = new gameThread();
	    thread.start();
	  }

	
	public class gameThread extends Thread implements KeyListener{
		
	//Ajoute la direction gauche ou droite a la map<integer,direction> si l'utilisateur a appuyer sur une touche gauche/droite
      public void keyPressed(KeyEvent keyEvent) {
    	  int i=0;
    	  for ( int key : commandsPlayer.keySet() ) {
    		  if (keyEvent.getKeyCode() == commandsPlayer.get(key)[0])
    			  commands.put(ids[i], Direction.LEFT);
              if (keyEvent.getKeyCode() == commandsPlayer.get(key)[1])
            	  commands.put(ids[i], Direction.RIGHT);
 			 i++;
 		 }
            if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
            	fin=true;
            }
	      }
      public void keyReleased(KeyEvent keyEvent) {
    	  int i=0;
    	  for ( int key : commandsPlayer.keySet() ) {
    		  if (keyEvent.getKeyCode() == commandsPlayer.get(key)[0] || keyEvent.getKeyCode() == commandsPlayer.get(key)[1])
    			  commands.put(ids[i], Direction.NONE);
 			 i++;
 		 }
      }

      public void keyTyped(KeyEvent keyEvent) {
      }
	        
	/**
	 * Lance une partie locale.
	 */	
	 public void run()
	 {	
		 ids = new int[commandsPlayer.size()];
		 int i=0;
		 ArrayList<Profile> lProfile = new ArrayList<Profile>();
		 for ( int key : commandsPlayer.keySet() ) {
			 ids[i]=key;
			 lProfile.add(searchProfile(key));
			 i++;
		 }

		 
		 Board board = new Board();
		 
		 
		 /* instancier le moteur physique et le moteur graphique*/
		 PhysicsEngine physicsEngine = new Round();
		// GraphicDisplay graphicDisplay = new GraphicDisplay();
		 
		 commands = new HashMap<>();
	     addKeyListener(this);
	     fin=false;
		 long time = System.currentTimeMillis();
		 board = physicsEngine.init( (int)(tempWidth*0.75), tempHeight, ids);
		 long elapsedTime = time;
		// graphicDisplay.init(board, 40, lProfile, gameBoard, dataBoard);
		 while(true)
		 {  
			 elapsedTime =  System.currentTimeMillis() - time;
			 physicsEngine.update(elapsedTime,commands);
			 time = System.currentTimeMillis();
			 
		//	 graphicDisplay.update();
			 gameBoard.validate();

			 gameBoard.repaint();
			 try {Thread.sleep(20);}
				catch(InterruptedException ex) {Thread.currentThread().interrupt();}
			 if (fin)
				 break;
		 }
	//	 graphicDisplay.end();
		 gameBoard.validate();
		 gameBoard.repaint();
		 fin=false;
		 while (!fin){
			 try {Thread.sleep(20);}
				catch(InterruptedException ex) {Thread.currentThread().interrupt();}
		 }
		 setVisible(false);
//		 new Game(tempWidth, tempHeight);
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
	}
}
