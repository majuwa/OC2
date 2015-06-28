package boids;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
	public static final double R_SEP = 10;
	public static final double R_ROW = 5;
	public static final double R_COLOUMN = 5;
	private Marine m;

	public Boiding(Marine tmp) {
		this.m = tmp;
	}

	/**
	 * Function to calculate the distance between the Marine and the nearest
	 * enemy
	 * 
	 * @return Distance
	 */
	public Vector2D ruleOne() {
		Unit t = m.getClosestEnemy();
		Vector2D vector = new Vector2D(new double[] { (double) t.getX(),
				(double) t.getY() });
		return vector;
	}

	/**
	 * Tries to sepearte each Unit a bit from their neigbour. Separation rule
	 * 
	 * @return
	 */
	public Vector2D ruleTwo() {
		Vector2D tmp = new Vector2D(new double[] { (double) m.getUnit().getX(),
				(double) m.getUnit().getY() });
		return (Vector2D) m
				.unitsInRange(R_SEP)
				.stream()
				.map(e -> new Vector2D(new double[] { (double) e.getX(),
						(double) e.getY() }))
				.reduce((acc, el) -> acc = el.subtract(tmp)).get();
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
		return new Vector2D(
				(double) (pair.getKey().getX() - m.getUnit().getX()),
				(double) (pair.getKey().getY() - m.getUnit().getY()));
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
		return new Vector2D(
				(double) (pair.getKey().getX() - m.getUnit().getX()),
				(double) (pair.getKey().getY() - m.getUnit().getY()));
	}

	private Vector2D getCohesion() {
		double position[] = new double[2];
		position[0] = m.unitsInRange(R_SEP).stream()
				.mapToInt(value -> m.getUnit().getX()).average().getAsDouble();
		position[1] = m.unitsInRange(R_SEP).stream()
				.mapToInt(value -> m.getUnit().getY()).average().getAsDouble();
		return new Vector2D(position);
	}

	/**
	 * Counts units per Coloumn
	 * 
	 * @return
	 */
	private List<Pair<Point, Double>> unitsPerRow() {
		List<Pair<Point, Double>> l = new ArrayList<Pair<Point, Double>>();
		List<Rectangle> rect = new ArrayList<Rectangle>();
		Rectangle tmp1 = new Rectangle((int) (m.getUnit().getX() - R_SEP),
				(int) (m.getUnit().getY() + R_SEP), (int) (2 * R_SEP),
				(int) R_ROW);
		Rectangle tmp2 = new Rectangle((int) (m.getUnit().getX() - R_SEP),
				(int) (m.getUnit().getY()), (int) (2 * R_SEP), (int) R_ROW);

		rect.add(tmp1);
		rect.add(tmp2);
		l.add(new Pair<Point, Double>(new Point((int) tmp1.getCenterX(),
				(int) tmp1.getCenterY()), (double) m.unitsInRange(R_SEP)
				.stream().filter(e -> tmp1.contains(e.getX(), e.getY()))
				.count()));
		l.add(new Pair<Point, Double>(new Point((int) tmp2.getCenterX(),
				(int) tmp2.getCenterY()), (double) m.unitsInRange(R_SEP)
				.stream().filter(e -> tmp2.contains(e.getX(), e.getY()))
				.count()));

		for (int i = 0; i < l.size(); i++) {
			final int position = i;
			double x = m
					.unitsInRange(R_SEP)
					.stream()
					.filter(e -> rect.get(position).contains(
							new Point(e.getX(), e.getY())))
					.mapToInt(value -> m.getUnit().getX()).average()
					.getAsDouble();
			double y = m
					.unitsInRange(R_SEP)
					.stream()
					.filter(e -> rect.get(position).contains(
							new Point(e.getX(), e.getY())))
					.mapToInt(value -> m.getUnit().getY()).average()
					.getAsDouble();

			double test = (l.get(i).getValue() / m.getPosition()
					.add(new Vector2D(x, y)).getNorm());

			Pair<Point, Double> tmp = new Pair<Point, Double>(
					l.get(i).getKey(), test);
			l.remove(i);
			l.add(i, tmp);
		}
		return l;
	}

	private List<Pair<Point, Double>> unitsPerColumn() {
		List<Pair<Point, Double>> l = new ArrayList<Pair<Point, Double>>();
		List<Rectangle> rect = new ArrayList<Rectangle>();
		Rectangle tmp1 = new Rectangle((int) (m.getUnit().getX() - R_SEP),
				(int) (m.getUnit().getY() + R_SEP), (int) (2 * R_SEP),
				(int) R_ROW);
		Rectangle tmp2 = new Rectangle((int) (m.getUnit().getX() - R_SEP),
				(int) (m.getUnit().getY()), (int) (2 * R_SEP), (int) R_ROW);

		rect.add(tmp1);
		rect.add(tmp2);
		l.add(new Pair<Point, Double>(new Point((int) tmp1.getCenterX(),
				(int) tmp1.getCenterY()), (double) m.unitsInRange(R_SEP)
				.stream().filter(e -> tmp1.contains(e.getX(), e.getY()))
				.count()));
		l.add(new Pair<Point, Double>(new Point((int) tmp2.getCenterX(),
				(int) tmp2.getCenterY()), (double) m.unitsInRange(R_SEP)
				.stream().filter(e -> tmp2.contains(e.getX(), e.getY()))
				.count()));

		for (int i = 0; i < l.size(); i++) {
			final int position = i;
			double x = m
					.unitsInRange(R_SEP)
					.stream()
					.filter(e -> rect.get(position).contains(
							new Point(e.getX(), e.getY())))
					.mapToInt(value -> m.getUnit().getX()).average()
					.getAsDouble();
			double y = m
					.unitsInRange(R_SEP)
					.stream()
					.filter(e -> rect.get(position).contains(
							new Point(e.getX(), e.getY())))
					.mapToInt(value -> m.getUnit().getY()).average()
					.getAsDouble();

			double test = (l.get(i).getValue() / m.getPosition()
					.add(new Vector2D(x, y)).getNorm());

			Pair<Point, Double> tmp = new Pair<Point, Double>(
					l.get(i).getKey(), test);
			l.remove(i);
			l.add(i, tmp);
		}
		return l;
	}

	public Vector2D nextPosition() {
		double w1 = 0;
		double w2 = 0;
		double w3 = 0;
		double w4 = 0;
		Vector2D x1 = ruleOne().scalarMultiply(w1);
		Vector2D x2 = ruleTwo().scalarMultiply(w2);
		Vector2D x3 = ruleThree().scalarMultiply(w3);
		Vector2D x4 = ruleFour().scalarMultiply(w4);
		
		return m.getPosition().add(x1).add(x2).add(x3).add(x4);
		
	}
}
