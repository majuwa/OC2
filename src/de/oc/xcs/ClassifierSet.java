package de.oc.xcs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Container der alle Classifier enthält. Berechnet das Match-Set.
 */
public class ClassifierSet implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6947191540676110683L;
	private static ClassifierSet singleton;
	private List<Classifier> list;

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
		returnList = list.parallelStream()
				.filter(e -> e.getSituation().equals(sit))
				.collect(Collectors.toList());
		if (returnList.isEmpty() || (calcAvg() * 0.5) > calcAvg(returnList)) {
			initializeValues(sit);
			returnList = list.parallelStream()
					.filter(e -> e.getSituation().equals(sit))
					.collect(Collectors.toList());
		}
		if (false) {
			Classifier max = returnList.get(0), max2 = returnList.get(1);
			for (Classifier a : returnList) {
				if (a.getFitness() > max.getFitness()) {
					max2 = max;
					max = a;
				}
			}
			Classifier tmp = new Classifier(max.getSituation(), max2.getAction(), max.getPrediction(), max.getPredictionError(), (max.getFitness() + max2.getFitness())/2);
			// resulting two classifiers get half of their parents’ fitness
			list.add(tmp);
			returnList.add(tmp);
			returnList.remove(max);
			Classifier tmp1 = new Classifier(max2.getSituation(), max.getAction(), max2.getPrediction(), max2.getPredictionError(),  (max.getFitness() + max2.getFitness())/2);
			list.add(tmp1); //add to classifier list
			returnList.add(tmp1); //add to matchset
			returnList.remove(max2);
			if(Math.random() < 0.00001){ //mutation
				Classifier mutTmp = new Classifier(tmp.getSituation(),new Action((int) (Math.random()*4 + 1)),tmp.getPrediction(),tmp.getPredictionError(),tmp.getFitness());
				list.remove(tmp); // remove old classifier
				returnList.remove(tmp); //remove old classifier matchset
				returnList.add(mutTmp);
				list.add(mutTmp);
			}
			if(Math.random() < 0.00001){ //mutation
				Classifier mutTmp = new Classifier(tmp1.getSituation(),new Action((int) (Math.random()*4 + 1)),tmp1.getPrediction(),tmp1.getPredictionError(),tmp1.getFitness());
				list.remove(tmp1); // remove old classifier
				returnList.remove(tmp1); //remove old classifier matchset
				returnList.add(mutTmp);
				list.add(mutTmp);
			}
				
		}
		return returnList;
	}

	private void initializeValues(Situation sit) {
		String[] t = new String[Situation.SITUATION_COUNTER];
		for (int i = 0; i < Situation.SITUATION_COUNTER; i++) {
			double randomWildcard = Math.random();
			if (randomWildcard < 0.334)
				t[i] = "#";
			else
				t[i] = sit.getSituation()[i];
		}
		/*
		 * list.add(new Classifier(new Situation(getAttack(80)),
		 * ActionSet.instance().getAttack(), 10, 1/100.0,100)); list.add(new
		 * Classifier(new Situation(getAttack(105)),
		 * ActionSet.instance().getAttack(), 10, 1/100.0,100));
		 * 
		 * String [] l = getAttack(80); l[Situation.HP] = "100"; list.add(new
		 * Classifier(new Situation(l), ActionSet.instance().getAttack(), 10,
		 * 0.1, 10));
		 */
		list.add(new Classifier(new Situation(t), ActionContainer.instance()
				.getRandomAction(), 10, 1 / 10, 10));

	}

	private String[] getAttack(int x) {
		String[] t2 = new String[Situation.SITUATION_COUNTER];
		for (int i = 0; i < t2.length; i++)
			t2[i] = "#";
		t2[Situation.DISTANCE] = Integer.toString(x);
		return t2;
	}

	private String[] getFlee() {
		String[] t2 = new String[Situation.SITUATION_COUNTER];
		for (int i = 0; i < t2.length; i++)
			t2[i] = "#";
		t2[Situation.DISTANCE] = Integer.toString(10);
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

	private double calcAvg(List<Classifier> list) {
		return list.parallelStream().map(e -> e.getFitness()).count()
				/ list.size();
	}

	private double calcAvg() {
		return calcAvg(this.list);
	}

	public List<Classifier> getList() {
		return this.list;
	}

	public void setList(List<Classifier> l) {
		list = l;
	}

}
