package fr.univavignon.courbes.inter.groupe09.configPartie;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.groupe09.Fenetre;
import fr.univavignon.courbes.inter.groupe09.Game;
import fr.univavignon.courbes.inter.groupe09.menu.Menu;

public class Localserver extends Fenetre implements ActionListener{

	
	private JLabel vide1, vide2;
	private JButton b1, b2, b3;
	private JPanel p1;
	ArrayList<Profile> serverList = new ArrayList<Profile>();
	
	public Localserver(String titre, int x, int y) {
		super(titre, x, y);
		p1 = new JPanel();
		b1 = new JButton("Partie local");
		b2 = new JButton("Partie serveur");
		b3 = new JButton("ANNULER");
		vide1 = new JLabel("");
		vide2 = new JLabel("");
		contenu.setLayout(new FlowLayout(FlowLayout.CENTER));
		contenu.add(p1);
		p1.setLayout(new GridLayout(5, 1, 10, 10));
		p1.add(vide1);
		p1.add(vide2);
		p1.add(b1);
		p1.add(b2);
		p1.add(b3);
		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);
		
		setVisible(true);
		setResizable(false);
		
	}
	
	


	@Override
	public void actionPerformed(ActionEvent event) {
		String nom = event.getActionCommand();
		switch(nom) {
		
			case "Partie local" :
				new Game("Mode Serveur", 1024, 680, serverList);
				this.dispose();
				break;
			
			case "Partie serveur" :
				new ServeurGame("Mode Client", 300, 400);
				this.dispose();				
				break;
				
			case "ANNULER"	:
				new ConfigPartie("ma fenere", 300, 400);
				this.dispose();
				break;
		}
	}
}	
	
