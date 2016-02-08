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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.simpleimpl.communication.ServerSelectLocalPlayers;
import fr.univavignon.courbes.inter.simpleimpl.data.PrintableProfile;

/**
 * Remote profile displayed for a server.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class RemoteProfileSelector {
	
	private JCheckBox jcb_ready = new JCheckBox();
	private JButton jb_kick = new JButton("Retirer");
	
	private PrintableProfile profile;
	
	public RemoteProfileSelector(final ServerSelectLocalPlayers sg, final JPanel jp, Profile p){

		profile = new PrintableProfile();
		profile.setProfile(p);
		
		jp.add(new JLabel(p.userName));
		jp.add(jcb_ready);
		jp.add(jb_kick);
		
		jcb_ready.setEnabled(false);
		
		jb_kick.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e){
				//TODO faire l'action de kick
//				sg. ...
			}
		});
		
				
	}

	public PrintableProfile getProfile() {
		return profile;
	}

	public JButton getJb_kick() {
		return jb_kick;
	}
	
	

}
