package ga;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.Population;

public class MyPopulation implements Population{
	private List<Chromosome> list;
	private int size;
	public MyPopulation(int size){
		this.size = size;
		this.list = new ArrayList<Chromosome>();
	}
	public MyPopulation(List<Chromosome> t, int size) {
		this.list = t;
		this.size = size;
	}
	@Override
	public Iterator<Chromosome> iterator() {
		return list.iterator();
	}
	@Override
	public void addChromosome(Chromosome arg0) throws NumberIsTooLargeException {
		list.add(arg0);
		
	}
	@Override
	public Chromosome getFittestChromosome() {
		return list.stream().max(Comparator.comparing(e-> e.getFitness())).get();
	}
	@Override
	public int getPopulationLimit() {
		return size;
	}
	@Override
	public int getPopulationSize() {
		return list.size();
	}
	@Override
	public Population nextGeneration() {
		return new MyPopulation(list, size);
	}
	

}
