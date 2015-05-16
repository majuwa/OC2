import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import de.oc.xcs.Action;
import de.oc.xcs.ActionSelection;
import de.oc.xcs.Classifier;
import de.oc.xcs.ClassifierSet;
import de.oc.xcs.Situation;
import de.oc.xcs.ActionSet;

public class Vulture {

	final private JNIBWAPI bwapi;
	private final HashSet<Unit> enemyUnits;
	final private Unit unit;
	private Position old;
	private int i = 0;
	private Unit nextEnemy;
	private int hpEnemy;
	private double rewardOld;
	public Vulture(Unit unit, JNIBWAPI bwapi, HashSet<Unit> enemyUnits) {
		this.unit = unit;
		this.bwapi = bwapi;
		this.enemyUnits = enemyUnits;
		rewardOld = Double.NaN;
	}

	public void step() {
		/**
		 * TODO: XCS
		 */
		// System.out.println(bwapi.getFrameCount());
		if(nextEnemy == null){
			this.nextEnemy = getClosestEnemy();
		}
		if(nextEnemy == null)
			return;
		double reward;
		double finalReward;
		if(VultureAI.destroyedEnemy>0){
			reward = 500 * unit.getHitPoints() + VultureAI.destroyedEnemy * 1000 + 3 * (100- nextEnemy.getHitPoints());
		}
		else{
			reward = unit.getHitPoints() + 2 * (100- nextEnemy.getHitPoints());
			
		}
		if(!Double.isNaN(rewardOld)){
			finalReward = reward - rewardOld;
			rewardOld = reward;
		}
		else{
			finalReward = reward;
		}
		if(unit.getHitPoints()<30)
			reward -=reward * 0.8 + 30;
		if(!enemyUnits.contains(nextEnemy)){
			System.out.println("Enemy Destroyed");
			nextEnemy = getClosestEnemy();
			this.hpEnemy = nextEnemy.getHitPoints();
		}
		ActionSet.instance().setReward(finalReward);
		String distance = Double.toString(getDistance(nextEnemy));
		String hp = Integer.toString(unit.getHitPoints());
		String hpEnemy = Integer.toString(nextEnemy.getHitPoints());
		String positionX = Integer.toString(unit.getPosition().getPX());
		String positionY = Integer.toString(unit.getPosition().getPX());
		String countEnemy = Integer.toString(enemyUnits.size());
		Situation s = new Situation(distance, hp, hpEnemy, positionX,
				positionY);
		List<Classifier> matchingSet = ClassifierSet.instance()
				.findMatchingItems(s);
		ActionSelection.selectAction(matchingSet, unit, nextEnemy);
		old = unit.getPosition();
	}

	private void move(Unit target) {
		unit.move(new Position(target.getPosition().getPX(), target
				.getPosition().getPY()), false);
	}

	private Unit getClosestEnemy() {
		Unit result = null;
		double minDistance = Double.POSITIVE_INFINITY;
		for (Unit enemy : enemyUnits) {
			double distance = getDistance(enemy);
			if (distance < minDistance) {
				minDistance = distance;
				result = enemy;
			}
		}

		return result;
	}

	private double getDistance(Unit enemy) {
		int myX = unit.getPosition().getPX();
		int myY = unit.getPosition().getPY();

		int enemyX = enemy.getPosition().getPX();
		int enemyY = enemy.getPosition().getPY();

		int diffX = myX - enemyX;
		int diffY = myY - enemyY;

		double result = Math.pow(diffX, 2) + Math.pow(diffY, 2);

		return Math.sqrt(result);
	}
}
