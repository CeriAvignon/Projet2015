package fr.univavignon.courbes.physics.groupe16;

import java.util.HashMap;
import java.util.Map;

import fr.univavignon.courbes.common.Direction;


public class MainTest {

	public static void main(String[] args) {
		Round round = new Round();
		round.init(400, 400, 1);
		Map<Integer, Direction> commandesTest = new HashMap<Integer, Direction>();
		round.update(30, commandesTest);
		round.update(30, commandesTest);
		round.update(30, commandesTest);
	}

}
