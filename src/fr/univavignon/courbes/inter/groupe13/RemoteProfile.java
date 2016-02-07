package fr.univavignon.courbes.inter.groupe13;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.network.groupe06.Server;

/**
 * Remote profile displayed for a client
 * @author zach
 *
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
