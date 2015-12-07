
public class Changes 
{
	double timerLeft;
	Item type;
	
	public Changes(Item type)
	{	
		if(type instanceof Bonus)
		{
			type=new Bonus();
			timeLeft=5;  // en secondes
			this.type=type;
		}
		else if(type instanceof Malus)
		{
			type=new Malus();
			timeLeft=7; // en secondes
			this.type=type;
		}
		else if(type instanceof GeneralEffectItem)
		{
			type=new GeneralEffectItem();
			timeLeft=10; // en secondes
			this.type=type;
		}
		else
		{
			System.out.println("Error");
		}
	}
	
	

	
}
