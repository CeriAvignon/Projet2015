package fr.univavignon.courbes.inter.groupe13;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.univavignon.courbes.common.Profile;

/**
 * Remote profile displayed for a server
 * @author zach
 *
 */
public class RemoteProfileSelector {
	
	private JCheckBox jcb_ready = new JCheckBox();
	private JButton jb_kick = new JButton("Retirer");
	
	private PrintableProfile profile;
	
	public RemoteProfileSelector(final ServerGame sg, final JPanel jp, Profile p){

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
