package fr.univavignon.courbes.src.mp;

public abstract class Item 
{
	protected Position spawn;
}

class ItemEffect extends Item
{
	enum TypeEffect
	{ 
		Feed,         // Malus grossissant les autres joueurs, et uniquement les autres.
		SonicPlayer,  // Augmente la vitesse du snake du joueur
		SonicOthers,  // Augmente la vitesse des snakes des autres joueurs
		SlowPlayer,   // Réduit la vitesse du snake du joueur
		SlowOthers,   // Réduit la vitesse des snakes des autres joueurs.
		PassKey,      // Bonus anti-collision.
		Invert,       // Malus inversant le sens des touches. Gauche donne droite et vice-versa.
		Hole          // Malus augmentant le taux de trous dans le tracé du joueur.
	}
	private TypeEffect type;
}


class ItemEvent extends Item
{
	enum TypeEvent
	{ 
		Clean,       // Effaçant la totalité des tracés.
		Earth,       // "Mode Avion", passer sur un bord nous envoie de l’autre côté du terrain.
		MoreItems    // Le taux de spawn des objets est augmenté.
		
	}
	private TypeEvent type;
}
