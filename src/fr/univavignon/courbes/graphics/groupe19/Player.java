package fr.univavignon.courbes.graphics.groupe19;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
 
public class Player {
   
    private Path2D playerPath;
    private Path2D playerPath1;
    private BasicStroke playerStroke;
    private BasicStroke playerStroke1;
    private Color color,color1;
    private int width,width1;
    private double positionX;
    private double positionY;
    private double positionQ;
    private double positionZ;
    private double speedX;
    private double speedY;
    private double speedQ;
    private double speedZ;
    private double movementX;
    private double movementY;
    private double movementQ;
    private double movementZ;
    private double angle;
    private double angle1;
    private double rotationSpeed;
    private double rotationSpeed1;
   
    public Player(Color color){
        this.color = color;
        this.width = 20;
        this.positionX = 400;
        this.positionY = 300;
        this.speedX = 4;
        this.speedY = 4;
        this.rotationSpeed = 0.25;
        this.angle = Math.toRadians(0);
        playerPath = new GeneralPath();
        playerStroke = new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        playerPath.moveTo(positionX, positionY);

    }
   
    public void update(long delta, boolean rotateRight, boolean rotateLeft){
        if(rotateRight) angle += rotationSpeed * delta / 1e15;
        else if(rotateLeft) angle -= rotationSpeed * delta / 1e15;
        movementX = Math.cos(angle) * speedX * delta / 1e15;
        movementY = Math.sin(angle) * speedY * delta / 1e15;
        positionX += movementX;
        positionY += movementY;
        playerPath.lineTo(positionX, positionY);
    }
    
   
    public void rotate(double angle,double angle1){
        this.angle = Math.toRadians(angle);
        this.angle1 = Math.toRadians(angle1);
    }
 
    public int getWidth(){
        return width;
    }
   
    public double getPositionX(){
        return positionX;
    }
   
    public double getPositionY(){
        return positionY;
    }

    
    public double getAngle(){
        return angle;
    }

   
    public Color getColor(){
        return color;
    }

   
    public Path2D getPath(){
        return playerPath;
    }
    public Path2D getPath1(){
		return playerPath1;
    	
    }
    
    public Stroke getStroke(){
        return playerStroke;
    }


}

