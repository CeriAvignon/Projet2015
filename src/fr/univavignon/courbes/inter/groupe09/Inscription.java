package fr.univavignon.courbes.inter.groupe09;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import fr.univavignon.courbes.common.Profile;

import java.io.*;
import java.util.LinkedList;
import java.util.regex.*;
/**
 * @author groupe09
 * Classe correspendant au menu afficher au joueur lorsqu'il demande à s'inscrire
 */
public class Inscription extends Fenetre implements ActionListener {

	Controle c = new Controle();
	public static Profile[] tabP ; 
	private JLabel email, userName, password, country, timeZone;
	private JTextField txt1;
	private JTextField txt2;
	private JTextField txt3;
	private JTextField txt4;
	//private JTextField txt5;
	private String [] gmt = {"gmt-9","gmt-8","gmt-7","gmt-6","gmt-5","gmt-4","gmt-3",
			"gmt-2","gmt-1","gmt","gmt+1","gmt+2","gmt+3","gmt+4","gmt+5",
			"gmt+6","gmt+7","gmt+8","gmt+9"};
	private JComboBox txt5;
 
	private JPanel p1;
	private JPanel p2;
	private JButton b1;
	private JButton b2;
	private JLabel lab1;
	private JLabel lab2;
		/**
	 * @param titre
	 * Titre de la fenetre
	 * @param x
	 * Taille en abscisse de la fenetre 
	 * @param y
	 * Taille en ordonné de la fenetre
	 */
	Inscription (String titre, int x, int y) 
	{
		super(titre, x, y);
		p1 = new JPanel();
		p2 = new JPanel();
		b1 = new JButton("S'INSCRIRE");
		b2 = new JButton("ANNULER");
		lab1 = new JLabel("Formulaire d'inscription");
		lab2 = new JLabel("");
		contenu.setLayout(new FlowLayout(FlowLayout.CENTER));
		contenu.add(p1);
		p1.setLayout(new GridLayout(8, 2, 5, 5));
		email = new JLabel("email");
		userName = new JLabel("userName");
		password = new JLabel("password");
		country = new JLabel("country");
		timeZone = new JLabel("timeZone");
		txt1 = new JTextField();
		txt2 = new JTextField();
		txt3 = new JTextField();
		txt4 = new JTextField();
		txt5 = new JComboBox(gmt);
		txt5.setSelectedIndex(10);
		//JScrollPane scrol = new JScrollPane(txt5);
		
		p1.add(lab1);
		p1.add(lab2);
		p1.add(email);
		p1.add(txt1);
		p1.add(userName);
		p1.add(txt2);
		p1.add(password);
		p1.add(txt3);
		p1.add(country);
		p1.add(txt4);
		p1.add(timeZone);
		p1.add(txt5);
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

		switch (nom) {
		case "S'INSCRIRE":
			
			Pattern email_1 = Pattern.compile("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)+$");
		    Matcher email_2 = email_1.matcher(txt1.getText());
		    Pattern pays_1 = Pattern.compile("[^0-9].");
		    Matcher pays_2 = pays_1.matcher(txt4.getText());
		    int rep = c.controlePseudo(txt2.getText());
		    String ch =(String)txt5.getSelectedItem();
			if(txt1.getText().equals("") 
					|| txt2.getText().equals("")
					|| txt3.getText().equals("")
					|| txt4.getText().equals("")
					|| ch.equals("") 
					|| !email_2.find() 
					|| !pays_2.find()) {
				JOptionPane.showMessageDialog(this,
						"Erreur dans la saisie du formulaire", 
						"Erreur", JOptionPane.WARNING_MESSAGE);
			}	
			else if ( rep == 1) {
					JOptionPane.showMessageDialog(this,
							"User name existe déjà", 
							"Erreur", JOptionPane.WARNING_MESSAGE);
			}
			else {
				user = txt1.getText();
				c.inscriptionDe(txt1.getText(), txt2.getText(), txt3.getText(), txt4.getText(), ch,0);
				this.loadProfile(profil);
				v = true;
				this.dispose();
				new Menu("LE JEUX", 500, 500);
			}
			
			
			break;
		case "ANNULER":
			new Bienvenu("ma fenere", 300, 400);
			this.dispose();
			break;
		}
	}
	public void loadProfile(LinkedList p) {
		p.add ( txt1.getText());
		p.add(txt2.getText());
		p.add( txt4.getText());
		p.add( (String)txt5.getSelectedItem());
		p.add("0");
		p.add(txt3.getText());
	}
	
	
	
	
	
}