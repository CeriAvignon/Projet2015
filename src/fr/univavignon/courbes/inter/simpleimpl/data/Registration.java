package fr.univavignon.courbes.inter.simpleimpl.data;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.regex.*;

import javax.swing.*;

import fr.univavignon.courbes.inter.simpleimpl.Error;
import fr.univavignon.courbes.inter.simpleimpl.Window;
import fr.univavignon.courbes.inter.simpleimpl.menus.Menu;

public class Registration extends Window {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int tempWidth, tempHeight;
	private JPasswordField tPassword ;
	private JTextField tUserName, tEmail, tCountry, tTimeZone;


	/*
	 * Créer une fenêtre contenant le formulaire d'inscription.
	 * @param width  Largeur de la fenêtre.
	 * @param height Hauteur de la fenêtre.
	 */
	public Registration(int width, int height) 
	{
		super( width, height);	 
		tempWidth = width;
		tempHeight = height;
		
	    JPanel registrationForm = new JPanel();

	    registrationForm.setLayout(new GridLayout(6, 2, 10, 10));
	    JLabel userName = new JLabel("userName");
		registrationForm.add(userName);
		tUserName = new JTextField();
		registrationForm.add(tUserName);
		JLabel password = new JLabel("password");
		registrationForm.add(password);
		tPassword = new JPasswordField();
		registrationForm.add(tPassword);
		JLabel email = new JLabel("email");
		registrationForm.add(email);
		tEmail = new JTextField();
		registrationForm.add(tEmail);
		JLabel country = new JLabel("country");
		registrationForm.add(country);
		tCountry = new JTextField();
		registrationForm.add(tCountry);
		JLabel timeZone = new JLabel("timeZone");
		registrationForm.add(timeZone);
		tTimeZone = new JTextField();
		registrationForm.add(tTimeZone);
	    JButton bRegistration = new JButton("Valider l'inscription");
		registrationForm.add(bRegistration);
		bRegistration.addActionListener(new bRegistrationListener());
		JButton bBack = new JButton("Retour");
		registrationForm.add(bBack);
		bBack.addActionListener(new bBackListener());
		container.setLayout(new FlowLayout(FlowLayout.CENTER));
		container.add(registrationForm);
		setVisible(true);
	}
	
	/*
	 * Vérifie si le formulaire peut donner lieu à une inscription.
	 */
	public void Verification()
	{
		boolean bUserName = Pattern.matches("^[a-zA-Z]*$",tUserName.getText());
		String stringPassword = String.valueOf(tPassword.getPassword());
		boolean bPassword = Pattern.matches("^.*[,].*$",stringPassword);
		boolean bCountry = Pattern.matches("^[a-zA-Z0-9]*$",tCountry.getText());
		boolean bTimeZone = Pattern.matches("^[a-zA-Z0-9]*$",tTimeZone.getText());
		boolean bEmail = Pattern.matches("^[_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)+$", tEmail.getText()) ;
	    boolean bUserNameFound = searchUserName(tUserName.getText());
		if(tUserName.getText().equals("") || stringPassword.equals("") || tEmail.getText().equals("")	|| tCountry.getText().equals("")	|| tTimeZone.getText().equals(""))
		{
			Error error = new Error();
			error.displayError("Un ou plusieur champ du formulaire ne sont pas complétés");
		}
		else if(!bEmail){
			Error error = new Error();
			error.displayError("Erreur dans le champ email");
		}
		else if(!bUserName)
		{
			Error error = new Error();
			error.displayError("Username incorrect");
		} 
		else if(bPassword)
		{
			Error error = new Error();
			error.displayError("Mot de passe incorrect");
		}
		else if(!bCountry)
		{
			Error error = new Error();
			error.displayError("Pays incorrect");
		}
		else if(!bTimeZone)
		{
			Error error = new Error();
			error.displayError("Timezone incorrect");
		}
		else if (bUserNameFound)
		{
			Error error = new Error();
			error.displayError("Cet UserName est déjé utilisé");
		}
		else {
				writeRegistration(tUserName.getText(), stringPassword, tEmail.getText(), tCountry.getText(), tTimeZone.getText());
		}
	}
	
	/*
	 * Cherche le pseudo de l'utilisateur dans le fichier des profiles.
	 * @param userName  Pseudo saisie par l'utilisateur.
	 * @param password  Mot de passe saisie par l'utilisateur. 
	 * @param email     Email saisie par l'utilisateur.
	 * @param country   Pays saisie par l'utilisateur 
	 * @param timeZone  Fuseau horaire saisie par l'utilisateur.
	 * @return vrai(true) si le pseudo est déja utilisé (false) si le pseudo est libre
	 */
	public boolean searchUserName(String userName)
	{
		
	    try {
	      InputStream is = new FileInputStream(new File("res/profiles/profiles.txt"));
	      InputStreamReader isr = new InputStreamReader(is);
		  BufferedReader br = new BufferedReader(isr);
		  String line;
	      while ((line=br.readLine())!=null)
	      {
	    		  String elem[] = line.split(",");
	    		  if(elem[3].equals(userName)) 
	    		  {
	    			  br.close();
	    			  return true;
	    		  }
	      }		
	      br.close();  
	    }
		catch (IOException e) {
		System.out.println(e.getMessage());
		}
	    
	    return false;
	}
	
	
	public int lastId()
	{
		try {
		      InputStream is = new FileInputStream(new File("res/profiles/profiles.txt"));
		      InputStreamReader isr = new InputStreamReader(is);
			  BufferedReader br = new BufferedReader(isr);
			  String tempLine, line="0";
		      while ((tempLine=br.readLine())!=null)
		      {
		    	  line=tempLine;
		      }		
		      br.close();  
		      String elem[] = line.split(",");
		      return Integer.parseInt(elem[0]);
	    }
		catch (IOException e) {
		System.out.println(e.getMessage());
		}
		return 1;
		    
	}
	
	/*
	 * Ajoute l'utilisateur dans le fichier répertoriant les utilisateurs inscrit.
	 * @param userName  Pseudo saisie par l'utilisateur.
	 * @param password  Mot de passe saisie par l'utilisateur. 
	 * @param email     Email saisie par l'utilisateur.
	 * @param country   Pays saisie par l'utilisateur 
	 * @param timeZone  Fuseau horaire saisie par l'utilisateur.
	 */
	public void writeRegistration(String userName,String password, String email, String country, String  timeZone)
	{
		try {
			int id= lastId()+1;
			String content = id+","+"0"+","+email+","+userName+","+password+","+country+","+timeZone;
			
			File file = new File("res/profiles/profiles.txt");

			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter("res/profiles/profiles.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.newLine();
			bw.close();
			setVisible(false);
			new Menu();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Implemente l'action associé au bouton bRegistrationListener.
	 */
	class bRegistrationListener implements ActionListener
	{
		/*
		 * Permet de s'inscrire.
		 */
		public void actionPerformed(ActionEvent arg0) 
		{  
			Verification();
		}
	}
	
	/*
	 * Implemente l'action associé au bouton bBackListener.
	 */
	class bBackListener implements ActionListener
	{
		/*
		 * Permet de revenir au menu hors ligne.
		 */
		public void actionPerformed(ActionEvent arg0) 
		{  
			setVisible(false);
			new Menu();
		}
	}
}
