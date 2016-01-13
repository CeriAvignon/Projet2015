package fr.univavignon.courbes.inter.groupe09.menu;
import fr.univavignon.courbes.inter.groupe09.configPartie.*;
import fr.univavignon.courbes.inter.groupe09.Fenetre;
import fr.univavignon.courbes.inter.groupe09.inscription.Myprofil;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * @author groupe09
 * Classe correspendant au menu afficher au joueur lorsqu'il c est authentifié 
 */
public class Menu extends Fenetre implements ActionListener {
	/**
	 * boutton de menu
	 */
	private JButton b1,b2,b3,b4;
	/**
	 * panel contenant tout le menu ( toute le contenu de la fentre)
	 */
	private JPanel j;
	/**
	 * label pour ajuster l'affichage des bouttons menu
	 */
	private JLabel l,l2;

	/**
	 * @param titre
	 * Titre de la fenetre
	 * @param x
	 * Taille en abscisse de la fenetre 
	 * @param y
	 * Taille en ordonné de la fenetre
	 */
	public Menu(String titre, int x, int y) {
		
		super(titre, x, y);
		j = new JPanel();
		l2 = new JLabel();
		l = new JLabel("ATCHUNG DIE KURV");
		contenu = getContentPane();
		contenu.setLayout(new FlowLayout(FlowLayout.CENTER));
		contenu.add(j);
		j.setLayout(new GridLayout(5, 0, 10, 20));
		j.add(l2);
		j.add(l);
		b1 = new JButton("JOUER");
		b2 = new JButton("PROFIL");
		b4 = new JButton("QUITTER");
		j.add(b1);
		j.add(b2);
		j.add(b4);
		b1.addActionListener(this);
		b2.addActionListener(this);
		b4.addActionListener(this);
		setVisible(true);
		setResizable(false);
	}
	@Override
	public void actionPerformed(ActionEvent event) {
		String nom = event.getActionCommand();
		switch (nom) {
		case "JOUER": 
			new ConfigPartie("Choix du mode", 300, 300);
			this.dispose();
			
			break;
		case "PROFIL": 
			new Myprofil ("myprofil", 400, 400);
			this.dispose();
			
			break;
		case "QUITTER": 
			System.exit(0);
			break;
		}
	}

}