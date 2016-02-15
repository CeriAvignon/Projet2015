package fr.univavignon.courbes.physics.simpleimpl;

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
import java.util.Set;
import java.util.TreeSet;

import fr.univavignon.courbes.common.Position;

/**
 * Ensemble de méthodes communes, permettant de faire différents traitements
 * graphiques susceptibles d'être utilisés un peu n'importe où...
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class GeometricTools
{	
	/**
	 * Renvoie la liste des pixels constituant le segment qui relie les points
	 * (x1,y1) et (x2,y2). On se base pour cela sur l'algorithme de Bresenham, décrit 
	 * <a href="https://fr.wikipedia.org/wiki/Algorithme_de_trac%C3%A9_de_segment_de_Bresenham">ici</a>.
	 * 
	 * @param pos1
	 * 		Point de départ.
	 * @param pos2
	 * 		Point d'arrivée.
	 * @return
	 * 		Ensemble de points constituant le segment.
	 */
	public static List<Position> processSegment(Position pos1, Position pos2)
	{	List<Position> result = new ArrayList<Position>();
		int x1 = pos1.x;
		int y1 = pos1.y;
		int x2 = pos2.x;
		int y2 = pos2.y;
		int x = x1;
		int y = y1;
		int xInc;	// increment de x
		int yInc;	// increment de y
		int dx = x2 - x1;
		int dy = y2 - y1;
		int d;
	
		// initialisation
		if (dx>=0)
			xInc = 1;
		else
			xInc = -1;
		if(dy>0)
			yInc = 1;
		else
			yInc = -1;
		dx = xInc*dx; // dx=abs(dx)
		dy = yInc*dy; // dy=abs(dy)
	
		// ajout du pixel au résultat
		Position pos = new Position(x,y);
		result.add(pos);
		// 0<=abs(a)<=1 : on suit les colonnes
		if(dy<=dx)
		{	d = 2*dy - dx;
			for(int i=0;i<dx;i++)
			{	if(d<=0)
					d = d + 2*dy;
				else
				{	d = d + 2*(dy-dx);
					y = y + yInc;
				}
				x = x + xInc;
				pos = new Position(x,y);
				result.add(pos);
			}
		} 
		// abs(a)>1 : on suit les lignes
		else
		{	d = 2*dx - dy;
			for(int i=0;i<dy;i++)
			{	if(d<=0)
					d = d + 2*dx;
				else
				{	d = d + 2*(dx-dy);
					x = x + xInc;
				}
				y = y + yInc;
				pos = new Position(x,y);
				result.add(pos);
			}
		}
		
		return result;
	}
	
	/**
	 * Calcule les pixels formant un disque de centre {@code center}
	 * et de rayon {@code radius}.
	 * 
	 * @param center
	 * 		Centre du disque.
	 * @param radius
	 * 		Rayon du disque.
	 * @return
	 * 		Ensemble de pixels formant le disque.
	 */
	public static Set<Position> processDisk(Position center, int radius)
	{	// on identifie les limites du carré contenant le disque
		int top = center.y - radius;
		int bottom = center.y + radius;
		int left = center.x - radius;
		int right = center.x + radius;
		
		// on passe en revue tous les pixels dans ce carré, en rejetant ceux qui sont hors du disque
		Set<Position> result = new TreeSet<Position>();
		for(int x=left;x<=right;x++)
		{	for(int y=top;y<=bottom;y++)
			{	double dist = Math.sqrt(Math.pow(x-center.x,2) + Math.pow(y-center.y,2));
				if(dist<=radius)
				{	Position pos = new Position(x,y);
					result.add(pos);
				}
			}
		}
		
		return result;
	}
}
