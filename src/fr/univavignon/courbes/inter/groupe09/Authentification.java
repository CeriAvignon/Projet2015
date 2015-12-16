package fr.univavignon.courbes.inter.groupe09;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/** 
 * @author Groupe09
 * Classe correspendant au menu d'authentification afficher au joueur lorsqu'il demande à s'enthetifier
 */
public class Authentification extends Fenetre implements ActionListener {

	private JLabel userName, password, ath, vide;
	private JTextField txt1;
	private JTextField txt2;
	private JPanel p1;
	private JPanel p2;
	private JButton b1;
	private JButton b2;
	Controle c = new Controle();

	/**
	 * @param titre
	 * Titre de la fenetre
	 * @param x
	 * Taille en abscisse de la fenetre 
	 * @param y
	 * Taille en ordonné de la fenetre
	 */
	Authentification(String titre, int x, int y) 
	
	{
		super(titre, x, y);
		p1 = new JPanel();
		p2 = new JPanel();
		b1 = new JButton("Connexion");
		b2 = new JButton("ANNULER");
		ath = new JLabel("AUTHENTIFICATION");
		vide = new JLabel("");
		contenu.setLayout(new FlowLayout(FlowLayout.CENTER));
		contenu.add(p1);
		p1.setLayout(new GridLayout(4, 2, 5, 5));
		userName = new JLabel("userName");
		password = new JLabel("password");
		txt1 = new JTextField();
		txt2 = new JTextField();
		
		p1.add(ath);
		p1.add(vide);
		p1.add(userName);
		p1.add(txt1);
		p1.add(password);;
		p1.add(txt2);
		p1.add(b1);
		p1.add(b2);
		b1.addActionListener(this);
		b2.addActionListener(this);

		setVisible(true);
		setResizable(false);
			
		
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent event) {
		String nom = event.getActionCommand();
		int rep = c.controleConnexion(txt1.getText(), txt2.getText());
		switch (nom) {
		case "Connexion":
			switch (rep) {
			case 0: 
				JOptionPane.showMessageDialog(this,"Veuillez saisire votre Username","Erreur", JOptionPane.WARNING_MESSAGE);
	
				break;
			case 1: 
				this.dispose();
				new Menu("JEUX", 500, 500);
				break;
			case 2: 
				JOptionPane.showMessageDialog(this,"Username incorrecte","Erreur", JOptionPane.WARNING_MESSAGE);

				break;
			case 3: 
				JOptionPane.showMessageDialog(this,"Veuillez saisire votre Mot de passe","Erreur", JOptionPane.WARNING_MESSAGE);
	
				break;
			case 4: 
				JOptionPane.showMessageDialog(this,"Mot de passe Incorrecte","Erreur", JOptionPane.WARNING_MESSAGE);
	
				break;
			}
			break;
		case "ANNULER":
			new Bienvenu("ma fenere", 300, 400);
			this.dispose();
			break;
		}
	}
	
}