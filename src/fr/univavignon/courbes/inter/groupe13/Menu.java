package fr.univavignon.courbes.inter.groupe13;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


public class Menu extends Fenetre{
	private int tempLargeur;
	private int tempHauteur;
	public Menu(int largeur, int hauteur) 
	{
		super(largeur, hauteur);	
		tempLargeur = largeur;
		tempHauteur = hauteur;	
		MenuNonConnecter();
	}
	public Menu(int largeur, int hauteur, boolean connecte) 
	{
		super(largeur, hauteur);	
		tempLargeur = largeur;
		tempHauteur = hauteur;
		if(connecte==true)
			MenuConnecter();	
		else
			MenuNonConnecter();
	}
	 public void MenuNonConnecter()
	  {
		    JPanel menuNonConnecter = new JPanel(new BorderLayout());
		    JButton bConnexion = new JButton("Connexion");
		    bConnexion.addActionListener(new bConnexionListener());
		    JButton bInscrire = new JButton("S'inscrire");
		    bInscrire.addActionListener(new bInscrireListener());
			JButton bQuitter = new JButton("Quitter");
		    bQuitter.addActionListener(new bQuitterListener());
		    JPanel north = new JPanel(new GridLayout(0,1,5,5));
		    JTextField userName = new JTextField();
		    north.add(new JLabel("userName", SwingConstants.CENTER));	
			north.add(userName); 
		    JTextField password = new JTextField();
		    north.add(new JLabel("password", SwingConstants.CENTER));
			north.add(password);  
		    JPanel center = new JPanel(new GridLayout(0,1,10,10));
		    center.add(bConnexion);
		    center.add(bInscrire);
		    center.add(bQuitter);
		    center.setBorder(new EmptyBorder(40,70,40,70));
		    menuNonConnecter.add(north, BorderLayout.NORTH);
		    menuNonConnecter.add(center, BorderLayout.CENTER);
		    container.setLayout(new FlowLayout(FlowLayout.CENTER));
		    container.add(menuNonConnecter);
		    setVisible(true);
	  }
	 
	 
	 public void MenuConnecter()
	  {
			JPanel menuConnecter = new JPanel(new BorderLayout());
			JButton bLocal = new JButton("Démarrer une partie locale");
			bLocal.addActionListener(new bLocalListener());
			JButton bCreerReseau = new JButton("Démarrer une partie réseau");
			bCreerReseau.addActionListener(new bCreerReseauListener());
			JButton bRejoindreReseau = new JButton("Rejoindre une partie réseau");
			bRejoindreReseau.addActionListener(new bRejoindreReseauListener());
			JButton bStatistique = new JButton("Voir mes statistiques");
			bStatistique.addActionListener(new bStatistiqueListener());		
			JButton bDeconnexion = new JButton("Deconnexion");
			bDeconnexion.addActionListener(new bDeconnexionListener());
			bStatistique.addActionListener(new bStatistiqueListener());
		    JPanel center = new JPanel(new GridLayout(0,1,10,10));
		    center.add(bLocal);
		    center.add(bCreerReseau);
		    center.add(bRejoindreReseau);
		    center.add(bStatistique);
		    center.add(bDeconnexion);
		    center.setBorder(new EmptyBorder(40,70,40,70));
		    menuConnecter.add(center, BorderLayout.CENTER);
		    container.setLayout(new FlowLayout(FlowLayout.CENTER));
		    container.add(menuConnecter);
		    setVisible(true);
	  }
	 
		class bConnexionListener implements ActionListener
		{
			public void actionPerformed(ActionEvent arg0) 
			{ 
				setVisible(false);
				new Menu(tempLargeur, tempHauteur, true);			
			}
		}
		
		class bInscrireListener implements ActionListener
		{
			public void actionPerformed(ActionEvent arg0) 
			{  
				setVisible(false);
				new Inscription(tempLargeur, tempHauteur);
			}
		}
		
		class bQuitterListener implements ActionListener
		{
			public void actionPerformed(ActionEvent arg0) 
			{  
				System.exit(0);
			}
		}
		
		class bLocalListener implements ActionListener
		{
			public void actionPerformed(ActionEvent arg0) 
			{  
				setVisible(false);
				new Jeu(tempLargeur, tempHauteur);
			}
		}
		
		class bCreerReseauListener implements ActionListener
		{
			public void actionPerformed(ActionEvent arg0) 
			{ 
			}
		}
		
		class bRejoindreReseauListener implements ActionListener
		{
			public void actionPerformed(ActionEvent arg0) 
			{  
			}
		}
		class bStatistiqueListener implements ActionListener
		{
			public void actionPerformed(ActionEvent arg0) 
			{  
			}
		}
		class bDeconnexionListener implements ActionListener
		{
			public void actionPerformed(ActionEvent arg0) 
			{  
				setVisible(false);
				new Menu(tempLargeur, tempHauteur);
			}
		}
		
}
