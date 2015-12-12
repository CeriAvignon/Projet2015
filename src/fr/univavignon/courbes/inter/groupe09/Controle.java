package fr.univavignon.courbes.inter.groupe09;

import java.io.*;

public class Controle {
	
	public static int controlePseudo(String ps)
	{	
		boolean existe = false;
		String laChainne;
	    InputStream fis;
	    InputStreamReader isr;
	    BufferedReader bis;        
	    try {
	      fis = new FileInputStream(new File("src/text/user.txt"));
	      isr = new InputStreamReader(fis);
	      bis = new BufferedReader(isr);
	      
	      try{		
	    	  while (!existe && (laChainne = bis.readLine()) != null) { 
	    		  String elem[] = laChainne.split(";");
	    		  if(elem[0].equals(ps)){
	    			  existe = true;
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
	    if(existe){
	    	return 1; // si le pseudo existe deja 
	    }
	    return 2; // tout est ok, le pseudo n'existe pas  
	}
	
	
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
	      fis = new FileInputStream(new File("src/text/user.txt"));
	      isr = new InputStreamReader(fis);
	      bis = new BufferedReader(isr);
	      
	      try{		
	    	  while (!existe && (laChainne = bis.readLine()) != null) { 
	    		  String elem[] = laChainne.split(";");
	    		  if(elem[0].equals(ps)){
	    			  entree ++;
	    			  if(elem[1].equals(mdp)){
	    				  existe = true;
	    			  }
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
	    if(existe){ // si le mot de passe et le pseudo sont juste
	    	return 1;
	    }
	    if(entree == 1){ // si le pseudo est juste mais pas le mot de passe
	    	return 4;
	    }
	    return 2; // pseudo faux mais mdp juste 
	}
	

public void inscriptionDe(String email, String ps, String mdp,String pays, String temps ){
		  try {
			 // int i = 0; 
				 BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("src/text/user.txt")));
			     StringBuffer sb = new StringBuffer(); 
		         String line;      
		         while((line = reader.readLine()) != null) {
		        	 sb.append(line);
		        	 sb.append(System.getProperty("line.separator"));
		         }
		         reader.close();
		         
		         BufferedWriter out = new BufferedWriter(new FileWriter("src/text/user.txt"));
		         out.write(ps+";"+mdp+";"+email+";"+pays+";"+temps);
		         out.newLine();
		         out.write(sb.toString());
		         out.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		      
	}

	
}
	
	