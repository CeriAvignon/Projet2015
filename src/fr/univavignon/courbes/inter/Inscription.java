package fr.univavignon.courbes.inter;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Inscription extends Fenetre {
	private JLabel login;
	private JLabel password ;
	private JLabel email;
	public Inscription() {
		super( 400, 200);
	    JPanel RegisterForm = new JPanel();
	    RegisterForm.setLayout(new GridLayout(6, 2, 10, 10));
		login = new JLabel("Login");
		RegisterForm.add(login);
		JTextField login = new JTextField();
		RegisterForm.add(login);
		password = new JLabel("Password");
		RegisterForm.add(password);
		JTextField Password = new JTextField();
		RegisterForm.add(Password);
		email = new JLabel("Email");
		RegisterForm.add(email);
		JTextField Email = new JTextField();
		RegisterForm.add(Email);
	    JButton Inscription = new JButton("S'inscrire");
	    RegisterForm.add(Inscription);
		JButton Retour = new JButton("Retour au menu");
		RegisterForm.add(Retour);
		Retour.addActionListener(new RetourListen());
		container.setLayout(new FlowLayout(FlowLayout.CENTER));
		container.add(RegisterForm);
		setVisible(true);
	}
	
	class RetourListen implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {  
			setVisible(false);
			new Menu(400,250,false);
		}
	}
}
