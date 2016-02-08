package fr.univavignon.courbes.inter.simpleimpl.communication;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
import fr.univavignon.courbes.inter.simpleimpl.data.PrintableProfile;
import fr.univavignon.courbes.inter.simpleimpl.data.ProfileFileManager;
import fr.univavignon.courbes.inter.simpleimpl.data.RemoteProfile;
import fr.univavignon.courbes.inter.simpleimpl.game.ControllableProfile;
import fr.univavignon.courbes.network.simpleimpl.Server;
import net.miginfocom.swing.MigLayout;

public class ServerSelectRemotePlayers extends JFrame implements ServerProfileHandler{

	JComboBox<Integer> jcb_nbOfPlayers;
	
	ArrayList<ControllableProfile> local_players;
	List<RemoteProfile> remote_players;
	
	JPanel localPlayerPanel;
	JPanel remotePlayerPanel;

	JButton jb_back = new JButton("Retour");
	JButton jb_start = new JButton("Démarrer");
	Vector<PrintableProfile> availableProfiles;
	
	int currentNumberOfPlayers;
	
	Server server;


	public ServerSelectRemotePlayers(final ServerSelectLocalPlayers serverSelectLocalPlayers,
			ArrayList<ControllableProfile> local_players, int numberOfPlayers) {
		
		super();
		
		currentNumberOfPlayers = numberOfPlayers;
		
		this.setSize(new Dimension(800, 600));
		
		this.local_players = local_players;
		remote_players = new ArrayList<>();
		
		this.setLayout(new MigLayout("fill", "", "[][][]push[][]"));
		
		availableProfiles = ProfileFileManager.getProfiles();
		
		server = new Server();
		server.launchServer();
		
		JPanel jp_player_number = new JPanel(new FlowLayout());
		JPanel jp_previous_next = new JPanel(new FlowLayout());

		remotePlayerPanel = new JPanel(new MigLayout("fill", "[]20[]"));
		localPlayerPanel = new JPanel(new MigLayout("fill", "[]20[]20[]", ""));
		
		
		String ip = "";
		
		try {
			ip = InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		
		this.add(jp_player_number, "wrap");
		this.add(localPlayerPanel, "wrap");
		this.add(remotePlayerPanel, "wrap");
		
		if(!"".equals(ip))
			this.add(new JLabel("IP du serveur : " + ip), "wrap");
		this.add(jp_previous_next, "wrap");
		
		Vector<Integer> v = new Vector<>();
		
		int initialSelectedIndex = 0;
		boolean indexFound = false;
		for(int i = local_players.size() ; i <= 6 ; ++i){
			v.add(i);
			
			if(!indexFound)
				if(i == numberOfPlayers)
					indexFound = true;
				else 
					initialSelectedIndex++;
		}
		
		jcb_nbOfPlayers = new JComboBox<>(v);
		jcb_nbOfPlayers.setSelectedIndex(initialSelectedIndex);
		
		jp_player_number.add(new JLabel("Nombre de joueurs"));
		jp_player_number.add(jcb_nbOfPlayers);		
		
		remotePlayerPanel.add(new JLabel("Joueurs distants"));
		remotePlayerPanel.add(new JLabel("Prêt"), "wrap");
		
		localPlayerPanel.add(new JLabel("Joueurs locaux"));
		localPlayerPanel.add(new JLabel("Gauche"));
		localPlayerPanel.add(new JLabel("Droite"), "wrap");
		
		for(ControllableProfile cp : local_players){
			localPlayerPanel.add(new JLabel(cp.getProfile().userName));
			localPlayerPanel.add(new JLabel(KeyEvent.getKeyText(cp.getLeft().getKeyCode())));
			localPlayerPanel.add(new JLabel(KeyEvent.getKeyText(cp.getRight().getKeyCode())), "wrap");
		}

		jp_previous_next.add(jb_back);
		jp_previous_next.add(jb_start);
		
		jb_back.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				ServerSelectRemotePlayers.this.server.closeServer();
				ServerSelectRemotePlayers.this.dispatchEvent(new WindowEvent(ServerSelectRemotePlayers.this, WindowEvent.WINDOW_CLOSING));
				serverSelectLocalPlayers.setVisible(true);
			}
		});
		
		jb_start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(isReadyToStartGame()){
					//TODO démarrer la partie
				}
				else{
					JOptionPane.showMessageDialog(ServerSelectRemotePlayers.this, "<html>Les données des joueurs locaux ne sont pas correctement remplies. Vérifiez que :" +
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
								
				if(previousNbOfPlayers > currentNumberOfPlayers){
					
					/* Remove the exceeding remote players */
					for(int i = ServerSelectRemotePlayers.this.local_players.size() + remote_players.size()
							; i > currentNumberOfPlayers ; --i)
						removeRemoteProfile(i-1);
					
				}
			}
		});
		

		this.setTitle("Partie réseau (seveur) : connexion des profiles distants");
		this.setVisible(true);
		
	}
	
	//TODO Utiliser UUID pour les profils


	/**
	 * Check that the profils are all different (the command have already been checked in the previous frame
	 * @return True if all the profils are different
	 */
	protected boolean isReadyToStartGame() {

		/* Check that the local and remote profils are all different */
		List<Profile> profils = getProfileList();

		boolean isReady = true;
		
		/* For all couple of profiles (except the last one which is empty) */
		for(int i = 0 ; i < profils.size()  ; ++i){
			
			Profile cp1 = profils.get(i);
			
			if(cp1 == null)
				isReady = false;
			
			for(int j = i+1 ; j < local_players.size() - 1 ; ++j){
				
				Profile cp2 = profils.get(j);
				
				if(cp2 == null
						|| cp1.userName.equals(cp2.userName)){
					isReady = false;
					
				}
			}
		}
		
		return isReady;
	}
	
	//TODO voire comment le serveur sait qu'un clien est prêt
	
	public void addRemoteProfile(Profile p){
		
		RemoteProfile rp = new RemoteProfile(p, server, this);
		remote_players.add(rp);
		remotePlayerPanel.add(rp.getJl());
		remotePlayerPanel.add(rp.getIsReady());
		remotePlayerPanel.add(rp.getJb_kick());
		remotePlayerPanel.validate();
		remotePlayerPanel.repaint();
		
	}
	
	public void removeRemoteProfile(int i){
		
		RemoteProfile rp = remote_players.get(i);
		remote_players.remove(i);

		remotePlayerPanel.remove(rp.getJl());
		remotePlayerPanel.remove(rp.getIsReady());
		remotePlayerPanel.remove(rp.getJb_kick());
		remotePlayerPanel.validate();
		remotePlayerPanel.repaint();
		
	}

	@Override
	public boolean fetchProfile(Profile profile) {
		
		/* If the maximal number of players is reached  */
		if(currentNumberOfPlayers == local_players.size() + remote_players.size())
			
			/* Don't add the player */
			return false;
		
		else{
			
			/* Add this profile to the remote players */
			addRemoteProfile(profile);
			
			return true;
		}
			
	}
	
	private List<Profile> getProfileList(){
		List<Profile> result = new ArrayList<>();
		
		for(ControllableProfile cp : local_players)
			result.add(cp.getProfile());
		
		for(RemoteProfile rp : remote_players)
			result.add(rp.getProfile());
		
		return result;
	}
	
	public Server getServer(){
		return server;
	}

	public void removeRemoteProfile(RemoteProfile remoteProfile) {
		removeRemoteProfile(this.remote_players.indexOf(remoteProfile));
	}
	
}
