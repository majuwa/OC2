package ga;

import jnibwapi.model.Unit;
import marineAI.MarineAI;

import org.apache.commons.math3.genetics.Chromosome;
/**
 * Chromosom Helper Class
 * @author Maximilian Walter
 *
 */
public class Chromo extends Chromosome {
	private double w1,w2,w3,w4,rSep,rCol,rRow, fitness;
	
	public Chromo(double w1, double w2, double w3, double w4, double rSep,
			double rCol, double rRow) {
		super();
		this.w1 = w1;
		this.w2 = w2;
		this.w3 = w3;
		this.w4 = w4;
		this.rSep = rSep;
		this.rCol = rCol;
		this.rRow = rRow;
		this.fitness = Math.random()*10;
	}
	public Chromo(){
		setW1(Math.random() * 2);
		setW2(Math.random() * 2);
		setW3(Math.random() * 2);
		setW4(Math.random() * 2);
		setrSep(Math.random()*5 +3);
		setrCol(Math.random()*3);
		setrRow(Math.random()*3);
	}

	/**
	 * Weigth for x1
	 * @return
	 */
	public double getW1() {
		return w1;
	}

	/**
	 * Weight for x1
	 * @param w1
	 */
	public void setW1(double w1) {
		this.w1 = w1;
	}

	/**
	 * 
	 * @return
	 */
	public double getW2() {
		return w2;
	}


	public void setW2(double w2) {
		this.w2 = w2;
	}


	public double getW3() {
		return w3;
	}


	public void setW3(double w3) {
		this.w3 = w3;
	}


	public double getW4() {
		return w4;
	}


	public void setW4(double w4) {
		this.w4 = w4;
	}


	public double getrSep() {
		return rSep;
	}


	public void setrSep(double rSep) {
		this.rSep = rSep;
	}


	public double getrCol() {
		return rCol;
	}


	public void setrCol(double rCol) {
		this.rCol = rCol;
	}


	@Override
	protected boolean isSame(Chromosome obj) {
		Chromo other = (Chromo) obj;
		if (Double.doubleToLongBits(rCol) != Double
				.doubleToLongBits(other.rCol))
			return false;
		if (Double.doubleToLongBits(rRow) != Double
				.doubleToLongBits(other.rRow))
			return false;
		if (Double.doubleToLongBits(rSep) != Double
				.doubleToLongBits(other.rSep))
			return false;
		if (Double.doubleToLongBits(w1) != Double.doubleToLongBits(other.w1))
			return false;
		if (Double.doubleToLongBits(w2) != Double.doubleToLongBits(other.w2))
			return false;
		if (Double.doubleToLongBits(w3) != Double.doubleToLongBits(other.w3))
			return false;
		if (Double.doubleToLongBits(w4) != Double.doubleToLongBits(other.w4))
			return false;
		return true;
	}


	public double getrRow() {
		return rRow;
	}


	public void setrRow(double rRow) {
		this.rRow = rRow;
	}


	@Override
	public double fitness() {
		return this.fitness;
	}
	public void setFitness(double fitness){
		this.fitness = fitness;
	}
}
