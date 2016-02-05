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

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import fr.univavignon.courbes.common.Profile;

public class LocalGame extends JFrame{

	JComboBox<Integer> jcb_nbOfPlayers;
	ArrayList<LocalProfileSelector> players;
	Vector<PrintableProfile> availableProfiles;
	
	JButton jb_back = new JButton("Retour");
	JButton jb_start = new JButton("Démarrer");
	JPanel playerPanel;
	
	public LocalGame(final Menu m){
		
		super();
		
		this.setSize(new Dimension(400,380));
		players = new ArrayList<>();
		
		this.setLayout(new MigLayout("fill", "", ""));
		
		JPanel jp_numberOfPlayers = new JPanel(new FlowLayout());
		JPanel jp_previousNext = new JPanel(new FlowLayout());
		

		playerPanel = new JPanel();
		playerPanel.setLayout(new MigLayout("fill", "[]10[]10[]", "[]10[]"));
		
		this.add(jp_numberOfPlayers, "wrap");
		this.add(playerPanel, "wrap");
		this.add(jp_previousNext, "wrap");

		
		JLabel jl1 = new JLabel("Joueur");
		JLabel jl2 = new JLabel("Droite");
		JLabel jl3 = new JLabel("Gauche");
		playerPanel.add(jl1);
		playerPanel.add(jl2);
		playerPanel.add(jl3, "wrap");

		jl1.setMaximumSize(new Dimension(1600, 30));
		jl2.setMaximumSize(new Dimension(1600, 30));
		jl3.setMaximumSize(new Dimension(1600, 30));
		
//		playerPanel.setMaximumSize(new Dimension(500, 500));
		
		Vector<Integer> v = new Vector<>();
		v.add(2);
		v.add(3);
		v.add(4);
		v.add(5);
		v.add(6);

		availableProfiles = ProfileFileManager.getProfiles();
		jcb_nbOfPlayers = new JComboBox<>(v);
		jcb_nbOfPlayers.setSelectedIndex(0);
		addLocalProfile();
		addLocalProfile();
		
		
		jp_numberOfPlayers.add(new JLabel("Nombre de joueurs"));
		jp_numberOfPlayers.add(jcb_nbOfPlayers);
		
		jp_previousNext.add(jb_back);
		jp_previousNext.add(jb_start);
		
		jb_back.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				LocalGame.this.dispatchEvent(new WindowEvent(LocalGame.this, WindowEvent.WINDOW_CLOSING));
				m.setVisible(true);
				
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
					new Game(width, height, players);
				}
				else{
					JOptionPane.showMessageDialog(LocalGame.this, "<html>Les données des joueurs locaux ne sont pas correctement remplies. Vérifiez que :" +
							"<br>- tous les profils sont définis et différents ;" +
							"<br>- toutes les commandes sont définies et différentes.</html>");
				}
			}
		});
		
		jcb_nbOfPlayers.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int previousNbOfPlayers = players.size();
				int newNbOfPlayers = (int) jcb_nbOfPlayers.getSelectedItem();
				
				if(previousNbOfPlayers < newNbOfPlayers){
					
					for(int i = previousNbOfPlayers ; i < newNbOfPlayers ; ++i){
						addLocalProfile();
					}
					
				}
				else
					for(int i = previousNbOfPlayers - 1 ; i >= newNbOfPlayers ; --i){
						
						LocalProfileSelector lps = players.get(i);
						players.remove(i);
						
						playerPanel.remove(lps.getJc_playerSelector());
						playerPanel.remove(lps.getLeftButton());
						playerPanel.remove(lps.getRightButton());
						
						playerPanel.repaint();
					}
			}
		});
		
		this.setTitle("Partie locale");
		this.setVisible(true);
		
		
	}

	protected boolean isReadyToStartGame() {

		boolean isReady = true;
		
		/* For all couple of profiles */
		for(int i = 0 ; i < players.size() ; ++i){
			
			ControllableProfile cp1 = players.get(i).getC_profile();
			int key1_1 = cp1.getLeftKeyCode();
			int key1_2 = cp1.getRightKeyCode();
			
			if(key1_1 == key1_2 || cp1.getProfile() == null || key1_1 == -1 || key1_2 == -1)
				isReady = false;
			
			for(int j = i+1 ; j < players.size() ; ++j){
				
				ControllableProfile cp2 = players.get(j).getC_profile();
				int key2_1 = cp2.getLeftKeyCode();
				int key2_2 = cp2.getRightKeyCode();
				
				if(cp2.getProfile().userName.equals(cp1.getProfile().userName)){
					System.out.println("Identique username: " + cp2.getProfile().userName);
				}
				
				if(key1_1 == key2_1 
						|| key1_1 == key2_2
						|| key1_2 == key2_1
						|| key1_2 == key2_2
						|| key2_1 == -1
						|| key2_2 == -1
						|| cp2.getProfile() == null
						|| cp1.getProfile().userName.equals(cp2.getProfile().userName)
						){
					isReady = false;
					
				}
			}
		}
		
		return isReady;
	}

	private void addLocalProfile() {
		
		LocalProfileSelector lps = new LocalProfileSelector(availableProfiles, playerPanel);
		players.add(lps);
		
		this.validate();
		this.repaint();
	}


}
