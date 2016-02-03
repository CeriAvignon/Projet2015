package fr.univavignon.courbes.inter.groupe13;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import fr.univavignon.courbes.common.Profile;

public class LocalGame extends JFrame{

	JComboBox<Integer> jcb_nbOfPlayers;
	List<LocalProfileSelector> players;
	Vector<Profile> availableProfiles;
	Menu m;
	
	JButton jb_back = new JButton("Retour");
	JButton jb_start = new JButton("Démarrer");
	JPanel playerPanel = new JPanel(new GridLayout(7,3));
	
	public LocalGame(Menu m){
		
		super();
		
		this.m = m;
		this.setSize(new Dimension(800,600));
		
		players = new ArrayList<>();
		
		this.setLayout(new BorderLayout());
		
		JPanel jp_numberOfPlayers = new JPanel(new FlowLayout());
		JPanel jp_previousNext = new JPanel(new FlowLayout());
		
		this.add(jp_numberOfPlayers, BorderLayout.NORTH);
		this.add(playerPanel, BorderLayout.CENTER);
		this.add(jp_previousNext, BorderLayout.SOUTH);
		
		jp_numberOfPlayers.add(new JLabel("Nombre de joueurs"));
		jp_numberOfPlayers.add(jcb_nbOfPlayers);
		
		jp_previousNext.add(jb_back);
		jp_previousNext.add(jb_start);
		
		playerPanel.add(new JLabel("Joueur"));
		playerPanel.add(new JLabel("Gauche"));
		playerPanel.add(new JLabel("Droite"));
		
		jb_back.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				LocalGame.this.dispatchEvent(new WindowEvent(LocalGame.this, WindowEvent.WINDOW_CLOSING));
				LocalGame.this.m.setVisible(true);
				
			}
		});
		
		jb_start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(isReadyToStartGame()){

					setVisible(false);
					Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
					int width = (int)screenSize.getWidth();
					int height= (int)screenSize.getHeight();			
					new Game(width, height);
				}
				else{
					JOptionPane.showMessageDialog(LocalGame.this, "<html>Les données des joueurs locaux ne sont pas correctement remplies. Vérifiez que :" +
							"<br>- le profil d'au moins un joueur n'est pas précisé ;" +
							"<br>- plusieurs profiles sont identiques (même id) ;" +
							"<br>- une touche est assignée plusieurs fois.</html>");
				}
			}
		});
		
		
		availableProfiles = ProfileFileManager.getProfiles();
		
		Vector<Integer> v = new Vector<>();
		v.add(1);
		v.add(2);
		v.add(3);
		v.add(4);
		v.add(5);
		v.add(6);
		
		jcb_nbOfPlayers = new JComboBox<>(v);
		
		jcb_nbOfPlayers.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int previousNbOfPlayers = players.size();
				int newNbOfPlayers = (int) jcb_nbOfPlayers.getSelectedItem();
				
				if(previousNbOfPlayers < newNbOfPlayers){
					
					for(int i = previousNbOfPlayers ; i < newNbOfPlayers ; ++i){
						LocalProfileSelector lps = new LocalProfileSelector(availableProfiles, playerPanel);
						players.add(lps);
						
						playerPanel.repaint();
					}
					
				}
				else
					for(int i = previousNbOfPlayers ; i > newNbOfPlayers ; --i){
						
						LocalProfileSelector lps = players.get(i);
						players.remove(i-1);
						
						playerPanel.remove(lps.getJc_playerSelector());
						playerPanel.remove(lps.getLeftButton());
						playerPanel.remove(lps.getRightButton());
						
						playerPanel.repaint();
					}
			}
		});
		
		
		
		
	}

	protected boolean isReadyToStartGame() {

		boolean isReady = true;
		
		/* For all couple of profiles */
		for(int i = 0 ; i < players.size() ; ++i){
			
			ControllableProfile cp1 = players.get(i).getC_profile();
			int key1_1 = cp1.getLeft().getKeyCode();
			int key1_2 = cp1.getRight().getKeyCode();
			
			if(key1_1 == key1_2 || cp1.getProfile() == null)
				isReady = false;
			
			for(int j = i+1 ; j < players.size() ; ++j){
				
				ControllableProfile cp2 = players.get(j).getC_profile();
				int key2_1 = cp2.getLeft().getKeyCode();
				int key2_2 = cp2.getRight().getKeyCode();
				
				if(key1_1 == key2_1 
						|| key1_1 == key2_2
						|| key1_2 == key2_1
						|| key1_2 == key2_2
						|| cp2.getProfile() == null
						|| cp1.getProfile().userName.equals(cp2.getProfile().userName)
						){
					isReady = false;
					
				}
			}
		}
		
		return false;
	}


}
