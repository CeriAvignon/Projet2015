package fr.univavignon.courbes.userInterface;

import java.util.List;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;

public class BoutonMenu extends JButton implements MouseListener{

	private Menu parent;
	private String name;

	public BoutonMenu(String str, Menu menuParent){
		super(str);
		this.name = str;
		parent = menuParent;

		//Des qu'un evenement de la souris sera intercept, il sera averti
		this.addMouseListener(this);
	}

	//Methode appele lors du clic de souris
	public void mouseClicked(MouseEvent event) { 

		BoutonMenu btn = (BoutonMenu) event.getSource();
		System.out.println(btn.getText());


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
		else if(btn.getText() == "Lancer une partie en réseau")
		{
			parent.MenuCreerPartieEnRéseau();
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
	}

	public void mouseEntered(MouseEvent event) {
	}

	public void mouseExited(MouseEvent event) { 
	}

	public void mousePressed(MouseEvent event) { 
	}

	public void mouseReleased(MouseEvent event) { }

}

