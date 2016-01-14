package fr.univavignon.courbes.userInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Classe permettant d'afficher les parties en ligne créées.
 * @author Florian DEMOL - Alexis MASSIA
 */


public class AfficherTableauGame {
	
	
		String chaine = null; 
		String newLigne=System.getProperty("line.separator");
		
		public AfficherTableauGame(){
		
		 try{
			 InputStream ips = new FileInputStream("game.txt");
			 InputStreamReader ipsr = new InputStreamReader(ips);
			 BufferedReader br = new BufferedReader(ipsr);
			 String ligne;
			 while((ligne = br.readLine()) != null){
				 ligne.split(";");
				 chaine += ligne;
			 }
			 br.close();
		 } catch (Exception e) {
			 System.out.println(e.toString());
		 }

		 
		}
}


