package fr.univavignon.courbes.inter.groupe13;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
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

public class ServerGame extends JFrame implements ServerProfileHandler{

	private JoinServer js;
	private Client c;
	JComboBox<Integer> jcb_nbOfPlayers;
	
	List<LocalProfileSelector> local_players;
	List<RemoteProfile> remote_players;
	
	JPanel localPlayerPanel;
	JPanel remotePlayerPanel;

	JButton jb_back = new JButton("Retour");
	JButton jb_ready = new JButton("Prêt");
	JButton jb_start = new JButton("Démarrer");
	
	
	public ServerGame(JoinServer js, Client c){
		
		super();
		
		this.c = c;
		this.js = js;
		this.setSize(new Dimension(800, 600));
		
		this.setLayout(new GridLayout(4, 1));
		
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

				ServerGame.this.c.closeClient();
				ServerGame.this.dispatchEvent(new WindowEvent(ServerGame.this, WindowEvent.WINDOW_CLOSING));
				ServerGame.this.js.setVisible(true);
			}
		});
		
		jb_ready.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(isReadyToStartGame()){
					//TODO dire au serveur qu'on est prêt à démarrer la partie
				}
				else{
					JOptionPane.showMessageDialog(ServerGame.this, "<html>Les données des joueurs locaux ne sont pas correctement remplies. Vérifiez que :" +
							"<br>- le profil d'au moins un joueur n'est pas précisé ;" +
							"<br>- plusieurs profiles sont identiques (même id) ;" +
							"<br>- une touche est assignée plusieurs fois.</html>");
				}
			}
		});
		
		/* Add one profile selector (a new one is added when the profile selected) */
		addLocalProfileSelector();
		

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
						players.remove(i);
						
						playerPanel.remove(lps.getJc_playerSelector());
						playerPanel.remove(lps.getLeftButton());
						playerPanel.remove(lps.getRightButton());
						
						playerPanel.repaint();
					}
			}
		});
		
	}

	public Client getC() {
		return c;
	}

	public void setC(Client c) {
		this.c = c;
	}


	protected boolean isReadyToStartGame() {

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
		local_players.add(new LocalProfileSelector(ProfileFileManager.getProfiles(), this, localPlayerPanel)); 
		localPlayerPanel.repaint();
	}

	
	public void removeLocalProfile(int i){
		
		LocalProfileSelector lps = local_players.get(i);
		local_players.remove(i);
		
		localPlayerPanel.remove(lps.getJc_playerSelector());
		localPlayerPanel.remove(lps.getLeftButton());
		localPlayerPanel.remove(lps.getRightButton());
		localPlayerPanel.remove(lps.getSendProfileToServer());
		localPlayerPanel.remove(lps.getRemoveFromServer());
		localPlayerPanel.repaint();
	}
	
	public void addLocalProfile(Profile p){
		
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
		// TODO Auto-generated method stub
		return false;
	}
	
}
