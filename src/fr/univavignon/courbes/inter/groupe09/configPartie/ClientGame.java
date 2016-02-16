package fr.univavignon.courbes.inter.groupe09.configPartie;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ClientProfileHandler;
import fr.univavignon.courbes.inter.groupe09.Fenetre;
import fr.univavignon.courbes.inter.groupe09.inscription.*;
import fr.univavignon.courbes.inter.groupe09.Error;
import fr.univavignon.courbes.inter.groupe09.menu.Menu;
import fr.univavignon.courbes.inter.groupe09.moteur.NetworksClient;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class ClientGame extends Fenetre implements ClientProfileHandler, ActionListener {

	
	private String ip;
	private int port = 0;
	private int nbPlayers = 0;
	private JLabel ipServer, portServer, nbPlayer, vide1, vide2, vide3, vide4;
	private JTextField ipTxt, portTxt, nbplayerTxt;
	private JButton b1, b2;
	private JPanel p1;
	NetworksClient clientPartie;
	Error erreur = new Error();
	Profile p = new Profile();
	/**
	 * @param titre
	 * Titre de la fenetre
	 * @param x
	 * Taille en abscisse de la fenetre 
	 * @param y
	 * Taille en ordonné de la fenetre
	 */
	ClientGame(String titre, int x, int y) {
		super(titre, x, y);
		clientPartie = new NetworksClient();
		//c.setErrorHandler(erreur);
		p1 = new JPanel();
		b1 = new JButton("Connexion");
		b2 = new JButton("ANNULER");
		ipTxt = new JTextField();
		portTxt = new JTextField();
		nbplayerTxt = new JTextField();
		ipServer = new JLabel("Adresse ip serveur ");
		portServer = new JLabel("Port de connexion ");
		nbPlayer = new JLabel("Nombre de joueur ");
		vide1 = new JLabel("");
		vide2 = new JLabel("");
		vide3 = new JLabel("");
		vide4 = new JLabel("");
		contenu.setLayout(new FlowLayout(FlowLayout.CENTER));
		contenu.add(p1);
		p1.setLayout(new GridLayout(6, 2, 10, 10));
		
		p1.add(vide1);
		p1.add(vide2);
		p1.add(ipServer);
		p1.add(ipTxt);
		p1.add(portServer);
		p1.add(portTxt);
		p1.add(nbPlayer);
		p1.add(nbplayerTxt);
		p1.add(vide3);
		p1.add(vide4);
		p1.add(b1);
		p1.add(b2);
		b1.addActionListener(this);
		b2.addActionListener(this);
		
		setVisible(true);
		setResizable(false);
	
	}

	@Override
	public void updateProfiles(List<Profile> profiles) {
		

	}

	@Override
	public void actionPerformed(ActionEvent event) {
		
		String nom = event.getActionCommand();

		switch(nom) {
			
			case "Connexion" :
				Error erreur = new Error();
				if ( !(ipTxt.getText().equals("")) && !(portTxt.getText().equals("")) && !(nbplayerTxt.getText().equals("")) ) {
					
					ip = ipTxt.getText();
					port = Integer.parseInt(portTxt.getText());
					nbPlayers = Integer.parseInt(nbplayerTxt.getText());
					clientPartie.setIp(ip);
					clientPartie.setPort(port);
					clientPartie.launchClient();

					new Thread(new Runnable() {
						
						@Override
						public void run() {
							
							
							try {
								for (int i = 0; i < nbPlayers; i++) {
									new Authentification("Connexion client", 400, 400, false);
									Thread.sleep(20000);
								}
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}).start();
					new Thread(new Runnable() {
						
						@Override
						public void run() {
						
							while(clientProfile.size() < 1 ){
								
							}
							System.out.println(clientProfile.get(0).userName);
							
						}
					}).start();
				}
				else {
					erreur.displayError("Valeur saisie inccorecte");
				}
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						
						while (clientProfile.size() < nbPlayers) {
							
						}
						
						
						
					}
				}).start();
				
				System.out.println("ip : "+ip+" port : "+port+" nombre de joueur : "+nbPlayers);
				
						
				break;
				
			case "ANNULER"	:
				new Menu("ma fenere", 300, 400);
				this.dispose();
				break;
		}
		
	}

}

