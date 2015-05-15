package de.oc.xcs;


import jnibwapi.Unit;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ActionSelection {
    public ActionSelection{

    }
    public void selectAction(List<Classifier> matchset, Unit myUnit, Unit enemy){
        HashMap<Action,Double> map =  ClassifierSet.instance().getPredictionArray(matchset);
        final Action selected = map.entrySet().stream().max(Comparator.comparing(e -> e.getValue())).get().getKey();
        List<Classifier> actionSet = matchset.stream().filter(e -> e.getAction().equals(selected)).collect(Collectors.toList());
        selected.doAction(myUnit,enemy);
    }
}
