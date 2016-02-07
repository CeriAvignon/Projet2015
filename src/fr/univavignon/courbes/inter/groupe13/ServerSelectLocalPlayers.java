package fr.univavignon.courbes.inter.groupe13;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.ResourceBundle.Control;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ServerProfileHandler;
import fr.univavignon.courbes.network.groupe06.Server;
import net.miginfocom.swing.MigLayout;

public class ServerSelectLocalPlayers extends JFrame{

	JComboBox<Integer> jcb_nbOfPlayers;
	
	ArrayList<LocalProfileSelector> local_players;
	
	JPanel localPlayerPanel;

	JButton jb_back = new JButton("Retour");
	JButton jb_ready = new JButton("Créer le serveur");
	Vector<PrintableProfile> availableProfiles;
	
	int currentNumberOfPlayers = 2;
	
	public ServerSelectLocalPlayers(final Menu menu){
		
		super();
		
		this.setSize(new Dimension(800, 600));
		
		local_players = new ArrayList<>();
		
		
		this.setLayout(new MigLayout("fill", "", "[][]30[]push[]"));
		
		availableProfiles = ProfileFileManager.getProfiles();
		
		JPanel jp_player_number = new JPanel(new FlowLayout());
		JPanel jp_previous_next = new JPanel(new FlowLayout());

		localPlayerPanel = new JPanel(new MigLayout("fill", "[]20[]20[]20[]", ""));
		
		JButton addLocalPlayer = new JButton("Ajouter joueur local");
		
		addLocalPlayer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(local_players.size() < currentNumberOfPlayers)
					addLocalProfile(false);
			}
		});
		
		
		this.add(jp_player_number, "wrap");
		this.add(addLocalPlayer, "wrap");
		this.add(localPlayerPanel, "wrap");
		this.add(jp_previous_next, "wrap");
		
		Vector<Integer> v = new Vector<>();
		v.add(2);
		v.add(3);
		v.add(4);
		v.add(5);
		v.add(6);
		
		
		jcb_nbOfPlayers = new JComboBox<>(v);
		
		jp_player_number.add(new JLabel("Nombre total de joueurs (locaux et distants)"));
		jp_player_number.add(jcb_nbOfPlayers);		
		
		localPlayerPanel.add(new JLabel("Joueurs locaux"));
		localPlayerPanel.add(new JLabel("Gauche"));
		localPlayerPanel.add(new JLabel("Droite"));
		localPlayerPanel.add(new JLabel("Etat / Action"), "wrap");
		addLocalProfile(true);

		jp_previous_next.add(jb_back);
		jp_previous_next.add(jb_ready);
		
		jb_back.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				ServerSelectLocalPlayers.this.dispatchEvent(new WindowEvent(ServerSelectLocalPlayers.this, WindowEvent.WINDOW_CLOSING));
				menu.setVisible(true);
			}
		});
		
		jb_ready.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(isReadyToStartGame()){
					ServerSelectLocalPlayers.this.setVisible(false);
					new ServerSelectRemotePlayers(ServerSelectLocalPlayers.this, getControllableProfiles(), (int) jcb_nbOfPlayers.getSelectedItem());
				}
				else{
					JOptionPane.showMessageDialog(ServerSelectLocalPlayers.this, "<html>Les données des joueurs locaux ne sont pas correctement remplies. Vérifiez que :" +
							"<br>- tous les profils sont définis et différents ;" +
							"<br>- toutes les commandes sont définies et différentes.</html>");
				}
			}
		});		

		jcb_nbOfPlayers.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int previousNbOfPlayers = currentNumberOfPlayers;
				currentNumberOfPlayers = (int) jcb_nbOfPlayers.getSelectedItem();
							
				/* If we remove players */
				if(previousNbOfPlayers > currentNumberOfPlayers){
					
					/* Remove the exceeding local players */
					for(int i = previousNbOfPlayers ; i > currentNumberOfPlayers ; i--)
						removeLocalProfile(i-1);
					
				}
			}
		});
		

		this.setTitle("Partie réseau (seveur) : choix des profils locaux");
		this.setVisible(true);
		
	}
	
	//TODO Utiliser UUID pour les profils


	protected boolean isReadyToStartGame() {

		//TODO tester que le nombre de joueurs est le bon
		boolean isReady = true;
		
		/* For all couple of profiles (except the last one which is empty) */
		for(int i = 0 ; i < local_players.size() - 1  ; ++i){
			
			ControllableProfile cp1 = local_players.get(i).getC_profile();
			int key1_1 = cp1.getLeftKeyCode();
			int key1_2 = cp1.getRightKeyCode();
			
			if(key1_1 == key1_2 || cp1.getProfile() == null || key1_1 == -1 || key1_2 == -1)
				isReady = false;
			
			for(int j = i+1 ; j < local_players.size() - 1 ; ++j){
				
				ControllableProfile cp2 = local_players.get(j).getC_profile();
				int key2_1 = cp2.getLeftKeyCode();
				int key2_2 = cp2.getRightKeyCode();
				
				if(key1_1 == key2_1 
						|| key1_1 == key2_2
						|| key1_2 == key2_1
						|| key1_2 == key2_2
						|| key2_1 == -1
						|| key2_2 == -1
						|| cp2.getProfile() == null
						|| cp1.getProfile().userName.equals(cp2.getProfile().userName)){
					isReady = false;
					
				}
			}
		}
		
		return isReady;
	}
	
	public void addLocalProfile(boolean isFirstProfile){
		
		LocalProfileSelector lps;
		
		if(isFirstProfile)
			lps = new LocalProfileSelector(availableProfiles, localPlayerPanel);
		else
			lps = new LocalProfileSelector(availableProfiles, localPlayerPanel, this);
		
		local_players.add(lps);
		
		this.validate();
		this.repaint();
	}

	
	public void removeLocalProfile(int i){
		
		LocalProfileSelector lps = local_players.get(i);
		local_players.remove(i);
		
		localPlayerPanel.remove(lps.getJc_playerSelector());
		localPlayerPanel.remove(lps.getLeftButton());
		localPlayerPanel.remove(lps.getRightButton());
		localPlayerPanel.remove(lps.getRemove());
		localPlayerPanel.validate();
		localPlayerPanel.repaint();
	}

	public void removeLocalProfile(LocalProfileSelector lps) {
		removeLocalProfile(local_players.indexOf(lps));
	}
	
	public ArrayList<ControllableProfile> getControllableProfiles(){
		ArrayList<ControllableProfile> result = new ArrayList<>();
		
		for(LocalProfileSelector lps : local_players)
			result.add(lps.getC_profile());
		
		return result;
	}
}
