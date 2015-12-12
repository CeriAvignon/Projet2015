package fr.univavignon.courbes.inter.groupe09;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.regex.*;

public class Inscription extends Fenetre implements ActionListener {
	Controle c = new Controle(); 
	private JLabel email, userName, password, country, timeZone;
	private JTextField txt1;
	private JTextField txt2;
	private JTextField txt3;
	private JTextField txt4;
	private JTextField txt5;
 
	private JPanel p1;
	private JPanel p2;
	private JButton b1;
	private JButton b2;
	private JLabel lab1;
	private JLabel lab2;
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
		txt5 = new JTextField();
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
		    
			if(txt1.getText().equals("") 
					|| txt2.getText().equals("")
					|| txt3.getText().equals("")
					|| txt4.getText().equals("")
					|| txt5.getText().equals("") 
					|| !email_2.find() 
					|| !pays_2.find()) {
				JOptionPane.showMessageDialog(this,
						"Erreur dans la saisie du formulaire", 
						"Erreur", JOptionPane.WARNING_MESSAGE);
			}	
			if ( rep == 1) {
					JOptionPane.showMessageDialog(this,
							"User name existe déjà", 
							"Erreur", JOptionPane.WARNING_MESSAGE);
			}
			else {
				c.inscriptionDe(txt1.getText(), txt2.getText(), txt3.getText(), txt4.getText(),txt5.getText());
				this.dispose();
				new Menu("LE JEUX", 500, 500);
			}
			//String email, String ps, String mdp,String pays, String temps
			
			break;
		case "ANNULER":
			new Bienvenu("ma fenere", 300, 400);
			this.dispose();
			break;
		}
	}
	
}