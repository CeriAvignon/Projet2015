package fr.univavignon.courbes.graphics.groupe23;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Snake;
import fr.univavignon.courbes.graphics.GraphicDisplay;

public class MyGraphic implements GraphicDisplay {
Board b;
Panel panel= new Panel();
DefaultListModel dm =new DefaultListModel();
JList list=new JList(dm);
Color couleur[]={Color.blue,Color.red,Color.orange,Color.green,Color.yellow};
List<Profile> players;
List<Point> prec=new ArrayList();
public static void main(String[] args) throws InterruptedException
{
	JFrame fenetre= new JFrame();
	fenetre.setSize(600, 600);
	fenetre.setLayout(null);
	JPanel boardPanel = new JPanel();
	boardPanel.setBounds(126, 11, 314, 267);
JPanel scorePanel = new JPanel();
scorePanel.setBounds(10, 11, 100, 278);
Board board = new Board();
board.height=500;
board.width=500;
Snake []snakes={new Snake(),new Snake()};
board.snakes=snakes;
board.snakes[0].currentScore=0;
board.snakes[1].currentScore=0;
board.snakes[0].currentX=10;
board.snakes[0].currentY=10;
board.snakes[1].currentX=70;
board.snakes[1].currentY=50;
board.snakes[0].headRadius=10;
board.snakes[1].headRadius=10;
List<Profile> pro = new ArrayList();
Profile prof1 =new Profile();
prof1.userName="omar";
Profile prof2 =new Profile();
prof2.userName="omar";
pro.add(prof1);
pro.add(prof2);
MyGraphic graph =new MyGraphic();
graph.init(board,0,pro,boardPanel,scorePanel);
fenetre.add(boardPanel);
fenetre.add(scorePanel);
fenetre.setVisible(true);

for(int i=0;i<200;i++)
{
	board.snakes[0].currentX++;
	board.snakes[0].currentY++;
	board.snakes[1].currentX++;
	board.snakes[1].currentY--;
	graph.update();
}
}
@Override
	public void init(Board board, int pointThreshold, List<Profile> players, JPanel boardPanel, JPanel scorePanel){
		// TODO Auto-generated method stub
		this.b=board;
		boardPanel.setLayout(null);
		panel.setBackground(Color.BLACK);
		panel.setBounds(10, 11,b.width , b.height);
boardPanel.add(panel);
scorePanel.setLayout(null);
this.players=players;
list.setBounds(10, 11, 90, 209);
scorePanel.add(list);
String text="";
int i=0;
for(Profile j:players)
{
	text=pointThreshold+ " "+j.userName;
	dm.add(i,text);	
prec.add(new Point(b.snakes[i].currentX,b.snakes[i].currentX));
i++;
}
	}

	@Override
	public void update() {
		String text;
		int i =0;
		for(Profile j:players)
		{
		text=j.score+ " "+j.userName;
		dm.set(i, text);
Thread th =new Thread(new Repaint(b.snakes[i].currentX, b.snakes[i].currentY, (int)b.snakes[i].headRadius, couleur[i],panel,prec.get(i)));
th.start();
try {
	Thread.sleep(20);;
} catch (InterruptedException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
prec.set(i, new Point(b.snakes[i].currentX,b.snakes[i].currentY));
		i++;
		}
		
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		
	}

class Repaint implements Runnable
{
	int x; int y;int rayon;Color couleur;Panel panel;Point prec;
	public Repaint(int x, int y,int rayon,Color couleur,Panel panel,Point prec)
	{
	this.x=x;
	this.y=y;
	this.rayon=rayon;
	this.couleur=couleur;
	this.panel=panel;
	this.prec=prec;
	}
public void run()
{
panel.repaint(x, y, rayon,couleur,prec);	
}
}
}