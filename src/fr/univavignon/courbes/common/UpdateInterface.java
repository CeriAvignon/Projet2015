package fr.univavignon.courbes.common;

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

import java.io.Serializable;

/**
 * Objet utilisé pour faire la mise à jour du jeu à travers
 * le réseau, pendant une manche. Peut être carrément toute l'aire
 * de jeu {@link Board} (grosse mise à jour) ou bien un objet plus petit de 
 * classe {@link SmallUpdate} (petite mise à jour).
 * 
 * @author	L3 Info UAPV 2015-16
 */
public interface UpdateInterface extends Serializable
{	
	// pas de méthode dans cette interface
}
