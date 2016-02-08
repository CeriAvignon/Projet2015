package fr.univavignon.courbes.inter.simpleimpl.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import fr.univavignon.courbes.inter.simpleimpl.communication.JoinServer;
import fr.univavignon.courbes.inter.simpleimpl.communication.ServerSelectLocalPlayers;
import fr.univavignon.courbes.inter.simpleimpl.data.DisplayProfileFrame;
import fr.univavignon.courbes.inter.simpleimpl.data.ProfileFileManager;
import fr.univavignon.courbes.inter.simpleimpl.game.LocalGame;
import net.miginfocom.swing.MigLayout;

/**
 * @author zach
 * Main menu in which the user can: 
 * - select the game type
 * - view the currently existing profiles or the statistics
 */
public class Menu extends JFrame{

	private static final long serialVersionUID = 1L;
	private int width, height;

	/**
	 * Initialise les variables tempWidth et tempWidth puis affiche le menu qui
	 * permet de choisir les joueurs et leurs touches
	 * 
	 * @param width Largeur de la fenêtre.
	 * @param height Hauteur de la fenêtre.
	 */
	public Menu() {
		super();
		menuPlayer(); 
	}

	/*
	 * affciche le menu qui permet de choisir les joueurs (en local) et leurs
	 * touches
	 */
	public void menuPlayer() {
		this.setLayout(new MigLayout("", "[fill]", ""));

		JButton localGame = new JButton("Partie locale");
		JButton serverGame = new JButton("Créer une partie réseau");
		JButton clientGame = new JButton("Rejoindre une partie réseau");
		JButton profils = new JButton("Profils");
//		JButton stats = new JButton("Statistiques");
		JButton quit = new JButton("Quitter");
		
		localGame.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				if(ProfileFileManager.getProfiles().size() > 1){
					setVisible(false);
					new LocalGame(Menu.this);
				}
				else
					JOptionPane.showMessageDialog(Menu.this, "<html>Pour démarrer une partie locale, vous devez avoir défini au minimum 2 profils." +
				"<br>(pour définir des profils, cliquez sur \"Profils\")</html>");
						
//				new Game(width, height);
			}
		});
		
		serverGame.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				if(ProfileFileManager.getProfiles().size() > 0){
					setVisible(false);
					new ServerSelectLocalPlayers(Menu.this);
				}
				else
					JOptionPane.showMessageDialog(Menu.this, "<html>Pour démarrer un serveur, vous devez avoir défini au minimum 1 profil." +
				"<br>(pour définir des profils, cliquez sur \"Profils\")</html>");
//				new Network(width, height, false);
				
			}
		});
		
		clientGame.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				if(ProfileFileManager.getProfiles().size() > 0){
					setVisible(false);
					new JoinServer(Menu.this);
				}
				else
					JOptionPane.showMessageDialog(Menu.this, "<html>Pour démarrer un client, vous devez avoir défini au minimum 1 profil." +
				"<br>(pour définir des profils, cliquez sur \"Profils\")</html>");
//				new Network(width, height, true);
				
			}
		});

		quit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Menu.this.dispatchEvent(new WindowEvent(Menu.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		profils.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				DisplayProfileFrame df = new DisplayProfileFrame(Menu.this);
				Menu.this.setVisible(false);
			}
		});

		this.add(localGame, "wrap");
		this.add(clientGame, "wrap");
		this.add(serverGame, "wrap");
		this.add(profils, "wrap");
		this.add(quit);

		this.setVisible(true);

	}

}

