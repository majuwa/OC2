package de.oc.xcs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ClassifierSet {
	private static ClassifierSet singleton;
	private ArrayList<Classifier> list;

	private ClassifierSet() {
		list = new ArrayList<Classifier>();
	}

	public static ClassifierSet instance() {
		return singleton == null ? singleton = new ClassifierSet() : singleton;
	}

	public List<Classifier> findMatchingItems(Situation sit) {
		List<Classifier> returnList;
		if (list.isEmpty())
			initializeValues(sit);
		returnList = list.parallelStream().filter(e -> e.getSituation().equals(sit)).collect(Collectors.toList());
		if(calcAvg()> calcAvg(returnList)) {
			initializeValues(sit);
			returnList = list.parallelStream().filter(e -> e.getSituation().equals(sit)).collect(Collectors.toList());
		}
		return returnList;
	}

	private void initializeValues(Situation sit) {
		String[] t = new String[Situation.SITUATION_COUNTER];
		for (int i = 0; i < Situation.SITUATION_COUNTER; i++) {
			double randomWildcard = Math.random();
			if(randomWildcard < 0.334)
				t[i] = "#";
			else
				t[i] = sit.getSituation()[i];
		}
		list.add(new Classifier(new Situation(t),ActionSet.instance().getRandomAction(),10,10,10));

	}

	public HashMap<Action, Double> getPredictionArray(List<Classifier> matchSet) {
		HashMap<Action, Double> map = new HashMap<>();
		HashMap<Action, Double> fitnesMap = new HashMap<>();
		for (Classifier c : matchSet) {
			double preFit = c.getPrediction() * c.getFitness();
			if (map.containsKey(c.getAction())) {
				map.put(c.getAction(), map.get(c.getAction()) + preFit);
				fitnesMap.put(c.getAction(),
						fitnesMap.get(c.getAction()) + c.getFitness());
			} else {
				map.put(c.getAction(), preFit);
				fitnesMap.put(c.getAction(), c.getFitness());
			}
		}
		for (java.util.Map.Entry<Action, Double> entry : map.entrySet()) {
			entry.setValue(entry.getValue() / fitnesMap.get(entry.getKey()));
		}
		return map;
	}
	private double calcAvg(List<Classifier> list){
		return list.parallelStream().map(e-> e.getFitness()).count() /list.size();
	}
	private double calcAvg(){
		return calcAvg(this.list);
	}

}
