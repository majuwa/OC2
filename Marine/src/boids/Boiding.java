package boids;

import ga.Chromo;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;

import jnibwapi.model.Unit;
import marineAI.Marine;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.Pair;

/**
 * Class with RTS Boiding in StarCraft
 * 
 * @author Maximilian
 *
 */
public class Boiding {
	public static double R_SEP;
	public static double R_ROW;
	public static double R_COLOUMN;
	private Marine m;

	public Boiding(Marine tmp, Chromo ch) {
		R_SEP = ch.getrSep();
		R_ROW = ch.getrRow();
		R_COLOUMN = ch.getrCol();
		this.m = tmp;
	}

	private void calc_list_of_points(List<Pair<Point, Double>> l,
			List<Rectangle> rect) {
		for (int i = 0; i < l.size(); i++) {
			final int position = i;
			double x = calcXAverage(rect, position);
			double y = calcYAverage(rect, position);

			Pair<Point, Double> tmp = createAvergePoint(l, i, x, y);
			l.remove(i);
			l.add(i, tmp);
		}
	}

	private double calcXAverage(List<Rectangle> rect, final int position) {
		OptionalDouble x = m
				.unitsInRange(R_SEP)
				.stream()
				.filter(e -> rect.get(position).contains(
						new Point(e.getX(), e.getY())))
				.mapToInt(value -> m.getUnit().getX()).average();
		if (x.isPresent())
			return x.getAsDouble();
		else
			return (double) m.getUnit().getX();
	}

	private double calcYAverage(List<Rectangle> rect, final int position) {
		OptionalDouble y = m
				.unitsInRange(R_SEP)
				.stream()
				.filter(e -> rect.get(position).contains(
						new Point(e.getX(), e.getY())))
				.mapToInt(value -> m.getUnit().getY()).average();
		if (y.isPresent())
			return y.getAsDouble();
		else
			return (double) m.getUnit().getY();
	}

	private Pair<Point, Double> createAvergePoint(List<Pair<Point, Double>> l,
			int i, double x, double y) {
		double test = (l.get(i).getValue() / m.getPosition()
				.add(new Vector2D(x, y)).getNorm());

		Pair<Point, Double> tmp = new Pair<Point, Double>(l.get(i).getKey(),
				test);
		return tmp;
	}

	private Vector2D getCohesion() {
		double position[] = new double[2];
		position[0] = m.unitsInRange(R_SEP).stream()
				.mapToInt(value -> m.getUnit().getX()).average().getAsDouble();
		position[1] = m.unitsInRange(R_SEP).stream()
				.mapToInt(value -> m.getUnit().getY()).average().getAsDouble();
		return new Vector2D(position);
	}

	public Vector2D nextPosition(double w1, double w2, double w3, double w4) {
		Vector2D x1 = ruleOne().scalarMultiply(w1);
		Vector2D x2 = ruleTwo().scalarMultiply(w2);
		Vector2D x3 = ruleThree().scalarMultiply(w3);
		Vector2D x4 = ruleFour().scalarMultiply(w4);

		return m.getPosition().add(x1).add(x2).add(x3).add(x4);

	}

	/**
	 * Move to center of a coloumn
	 * 
	 * @return
	 */
	public Vector2D ruleFour() {
		double position[] = new double[2];
		position[0] = m.getUnit().getX();
		position[1] = m.getUnit().getY();
		Vector2D p = new Vector2D(position);
		p = p.add(getCohesion());
		double norm = p.getNorm();
		Pair<Point, Double> pair = unitsPerColumn().stream()
				.max(Comparator.comparing(e -> e.getValue())).get();
		return new Vector2D(pair.getKey().getX() - m.getUnit().getX(), pair
				.getKey().getY() - m.getUnit().getY());
	}

	/**
	 * Function to calculate the distance between the Marine and the nearest
	 * enemy
	 * 
	 * @return Distance
	 */
	public Vector2D ruleOne() {
		Unit t = m.getClosestEnemy();
		Vector2D vector = new Vector2D(new double[] { t.getX(), t.getY() });
		return vector;
	}

	/**
	 * Move to center of a coloumn
	 * 
	 * @return
	 */
	public Vector2D ruleThree() {
		double position[] = new double[2];
		position[0] = m.getUnit().getX();
		position[1] = m.getUnit().getY();
		Vector2D p = new Vector2D(position);
		p = p.add(getCohesion());
		double norm = p.getNorm();
		Pair<Point, Double> pair = unitsPerRow().stream()
				.max(Comparator.comparing(e -> e.getValue())).get();
		return new Vector2D(pair.getKey().getX() - m.getUnit().getX(), pair
				.getKey().getY() - m.getUnit().getY());
	}

	/**
	 * Tries to sepearte each Unit a bit from their neigbour. Separation rule
	 * 
	 * @return
	 */
	public Vector2D ruleTwo() {
		Vector2D tmp = new Vector2D(new double[] { m.getUnit().getX(),
				m.getUnit().getY() });
		return m.unitsInRange(R_SEP).stream()
				.map(e -> new Vector2D(new double[] { e.getX(), e.getY() }))
				.reduce((acc, el) -> acc = el.subtract(tmp)).get();
	}

	private List<Pair<Point, Double>> unitsPerColumn() {
		List<Pair<Point, Double>> l = new ArrayList<Pair<Point, Double>>();
		List<Rectangle> rect = new ArrayList<Rectangle>();
		for (int i = 0; i < 2 * R_SEP; i += R_ROW) {
			rect.add(new Rectangle((int) (m.getUnit().getX() - R_SEP) + i,
					(int) (m.getUnit().getY() + R_SEP), (int) (R_ROW),
					(int) (2 * R_SEP)));

		}
		for(int i = 0; i< rect.size();i++)
			addUnitsInRectangle(l, rect, i);
		calc_list_of_points(l, rect);
		return l;
	}

	/**
	 * Counts units per Coloumn
	 * 
	 * @return
	 */
	private List<Pair<Point, Double>> unitsPerRow() {

		List<Pair<Point, Double>> l = new ArrayList<Pair<Point, Double>>();
		List<Rectangle> rect = new ArrayList<Rectangle>();

		for (int i = 0; i < 2 * R_SEP; i += R_ROW) {
			rect.add(new Rectangle((int) (m.getUnit().getX() - R_SEP), (int) (m
					.getUnit().getY() + R_SEP) + i, (int) (2 * R_SEP),
					(int) R_ROW));

		}
		for (int i = 0; i < rect.size(); i++) {
			addUnitsInRectangle(l, rect, i);
		}
		calc_list_of_points(l, rect);
		return l;
	}

	private void addUnitsInRectangle(List<Pair<Point, Double>> l,
			List<Rectangle> rect, int i) {
		l.add(new Pair<Point, Double>(new Point((int) rect.get(i).getCenterX(),
				(int) rect.get(i).getCenterY()), (double) m.unitsInRange(R_SEP)
				.stream().filter(e -> rect.get(i).contains(e.getX(), e.getY()))
				.count()));
	}
}
