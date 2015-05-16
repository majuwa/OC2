package de.oc.xcs;

import java.io.Serializable;

public class Classifier implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1622865511352294058L;
	private double prediction;
	private double predictionError;
	private double fitness;
	private Action action;
	private Situation situation;
	public Classifier( Situation situation, Action action, double prediction, double predictionError,
			double fitness) {
		super();
		this.prediction = prediction;
		this.predictionError = predictionError;
		this.fitness = fitness;
		this.action = action;
		this.situation = situation;
	}
	public double getPrediction() {
		return prediction;
	}
	public void setPrediction(double prediction) {
		this.prediction = prediction;
	}
	public double getPredictionError() {
		return predictionError;
	}
	public void setPredictionError(double predictionError) {
		this.predictionError = predictionError;
	}
	public double getFitness() {
		return fitness;
	}
	public Situation getSituation(){return situation;}
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	public Action getAction(){
		return this.action;
	}

}
