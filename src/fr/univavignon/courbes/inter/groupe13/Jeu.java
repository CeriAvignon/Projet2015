package fr.univavignon.courbes.inter.groupe13;

import java.awt.*;
import javax.swing.JPanel;

public class Jeu extends Fenetre {
	
	private JPanel terrain;
	private JPanel donnee;
	public Jeu(int largeur, int hauteur) 
	{
			super( largeur, hauteur);
			JPanel jeu = new JPanel(new GridBagLayout());
			jeu.setPreferredSize(new Dimension(100, 100));
			terrain = new JPanel(new GridLayout());
			terrain.setPreferredSize(new Dimension(100, 100));
			donnee = new JPanel(new GridLayout());
		    donnee.setPreferredSize(new Dimension(100, 100));
		    GridBagConstraints gbc = new GridBagConstraints(); 
		    gbc.fill = GridBagConstraints.BOTH;
		    gbc.weightx = 0.75;
		    gbc.weighty = 1;
		    gbc.gridx = 0;
		    gbc.gridy = 0;
		    jeu.add(terrain, gbc);
		    gbc.fill = GridBagConstraints.BOTH;
		    gbc.weightx = 0.25;
		    gbc.gridx = 1;		    
		    jeu.add(donnee, gbc);
		    container.add(jeu);
		    setVisible(true);
		    start();
	  }
	

	
	 public void start()
	 {
		/*  Board board = init(width, height, nbJoueur());
		  init(board, int pointThreshold, List<Profile> players, terrain, donnee);
		  long elapsedTime = System.currentTimeMillis();
		  while(true)
		  {
	
			  
			  update(elapsedTime, Map<Integer,Direction> commands);
			  elapsedTime = System.currentTimeMillis() - elapsedTime;
			  update();
			  end(); 
		  }*/
	 }

}
