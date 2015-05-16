package de.oc.xcs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ActionContainer implements Iterable<Action>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7481591428911335870L;
	private static ActionContainer singleton;
	private List<Action> list;
	private boolean first;

	private ActionContainer() {
		list = new ArrayList<>();
		list.add(new Action(Action.ATACK));
		list.add(new Action(Action.FLEE_MEDIUM));
		list.add(new Action(Action.FLEE_MAX));
		list.add(new Action(Action.FLEE_LESS));
		first = true;
	}

	public static ActionContainer instance() {
		return singleton == null ? singleton = new ActionContainer()
				: singleton;
	}

	public Iterator<Action> iterator() {
		return list.iterator();
	}

	public Action getRandomAction() {
		if (first){
			first= !first;
			return list.get(0);
		}
		Collections.shuffle(list);
		return list.get(0);
	}
}
