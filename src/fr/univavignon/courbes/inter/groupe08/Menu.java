package fr.univavignon.courbes.inter.groupe08;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Menu {

	JFrame frame;
	private JTextField textField;
	private JTextField Login_inscription;
	JTextField Login_Co;
	private JTextField textField_1;
	private JTextField Password_inscription;
	private JTextField Password;
	private JTextField Email;
	protected String user;

	/**
	 * Launch the application.
	 * @param args 
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Menu window = new Menu();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Menu() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		final File file = new File("user.txt");
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.NORTH);
		
		JPanel Connexion = new JPanel();
		tabbedPane.addTab("Connexion", null, Connexion, null);
		Connexion.setLayout(new GridLayout(6, 2, 10, 10));
		
		Login_Co = new JTextField();
		Connexion.add(Login_Co);
		Login_Co.setColumns(10);
		
		Password = new JPasswordField();
		Connexion.add(Password);
		Password.setColumns(10);
		
		JButton btnConnexion = new JButton("Connexion");
		btnConnexion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean connect = false;
				String laChainne;
			    InputStream fis;
			    InputStreamReader isr;
			    BufferedReader bis;        
			    try {
			      fis = new FileInputStream(new File("user.txt"));
			      isr = new InputStreamReader(fis);
			      bis = new BufferedReader(isr);
			      
			      try {		
			    	  while (!connect && (laChainne = bis.readLine()) != null) { 
			    		  String elem[] = laChainne.split("\\|");
			    		  if(elem[1].equals(Login_Co.getText().toString())) {
			    			  if(elem[0].equals(Password.getText().toString())) {
			    				  connect = true;
			    			  }
			    		  }
			    	  }
			      }
			      catch(NumberFormatException e) {
			    	  e.printStackTrace();
			      }
			      bis.close();
			                
			    } 
			    catch (FileNotFoundException e) {
			      e.printStackTrace();
			    } 
			    catch (IOException e) {
			      e.printStackTrace();
			    }
			    if(connect){
			    	Stats window = new Stats();
					window.frame.setVisible(true);
					frame.setVisible(false);
			    }
			}
		});
		Connexion.add(btnConnexion);
		
		JPanel Inscription = new JPanel();
		tabbedPane.addTab("Inscription", null, Inscription, null);
		Inscription.setLayout(null);
		
		JLabel lblIdentifiant = new JLabel("Identifiant");
		lblIdentifiant.setBounds(0, 2, 53, 24);
		Inscription.add(lblIdentifiant);
		
		Login_inscription = new JTextField();
		Login_inscription.setBounds(100, 2, 303, 25);
		Inscription.add(Login_inscription);
		Login_inscription.setColumns(10);
		
		Password_inscription = new JPasswordField();
		Password_inscription.setBounds(100, 48, 303, 25);
		Inscription.add(Password_inscription);
		Password_inscription.setColumns(10);
		
		Email = new JTextField();
		Email.setBounds(100, 107, 303, 25);
		Inscription.add(Email);
		Email.setColumns(10);
		
		JLabel lblMotDePasse = new JLabel("Mot de passe");
		lblMotDePasse.setBounds(0, 53, 53, 24);
		Inscription.add(lblMotDePasse);
		
		JLabel lblEmail = new JLabel("E-mail");
		lblEmail.setBounds(0, 107, 53, 24);
		Inscription.add(lblEmail);
		
		JButton btnInscription = new JButton("Inscription");
		btnInscription.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {  
				FileWriter fw;
			    try {
			        fw = new FileWriter(file,true);
			        fw.write(Password_inscription.getText().toString()+"|"+Login_inscription.getText().toString()+"|"+Email.getText().toString()+"\r\n");
			        fw.close();
			      } catch (FileNotFoundException e) {
			        e.printStackTrace();
			      } catch (IOException e) {
			        e.printStackTrace();
			      }
			}
		});
		btnInscription.setBounds(190, 156, 89, 23);
		Inscription.add(btnInscription);
	}

}
