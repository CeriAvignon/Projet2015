
Cette interface utilisateur ne prend pas totalement en charge la partie réseau.
La fin de partie n'est pas implémenter. Pour afficher les score il faudra donc clicker sur "Entrée" puis vous pouvez reclicker sur "Entrée" pour relancer une partie	

Instruction AVANT D'UTILISER LE CODE :

Dans les modifications suivantes "XX" correspond au numéro de groupe de l'interface utilisé.

	* ClientProfile.java
	
		- Ajouter l'import : "import fr.univavignon.courbes.network.groupeXX.*;"
		- Instancier le moteur reseau partie reseau ligne 20.
		
	* ServerProfile.java
	
		- Ajouter l'import : "import fr.univavignon.courbes.network.groupeXX.*;"
		- Instancier le moteur reseau partie client ligne 17.
		
	* Network.java
	
		- Ajouter l'import : "import fr.univavignon.courbes.network.groupeXX.*;"
		- Instancier le moteur reseau partie client ligne 66.
		- Instancier le moteur reseau partie reseau ligne 121.
		
	* Game.java

		- Ajouter l'import : "import fr.univavignon.courbes.physics.groupeXX.*;".
		- Ajouter l'import : "import fr.univavignon.courbes.graphics.groupeXX.*;".
		- Instancier le moteur graphique ligne 123.
		- Instancier le moteur physique ligne 124.
		
Instruction AVANT D'UTILISER L'APPLICATION :
	* Pour lancer l'application à partit d'Eclipse il suffit de run mainMenu.java
	* Penser à vous inscrire avec un pseudo et un mot de passe que vous retiendrez (données sauvegarder dans res/profiles/profiles.txt).
	* Pour vous connecter, écriver votre pseudo et votre mot de passe plus clicker sur le bouton .
	* "Bouton : direction gauche" puis sur une touche du clavier pour assigner la touche au déplacement vers la gauche. Même principe pour la droite.
	* La fin de partie n'est pas implémenter. Pour afficher les score il faudra donc clicker sur "Entrée" puis vous pouvez reclicker sur "Entrée" pour relancer une partie.
		