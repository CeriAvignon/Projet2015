package fr.univavignon.courbes.inter.groupe13;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class DisplayProfileFrame extends JFrame{
	
	public DisplayProfileFrame(){
		super();
		this.setSize(new Dimension(800, 600));
		
		
		
		
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
			rowda
		}

		@Override
		public int getRowCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getColumnCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}
		
		
		
	}

}
