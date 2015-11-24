package fr.univavignon.courbes;

public class Main {

	public static void main(String[] args) 
	{
		//test snake
		Snake A=new Snake(5);
		int id=A.getID();
		System.out.println(id);
		A.getChanges();
		
		
		
		//test board
		Board B =new Board();
		System.out.println("size :" + B.size()+ "\n\n");
		
		A.getPosition().displayPosition();
		System.out.println("This position, in hashmap core ? \n" + B.containsPosition(A.getPosition()));
		
		B.addNewElementSnake(A.getPosition(), A);
		System.out.println("\n\nWe added the position, in now ?\n" + B.containsPosition(A.getPosition()));
		
		System.out.println("\nsize :" + B.size());
	}

}
