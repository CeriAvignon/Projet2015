package fr.univavignon.courbes.inter.groupe13;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import fr.univavignon.courbes.network.groupe06.Client;

public class JoinServer extends JFrame{
	
	Menu m;
	JTextField jtf_ip = new JTextField("192.168.0.1");
	JTextField jtf_port = new JTextField("453");
	
	
	public JoinServer(Menu m){
	
		super();
		this.setSize(new Dimension(800, 600));
		this.m = m;
		
		this.setLayout(new GridLayout(3, 2));

		JButton jb_back = new JButton("Retour");
		JButton jb_next = new JButton("Continuer");
		
		this.add(new JLabel("IP"));
		this.add(jtf_ip);
		this.add(new JLabel("Port"));
		this.add(jtf_port);
		
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
				
				JoinServer.this.setVisible(false);
				
				
			}
		});
		

	}
}
