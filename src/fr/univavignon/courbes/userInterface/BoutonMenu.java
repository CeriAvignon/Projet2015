package fr.univavignon.courbes.userInterface;

import java.util.List;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;

/**
 * Classe interceptant les cliques sur les différents boutons de l'application afin de lancer les actions associées
 * @author Florian DEMOL - Alexis MASSIA
 */


public class BoutonMenu extends JButton implements MouseListener{

	private Menu parent;
	private String name;

	public BoutonMenu(String str, Menu menuParent){
		super(str);
		this.name = str;
		parent = menuParent;
		this.addMouseListener(this);
	}

	
	public void mouseClicked(MouseEvent event) { 

		BoutonMenu btn = (BoutonMenu) event.getSource();


		if(btn.getText() == "S'inscrire")
		{
			parent.menuInscription();
		}
		else if(btn.getText() == "Retour")
		{
			parent.afficherMenuPrincipal();
		}
		else if(btn.getText() == "Se Connecter")
		{
			parent.menuConnection();
		}
		else if(btn.getText() == "Valider Inscription")
		{
			parent.VerifInscription();
		}
		else if(btn.getText() == "Retour Menu")
		{
			parent.afficherMenuPrincipal();
		}
		else if(btn.getText() == "Go !")
		{
			parent.VerifConnection();
		}
		else if(btn.getText() == "Créer une partie en ligne")
		{
			parent.MenuCreerPartieEnLigne();
		}
		else if(btn.getText() == "Lancer une partie en local")
		{
			parent.MenuCreerPartieEnLocal();
		}
		else if(btn.getText() == "Nevermind")
		{
			parent.MenuLancerUnePartie();
		}
		else if(btn.getText() == "Play As Guest")
		{
			parent.PlayAsGuest();
		}
		else if(btn.getText() == "Créer la partie")
		{
			parent.ValidationPartieCree();
		}
		else if(btn.getText() == "Rejoindre une partie en ligne")
		{
			parent.MenuRejoindrePartieEnLigne();
		}
		else if(btn.getText() == "Back")
		{
			parent.MenuLancerUnePartie();
		}
		else if(btn.getText() == "Voir mon profil")
		{
			parent.MenuVoirSonProfil();
		}
	}

	public void mouseEntered(MouseEvent event) {
	}

	public void mouseExited(MouseEvent event) { 
	}

	public void mousePressed(MouseEvent event) { 
	}

	public void mouseReleased(MouseEvent event) { }

}

