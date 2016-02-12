package fr.univavignon.courbes.inter.simpleimpl.remote.client;

/*
 * Courbes
 * Copyright 2015-16 L3 Info UAPV 2015-16
 * 
 * This file is part of Courbes.
 * 
 * Courbes is free software: you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation, 
 * either version 2 of the License, or (at your option) any later version.
 * 
 * Courbes is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Courbes. If not, see <http://www.gnu.org/licenses/>.
 */

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.rmi.server.RemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ClientProfileHandler;
import fr.univavignon.courbes.inter.ErrorHandler;
import fr.univavignon.courbes.inter.simpleimpl.LocalProfileSelectorOoold;
import fr.univavignon.courbes.inter.simpleimpl.profiles.ProfileManager;
import fr.univavignon.courbes.inter.simpleimpl.remote.server.RemoteProfile;
import fr.univavignon.courbes.network.simpleimpl.client.ClientCommunicationImpl;
import net.miginfocom.swing.MigLayout;

/**
 * 
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ClientGame extends JFrame implements ClientProfileHandler, ErrorHandler{

	private ClientGameServerConnectionPanel js;
	private ClientCommunicationImpl c;
	
	ArrayList<LocalProfileSelectorOoold> local_players;
	List<RemoteProfile> remote_players;
	Vector<PrintableProfile> availableProfiles;
	
	JPanel localPlayerPanel;
	JPanel remotePlayerPanel;

	JButton jb_back = new JButton("Retour");
	JButton jb_ready = new JButton("Prêt");
	
	
	public ClientGame(ClientGameServerConnectionPanel js, ClientCommunicationImpl c){
		
		super();
		
		this.c = c;
		this.js = js;
		this.setSize(new Dimension(800, 600));
		
		local_players = new ArrayList<>();
		remote_players = new ArrayList<>();
		
		this.setLayout(new GridLayout(3, 1));

		availableProfiles = ProfileManager.getProfiles();
		JPanel jp_previous_next = new JPanel(new FlowLayout());
		
		remotePlayerPanel = new JPanel(new MigLayout());
		remotePlayerPanel.add(new JLabel("Joueurs distants"), "wrap");
		
		localPlayerPanel = new JPanel(new MigLayout());
		
		this.add(remotePlayerPanel);
		this.add(localPlayerPanel);
		this.add(jp_previous_next);
		
//		remotePlayerPanel.setLayout(new GridLayout(6,2));
//		remotePlayerPanel.add(new JLabel("Prêt"));
		
		localPlayerPanel.add(new JLabel("Joueurs locaux"));
		localPlayerPanel.add(new JLabel("Gauche"));
		localPlayerPanel.add(new JLabel("Droite"));
		localPlayerPanel.add(new JLabel("Etat / Action"), "wrap");

		jp_previous_next.add(jb_back);
		jp_previous_next.add(jb_ready);
		
		jb_back.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				ClientGame.this.c.closeClient();
				ClientGame.this.dispatchEvent(new WindowEvent(ClientGame.this, WindowEvent.WINDOW_CLOSING));
				ClientGame.this.js.setVisible(true);
			}
		});
		
		jb_ready.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(isReadyToStartGame()){
					//TODO dire au serveur qu'on est prêt à démarrer la partie
				}
				else{
					JOptionPane.showMessageDialog(ClientGame.this, "<html>Les données des joueurs locaux ne sont pas correctement remplies. Vérifiez que :" +
							"<br/>- tous les profils sont définis et différents ;" +
							"<br/>- toutes les commandes sont définies et différentes.</html>");
				}
			}
		});
		
		/* Add one profile selector (a new one is added when the profile selected) */
		addLocalProfileSelector();
		
		this.setTitle("Partie réseau (client)");
		
	}
	
	public void display(){
		this.setVisible(true);
	}

	public ClientCommunicationImpl getC() {
		return c;
	}

	public void setC(ClientCommunicationImpl c) {
		this.c = c;
	}

	@Override
	public void updateProfiles(List<Profile> profiles) {
		
		
		//TODO Comment faire pour avoir les meme ID locaux et remote ?
		
		boolean[] remoteProfileRemoved = new boolean[this.remote_players.size()];
		boolean[] localProfileRemoved = new boolean[this.local_players.size()];

		/* The last local profile selector is always an empty profile to enable the selection of a new local player */
		localProfileRemoved[localProfileRemoved.length-1] = true;
		
		for(int i = 0 ; i < localProfileRemoved.length ; ++i)
			localProfileRemoved[i] = true;
		 for(int i = 0 ; i < remoteProfileRemoved.length ; ++i)
			remoteProfileRemoved[i]= true;
		 
		 /* For each profile on the server */
		 for(Profile p : profiles){
			 
			 boolean found = false;
			 int i = 0;
			 
			 /* Check if the profile id appears in the local profiles */ 
			 while(!found && i < local_players.size() - 1){
				 
				 LocalProfileSelectorOoold lps = local_players.get(i);
				 
				 if(lps.getC_profile().getProfile().profileId == p.profileId){
					 localProfileRemoved[i] = false;
					 lps.getSendProfileToServer().setText("Enregistré");
					 found = true;
				 }
				 
				 ++i;
				 
			 }
			 
			 i = 0;
			 
			 /* Check if the profile id appears in the remote */
			 while(!found && i < remote_players.size()){
				 
				 Profile p_remote =  remote_players.get(i).getProfile();
				 
				 if(p_remote.profileId == p.profileId){
					 remoteProfileRemoved[i] = false;
					 found = true;
				 }
				 
				 ++i;
				 
			 }
			 
			 if(!found)
				 addRemoteProfile(p);
			 
		 }

		 for(int i = 0 ; i < remote_players.size() ; ++i)
			 if(remoteProfileRemoved[i])
				 removeRemoteProfile(i);
		 
		  for(int i = 0 ; i < local_players.size() ; ++i)
			 if(localProfileRemoved[i])
				 removeLocalProfile(i);
			 
	}

	protected boolean isReadyToStartGame() {

		boolean isReady = true;
		
		/* For all couple of profiles (except the last one which is empty) */
		for(int i = 0 ; i < local_players.size() - 1  ; ++i){
			
			ControllableProfile cp1 = local_players.get(i).getC_profile();
			int key1_1 = cp1.getLeft().getKeyCode();
			int key1_2 = cp1.getRight().getKeyCode();
			
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
		
		return false;
	}
	
	public void addLocalProfileSelector(){
		local_players.add(new LocalProfileSelectorOoold(availableProfiles, this, localPlayerPanel)); 
		localPlayerPanel.repaint();
	}

	
	public void removeLocalProfile(int i){
		
		LocalProfileSelectorOoold lps = local_players.get(i);
		local_players.remove(i);
		
		localPlayerPanel.remove(lps.getJc_playerSelector());
		localPlayerPanel.remove(lps.getLeftButton());
		localPlayerPanel.remove(lps.getRightButton());
		localPlayerPanel.remove(lps.getSendProfileToServer());
		localPlayerPanel.remove(lps.getRemove());
		localPlayerPanel.validate();
		localPlayerPanel.repaint();
	}
	
	public void addRemoteProfile(Profile p){
		
		RemoteProfile rp = new RemoteProfile(p);
		remote_players.add(rp);
		remotePlayerPanel.add(rp.getJl(), "wrap");
		remotePlayerPanel.validate();
		remotePlayerPanel.repaint();
		
	}
	
	public void removeRemoteProfile(int i){
		
		RemoteProfile rp = remote_players.get(i);
		remote_players.remove(i);
		
		remotePlayerPanel.remove(rp.getJl());
		remotePlayerPanel.repaint();
		
	}

	@Override
	public void displayError(String errorMessage) {

		JOptionPane.showMessageDialog(this, errorMessage);

		/* Close the window and display the JoinServer window */
		this.c.closeClient();
		this.dispatchEvent(new WindowEvent(ClientGame.this, WindowEvent.WINDOW_CLOSING));
		this.js.setVisible(true);
	}
	
}
