package de.oc.xcs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
		if(returnList.isEmpty() || (calcAvg() * 0.5)> calcAvg(returnList)) {
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
		list.add(new Classifier(new Situation(getAttack(50)), ActionSet.instance().getAttack(), 5.0, 40.0,1/40));
		list.add(new Classifier(new Situation(getAttack(70)), ActionSet.instance().getAttack(), 10.0, 10.0,1/10));
		list.add(new Classifier(new Situation(getAttack(80)), ActionSet.instance().getAttack(), 10.0, 10.0,1/10));
		list.add(new Classifier(new Situation(getFlee()), ActionSet.instance().getFlee(), 30.0, 5.0,1/10));
		list.add(new Classifier(new Situation(t),ActionSet.instance().getRandomAction(),10,10,1/10));
		

	}
	private String[] getAttack(int x){
		String[] t2 =new String[Situation.SITUATION_COUNTER];
		for(int i = 0;i<t2.length;i++)
			t2[i] = "#";
		t2[Situation.DISTANCE]= Integer.toString(x);
		return t2;
	}
	private String[] getFlee(){
		String[] t2 =new String[Situation.SITUATION_COUNTER];
		for(int i = 0;i<t2.length;i++)
			t2[i] = "#";
		t2[Situation.DISTANCE]= Integer.toString(10);
		return t2;
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
