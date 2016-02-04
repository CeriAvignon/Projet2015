package fr.univavignon.courbes.inter.groupe13;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import fr.univavignon.courbes.common.Profile;

public class ProfileFileManager {
	

	private static String fileName = "res/profiles/profiles.txt"; 
	
	public static Vector<Profile> getProfiles() {
		
		Vector<Profile> result = new Vector<>();
		
		try {
	      InputStream is = new FileInputStream(new File(fileName));
	      InputStreamReader isr = new InputStreamReader(is);
		  BufferedReader br = new BufferedReader(isr);
		  String line;
	      while ((line=br.readLine())!=null)
	      {
	    		  String elem[] = line.split(",");
	    		  
	    		  if(elem.length >= 3){
	    		  
	    			  Profile p = new Profile();
	    			  p.userName = elem[0];
	    			  p.country = elem[1];
	    			  p.score = Integer.parseInt(elem[2].trim());
	    			  
	    			  result.add(p);
	    			  
	    		  }
	    		  else
	    			  System.err.println("Error, line only contain " + elem.length + " it should contain 3 elements (line: "+ elem + ")");
	    		  
	      }
	    	
	      br.close();  
	      
	      return result;
	      
  }
	catch (IOException e) {
	System.out.println(e.getMessage());
	}
		return null;
	}
	
	public static void addProfile(String pseudo, String country){
		
		 try{
		     FileWriter fw = new   FileWriter(fileName, true);
		     BufferedWriter output = new BufferedWriter(fw);

		     output.write("\n" + pseudo + ", " + country + ", 0");
		     output.flush();
		     output.close();
		 }
		 catch(IOException ioe){
		     System.out.print("Erreur : ");
		     ioe.printStackTrace();
		 }

		
	}
	
	public static boolean containPseudo(String pseudo){
		
		boolean result = false;
				
		try {
	      InputStream is = new FileInputStream(new File(fileName));
	      InputStreamReader isr = new InputStreamReader(is);
		  BufferedReader br = new BufferedReader(isr);
		  String line;
	      while (!result && (line=br.readLine())!=null)
	      {
	    		  String elem[] = line.split(",");
	    		  
	    		  if(elem.length >= 3){
	    		  
	    			  if(pseudo.equals(elem[0]))
	    				  result = true;
	    			  
	    		  }
	    		  else
	    			  System.err.println("Error, line only contain " + elem.length + " it should contain 3 elements (line: "+ elem + ")");
	    		  
	      }

	      br.close(); 
	      
		}
		 catch(IOException ioe){
		     System.out.print("Erreur : ");
		     ioe.printStackTrace();
		 }
	    	 
	      
	      return result;
	}
	
}
