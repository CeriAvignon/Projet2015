package fr.univavignon.courbes.userInterface;

import java.io.*;
import java.util.Date;


/**
 * Classe permettant de controler l'authentification ainsi que d'écrire dans un fichier les informations lors de l'inscription
 * @author Florian DEMOL - Alexis MASSIA
 */

public class Sauvegarde {
	
	Profile p = new Profile();
	static boolean namePass = false;
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
	String texte_date = sdf.format(new Date());
	
	/**
	 * Méthode permettant de controler si l'utilisateur est bien inscrit dans la base ainsi que si le pseudo correspond bien au mot de passe 
	 * @param ps = pseudo de l'utilisateur
	 * @param mdp = mot de passe de l'utilisateur
	 */
	
	public void authentificateur(String ps,String mdp){
		 
		if(ps.equals("") || mdp.equals("")){
			namePass = true; // si le champs  pseudo est vide 
		}
		
		boolean good = false;
		String ligne;
	    InputStream fis;
	    InputStreamReader isr;
	    BufferedReader bis;     
	    
	    try {
	      fis = new FileInputStream(new File("user.txt"));
	      isr = new InputStreamReader(fis);
	      bis = new BufferedReader(isr);
	      
	      try{		
	    	  while (!good && (ligne = bis.readLine()) != null) { 
	    		  String champ[] = ligne.split(";");
	    		  if(champ[0].equals(ps) && champ[1].equals(mdp)){
	    			  good = true;
	    			  p.Player(champ[3], champ[4], champ[0], champ[1], texte_date, Integer.parseInt(champ[2]));
	    		  }
	    	  }
	      }catch(NumberFormatException e){
	    	  e.printStackTrace();
	      }

	      bis.close();
	                
	    } catch (FileNotFoundException e) {
	      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    if(good == true){ 
	    	namePass = false;
	    }
	    else{ 
	    	namePass = true;
	    }
	}
	
	
	/**
	 * Méthode permettant d'inscrire les informations d'un utilisateur dans un fichier textes
	 * @param ps = pseudo de l'utilisateur
	 * @param mdp = mot de passe de l'utilisateur
	 * @param email = email de l'utilisateur
	 * @param pays = pays de l'utilisateur
	 * @param temps = heure de l'inscription
	 */
	
	public void inscriptionUser(String ps, String mdp, String email, String pays, String temps){
		
		  try {
			  	 int id = this.idJoueur();
				 BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("user.txt")));
			     StringBuffer sb = new StringBuffer(); 
		         String ligne;      
		         while((ligne = reader.readLine()) != null) {
		        	 sb.append(ligne);
		        	 sb.append(System.getProperty("line.separator"));
		         }
		         reader.close();
		                 
		         BufferedWriter out = new BufferedWriter(new FileWriter("user.txt"));
		         out.write(ps+";"+mdp+";"+id+";"+email+";"+pays+";"+temps);
		         out.newLine();
		         out.write(sb.toString());
		         out.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		      
	}
	
	/**
	 * Méthode permettant de créer les id des utilisateurs
	 */
	
	public int idJoueur(){
		String str;
		int initial = 0;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("user.txt");
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
	
	
