package fr.univavignon.courbes.inter.groupe08;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JLayeredPane;
import javax.swing.JTable;
import javax.swing.JMenu;
import javax.swing.JProgressBar;
import javax.swing.JLabel;

public class Stats {

	JFrame frame;
	private JTextField nb_parties;
	private JTextField nb_victoires;
	private JTextField nb_defaite;
	private JTextField ratio;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Stats window = new Stats();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Stats() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		BufferedReader in = null;
		String nbparties = null,victoires = null,defaites = null;
		int ratiovic = 0;
		try {
		    in = new BufferedReader(new FileReader("score.txt"));
		    String read = null;
		    while ((read = in.readLine()) != null) {
		        String[] splited = read.split("\\|");
		        nbparties = splited[0];
		        victoires = splited[1];
		        defaites = splited[2];
		        ratiovic = Integer.parseInt(victoires)/Integer.parseInt(defaites);
		        System.out.println(ratiovic);
		        System.out.println(nb_parties);
		        System.out.println(victoires);
		        System.out.println(defaites);
		    }
		} catch (IOException e) {
		    System.out.println("There was a problem: " + e);
		    e.printStackTrace();
		} finally {
		    try {
		        in.close();
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
		}
		
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 434, 261);
		frame.getContentPane().add(tabbedPane);
		
		JPanel Stats = new JPanel();
		tabbedPane.addTab("Statistiques", null, Stats, null);
		Stats.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Nombre de parties : ");
		lblNewLabel.setBounds(33, 24, 131, 14);
		Stats.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Nombre de victoires : ");
		lblNewLabel_1.setBounds(33, 64, 131, 14);
		Stats.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Nombre de défaites : ");
		lblNewLabel_2.setBounds(33, 109, 131, 14);
		Stats.add(lblNewLabel_2);
		
		JLabel lblRatio = new JLabel("Ratio : ");
		lblRatio.setBounds(33, 144, 94, 14);
		Stats.add(lblRatio);
		
		nb_parties = new JTextField();
		nb_parties.setEditable(false);
		nb_parties.setText(nbparties);
		nb_parties.setBounds(216, 21, 57, 20);
		Stats.add(nb_parties);
		nb_parties.setColumns(10);
		
		nb_victoires = new JTextField();
		nb_victoires.setEditable(false);
		nb_victoires.setText(victoires);
		nb_victoires.setColumns(10);
		nb_victoires.setBounds(216, 61, 57, 20);
		Stats.add(nb_victoires);
		
		nb_defaite = new JTextField();
		nb_defaite.setEditable(false);
		nb_defaite.setText(defaites);
		nb_defaite.setColumns(10);
		nb_defaite.setBounds(216, 106, 57, 20);
		Stats.add(nb_defaite);
		
		ratio = new JTextField();
		ratio.setEditable(false);
		String rate = Integer.toString(ratiovic);
		ratio.setText(rate);
		ratio.setColumns(10);
		ratio.setBounds(216, 141, 57, 20);
		Stats.add(ratio);
		
		JPanel Paramètres = new JPanel();
		tabbedPane.addTab("Paramètres", null, Paramètres, null);
	}
}
