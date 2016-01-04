package fr.univavignon.courbes.userInterface;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SauvegardePartie {
	
	static void sauvegarderPartie()
	{
		try {
			File save = null;
			save = new File("./SauvegardePartie/".concat(Menu.getNomPartie()).concat(".txt"));
			if (!save.exists()) {
				save.delete();
				save.createNewFile();
				System.out.println("Partie créée !");
			}
			else
			{
				save.createNewFile();
			}
			
			try{
				BufferedWriter bw = new BufferedWriter(new FileWriter(save.getAbsoluteFile()));
				bw.write(Menu.getNomPartie());
				bw.flush();
				bw.newLine();
				bw.write(Menu.getNbJoueursPartie());
				bw.flush();
				bw.newLine();
				bw.write(Menu.getTaillePlateau());
				bw.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}catch (IOException e) {
				e.printStackTrace();
			}

			
	}

}

