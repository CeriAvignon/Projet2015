package fr.univavignon.courbes.inter.simpleimpl.communication;

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
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import fr.univavignon.courbes.inter.simpleimpl.menus.Menu;
import fr.univavignon.courbes.network.simpleimpl.Client;
import net.miginfocom.swing.MigLayout;

/**
 * 
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class JoinServer extends JFrame{
	
	Menu m;
	JTextField jtf_ip = new JTextField("192.168.0.1");
	JTextField jtf_port = new JTextField("453");
	
	
	public JoinServer(Menu m){
	
		super();
		this.setSize(new Dimension(220, 120));
		this.m = m;
		
		this.setLayout(new MigLayout());

		JButton jb_back = new JButton("Retour");
		JButton jb_next = new JButton("Continuer");
		
		this.add(new JLabel("IP"));
		this.add(jtf_ip, "wrap");
		this.add(new JLabel("Port"));
		this.add(jtf_port, "wrap");
		this.add(jb_next);
		this.add(jb_back);
		
		jb_back.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JoinServer.this.dispatchEvent(new WindowEvent(JoinServer.this, WindowEvent.WINDOW_CLOSING));
				JoinServer.this.m.setVisible(true);
			}
		});
		
		jb_next.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Client c = new Client();
				c.setIp(jtf_ip.getText());
				c.setPort(Integer.parseInt(jtf_port.getText()));

				ClientGame cg = new ClientGame(JoinServer.this, c);
				c.setProfileHandler(cg);
				
				//TODO Comment savoir si la connexion a bien eu lieu ?
				c.launchClient();

				cg.display();
				JoinServer.this.setVisible(false);
				
				
			}
		});
		
		this.setTitle("Choix du serveur");
		this.setVisible(true);

	}
}
