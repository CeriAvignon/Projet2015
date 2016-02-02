package fr.univavignon.courbes.inter.groupe13;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class DisplayProfileFrame extends JFrame{
	
	private PlayerTable player_table;
	private final Menu menu;

	public DisplayProfileFrame(Menu menu){
		super();
		this.setSize(new Dimension(800, 600));
		
		player_table = new PlayerTable();
		
		this.menu = menu;
		
		JButton jb_back = new JButton("Précédent");
		JButton jb_add = new JButton("Ajouter");
		JButton jb_remove = new JButton("Supprimer");
		
		jb_back.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				DisplayProfileFrame.this.dispatchEvent(new WindowEvent(DisplayProfileFrame.this, WindowEvent.WINDOW_CLOSING));
				DisplayProfileFrame.this.menu.setVisible(true);
				
			}
		});
		
	}
	
	
	public class PlayerTable extends JTable{
		
		public PlayerTable(){
			super();
			JScrollPane jsp_parent = new JScrollPane(this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jsp_parent.getVerticalScrollBar().setUnitIncrement(10);
			jsp_parent.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

			
			this.setModel(new PlayerTableModel());
		}
		
	}
	
	public class PlayerTableModel extends AbstractTableModel{

		private List<ArrayList<String>> rowdata;
		
		public PlayerTableModel() {
			
			rowdata = new ArrayList<ArrayList<String>>();
			
			 try {
			      InputStream is = new FileInputStream(new File("res/profiles/profiles.txt"));
			      InputStreamReader isr = new InputStreamReader(is);
				  BufferedReader br = new BufferedReader(isr);
				  String line;
			      while ((line=br.readLine())!=null)
			      {
			    		  String elem[] = line.split(",");
			    		  
			    		  if(elem.length >= 4){
			    		  
			    			  ArrayList<String> row = new ArrayList<String>();
			    			  
			    			  row.add(elem[3]);
			    			  row.add(elem[2]);
			    			  row.add(elem[1]);
			    			  row.add(elem[0]);
			    			  rowdata.add(row);
			    			  
			    		  }
			    		  else
			    			  System.err.println("Error, line only contain " + elem.length + " it should contain 4 elements");
			    		  
			      }		
			      br.close();  
			      
		    }
			catch (IOException e) {
			System.out.println(e.getMessage());
			}
			
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
	}

}
