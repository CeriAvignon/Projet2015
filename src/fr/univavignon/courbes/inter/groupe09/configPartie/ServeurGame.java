package fr.univavignon.courbes.inter.groupe09.configPartie;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ServerProfileHandler;
import fr.univavignon.courbes.inter.groupe09.Fenetre;
import fr.univavignon.courbes.inter.groupe09.Error;
import fr.univavignon.courbes.inter.groupe09.menu.Menu;
import fr.univavignon.courbes.inter.groupe09.moteur.NetworksServer;


import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;



public class ServeurGame extends Fenetre implements ServerProfileHandler, ActionListener {
	
	private JLabel portServer, nbPlayer, vide1, vide2, vide3, vide4;
	private JTextField portTxt, nbplayerTxt;
	private JButton b1, b2;
	private JPanel p1;
	private int port = 0;
	private int nbPlayers = 0;
	Error erreur = new Error();
	NetworksServer serverPartie = new NetworksServer();
	
	
	/**
	 * @param titre
	 * Titre de la fenetre
	 * @param x
	 * Taille en abscisse de la fenetre 
	 * @param y
	 * Taille en ordonné de la fenetre
	 */
	ServeurGame(String titre, int x, int y) {
		
		super(titre, x, y);
		p1 = new JPanel();
		b1 = new JButton("Connexion");
		b2 = new JButton("ANNULER");
		portTxt = new JTextField();
		nbplayerTxt = new JTextField();
		portServer = new JLabel("Port de connexion ");
		nbPlayer = new JLabel("Nombre de joueur ");
		vide1 = new JLabel("");
		vide2 = new JLabel("");
		vide3 = new JLabel("");
		vide4 = new JLabel("");
		contenu.setLayout(new FlowLayout(FlowLayout.CENTER));
		contenu.add(p1);
		p1.setLayout(new GridLayout(5, 2, 10, 10));
		
		p1.add(vide1);
		p1.add(vide2);
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
	public boolean fetchProfile(Profile profile) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		
		String nom = event.getActionCommand();
		//s.setErrorHandler(errorHandler);

		switch(nom) {
			
			case "Connexion" :
				if (!(portTxt.getText().equals("")) && !(nbplayerTxt.getText().equals("")) ) {
					
					port = Integer.parseInt(portTxt.getText());
					nbPlayers = Integer.parseInt(nbplayerTxt.getText());
					serverPartie.setPort(port);
					serverPartie.launchServer();
				}
				else {
					erreur.displayError("Valeur saisie inccorecte");
				}
				break;
				
			case "ANNULER"	:
				new Menu("ma fenere", 300, 400);
				this.dispose();
				break;
		}
	
	}
	
	
	
	
	// connexion
	public static void main(String[] args) {
		
		// teste des menu du jeu
		ServeurGame serv = new ServeurGame("Mode Client", 300, 300);		
		}

	

}


