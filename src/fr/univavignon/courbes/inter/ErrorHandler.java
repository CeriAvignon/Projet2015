package fr.univavignon.courbes.inter;

/**
<<<<<<< HEAD
 * Interface implémentant les méthodes permettant au MR de demander à l'IU d'afficher 
 * des messages d'erreur. Par exemple, cela peut être fait dans la console texte, ou 
 * bien sous forme de fenêtre de type pop-up.
 * <br/>
 * Chaque binôme de la composante Interface Utilisateur doit définir une classe implémentant 
 * cette interface, puis l'instancier.
=======
 * Classe permettant d'afficher des messages d'erreur. Par
 * exemple, cela peut être fait dans la console texte, ou 
 * bien sous forme de fenêtre de type pop-up.
 * <br/>
 * Chaque binôme de la composante Interface Utilisateur doit 
 * définir une classe implémentant cette interface, puis
 * l'instancier.
>>>>>>> 005e8598e797176e5596a6d6e9b00f6a7466cf81
 */
public interface ErrorHandler 
{	/**
	 * Demande à l'interface utilisateur d'afficher le message indiqué
	 * en paramètre. La méthode est essentiellement destinée au Moteur
	 * Réseau, afin de faire remonter des problèmes du client vers le
	 * serveur, et inversement.
	 * 
	 * @param errorMessage
	 * 		Le message d'erreur à afficher.
	 */
	public void displayError(String errorMessage);
}
