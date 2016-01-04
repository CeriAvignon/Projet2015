package fr.univavignon.courbes.userInterface;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class Sauvegarde {
	
	Player p = new Player();
	
	public int controlePseudo(String ps)
	{	
		boolean existe = false;
		String laChainne;
	    InputStream fis;
	    InputStreamReader isr;
	    BufferedReader bis;        
	    try {
	      fis = new FileInputStream(new File("user.txt"));
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
	    	System.out.println("Le pseudo existe déjà !");
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
	      fis = new FileInputStream(
	    		  new File("user.txt"));
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
	    	String laChaine = null;
	    	BufferedReader biso = null;
			try {
				laChaine = biso.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	String elem[] = laChaine.split(";");
	    	p.Player(elem[4], elem[5], elem[0], elem[1], elem[6]);
	    }
	    if(entree == 1){ // si le pseudo est juste mais pas le mot de passe
	    	//return 4;
	    	System.out.println("mdp faux");
	    }
	    return 2; // pseudo faux mais mdp juste 
	}
	
	public int idPlayer()
	{ String str;
      int count = 0;
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
		            		  count = 1;
		            	  else {
							  while ((str=l.readLine())!=null)
								  count = l.getLineNumber();
		            	  	   }
					
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            return count;
	}
	public void inscriptionDe(String ps, String mdp, String email, String pays/*, String temps ,int score*/  ){
		
		  try {
			 // int i = 0; 
				 BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("user.txt")));
			     StringBuffer sb = new StringBuffer(); 
		         String line;      
		         while((line = reader.readLine()) != null) {
		        	 sb.append(line);
		        	 sb.append(System.getProperty("line.separator"));
		         }
		         reader.close();
		         
		         int id = this.idPlayer();
		         BufferedWriter out = new BufferedWriter(
		        		 new FileWriter("user.txt"));
		         out.write(ps+";"+mdp+";"+id+";"+email+";"+pays+";"/*+temps+";"+score*/);
		         out.newLine();
		         out.write(sb.toString());
		         out.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		      
	}
	

	
}
	
	
