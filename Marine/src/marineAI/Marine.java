package marineAI;

import ga.Chromo;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import jnibwapi.JNIBWAPI;
import jnibwapi.model.Unit;
import jnibwapi.types.WeaponType;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import boids.Boiding;

/**
 * Created by Stefan Rudolph on 18.02.14.
 */
public class Marine {

    final private JNIBWAPI bwapi;
    private final HashSet<Unit> enemyUnits;
    private HashSet<Marine> marines;
    final private Unit unit;
    private int id;
    private Chromo chrom;
    public Marine(Unit unit, JNIBWAPI bwapi, HashSet<Unit> enemyUnits, int id, HashSet<Marine> marines, Chromo chrom) {
        this.unit = unit;
        this.bwapi = bwapi;
        this.enemyUnits = enemyUnits;
        this.id = id;
        this.marines = marines;
        this.chrom = chrom;
    }

    public void step() {
        Unit target = getClosestEnemy();

        if (unit.getOrderID() != 10 && !unit.isAttackFrame() && !unit.isStartingAttack() && !unit.isAttacking() && target != null) {
            if (bwapi.getWeaponType(WeaponType.WeaponTypes.Gauss_Rifle.getID()).getMaxRange() > getDistance(target) - 20.0) {
                bwapi.attack(unit.getID(), target.getID());
            } else {
            	move(target);
            }
        }
    }
    
    private void move(Unit target){
    	Boiding t = new Boiding(this,chrom);
    	Vector2D moveTarget = t.nextPosition(chrom.getW1(), chrom.getW2(), chrom.getW3(), chrom.getW4());
    	bwapi.move(unit.getID(),(int) moveTarget.getX(), (int)moveTarget.getY());
    }

    public Unit getClosestEnemy() {
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
        int myX = unit.getX();
        int myY = unit.getY();

        int enemyX = enemy.getX();
        int enemyY = enemy.getY();

        int diffX = myX - enemyX;
        int diffY = myY - enemyY;

        double result = Math.pow(diffX, 2) + Math.pow(diffY, 2);

        return Math.sqrt(result);
    }
    public double getDistance2Enemy(){
    	return getDistance(getClosestEnemy());
    }
    public double getRTSDistance(Unit u){
        int myX = unit.getX();
        int myY = unit.getY();

        int enemyX = u.getX();
        int enemyY = u.getY();

        int diffX = enemyX - myX;
        int diffY = enemyY -myY;

        double result = Math.pow(diffX, 2) + Math.pow(diffY, 2);

        return Math.sqrt(result);
    }
    public Vector2D getPosition(){
    	return new Vector2D((double) unit.getX(),(double) unit.getY());
    }
    /**
     * Calculates a list with friendly marines. The distance is less or equal to radius 
     * @param radius maximal Distance of a marine in {@link Double}
     * @return {@link List} of Units
     */
    public List<Unit> unitsInRange(double radius){
    	return marines.stream().filter(m -> getDistance(m.unit)<=radius).map(u -> u.unit).collect(Collectors.toList());
    }
    public int getID() {
        return unit.getID();
    }
    public Unit getUnit(){
    	return unit;
    }
}
