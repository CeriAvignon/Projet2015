package fr.univavignon.courbes.inter.simpleimpl.menus;

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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import fr.univavignon.courbes.common.Player;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.simpleimpl.communication.ClientGame;
import fr.univavignon.courbes.inter.simpleimpl.communication.ServerSelectLocalPlayers;
import fr.univavignon.courbes.inter.simpleimpl.data.PrintableProfile;
import fr.univavignon.courbes.inter.simpleimpl.game.ControllableProfile;

/**
 * 
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class LocalProfileSelector {
	
	private List<Player> availablePlayers;
	
	private JButton leftButton = new JButton("Non défini");
	private JButton rightButton = new JButton("Non défini");
	
	private JButton sendProfileToServer = new JButton("Envoyer au serveur");
	private JButton remove = new JButton("Retirer");

	private JComboBox<PrintableProfile> jc_playerSelector;
	
	private ControllableProfile c_profile;
	
	/**
	 * Constructor only used by the client. Call the standard constructor and add 2 buttons (the first one to send a profile to the server, the second one to remove a profile from the server)
	 * @param players
	 * @param cg
	 * @param jp
	 */
	public LocalProfileSelector(Vector<PrintableProfile> players, final ClientGame cg, final JPanel jp){
		this(players, jp, false);
		
		jp.add(sendProfileToServer);
		jp.add(remove, "wrap");

		remove.setEnabled(false);
		
		jc_playerSelector.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

					cg.addLocalProfileSelector();
			}
		});
		
		sendProfileToServer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				sendProfileToServer.setEnabled(false);
				sendProfileToServer.setText("Envoie...");
				cg.getC().addProfile(c_profile.getProfile());
				sendProfileToServer.setText("Envoyé");
				remove.setEnabled(true);
				jc_playerSelector.setEnabled(false);
				
				/* Add a new profile selector */
				cg.addLocalProfileSelector();
				
				
			}
		});
		
		remove.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				remove.setText("Suppression...");
				cg.getC().removeProfile(c_profile.getProfile());
				jp.remove(jc_playerSelector);
				jp.remove(leftButton);
				jp.remove(rightButton);
				jp.remove(sendProfileToServer);
				jp.remove(remove);
				jp.validate();
				jp.repaint();
			}
		});
		
	}

	/**
	 * Constructor used to create a default profile selector (1 combobox for the profile, 2 buttons for the directions)
	 * @param players
	 * @param jp
	 */
	public LocalProfileSelector(Vector<PrintableProfile> players, final JPanel jp, final ServerSelectLocalPlayers sslp){
		this(players, jp, false);
		
		jp.add(remove, "wrap");

		remove.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sslp.removeLocalProfile(LocalProfileSelector.this);
			}
		});
	}
	
	/**
	 * Constructor used to create a default profile selector (1 combobox for the profile, 2 buttons for the directions)
	 * @param players
	 * @param jp
	 */
	public LocalProfileSelector(Vector<PrintableProfile> players, JPanel jp){
		this(players, jp, true);
	}
	
	
	private LocalProfileSelector(Vector<PrintableProfile> players, JPanel jp, boolean newLayoutLineAfterAdd){
		
		c_profile = new ControllableProfile();
		
		jc_playerSelector = new JComboBox<>(players);
		
		jp.add(this.jc_playerSelector);
		jp.add(this.leftButton);
		
		if(newLayoutLineAfterAdd)
			jp.add(this.rightButton, "wrap");
		else
			jp.add(this.rightButton);
		
		c_profile = new ControllableProfile();
		c_profile.setProfile(players.get(0).getProfile());
		
		rightButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				rightButton.setText("?");
			}
		});
		
		leftButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				leftButton.setText("?");
			}
		});

		rightButton.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				rightButton.setText(KeyEvent.getKeyText(e.getKeyCode()));
		   		c_profile.setRight(e);}
			
			@Override
			public void keyPressed(KeyEvent e) {}
		});
		
		leftButton.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				leftButton.setText(KeyEvent.getKeyText(e.getKeyCode()));
	   		   c_profile.setLeft(e);
	   		}
			
			@Override
			public void keyPressed(KeyEvent e) {}
		});
		

		jc_playerSelector.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				PrintableProfile pp = (PrintableProfile)jc_playerSelector.getSelectedItem();
				ControllableProfile previous_profile = c_profile;
				c_profile = new ControllableProfile();
				c_profile.setProfile(pp.getProfile());
				c_profile.setLeft(previous_profile.getLeft());
				c_profile.setRight(previous_profile.getRight());
				
			}
		});
	}

	public JButton getLeftButton() {
		return leftButton;
	}

	public ControllableProfile getC_profile() {
		return c_profile;
	}

	public void setC_profile(ControllableProfile c_profile) {
		this.c_profile = c_profile;
	}

	public void setLeftButton(JButton leftButton) {
		this.leftButton = leftButton;
	}

	public JButton getRightButton() {
		return rightButton;
	}

	public void setRightButton(JButton rightButton) {
		this.rightButton = rightButton;
	}

	public JComboBox<PrintableProfile> getJc_playerSelector() {
		return jc_playerSelector;
	}

	public void setJc_playerSelector(JComboBox<PrintableProfile> jc_playerSelector) {
		this.jc_playerSelector = jc_playerSelector;
	}
	public JButton getSendProfileToServer() {
		return sendProfileToServer;
	}

	public void setSendProfileToServer(JButton sendProfileToServer) {
		this.sendProfileToServer = sendProfileToServer;
	}

	public JButton getRemove() {
		return remove;
	}

	public void setRemoveFromServer(JButton remove) {
		this.remove = remove;
	}
	
	

}
