package fr.univavignon.courbes.physics;

public class MainTest {

	public static void main(String[] args) {
		Round round = new Round();
		round.init(400, 400, 1);
		round.update(30, null);
		round.update(30, null);
		round.update(30, null);
	}

}
