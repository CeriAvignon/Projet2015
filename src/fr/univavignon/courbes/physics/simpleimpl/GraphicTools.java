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
public class GraphicTools
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
	 * Calcule les deux points d'intersection entre un cercle et une droite passant par son
	 * centre. Cela revient à résoudre une équation du second degré, cf. les commentaires dans
	 * la méthode.
	 * </br>
	 * <b>Note :</b> méthode finalement pas utilisée.
	 * 
	 * @param xc
	 * 		Abscisse du centre du cercle.
	 * @param yc
	 * 		Ordonnée du centre du cercle.
	 * @param r
	 * 		Rayon du cercle.
	 * @param m
	 * 		Coefficient directeur de la droite.
	 * @param d
	 * 		Ordonnée à l'origine de la droite.
	 * @return
	 * 		Tableau de deux positions, chacune représentant un point d'intersection.
	 */
	public static Position[] processCircleLineIntersection(int xc, int yc, int r, float m, float d)
	{	// les deux équations sont :
		// 	- droite : y = mx + d
		// 	- cercle : (x-xc)²+(y-yc)² = r²
		// on doit résoudre l'équation quadratique :
		// 	(m²+1)x² + 2(m*c−m*yc−xc)x + (yc²−r²+xc²−2c*yc+c²) = 0
		
		float a = m*m + 1;
		float b = 2*(m*d - m*yc - xc);
		float c = yc*yc - r*r + xc*xc - 2*d*yc + d*d;
		float delta = b*b - 4*a*c;
		
		double x1 = (-b - Math.sqrt(delta)) / (2*a);
		double y1 = m * x1 + d;
		double x2 = (-b + Math.sqrt(delta)) / (2*a);
		double y2 = m * x2 + d;
		
		Position pos1 = new Position((int)Math.round(x1),(int)Math.round(y1));
		Position pos2 = new Position((int)Math.round(x2),(int)Math.round(y2));
		Position result[] = {pos1,pos2};
		
		return result;
	}
	
	/**
	 * Reçoit en paramètre un polygone dont seule la bordure est tracée,
	 * et un point situé à l'intérieur du polygone, puis remplit récursivement
	 * l'intérieur du polygone. 
	 * <br/>
	 * On appelle cet algorithme le <i>remplissage par frontière</i> ou 
	 * <a href="https://fr.wikipedia.org/wiki/Algorithme_de_remplissage_par_diffusion">
	 * par diffusion</a>. À noter que des algorithmes computationnellement plus efficaces 
	 * (mais plus complexes à implémenter) existent.
	 * </br>
	 * <b>Note :</b> méthode finalement pas utilisée.
	 * 
	 * @param result
	 * 		Ensemble de points représentant le polygone à compléter.
	 * @param seed
	 * 		Point situé à l'intérieur du polygone.
	 */
	public static void fillPolygon(Set<Position> result, Position seed)
	{	Position pos = seed;
		if(!result.contains(pos))
		{	result.add(pos);
			fillPolygon(result,new Position(pos.x-1,pos.y));
			fillPolygon(result,new Position(pos.x+1,pos.y));
			fillPolygon(result,new Position(pos.x,pos.y-1));
			fillPolygon(result,new Position(pos.x,pos.y+1));
		}
	}

	/**
	 * Calcule les points constituant un rectangle. Les points {@code pos1} et {@code pos2} correspondent
	 * aux centres de deux côtés opposés de ce rectangle, tandis que {@code side} est la longueur
	 * de ces mêmes côtés.
	 * </br>
	 * <b>Note :</b> méthode finalement pas utilisée.
	 * 
	 * @param pos1
	 * 		Centre du premier côté.
	 * @param pos2
	 * 		Centre du côté qui lui est opposé.
	 * @param side
	 * 		Longueur de ces deux côtés.
	 * @return
	 * 		Ensemble de points constituant le rectangle (y compris l'intérieur).
	 */
	public static Set<Position> processRectangle(Position pos1, Position pos2, int side)
	{	Set<Position> result = new TreeSet<Position>();
		List<Position> segment1,segment2,segment3,segment4;
		Position[] temp1 = new Position[2];
		Position[] temp2 = new Position[2];
		
		// cas particulier : perpendiculaires verticales
		if(pos1.y==pos2.y)
		{	// premier segment
			temp1[0] = new Position(pos1.x,pos1.y-side/2);
			temp1[1] = new Position(pos1.x,pos1.y+side/2);
			segment1 = processSegment(temp1[0], temp1[1]);
			// second segment
			temp2[0] = new Position(pos2.x,pos2.y-side/2);
			temp2[1] = new Position(pos2.x,pos2.y+side/2);
			segment2 = processSegment(temp2[0], temp2[1]);
		}
		// cas particulier : perpendiculaires horizontales
		else if(pos1.x==pos2.x)
		{	// premier segment
			temp1[0] = new Position(pos1.x-side/2,pos1.y);
			temp1[1] = new Position(pos1.x+side/2,pos1.y);
			segment1 = processSegment(temp1[0], temp1[1]);
			// second segment
			temp2[0] = new Position(pos2.x-side/2,pos2.y);
			temp2[1] = new Position(pos2.x+side/2,pos2.y);
			segment2 = processSegment(temp2[0], temp2[1]);
		}
		// cas général
		else
		{	// équation de la droite reliant l'ancienne et la nouvelle positions
			float a = (pos1.y-pos2.y)/(float)(pos1.x-pos2.x);
			//float b = pos2.y - a*pos2.x;	// pas utilisé
			
			// équation des droites perpendiculaires à la première droite
			// et passant chacune par l'une des deux positions
			float c = -1/a;
			float d1 = pos1.y - c*pos1.x;
			float d2 = pos2.y - c*pos2.x;
		
			// coordonnées des points à l'intersection entre la première perpendiculaire et le premier cercle
			temp1 = processCircleLineIntersection(pos1.x, pos1.y, side/2, c, d1);
			// segment allant d'un point à l'autre
			segment1 = processSegment(temp1[0], temp1[1]);
			// coordonnées des points à l'intersection entre la seconde perpendiculaire et le second cercle
			temp2 = processCircleLineIntersection(pos2.x, pos2.y, side/2, c, d2);
			// segment allant d'un point à l'autre
			segment2 = processSegment(temp2[0], temp2[1]);
		}
		
		// on déduit les 2 autres segments 
		segment3 = processSegment(temp1[0],temp2[0]);
		segment4 = processSegment(temp1[1],temp2[1]);
		
		// on initialise l'ensemble de points avec les 4 côtés du rectangle
		result.addAll(segment1);
		result.addAll(segment2);
		result.addAll(segment3);
		result.addAll(segment4);
		
		// on remplit le polygone obtenu
		Position pos = new Position((pos1.x + pos2.x)/2, (pos1.y + pos2.y)/2);
		fillPolygon(result, pos);
		
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
	
	/**
	 * Trace et remplit un triangle de base horizontale et orienté pointe vers le haut.
	 * Algorithme pris de <a href="// http://www.sunshine2k.de/coding/java/TriangleRasterization/TriangleRasterization.html">
	 * cette page</a>.
	 * </br>
	 * <b>Note :</b> méthode finalement pas utilisée.
	 * 
	 * @param pos1
	 * 		Premier point du triangle.
	 * @param pos2
	 * 		Deuxième point du triangle.
	 * @param pos3
	 * 		Troisième point du triangle.
	 * @return
	 * 		l'ensemble des positions constituant le triangle.
	 */
	public static Set<Position> processBottomFlatTriangle(Position pos1, Position pos2, Position pos3)
	{	Set<Position> result = new TreeSet<Position>();
		
		float invSlope1 = (pos2.x - pos1.x) / (float)(pos2.y - pos1.y);
		float invSlope2 = (pos3.x - pos1.x) / (float)(pos3.y - pos1.y);
		float curX1 = pos1.x;
		float curX2 = pos1.x;
		
		for(int scanlineY=pos1.y; scanlineY<=pos2.y; scanlineY++)
		{	Position p1 = new Position((int)curX1, scanlineY);
			Position p2 = new Position((int)curX2, scanlineY);
			List<Position> segment = processSegment(p1, p2);
			result.addAll(segment);
			curX1 = curX1 + invSlope1;
			curX2 = curX2 + invSlope2;
		}
		
		return result;
	}
	
	/**
	 * Trace et remplit un triangle de base horizontale et orienté pointe vers le bas.
	 * Algorithme pris de <a href="// http://www.sunshine2k.de/coding/java/TriangleRasterization/TriangleRasterization.html">
	 * cette page</a>.
	 * </br>
	 * <b>Note :</b> méthode finalement pas utilisée.
	 * 
	 * @param pos1
	 * 		Premier point du triangle.
	 * @param pos2
	 * 		Deuxième point du triangle.
	 * @param pos3
	 * 		Troisième point du triangle.
	 * @return
	 * 		l'ensemble des positions constituant le triangle.
	 */
	public static Set<Position> processTopFlatTriangle(Position pos1, Position pos2, Position pos3)
	{	Set<Position> result = new TreeSet<Position>();
		
		float invSlope1 = (pos3.x - pos1.x) / (float)(pos3.y - pos1.y);
		float invSlope2 = (pos3.x - pos2.x) / (float)(pos3.y - pos2.y);
		float curX1 = pos3.x;
		float curX2 = pos3.x;

		for(int scanlineY=pos3.y; scanlineY>pos1.y; scanlineY--)
		{	curX1 = curX1 - invSlope1;
			curX2 = curX2 - invSlope2;
			Position p1 = new Position((int)curX1, scanlineY);
			Position p2 = new Position((int)curX2, scanlineY);
			List<Position> segment = processSegment(p1, p2);
			result.addAll(segment);
		}
		
		return result;
	}
	
	/**
	 * Trace et remplit un triangle quelconque. Algorithme pris de 
	 * <a href="// http://www.sunshine2k.de/coding/java/TriangleRasterization/TriangleRasterization.html">
	 * cette page</a>.
	 * </br>
	 * <b>Note :</b> méthode finalement pas utilisée.
	 * 
	 * @param pos1
	 * 		Premier point du triangle.
	 * @param pos2
	 * 		Deuxième point du triangle.
	 * @param pos3
	 * 		Troisième point du triangle.
	 * @return
	 * 		l'ensemble des positions constituant le triangle.
	 */
	public static Set<Position> processTriangle(Position pos1, Position pos2, Position pos3)
	{	// on classe les points par y croissant
		Position temp;
		if(pos1.y > pos2.y)
		{	temp = pos1;
			pos1 = pos2;
			pos2 = temp;
		}
		if(pos2.y > pos3.y)
		{	temp = pos2;
			pos2 = pos3;
			pos3 = temp;
			if(pos1.y > pos2.y)
			{	temp = pos1;
				pos1 = pos2;
				pos2 = temp;
			}
		}
		
		// on calcule les points composant le triangle
		Set<Position> result;
		// cas particulier : base horizontale inférieure
		if (pos2.y == pos3.y)
			result = processBottomFlatTriangle(pos1, pos2, pos3);
		// cas particulier : base horizontale supérieure
		else if(pos1.y == pos2.y)
			result = processTopFlatTriangle(pos1, pos2, pos3);
		// cas général : on décompose en deux triangles à base horizontale
		else
		{	Position v4 = new Position((int)(pos1.x + ((float)(pos2.y - pos1.y) / (float)(pos3.y - pos1.y)) * (pos3.x - pos1.x)), pos2.y);
		    result = processBottomFlatTriangle(pos1, pos2, v4);
		    Set<Position> half = processTopFlatTriangle(pos2, v4, pos3);
		    result.addAll(half);
		}
		
		return result;
	}
}
