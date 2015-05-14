package de.oc.xcs;

import java.util.ArrayList;
import java.util.List;

public class ClassifiersGroup {
	private static ClassifiersGroup singleton;
	private ArrayList<Classifier> list;
	private ClassifiersGroup(){
		list = new ArrayList<Classifier>();
	}
	public static ClassifiersGroup instance(){
		return singleton == null ? singleton = new ClassifiersGroup() : singleton;
	}
	public List<Classifier> findMatchingItems(){
		ArrayList<Classifier> returnList = new ArrayList<Classifier>();
		
		return returnList;
	}
}
