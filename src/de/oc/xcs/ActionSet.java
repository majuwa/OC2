package de.oc.xcs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class ActionSet implements Iterable<Action> {
	private static ActionSet singleton;
	private ArrayList<Action> list;
	private Action attack;
	private Action flee;
	private ActionSet(){
		list = new ArrayList<>();
		attack = new Action(Action.ATACK);
		flee = new Action(Action.FLEE_UP);
		list.add(attack);
		//list.add(new Action(Action.FLEE_DOWN));
		list.add(flee);
	}
	public static ActionSet instance(){
		return singleton == null ? singleton = new ActionSet() : singleton;
	}
	public Iterator<Action> iterator(){
		return list.iterator();
	}
	public Action getRandomAction(){
		Collections.shuffle(list);
		return list.get(0);
	}
	public Action getAttack(){
		return attack;
	}
	public Action getFlee(){
		return flee;
	}
}
