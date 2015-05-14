package de.oc.xcs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class ActionSet implements Iterable<Action> {
	private static ActionSet singleton;
	private ArrayList<Action> list;
	private ActionSet(){
		list = new ArrayList<>();
		list.add(new Action(Action.ATACK));
		list.add(new Action(Action.FLEE_DOWN));
		list.add(new Action(Action.FLEE_UP));
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
}
