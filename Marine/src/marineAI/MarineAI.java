package marineAI;

import ga.Chromo;
import ga.GAHelper;

import java.util.HashSet;

import jnibwapi.BWAPIEventListener;
import jnibwapi.JNIBWAPI;
import jnibwapi.model.Unit;
import jnibwapi.types.UnitType;

/**
 * Created by Stefan Rudolph on 17.02.14.
 */
public class MarineAI implements BWAPIEventListener, Runnable {

	private final JNIBWAPI bwapi;

	private HashSet<Marine> marines;

	private HashSet<Unit> enemyUnits;
	private Chromo chrom;
	private int frame;
	private int marineID = 0;

	public MarineAI() {
		//System.out.println("This is the StupidMarineAI! :)");
		GAHelper.instance().setMarineAI(this);
		bwapi = new JNIBWAPI(this, false);
		chrom = GAHelper.instance().getFittest();
	}

	public static void main(String[] args) {
		new MarineAI().run();
	}

	@Override
	public void matchStart() {
		marines = new HashSet<>();
		enemyUnits = new HashSet<>();

		frame = 0;

		bwapi.enablePerfectInformation();
		bwapi.enableUserInput();
		bwapi.setGameSpeed(0);
	}

	@Override
	public void matchFrame() {

		for (Marine m : marines) {
			m.step();
		}

		if (frame % 1000 == 0) {
			System.out.println("Frame: " + frame);
		}
		frame++;
	}

	@Override
	public void unitDiscover(int unitID) {
		Unit unit = bwapi.getUnit(unitID);
		int typeID = unit.getTypeID();

		if (typeID == UnitType.UnitTypes.Terran_Marine.getID()) {
			if (unit.getPlayerID() == bwapi.getSelf().getID()) {
				marines.add(new Marine(unit, bwapi, enemyUnits, marineID,
						marines,chrom));
				marineID++;
			} else {
				enemyUnits.add(unit);
			}
		} else if (typeID == UnitType.UnitTypes.Terran_Vulture.getID()) {
			if (unit.getPlayerID() != bwapi.getSelf().getID()) {
				enemyUnits.add(unit);
			}
		}
	}

	@Override
	public void unitDestroy(int unitID) {
		Marine rm = null;
		for (Marine marine : marines) {
			if (marine.getID() == unitID) {
				rm = marine;
				break;
			}
		}
		marines.remove(rm);

		Unit rmUnit = null;
		for (Unit u : enemyUnits) {
			if (u.getID() == unitID) {
				rmUnit = u;
				break;
			}
		}
		enemyUnits.remove(rmUnit);
	}

	public HashSet<Marine> getMarines() {
		return marines;
	}

	@Override
	public void connected() {
		System.out.println("Connected");
	}

	@Override
	public void matchEnd(boolean winner) {
		if (getMarines().isEmpty())
			chrom.setFitness(-10.0);
		else {
			chrom.setFitness(getMarines().stream()
					.mapToInt(e -> e.getUnit().getHitPoints()).sum());
		}
		GAHelper.instance().nextStep();
		chrom = GAHelper.instance().getFittest();
		if(winner)
			System.out.println("Gewonnen");
	}

	@Override
	public void keyPressed(int keyCode) {

	}

	@Override
	public void sendText(String text) {

	}

	@Override
	public void receiveText(String text) {

	}

	@Override
	public void playerLeft(int playerID) {

	}

	@Override
	public void nukeDetect(int x, int y) {

	}

	@Override
	public void nukeDetect() {

	}

	@Override
	public void unitEvade(int unitID) {

	}

	@Override
	public void unitShow(int unitID) {

	}

	@Override
	public void unitHide(int unitID) {

	}

	@Override
	public void unitCreate(int unitID) {
	}

	@Override
	public void unitMorph(int unitID) {

	}

	@Override
	public void unitRenegade(int unitID) {

	}

	@Override
	public void saveGame(String gameName) {

	}

	@Override
	public void unitComplete(int unitID) {

	}

	@Override
	public void playerDropped(int playerID) {

	}

	@Override
	public void run() {
		bwapi.start();
	}
}
