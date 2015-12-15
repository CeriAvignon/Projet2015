package fr.univavignon.courbes.inter.groupe13;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Inscription extends Fenetre {
	private int tempLargeur;
	private int tempHauteur;
	
	private JLabel userName;
	private JLabel password ;
	private JLabel email;
	private JLabel country;
	private JLabel timeZone;
	public Inscription(int largeur, int hauteur) 
	{
		super( largeur, hauteur);	 
		tempLargeur = largeur;
		tempHauteur = hauteur;
		
	    JPanel FormulaireInscription = new JPanel();

	    FormulaireInscription.setLayout(new GridLayout(6, 2, 10, 10));
		userName = new JLabel("userName");
		FormulaireInscription.add(userName);
		JTextField tUserName = new JTextField();
		FormulaireInscription.add(tUserName);
		password = new JLabel("password");
		FormulaireInscription.add(password);
		JTextField tPassword = new JTextField();
		FormulaireInscription.add(tPassword);
		email = new JLabel("email");
		FormulaireInscription.add(email);
		JTextField tEmail = new JTextField();
		FormulaireInscription.add(tEmail);
		country = new JLabel("country");
		FormulaireInscription.add(country);
		JTextField tCountry = new JTextField();
		FormulaireInscription.add(tCountry);
		timeZone = new JLabel("timeZone");
		FormulaireInscription.add(timeZone);
		JTextField tTimeZone = new JTextField();
		FormulaireInscription.add(tTimeZone);
	    JButton bInscription = new JButton("Valider l'inscription");
		FormulaireInscription.add(bInscription);
		bInscription.addActionListener(new bInscriptionListener());
		JButton bRetour = new JButton("Retour");
		FormulaireInscription.add(bRetour);
		bRetour.addActionListener(new bRetourListener());
		container.setLayout(new FlowLayout(FlowLayout.CENTER));
		container.add(FormulaireInscription);
		setVisible(true);
	}
	
	
	class bInscriptionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0) 
		{  
		}
	}
	
	class bRetourListener implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0) 
		{  
			setVisible(false);
			new Menu(tempLargeur,tempHauteur);
		}
	}
}
