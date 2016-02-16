package fr.univavignon.courbes.physics.groupe04;
import java.awt.*;
import javax.swing.*;
import fr.univavignon.courbes.graphics.*;
import java.util.*;
import fr.univavignon.courbes.common.*;



public class MyPannel extends JPanel 
{

  public HashMap<Position, Integer> mapos = new HashMap<Position,Integer>();
  Snake serpent[];


  public MyPannel()
  {

    super();
    this.setBackground(Color.GRAY);
  }

	public void paintComponent(Graphics g)
	{
   
    super.paintComponent(g);
		
    for(Map.Entry<Position, Integer> mapentry : mapos.entrySet())
    {
      switch(mapentry.getValue())
      {
          case 0 :
            g.setColor(Color.orange);
            break;

          case 1 : 
            g.setColor(Color.red);
            break;

          case 2 : 
            g.setColor(Color.blue);
            break;

          case 3 : 
            g.setColor(Color.pink);
            break;

          case 4 : 
            g.setColor(Color.cyan);
            break;

          case 5 : 
            g.setColor(Color.green);
            break;

          case 6 : 
            g.setColor(Color.magenta);
            break;

          case 7 : 
            g.setColor(Color.yellow);
            break;
      }

      g.fillOval(mapentry.getKey().x, mapentry.getKey().y, 1, 1 );
    }

    for (int i = 0; i < serpent.length; i++ ) 
    {
        g.fillOval(serpent[i].currentX, serpent[i].currentY, (int) serpent[i].headRadius, (int) serpent[i].headRadius);
    }
		
  }

    public void addMapHead(Map<Position, Integer> map, Snake t[])
    {
        //mapos.put(mapentry.getKey(), mapentry.getValue());

        mapos.putAll(map);
        serpent = (Snake[]) t.clone();
    }
  

}