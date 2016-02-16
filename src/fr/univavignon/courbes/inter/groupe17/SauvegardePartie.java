package fr.univavignon.courbes.inter.groupe17;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * Classe permettant de sauvegarder dans un fichier les informations d'une partie créer
 * @author Florian DEMOL - Alexis MASSIA
 */

public class SauvegardePartie {
	
	
	/**
	 * Méthode permettant de sauvegarder les informations de la partie créer dans un fichier
	 * @param nameG = nom de la partie
	 * @param nbJoueurs = nombre de joueurs souhaité
	 * @param TaillePlateau = Taille du plateau (scalling ou full size)
	 */
	
	public void AddGame(String nameG, String nbJoueurs, String TaillePlateau){
		
		  try {
			  	 int id = this.idGame();
				 BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("game.txt")));
			     StringBuffer sb = new StringBuffer(); 
		         String ligne;      
		         while((ligne = reader.readLine()) != null) {
		        	 sb.append(ligne);
		        	 sb.append(System.getProperty("line.separator"));
		         }
		         reader.close();
		         
		         BufferedWriter out = new BufferedWriter(
		        		 new FileWriter("game.txt"));
		         out.write(nameG+";"+nbJoueurs+";"+TaillePlateau+";"+id+";");
		         out.newLine();
		         out.write(sb.toString());
		         out.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		      
	}
	
	public int idGame()
	{ String str;
      int initial = 0;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("game.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LineNumberReader l = new LineNumberReader(       
		       new BufferedReader(new InputStreamReader(fis)));
		              try {
		            	  if((str = l.readLine()) == null)
		            		  initial = 1;
		            	  else {
							  while ((str=l.readLine())!=null)
								  initial = l.getLineNumber();
		            	  	   }
					
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            return initial;
	}

}
