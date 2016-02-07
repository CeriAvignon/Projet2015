package fr.univavignon.courbes.inter.groupe13;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import net.miginfocom.swing.MigLayout;
import fr.univavignon.courbes.common.Profile;

public class DisplayProfileFrame extends JFrame{
	
	private PlayerTable player_table;
	private final Menu menu;
	private JTextField newName;
	private JTextField country;

	public DisplayProfileFrame(Menu menu){
		
		super();
		this.setSize(new Dimension(500, 580));
		
		player_table = new PlayerTable();
		
		this.menu = menu;
		
		this.setLayout(new MigLayout("", "", "[fill][][]"));
		
		JButton jb_back = new JButton("Précédent");
		JButton jb_add = new JButton("Ajouter");
		JLabel jl = new JLabel("Nouveau joueur");
		newName = new JTextField("Pseudonyme");
		country = new JTextField("Pays");
		
		
		newName.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {}
			
			@Override
			public void focusGained(FocusEvent e) {
				if("Pseudonyme".equals(newName.getText()))
					newName.setText("");
				
			}
		});
		
		country.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {}
			
			@Override
			public void focusGained(FocusEvent e) {
				if("Pays".equals(country.getText()))
					country.setText("");
				
			}
		});
		
		jb_back.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				DisplayProfileFrame.this.dispatchEvent(new WindowEvent(DisplayProfileFrame.this, WindowEvent.WINDOW_CLOSING));
				DisplayProfileFrame.this.menu.setVisible(true);
				
			}
		});
		
		
		jb_add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				String pseudo = newName.getText();
				String s_country = country.getText();
				
				/* If the field pseudo and country have been filed 
				 * and if no profile with the same pseudo exist in the profile file
				 */
				if(!"".equals(pseudo) && !"".equals(s_country) && !ProfileFileManager.containPseudo(pseudo)){
					
					ProfileFileManager.addProfile(pseudo, s_country);

				     if(player_table.getModel() instanceof PlayerTableModel){
				    	 PlayerTableModel model = (PlayerTableModel)player_table.getModel();
				    	 
				    	 ArrayList<String> newData = new ArrayList<>();
				    	 newData.add(pseudo);
				    	 newData.add(s_country);
				    	 newData.add("0");
				    
				    	 model.rowdata.add(newData);
				    	 model.fireTableRowsInserted(model.rowdata.size()-1, model.rowdata.size()-1);
				    	 
				    	 newName.setText("Pseudonyme");
				    	 country.setText("Pays");
				    	 
				     }

				}
				
			}
		});

		
		this.add(player_table.getJSP(), "grow, wrap");

		
		
		this.add(jl, "grow,wrap");
		this.add(newName, "grow, wrap");
		this.add(country, "grow, wrap");
		
		JPanel jp = new JPanel(new MigLayout("fill", "[]push[]", "[]"));
		jp.add(jb_add);
		jp.add(jb_back);
		
		this.add(jp, "grow, wrap");
		
		this.setTitle("Gestion des profils");
		this.setVisible(true);
		
	}
	
	
	public class PlayerTable extends JTable{
		
		JScrollPane jsp_parent;
		
		public PlayerTable(){
			super();
			jsp_parent = new JScrollPane(this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jsp_parent.getVerticalScrollBar().setUnitIncrement(10);
			jsp_parent.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

			
			this.setModel(new PlayerTableModel());
		}
		
		public JScrollPane getJSP(){
		 return jsp_parent;
		}
		
		
	}
	
	public class PlayerTableModel extends AbstractTableModel{

		private List<ArrayList<String>> rowdata;
    	private String[] columnNames;
		
		public PlayerTableModel() {
			
			rowdata = new ArrayList<ArrayList<String>>();
			
			 try {
			      InputStream is = new FileInputStream(new File("res/profiles/profiles.txt"));
			      InputStreamReader isr = new InputStreamReader(is);
				  BufferedReader br = new BufferedReader(isr);
				  String line;
				  
				  Vector<PrintableProfile> v_profiles = ProfileFileManager.getProfiles();
				  
				  for(PrintableProfile pp : v_profiles){
	  
	    			  ArrayList<String> row = new ArrayList<String>();
	    			  
	    			  row.add(pp.getProfile().userName);
	    			  row.add(pp.getProfile().country);
	    			  row.add(((Integer)pp.getProfile().score).toString());
	    			  rowdata.add(row);
			    	  
			      }
			    	
			      br.close();  
			      
		    }
			catch (IOException e) {
			System.out.println(e.getMessage());
			}

	      columnNames = new String[4];
	      columnNames[0] = "Pseudo";
	      columnNames[1] = "Pays";
	      columnNames[2] = "Score ELO";
	      
		}

		@Override
		public int getRowCount() {
			return rowdata.size();
		}

		@Override
		public int getColumnCount() {
			if(rowdata != null && rowdata.size() > 0)
				return rowdata.get(0).size();
			else
				return 0;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return rowdata.get(rowIndex).get(columnIndex);
		}
    	
    	public String getColumnName(int c){
    		return columnNames[c];
    	}
	}

}
