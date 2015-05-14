package de.oc.xcs;

import jnibwapi.Position;
import jnibwapi.Unit;

public class Action {
	public static final int ACTIONS_SIZE = 5;
	protected static final int FLEE_UP = 1;
	protected static final int FLEE_DOWN = 2;
	protected static final int ATACK = 3;
	private int action;
	protected Action(int action){
		this.action = action;
	}
	public void doAction(Unit own, Unit enemy){
		switch (action) {
		case FLEE_UP:
			own.move(new Position(3205, 1300), false);
			break;
		case FLEE_DOWN:
			own.move(new Position(3205, 1650), false);
			break;
		case ATACK:
			own.attack(enemy, false);
			break;
		default:
			break;
		}
	}
}
