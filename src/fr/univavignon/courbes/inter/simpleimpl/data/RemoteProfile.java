package fr.univavignon.courbes.inter.simpleimpl.data;

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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.simpleimpl.communication.ServerSelectRemotePlayers;
import fr.univavignon.courbes.network.simpleimpl.Server;

/**
 * Remote profile displayed for a client.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class RemoteProfile {

	Profile profile;
	JLabel jl;
	JCheckBox isReady;
	JButton jb_kick;
	
	public RemoteProfile(Profile profile){
		this.profile = profile;
		jl = new JLabel(profile.userName);		
		
	}
	
	public RemoteProfile(Profile profile, final Server server, final ServerSelectRemotePlayers ssrp){
		
		this(profile);

		isReady = new JCheckBox();
		isReady.setSelected(false);
		
		jb_kick = new JButton("Retirer");
		
		jb_kick.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ssrp.removeRemoteProfile(RemoteProfile.this);
			}
		});
		
		
	}
	
	public JCheckBox getIsReady() {
		return isReady;
	}

	public void setIsReady(JCheckBox isReady) {
		this.isReady = isReady;
	}

	public JButton getJb_kick() {
		return jb_kick;
	}

	public void setJb_kick(JButton jb_kick) {
		this.jb_kick = jb_kick;
	}

	public void setJl(JLabel jl) {
		this.jl = jl;
	}

	public Profile getProfile(){
		return profile;
	}
	
	public JLabel getJl(){
		return jl;
	}
}
