package fr.univavignon.courbes;

abstract class Item 
{
	protected Position apparition;
}

abstract class IndividualEffectItem extends Item
{
	// ????
}

public class Bonus extends IndividualEffectItem
{
	private boolean earth=false;
	private boolean sonicSlow=false;
	private boolean sonicFast=false;
	
	
	
}

public class Malus extends IndividualEffectItem
{
	private boolean feed=false;
	private boolean invert=false;
	private boolean hole=false;
	private boolean passkey=false;
	private boolean sonicSlow=false;
	private boolean sonicFast=false;
}




public class GeneralEffectItem extends Item
{
	private boolean clean=false;
	private boolean earth=false;
	private boolean moreloops=false;
}




