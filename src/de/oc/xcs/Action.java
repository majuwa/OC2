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
			own.move(new Position(enemy.getPosition().getPX() - 100, (own.getPosition().getPY())), false);
			break;
		/*case FLEE_DOWN:
			Position e = enemy.getPosition();
			Position o = own.getPosition();
			own.move(new Position(enemy.getPosition().getPX() + 100, (own.getPosition().getPY())), false);
			break;*/
		case ATACK:
			own.attack(enemy, false);
			break;
		default:
			break;
		}
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + action;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Action other = (Action) obj;
		if (action != other.action)
			return false;
		return true;
	}
}
