package fr.univavignon.courbes.inter.groupe09;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Bienvenu extends Fenetre implements ActionListener {


	private JButton b1,b2,b3;
	private JPanel j;
	private JLabel l;
	
	Bienvenu(String titre, int x, int y) 
	{
		super(titre, x, y);
		j = new JPanel();
		l = new JLabel("!!!!!! BIENVENUE !!!!!!");
		contenu.setLayout(new FlowLayout(FlowLayout.CENTER));
		j.setLayout(new GridLayout(4, 0, 50, 25));
		contenu.add(j);
		j.add(l);
		b1 = new JButton("INSCRIPTION");
		b2 = new JButton("AUTHENTIFICATION");
		b3 = new JButton("QUITTER");
		j.add(b1);
		j.add(b2);
		j.add(b3);
		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);
		setVisible(true);
		setResizable(false);
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent event) {
		String nom = event.getActionCommand();
		switch (nom) {
		case "INSCRIPTION": 
			new Inscription("Inscription", 400, 400);
			this.dispose();
			break;
		case "AUTHENTIFICATION": 
			new Authentification("Authentification", 400, 400);
			this.dispose();
			break;
		case "QUITTER": 
			System.exit(0);
			break;
		}
	}
	
}
