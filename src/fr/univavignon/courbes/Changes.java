package fr.univavignon.courbes;
public class Changes 
{
	double timerLeft;
	Item type;
	
	public Changes(Item type)
	{	
		if(type instanceof Bonus)
		{
			type=new Bonus();
			timerLeft=5;  // en secondes
			this.type=type;
		}
		else if(type instanceof Malus)
		{
			type=new Malus();
			timerLeft=7; // en secondes
			this.type=type;
		}
		else if(type instanceof GeneralEffectItem)
		{
			type=new GeneralEffectItem();
			timerLeft=10; // en secondes
			this.type=type;
		}
		else
		{
			System.out.println("Error");
		}
	}
	
	

	
}
