package fr.univavignon.courbes.inter;

/**
 * Classe permettant d'afficher des messages d'erreur. Par
 * exemple, cela peut être fait dans la console texte, ou 
 * bien sous forme de fenêtre de type pop-up.
 * <br/>
 * Chaque binôme de la composante Interface Utilisateur doit 
 * définir une classe implémentant cette interface, puis
 * l'instancier.
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
