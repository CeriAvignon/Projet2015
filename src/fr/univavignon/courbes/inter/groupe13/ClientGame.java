package fr.univavignon.courbes.inter.groupe13;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ClientProfileHandler;
import fr.univavignon.courbes.network.groupe06.Client;

public class ClientGame extends JFrame implements ClientProfileHandler{

	private JoinServer js;
	private Client c;
	
	List<LocalProfileSelector> local_players;
	List<Profile> remote_players;
	
	JPanel localPlayerPanel = new JPanel(new GridLayout(7,3));
	JPanel remotePlayerPanel = new JPanel(new GridLayout(7,3));

	JButton jb_back = new JButton("Retour");
	JButton jb_ready = new JButton("Prêt");
	
	
	public ClientGame(JoinServer js, Client c){
		
		super();
		
		this.c = c;
		this.js = js;
		this.setSize(new Dimension(800, 600));
		
		this.setLayout(new GridLayout(3, 1));
		
		JPanel jp_previous_next = new JPanel(new FlowLayout());
		
		this.add(remotePlayerPanel);
		this.add(localPlayerPanel);
		this.add(jp_previous_next);
		
		remotePlayerPanel.setLayout(new GridLayout(6,2));
		remotePlayerPanel.add(new JLabel("Joueurs distants"));
		remotePlayerPanel.add(new JLabel("Prêt"));
		
		localPlayerPanel.setLayout(new GridLayout(6, 4));
		localPlayerPanel.add(new JLabel("Joueurs locaux"));
		localPlayerPanel.add(new JLabel("Gauche"));
		localPlayerPanel.add(new JLabel("Droite"));
		localPlayerPanel.add(new JLabel("Etat / Action"));
		
		JButton jb_back = new JButton("Retour");
		JButton jb_next = new JButton("Prêt");
		
		jb_back.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				ClientGame.this.c.closeClient();
				ClientGame.this.dispatchEvent(new WindowEvent(ClientGame.this, WindowEvent.WINDOW_CLOSING));
				ClientGame.this.js.setVisible(true);
			}
		});
		
		jb_next.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(isReadyToStartGame()){
					//TODO dire au serveur qu'on est prêt à démarrer la partie
				}
				else{
					// TODO Pop-up indiquant que les profils locaux ou les touches ne sont pas compatibles
				}
			}
		});
		
		LocalProfileSelector lps = new LocalProfileSelector(ProfileFileManager.getProfiles()); 
		local_players.add(lps);
		
		//TODO afficher les infos de ce profile, avec le bouton état et "Retirer", lorsqu'un profil est sélectionné, ajouter une ligne
		
	}

	@Override
	public void updateProfiles(List<Profile> profiles) {
		
	}

	protected boolean isReadyToStartGame() {

		boolean isReady = true;
		
		/* For all couple of profiles */
		for(int i = 0 ; i < local_players.size() ; ++i){
			
			ControllableProfile cp1 = local_players.get(i).getC_profile();
			int key1_1 = cp1.getLeft().getKeyCode();
			int key1_2 = cp1.getRight().getKeyCode();
			
			if(key1_1 == key1_2)
				isReady = false;
			
			for(int j = i+1 ; j < local_players.size() ; ++j){
				
				ControllableProfile cp2 = local_players.get(j).getC_profile();
				int key2_1 = cp2.getLeft().getKeyCode();
				int key2_2 = cp2.getRight().getKeyCode();
				
				if(key1_1 == key2_1 
						|| key1_1 == key2_2
						|| key1_2 == key2_1
						|| key1_2 == key2_2
						|| cp1.getProfile().userName.equals(cp2.getProfile().userName)){
					isReady = false;
					
				}
			}
		}
		
		return false;
	}
	
}
