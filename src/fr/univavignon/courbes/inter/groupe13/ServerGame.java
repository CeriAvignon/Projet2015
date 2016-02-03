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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ServerProfileHandler;
import fr.univavignon.courbes.network.groupe06.Client;
import fr.univavignon.courbes.network.groupe06.Server;

public class ServerGame extends JFrame implements ServerProfileHandler{

	JComboBox<Integer> jcb_nbOfPlayers;
	
	List<LocalProfileSelector> local_players;
	List<RemoteProfile> remote_players;
	
	JPanel localPlayerPanel;
	JPanel remotePlayerPanel;

	JButton jb_back = new JButton("Retour");
	JButton jb_ready = new JButton("Prêt");
	JButton jb_start = new JButton("Démarrer");
	Vector<Profile> availableProfiles;
	
	int currentNumberOfPlayers = 0;
	
	Server server;
	
	public ServerGame(final Menu menu){
		
		super();
		
		this.setSize(new Dimension(800, 600));
		
		local_players = new ArrayList<>();
		remote_players = new ArrayList<>();
		
		
		this.setLayout(new GridLayout(4, 1));
		
		availableProfiles = ProfileFileManager.getProfiles();
		
		server = new Server();
		server.launchServer();
		
		JPanel jp_player_number = new JPanel(new FlowLayout());
		JPanel jp_previous_next = new JPanel(new FlowLayout());
		
		this.add(jp_player_number);
		this.add(localPlayerPanel);
		this.add(remotePlayerPanel);
		this.add(jp_previous_next);
		
		jp_player_number.add(new JLabel("Nombre de joueurs"));
		jp_player_number.add(jcb_nbOfPlayers);		
		
		Vector<Integer> v = new Vector<>();
		v.add(1);
		v.add(2);
		v.add(3);
		v.add(4);
		v.add(5);
		v.add(6);
		
		jcb_nbOfPlayers = new JComboBox<>(v);
		
		remotePlayerPanel.setLayout(new GridLayout(6,3));
		remotePlayerPanel.add(new JLabel("Joueurs distants"));
		remotePlayerPanel.add(new JLabel("Prêt"));
		remotePlayerPanel.add(new JLabel(""));
		
		localPlayerPanel.setLayout(new GridLayout(6, 5));
		localPlayerPanel.add(new JLabel("Joueurs locaux"));
		localPlayerPanel.add(new JLabel("Gauche"));
		localPlayerPanel.add(new JLabel("Droite"));
		localPlayerPanel.add(new JLabel("Etat / Action"));
		localPlayerPanel.add(new JLabel(""));

		jp_previous_next.add(jb_back);
		jp_previous_next.add(jb_ready);
		jp_previous_next.add(jb_start);
		
		jb_back.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				ServerGame.this.server.closeServer();
				ServerGame.this.dispatchEvent(new WindowEvent(ServerGame.this, WindowEvent.WINDOW_CLOSING));
				menu.setVisible(true);
			}
		});
		
		jb_ready.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(isReadyToStartGame()){
					//TODO démarrer la partie
				}
				else{
					JOptionPane.showMessageDialog(ServerGame.this, "<html>Les données des joueurs locaux ne sont pas correctement remplies. Vérifiez que :" +
							"<br>- le profil d'au moins un joueur n'est pas précisé ;" +
							"<br>- plusieurs profiles sont identiques (même id) ;" +
							"<br>- une touche est assignée plusieurs fois.</html>");
				}
			}
		});		

		jcb_nbOfPlayers.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int previousNbOfPlayers = currentNumberOfPlayers;
				currentNumberOfPlayers = (int) jcb_nbOfPlayers.getSelectedItem();
								
				/* If the number of player is fixed for the first time */
				if(previousNbOfPlayers == 0){
					
					/* Add one selector */
					addLocalProfileSelector();
					
				}
				/* If the number of player is decreased */
				else if(previousNbOfPlayers < currentNumberOfPlayers){
					
					/* Remove the exceeding remote players */
					
					int nbOfLocalPlayers = local_players.size();
					
					/* If the last player selector is empty decrease by one the number of local players */
					if(local_players.get(nbOfLocalPlayers-1).getC_profile().getProfile() == null)
						nbOfLocalPlayers--;
					
					/* Remove the exceeding remote players */
					for(int i = remote_players.size()-1 ; i+nbOfLocalPlayers+1 > currentNumberOfPlayers ; --i)
						removeRemoteProfile(i);
					
					/* Remove the exceeding local players */
					for(int i = previousNbOfPlayers ; i > currentNumberOfPlayers ; --i)
						removeLocalProfile(i-1);
					
					
				}
			}
		});
		
	}
	
	//TODO Utiliser UUID pour les profils


	protected boolean isReadyToStartGame() {

		//TODO tester que le nombre de joueurs est le bon
		boolean isReady = true;
		
		/* For all couple of profiles (except the last one which is empty) */
		for(int i = 0 ; i < local_players.size() - 1  ; ++i){
			
			ControllableProfile cp1 = local_players.get(i).getC_profile();
			int key1_1 = cp1.getLeft().getKeyCode();
			int key1_2 = cp1.getRight().getKeyCode();
			
			if(key1_1 == key1_2 || cp1.getProfile() == null)
				isReady = false;
			
			for(int j = i+1 ; j < local_players.size() - 1 ; ++j){
				
				ControllableProfile cp2 = local_players.get(j).getC_profile();
				int key2_1 = cp2.getLeft().getKeyCode();
				int key2_2 = cp2.getRight().getKeyCode();
				
				if(key1_1 == key2_1 
						|| key1_1 == key2_2
						|| key1_2 == key2_1
						|| key1_2 == key2_2
						|| cp2.getProfile() == null
						|| cp1.getProfile().userName.equals(cp2.getProfile().userName)){
					isReady = false;
					
				}
			}
		}
		
		return false;
	}
	
	public void addLocalProfileSelector(){
		
		LocalProfileSelector lps = new LocalProfileSelector(availableProfiles, localPlayerPanel);
		
		lps.getJc_playerSelector().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(currentNumberOfPlayers > local_players.size())
					addLocalProfileSelector();
			}
		});
		
		local_players.add(lps); 
		localPlayerPanel.repaint();
	}

	
	public void removeLocalProfile(int i){
		
		LocalProfileSelector lps = local_players.get(i);
		local_players.remove(i);
		
		localPlayerPanel.remove(lps.getJc_playerSelector());
		localPlayerPanel.remove(lps.getLeftButton());
		localPlayerPanel.remove(lps.getRightButton());
		localPlayerPanel.repaint();
	}
	
	public void addRemoteProfile(Profile p){
		
		RemoteProfile rp = new RemoteProfile(p);
		remote_players.add(rp);
		remotePlayerPanel.add(rp.getJl());
		remotePlayerPanel.repaint();
		
	}
	
	public void removeRemoteProfile(int i){
		
		RemoteProfile rp = remote_players.get(i);
		remote_players.remove(i);
		
		remotePlayerPanel.remove(rp.getJl());
		remotePlayerPanel.repaint();
		
	}

	@Override
	public boolean fetchProfile(Profile profile) {
		
		boolean numberOfPlayerReached = currentNumberOfPlayers == local_players.size() + remote_players.size();
		
		/* If the maximal number of players is reached 
		 * (i.e., the number of players is reached and the last local player is defined) */
		if(numberOfPlayerReached
				&& local_players.get(local_players.size()-1).getC_profile().getProfile() != null)
			
			/* Don't add the player */
			return false;
		
		else{
			
			/* If the maximal number of player is reached with the addition of the profile <profile> */
			if(numberOfPlayerReached)
			
				/* Remove the last undefined local player */
				removeLocalProfile(local_players.size()-1);
			

			/* Add this profile to the remote players */
			addRemoteProfile(profile);
			
			return true;
		}
			
	}
	
	private List<Profile> getProfileList(){
		List<Profile> result = new ArrayList<>();
		
		for(LocalProfileSelector lps : local_players)
			result.add(lps.getC_profile().getProfile());
		
		for(RemoteProfile rp : remote_players)
			result.add(rp.getProfile());
		
		return result;
	}
	
	public Server getServer(){
		return server;
	}
	
}
