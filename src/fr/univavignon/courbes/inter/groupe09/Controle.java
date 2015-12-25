package fr.univavignon.courbes.inter.groupe09;
import java.io.*;
/**
 * @author groupe09
 * classe contenant un ensemble de méthodes de controle sur les formulaire d'inscription, d'authentification
 *
 */
public class Controle {
	
	/**
	 * méthode permetant de vérifier si le pseudonyme saisie lors de l'inscription existe déjà ou non 
	 * @param ps
	 * pseudonyme de l'utilisateur, correspendant au userName de la classe profil 
	 * @return 
	 * return 1 si le pseudo existe déjà, 2 si il n'existe pas 
	 */
	public static int controlePseudo(String ps) {	
		boolean existe = false;
		String laChainne;
	    InputStream fis;
	    InputStreamReader isr;
	    BufferedReader bis;        
	    try {
	      fis = new FileInputStream(new File("src/user.txt"));
	      isr = new InputStreamReader(fis);
	      bis = new BufferedReader(isr);
	      
	      try {		
	    	  while (!existe && (laChainne = bis.readLine()) != null) { 
	    		  String elem[] = laChainne.split(";");
	    		  if(elem[0].equals(ps)) {
	    			  existe = true;
	    		  }
	    	  }
	      }
	      catch(NumberFormatException e) {
	    	  e.printStackTrace();
	      }				
	      bis.close();
	                
	    } 
	    catch (FileNotFoundException e) {
	      e.printStackTrace();
	    } 
	    catch (IOException e) {
	      e.printStackTrace();
	    }
	    if(existe) {
	    	/**
	    	*@return 1 si le pseudo existe deja 
	    	*/
	    	return 1;
	    }
	    /**
    	*@return 2 tout est ok, le pseudo n'existe pas
    	*/
	    return 2;   
	}
	
		/**
	 * Contrôle l'authentification d'un utilisateur, en vérifiant si il existe et si les donnée saisie sont juste
	 * @param ps
	 * pseudo de l'utilisateur, correspand à userName de la classe Profil
	 * @param mdp
	 * mot de passe de l'utilisateur 
	 * @return
	 * 0 si le pseudo donner est vide
	 * 1 si tout est ok, le pseudo est le mot de passe sont correcte, et l'utilisateur existe
	 * 2 si le pseudo donner est inccorecte
	 * 3 si le mot de passe donner est vide
	 * 4 si le pseudo est juste, donc l'utilisateur existe mais le mot de passe est incorrecte
	 */
	public int controleConnexion(String ps,String mdp){
		 
		if(ps.equals("")){
			return 0; // si le champs  pseudo est vide 
		}
		
		if(mdp.equals("")){
			return 3; // si le champs  mdp est vide 
		}
		
		boolean existe = false;
		int entree = 0;
		String laChainne;
	    InputStream fis;
	    InputStreamReader isr;
	    BufferedReader bis;        
	    try {
	      fis = new FileInputStream(
	    		  new File("src/user.txt"));
	      isr = new InputStreamReader(fis);
	      bis = new BufferedReader(isr);
	      
	      try {		
	    	  while (!existe && (laChainne = bis.readLine()) != null) { 
	    		  String elem[] = laChainne.split(";");
	    		  if(elem[0].equals(ps)) {
	    			  entree ++;
	    			  if(elem[1].equals(mdp)) {
	    				  existe = true;
	    			  }
	    		  }
	    	  }
	      }
	      catch(NumberFormatException e) {
	    	  e.printStackTrace();
	      }
	      bis.close();
	                
	    } 
	    catch (FileNotFoundException e) {
	      e.printStackTrace();
	    } 
	    catch (IOException e) {
	      e.printStackTrace();
	    }
	    if(existe) {  
	    	/**
	    	*@return 1  si le mot de passe et le pseudo sont juste
	    	*/
	    	return 1;
	    }
	    if(entree == 1) { 
	    	/**
	    	*@return 4  si le pseudo est juste mais pas le mot de passe
	    	*/
	    	return 4;
	    }
	    /**
    	*@return 2  pseudo faux mais mdp juste
    	*/
	    return 2;   
	}
	
	/**
	 * @return 
	 */
	public int idPlayer() {
		
		String str;
		int count = 0;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("src/user.txt");
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LineNumberReader l = new LineNumberReader(new 
												BufferedReader(new 
												InputStreamReader(fis)
																	)
																	);
		try {
			if((str = l.readLine()) == null)
         		  count = 1;
         	else {
         		while ((str=l.readLine())!=null)
         			count = l.getLineNumber();
         	}		           
					
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return count;
	}
 /**
 * Méthode qui permet d'inscrire l'utilisateur, si tout les donner saisie sont correcte dans la base de donner du jeu  
 * @param email
 * Eamil saisie par l'utilisateur
 * @param ps
 * Pseudonyme saisie par l'utilisateur, correspend à userName de la classe Profil
 * @param mdp
 * Mot de passe saisie par l'utilisateur 
 * @param pays
 * Pays de l'utilisateur 
 * @param temps
 * Fuseau horraire de l'utilisateur, correspend à timeZone de la classe profil  
 */
	public void inscriptionDe(String email, String ps, String mdp,String pays, String temps ,int score ) {
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("src/user.txt")));
			StringBuffer sb = new StringBuffer(); 
		    String line;      
		    while((line = reader.readLine()) != null) {
		    	sb.append(line);
		        sb.append(System.getProperty("line.separator"));
		    }
		    reader.close();
		    int id = this.idPlayer();
		    BufferedWriter out = new BufferedWriter( new FileWriter("src/user.txt"));
		    out.write(ps+";"+mdp+";"+id+";"+email+";"+pays+";"+temps+";"+score);
		    out.newLine();
		    out.write(sb.toString());
		    out.close();
			} 
		catch (Exception e) {
			e.printStackTrace();
		}
		      
	}

}
	
	