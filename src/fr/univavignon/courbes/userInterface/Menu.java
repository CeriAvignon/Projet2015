package fr.univavignon.courbes.userInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Menu extends JPanel{

	private BoutonMenu btnConnection,btnInscription,btnValiderInscription,btnRetour, btnSeConnecter, btnGo, btnRetourM, btnEnLigne,  btnGuestbtnRejoindrePartieEnLigne, btnEnLocal, btnCreerPartieEnLigne, btnRetourMenu, btnValiderL, btnGuest, btnRejoindrePartieEnLigne;
	private JTextField txtNom, txtPays, txtEmail, txtPseudo, txtMotdePasse, txtPseudoCo, txtMdPCo, NomPartieLigneT;
	private GUI gui;
	private static String nom, prenom, email, pseudo, motDePasse, pays ;
	Random rand = new Random();
	int nombre = rand.nextInt(1001); //Entre 0 et 1000
	private JComboBox choixNbJoueur = new JComboBox();
	String[] nbJ = {"1 joueur", "2 joueurs", "3 joueurs", "4 joueurs", "5 joueurs", "6 joueurs", "7 joueurs", "8 joueurs"};
	private JComboBox tPlateauBox = new JComboBox();
	String[] tPlateau = {"Scaling", "Full Size"};
	public static String nomPartie, nbJoueursPartie;
	static String taillePlateau;
	Sauvegarde save = new Sauvegarde();
	static Command kListener = new Command();
      

	public Menu(GUI gui) {
		this.gui = gui;
	}

	GUI getGUI() { return gui; }
	
	public void afficherMenuPrincipal(){
		
		this.removeAll();
		this.setBackground(new Color(255,255,255));
		Font f = new Font("Serif", Font.PLAIN, 36); 
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(Box.createRigidArea(new Dimension(5,125))); 
		
		JLabel titreMenu = new JLabel("CURVEFEVER");
		titreMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
		titreMenu.setFont(f);
		this.add(titreMenu, Component.CENTER_ALIGNMENT);
		
		this.add(Box.createRigidArea(new Dimension(5,125))); 

		btnConnection = new BoutonMenu("Se Connecter",this);
		btnConnection.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(btnConnection);

		this.add(Box.createRigidArea(new Dimension(5,35))); // Ajout d'un espace entre les boutons

		btnInscription = new BoutonMenu("S'inscrire",this);
		btnInscription.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(btnInscription);
		
		kListener.getCommand();

		this.validate();
		this.repaint();
	}

	public void menuInscription(){

		this.removeAll();
		this.setLayout(null);
		this.setBackground(new Color(255,255,255));
		Font f = new Font("Serif", Font.PLAIN, 36); 
		
		JLabel titre = new JLabel("Formulaire d'inscription");
		titre.setBounds(500, 15, 500, 50);
		titre.setFont(f);
		this.add(titre);
		
		/*JLabel titreNom = new JLabel("Nom : ");
		titreNom.setBounds(530, 100, 50,10);
		this.add(titreNom);
		
		txtNom = new JTextField();
		txtNom.setBounds(590, 95, 150, 25);
		this.add(txtNom);*/
		
		JLabel titrePays = new JLabel("Pays : ");
		titrePays.setBounds(510, 200, 60,10);
		this.add(titrePays);
		
		txtPays = new JTextField();
		txtPays.setBounds(590, 195, 150, 25);
		this.add(txtPays);
		
		JLabel titreEmail = new JLabel("Email : ");
		titreEmail.setBounds(530, 300, 50,10);
		this.add(titreEmail);
		
		txtEmail = new JTextField();
		txtEmail.setBounds(590, 295, 400, 25);
		this.add(txtEmail);
		
		JLabel titrePseudo = new JLabel("Pseudo : ");
		titrePseudo.setBounds(520, 400, 60,10);
		this.add(titrePseudo);
		
		txtPseudo = new JTextField();
		txtPseudo.setBounds(590, 395, 150, 25);
		this.add(txtPseudo);
		
		JLabel titreMDP = new JLabel("Mot de passe : ");
		titreMDP.setBounds(500, 500, 100,30);
		this.add(titreMDP);
		
		txtMotdePasse = new JTextField();
		txtMotdePasse.setBounds(590, 500, 150, 25);
		this.add(txtMotdePasse);
		
		
		btnRetour = new BoutonMenu("Retour",this);
		btnRetour.setBounds(500, 600, 100, 30);
		this.add(btnRetour);
		
		btnValiderInscription = new BoutonMenu("Valider Inscription",this);
		btnValiderInscription.setBounds(620, 600, 150, 30);
		this.add(btnValiderInscription);

		

		this.validate();
		this.repaint();
	}


	public void menuConnection(){
		
		this.removeAll();
		this.setLayout(null);
		this.setBackground(new Color(255,255,255));
		Font f = new Font("Serif", Font.PLAIN, 36); 

		
		JLabel titreCo = new JLabel("CURVEFEVER");
		titreCo.setFont(f);
		titreCo.setBounds(520, 15, 500, 50);
		this.add(titreCo);
		
		
		JLabel titrePseudoCo = new JLabel("Pseudo : ");
		titrePseudoCo.setBounds(530, 250, 60,10);
		this.add(titrePseudoCo);
		
		txtPseudoCo = new JTextField();
		txtPseudoCo.setBounds(590, 245, 150, 25);
		this.add(txtPseudoCo);
		
		JLabel titreMdpCo = new JLabel("Mot de passe : ");
		titreMdpCo.setBounds(500, 320, 100, 20);
		this.add(titreMdpCo);
		
		txtMdPCo = new JTextField();
		txtMdPCo.setBounds(590, 318, 150, 25);
		this.add(txtMdPCo);
		
		btnGo = new BoutonMenu("Go !",this);
		btnGo.setBounds(660, 430, 100, 30);
		this.add(btnGo);
		
		btnRetourM = new BoutonMenu("Retour Menu",this);
		btnRetourM.setBounds(30, 30, 140, 30);
		this.add(btnRetourM);
		
		btnGuest = new BoutonMenu("Play As Guest",this);
		btnGuest.setBounds(500, 430, 140, 30);
		this.add(btnGuest);
		
		this.validate();
		this.repaint();
		
	}

	public void VerifConnection() {
		save.controleConnexion(txtPseudoCo.getText(), txtMdPCo.getText());
		this.MenuLancerUnePartie();
	}
	
	public void VerifInscription(){
		if(txtPseudo.getText().length() > 15 || txtMotdePasse.getText().length()>15  ){
			this.menuInscription();
			System.out.println("Pas plus de 15 caractères pour le pseudo et le mot de passe");
		}
		else if(txtPseudo.getText() == null || txtMotdePasse.getText() == null){
			this.menuInscription();
			System.out.println("Pseudo ou mot de passe incorrect");
		}
		else{
			nom = txtNom.getText();
			pays = txtPays.getText();
			email = txtEmail.getText();
			pseudo = txtPseudo.getText();
			motDePasse = txtMotdePasse.getText();
			save.controlePseudo(pseudo);
			save.inscriptionDe(pseudo, motDePasse, email, pays);
			this.menuConnection();
		}
	}

	public void PlayAsGuest(){
		
		pseudo = "Guest".concat(Integer.toString(nombre));
		this.MenuLancerUnePartie();
	}
	
	public void MenuLancerUnePartie() {
		
		this.removeAll();
		this.setLayout(null);
		this.setBackground(new Color(255,255,255));
		Font f = new Font("Serif", Font.PLAIN, 36);
		
		JLabel accueilCurve = new JLabel("CURVEFEVER");
		accueilCurve.setFont(f);
		accueilCurve.setBounds(520, 15, 500, 50);
		this.add(accueilCurve);
		
		JLabel accueilPseudo = new JLabel("Bonjour "+Menu.pseudo);
		accueilPseudo.setFont(f);
		accueilPseudo.setBounds(510, 100, 500, 50);
		this.add(accueilPseudo);
		
		btnRejoindrePartieEnLigne = new BoutonMenu("Rejoindre une partie en ligne",this);
		btnRejoindrePartieEnLigne.setBounds(730, 350, 250, 30);
		this.add(btnRejoindrePartieEnLigne);
		
		btnCreerPartieEnLigne = new BoutonMenu("Créer une partie en ligne",this);
		btnCreerPartieEnLigne.setBounds(730, 300, 190, 30);
		this.add(btnCreerPartieEnLigne);
		
		btnEnLocal = new BoutonMenu("Lancer une partie en local",this);
		btnEnLocal.setBounds(300, 350, 220, 30);
		this.add(btnEnLocal);
		
		
		
		
		this.validate();
		this.repaint();
	}
	
	public void MenuCreerPartieEnLigne() {
		
		this.removeAll();
		this.setLayout(null);
		Font f = new Font("Serif", Font.PLAIN, 36);
		this.setBackground(new Color(255,255,255));
		
		JLabel accueilCurve = new JLabel("CURVEFEVER");
		accueilCurve.setFont(f);
		accueilCurve.setBounds(520, 15, 500, 50);
		this.add(accueilCurve);
		
		JLabel NomPartieLigne = new JLabel("Nom de la partie : ");
		NomPartieLigne.setBounds(480, 218, 250, 50);
		this.add(NomPartieLigne);
		
		NomPartieLigneT = new JTextField();
		NomPartieLigneT.setBounds(610, 230, 150, 25);
		this.add(NomPartieLigneT);
		
		JLabel NombreJoueurT = new JLabel("Nombre de joueurs : ");
		NombreJoueurT.setBounds(480, 298, 250, 50);
		this.add(NombreJoueurT);
		
		choixNbJoueur = new JComboBox(nbJ);
		choixNbJoueur.setBounds(610, 310, 150, 25);
		this.add(choixNbJoueur);
		
		JLabel TaillePlateauJeu = new JLabel("Taille du plateau : ");
		TaillePlateauJeu.setBounds(480, 385, 250, 50);
		this.add(TaillePlateauJeu);
		
		tPlateauBox = new JComboBox(tPlateau);
		tPlateauBox.setBounds(610, 395, 150, 25);
		this.add(tPlateauBox);

	    
		btnRetourMenu = new BoutonMenu("Nevermind",this);
		btnRetourMenu.setBounds(480, 550, 140, 30);
		this.add(btnRetourMenu);
		
		btnValiderL = new BoutonMenu("Créer la partie",this);
		btnValiderL.setBounds(630, 550, 140, 30);
		this.add(btnValiderL);
		
		
		this.validate();
		this.repaint();
		
	}
	
	public void ValidationPartieCree() {
		
		nomPartie = NomPartieLigneT.getText();
		nbJoueursPartie = choixNbJoueur.getSelectedItem().toString();
		taillePlateau = tPlateauBox.getSelectedItem().toString();
	}
	
	public void MenuCreerPartieEnRéseau(){
		
	}

	
	
	//getter Inscription
	static String getNom() { return nom; }
	static String getPrenom() { return prenom; }
	static String getEmail() { return email; }
	static String getPseudo() { return pseudo; }
	static String getMotDePasse() { return motDePasse; }
	static String getPays() { return pays; }
	
	
	//getter Création Partie
	static String getNomPartie() { return nomPartie; }
	static String getNbJoueursPartie() { return nbJoueursPartie; }
	static String getTaillePlateau() { return taillePlateau; }

}


