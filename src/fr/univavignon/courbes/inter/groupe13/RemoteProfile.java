package fr.univavignon.courbes.inter.groupe13;

import javax.swing.JLabel;

import fr.univavignon.courbes.common.Profile;

/**
 * Remote profile displayed for a client
 * @author zach
 *
 */
public class RemoteProfile {

	Profile profile;
	JLabel jl;
	
	public RemoteProfile(Profile profile){
		this.profile = profile;
		jl = new JLabel(profile.userName);
	}
	
	public Profile getProfile(){
		return profile;
	}
	
	public JLabel getJl(){
		return jl;
	}
}
