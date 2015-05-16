package de.oc.xcs;

import java.util.List;

public class ActionSet {
	private static ActionSet singleton;
	public static double LEARNING_RATE = 0.1;
	public static double GAMA = 0.71;
	private double maxValue;
	private List<Classifier> list;

	private ActionSet() {
		maxValue = Double.NaN;
	}

	public static ActionSet instance() {
		return singleton == null ? singleton = new ActionSet()
				: singleton;
	}

	public void setPredictionMax(double x) {
		this.maxValue = x;
	}

	public void setActionSet(List<Classifier> list) {
		this.list = list;
	}

	public void setReward(double reward) {
		if (Double.isNaN(maxValue))
			return;
		final double P = reward + GAMA * maxValue;
		list.stream().forEach(
				e -> {
					e.setPrediction(e.getPrediction() + LEARNING_RATE
							* (P - e.getPrediction()));
					e.setPredictionError(e.getPredictionError()
							+ LEARNING_RATE
							* (Math.abs(P - e.getPredictionError()
									- e.getPredictionError())));
					e.setFitness(e.getFitness() + LEARNING_RATE
							* (1 / e.getPredictionError() - e.getFitness()));
				});

	}

}
