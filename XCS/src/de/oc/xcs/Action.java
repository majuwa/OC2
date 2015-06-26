package de.oc.xcs;

import java.io.Serializable;

import jnibwapi.Position;
import jnibwapi.Unit;

/*
 * Alle Aktionen welche die Einheit Vulture ausführen kann
 */
public class Action implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8091463803669248170L;
	public static final int ACTIONS_SIZE = 5;
	protected static final int FLEE_LESS = 1;
	protected static final int FLEE_MAX = 2;
	protected static final int FLEE_MEDIUM = 3;
	protected static final int ATACK = 4;
	private int action;
	
	protected Action(int action){
		this.action = action;
	}
	
	public void doAction(Unit own, Unit enemy){
		switch (action) {
		case FLEE_LESS:
			own.move(new Position(enemy.getPosition().getPX() - 100, (own.getPosition().getPY())), false);
			break;
		case FLEE_MAX:
			own.move(new Position(enemy.getPosition().getPX() - 200, (own.getPosition().getPY())), false);
			break;
		case FLEE_MEDIUM:
			own.move(new Position(enemy.getPosition().getPX() - 150, (own.getPosition().getPY())), false);
			break;
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
