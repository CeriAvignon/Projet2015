package fr.univavignon.courbes.graphics.groupe19;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.graphics.GraphicDisplay;


public class Graphic extends JFrame implements GraphicDisplay  {
	
	private static final Profile ProfileIdHelper = null;

//	private GraphicDisplay graphic;
	
	
	public Board board;
	public int pointThreshold;
	public List<Profile> players;
	
	 /**
	 * 
	 */
	public Graphic() {
		// TODO Auto-generated constructor stub
		// titre de la fenêtre
        super("TestSnake");
        
        
    	
    	//pnln.add(panelLibelle);
    	//pnln.add(panelScore);
   
        
     //   this.graphic = new GraphicDisplay();
		
      
        setDefaultCloseOperation(EXIT_ON_CLOSE);
   
        setResizable(false);

        
     
        final JPanel PanelBoard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                  super.paintComponent(g);
                  
               
             //    Graphic.this.graphic.init(null, null, null);;
            }
          
      };
      

      PanelBoard.setPreferredSize(new Dimension(500, 500));
      PanelBoard.setBackground(Color.BLACK);
      setFocusable(false);
      PanelBoard.setFocusable(true);
      setContentPane(PanelBoard);
      
      Thread thread = new Thread(new Runnable() {                  
            @Override
            public void run() {
                  while (true) { 
 
              
                       
                	  Graphic.this.setLayout(new BorderLayout());
                        
            	    //   	panelScore.setLayout(new FlowLayout(FlowLayout.LEFT));
            	       	//panelScore.setLayout(new GridLayout(8,1));
            	    	//pnln.setLayout(new GridLayout(4,0));
            	      //pnln.setPreferredSize(new Dimension(50, 900));
            	     // Graphic.this.add(pnln,BorderLayout.WEST);
            	      
                        Graphic.this.add(PanelBoard,BorderLayout.EAST);
            	      
            	      PanelBoard.revalidate();
            	      
                      PanelBoard.repaint();
                     /*   PanelBoard.add(myButton, BorderLayout.SOUTH);
                        add(PanelBoard);
                        pack();
                        setVisible(true);*/
                  }                        
            }
      });
      
      thread.start();
      
	}

	public void init(Board board, int pointThreshold, List<Profile> players) {
		// TODO Auto-generated method stub
		
		 final List<Profile> listpro = new ArrayList<Profile>();
			
		 listpro.add(ProfileIdHelper);
		  for(int i = 0; i < listpro.size(); i++)

			 System.out.println("Élément à l'index " + i + " = " + listpro.get(i));
		
	}
	
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
	}
	
	
    /**
     * @param args
     */
    public static void main(String[] args) {
         
          Graphic fenetre = new Graphic();
      
          fenetre.pack();
     
          fenetre.setLocationRelativeTo(null);

          fenetre.setVisible(true);
    }

	@Override
	public void init(Board board, int pointThreshold, List<Profile> players,
			JPanel boardPanel, JPanel scorePanel) {
		// TODO Auto-generated method stub
		
	}


	
}
