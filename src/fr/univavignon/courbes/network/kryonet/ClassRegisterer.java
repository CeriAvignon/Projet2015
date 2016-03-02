package fr.univavignon.courbes.network.kryonet;

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
import java.util.LinkedList;
import java.util.TreeSet;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Board.State;
import fr.univavignon.courbes.common.ItemInstance;
import fr.univavignon.courbes.common.ItemType;
import fr.univavignon.courbes.common.Player;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Round;
import fr.univavignon.courbes.common.SmallUpdate;
import fr.univavignon.courbes.common.Snake;
import fr.univavignon.courbes.common.UpdateInterface;
import fr.univavignon.courbes.physics.simpleimpl.PhysBoard;
import fr.univavignon.courbes.physics.simpleimpl.PhysItemInstance;
import fr.univavignon.courbes.physics.simpleimpl.PhysSnake;

/**
 * Classe permettant d'enregistrer les objets susceptibles d'être envoyés 
 * sur le réseau. L'utilisation d'une unique classe Network pour le client 
 * et le serveur permet d'assurer que les objets sont envoyés de la même 
 * façon coté client et serveur.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ClassRegisterer
{	
	/**
	 * Enregistre les classes qui seront transmises via le réseau.
	 * 
	 * @param endPoint
	 * 		Objet utilisé pour l'enregistrement des classes.
	 */
	static public void register (EndPoint endPoint)
	{	Kryo kryo = endPoint.getKryo();
		
		kryo.register(Integer.class);
		kryo.register(Integer[].class);
		kryo.register(int[].class);
		kryo.register(float[].class);
		kryo.register(String.class);
		kryo.register(Profile.class);
		kryo.register(Profile[].class);
		kryo.register(UpdateInterface.class);
		kryo.register(Round.class);
		kryo.register(Player.class);
		kryo.register(Player[].class);
		kryo.register(Board.class);
		kryo.register(PhysBoard.class);
		kryo.register(PhysItemInstance.class);
		kryo.register(LinkedList.class);
		kryo.register(ArrayList.class);
		kryo.register(Snake[].class);
		kryo.register(ItemInstance.class);
		kryo.register(ItemInstance[].class);
		kryo.register(ItemType.class);
		kryo.register(PhysSnake.class);
		kryo.register(TreeSet.class);
		kryo.register(Position.class);
		kryo.register(State.class);
		kryo.register(SmallUpdate.class);
		kryo.register(boolean[].class);
		kryo.register(boolean.class);
	}
}
