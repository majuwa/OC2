package ga;

import marineAI.MarineAI;

import org.apache.commons.math3.genetics.GeneticAlgorithm;
import org.apache.commons.math3.genetics.OnePointCrossover;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.genetics.RandomKeyMutation;
import org.apache.commons.math3.genetics.TournamentSelection;

/**
 * GAHelper class in Singleton
 * @author Maximilian
 *
 */
public class GAHelper {
	private MarineAI marineAI;
	private Population pop;
	private static GAHelper uniquee = new GAHelper();
	private GeneticAlgorithm ga;
	private int helper;
	private GAHelper(){
		ga = new GeneticAlgorithm(new OnePointCrossover<Double>(), 0.9, new RandomKeyMutation(), 0.01,  new TournamentSelection(1));
		pop = new MyPopulation(20);
		for(int i = 0; i<24;i++){
			pop.addChromosome(new Chromo());
		}
		helper = 0;
	}
	public static GAHelper instance(){
		return uniquee;
	}
	public MarineAI getMarineAI(){
		return marineAI;
	}
	public void nextStep(){
		if(helper++ < 100)
		pop = ga.nextGeneration(pop);
	}
	public Chromo getFittest(){
		return (Chromo) pop.getFittestChromosome();
	}
	public void setMarineAI(MarineAI marineAI) {
		this.marineAI = marineAI;
	}
}
