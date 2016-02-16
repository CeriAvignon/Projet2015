package fr.univavignon.courbes.graphics.groupe19;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
 
import javax.swing.*;
/**
 * 
 * @author nsrkbc
 *
 */
public class Main extends JPanel implements Runnable {
   
    private Player player,player1;
    private boolean rotateLeft;
    private boolean rotateRight;
    private boolean rotateUp;
    private boolean rotateDown;
    private long lastFrame,lastFrame1;
    private long delta,delta1;
   
    public Main(){
        JFrame f = new JFrame();
      //  f.setExtendedState(JFrame.MAXIMIZED_BOTH);   
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        
        addKeyListener(movementListener);

        setFocusable(true);
        f.add(this);
        f.setVisible(true);       
        f.setResizable(false);
        f.setPreferredSize(new Dimension(500, 500));
        f.pack();
        Thread t = new Thread(this);
        t.start();
    }
   
    public void run(){
        player = new Player(Color.BLUE.darker());
       player1 = new Player(Color.GREEN.darker());
        lastFrame = System.nanoTime();
        lastFrame1 = System.nanoTime();
        while(true){
            updateTime();
            updatePlayer();
            repaint();
            
            try{Thread.sleep(1);}catch(Exception e){}
        }
    }
   
    private void updateTime(){
        long thisf = System.nanoTime() - lastFrame;
        delta = thisf - lastFrame;
    //    long thisn = System.nanoTime() - lastFrame1;
     //   delta1 = thisn - lastFrame1;
    }
   
    private void updatePlayer(){
        player.update(delta, rotateLeft, rotateRight);
    }
   
    public KeyAdapter movementListener = new KeyAdapter() {
        public void keyPressed(KeyEvent e){
            if(e.getKeyCode() == KeyEvent.VK_LEFT) rotateLeft = true;
            if(e.getKeyCode() == KeyEvent.VK_RIGHT) rotateRight = true;
        /*    if(e.getKeyCode() == KeyEvent.VK_UP) rotateUp = true;
            if(e.getKeyCode() == KeyEvent.VK_DOWN) rotateDown = true; */
        }
        public void keyReleased(KeyEvent e){
            if(e.getKeyCode() == KeyEvent.VK_LEFT) rotateLeft = false;
            if(e.getKeyCode() == KeyEvent.VK_RIGHT) rotateRight = false;
      /*      if(e.getKeyCode() == KeyEvent.VK_UP) rotateUp = false;
            if(e.getKeyCode() == KeyEvent.VK_DOWN) rotateDown = false; */
        }
    };
  
   
    public void paintComponent(Graphics g/*,Graphics d*/){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setStroke(player.getStroke());
        g2d.setColor(player.getColor());
        g2d.draw(player.getPath());
        g2d.setColor(player.getColor().darker());
        g2d.fillOval((int) player.getPositionX() - player.getWidth() / 2, (int) player.getPositionY() - player.getWidth() / 2, player.getWidth(), player.getWidth());
        g2d.setColor(player.getColor().brighter());
        g2d.fillOval((int) player.getPositionX() - player.getWidth() / 4, (int) player.getPositionY() - player.getWidth() / 4, player.getWidth() / 2, player.getWidth() / 2);
        
    }
   
    public static void main(String[] args) {
        new Main();
    }
}
