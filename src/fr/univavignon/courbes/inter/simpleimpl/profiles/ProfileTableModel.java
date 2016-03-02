package fr.univavignon.courbes.inter.simpleimpl.profiles;

/*
 * Courbes
 * Copyright 2015-16 L3 Info UAPV 2015-16
 * 
 * This file is part of Courbes.
 * 
 * Courbes is free software: you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation, 
 * either version 2 of the License, or (at your option) any later version.
 * 
 * Courbes is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Courbes. If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.swing.table.AbstractTableModel;

import fr.univavignon.courbes.common.Profile;

/**
 * Modèle associé à la table affichant la liste des profils
 * (i.e. objet chargé de gérer le contenu la table concernée).
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ProfileTableModel extends AbstractTableModel
{	/** Numéro de série de la classe */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Initialise le modèle de table.
	 */
	public ProfileTableModel()
	{	rowdata = new ArrayList<List<String>>();

		// on définit les titres des colonnes
		columnNames = new String[4];
		columnNames[0] = "Pseudo";
		columnNames[1] = "Pays";
		columnNames[2] = "Rang ELO";
		
		// on définit le contenu de la table
		TreeSet<Profile> profiles = ProfileManager.getProfiles();
		for (Profile profile : profiles)
		{	List<String> row = new ArrayList<String>();
			row.add(profile.userName);
			row.add(profile.country);
			row.add(Integer.toString(profile.eloRank));
			rowdata.add(row);
		}
	}
	
	/** Données */
	private List<List<String>> rowdata;
	/** En-têtes */
	private String[] columnNames;

	/**
	 * Rajoute un profil dans la table.
	 * 
	 * @param profile
	 * 		Le profil à rajouter.
	 */
	public void addProfile(Profile profile)
	{	ArrayList<String> newRow = new ArrayList<String>();
		newRow.add(profile.userName);
		newRow.add(profile.country);
		newRow.add(Integer.toString(profile.eloRank));
		
		rowdata.add(newRow);
		fireTableRowsInserted(rowdata.size()-1, rowdata.size()-1);
	}
	
	/**
	 * Supprime un profil de la table.
	 * 
	 * @param rowNbr
	 * 		Le numéro de ligne du profil à supprimer.
	 */
	public void removeProfile(int rowNbr)
	{	rowdata.remove(rowNbr);
		fireTableRowsDeleted(rowNbr,rowNbr);
	}
	
	@Override
	public int getRowCount()
	{	return rowdata.size();
	}

	@Override
	public int getColumnCount()
	{	if (rowdata != null && rowdata.size() > 0)
			return rowdata.get(0).size();
		else
			return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{	return rowdata.get(rowIndex).get(columnIndex);
	}

	@Override
	public String getColumnName(int c)
	{	return columnNames[c];
	}
}
