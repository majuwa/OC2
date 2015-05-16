import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashSet;

import de.oc.xcs.ClassifierSet;
import jnibwapi.BWAPIEventListener;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

public class VultureAI  implements BWAPIEventListener, Runnable {

    private final JNIBWAPI bwapi;
    public static int destroyedEnemy = 0; 
    private Vulture vulture;

    private HashSet<Unit> enemyUnits;

    private int frame;

    public VultureAI() {
        System.out.println("This is the VultureAI! :)");

        bwapi = new JNIBWAPI(this, false);
    }

    public static void main(String[] args) {
        new VultureAI().run();
    }

    @Override
    public void matchStart() {
        enemyUnits = new HashSet<Unit>();

        frame = 0;

        bwapi.enablePerfectInformation();
        bwapi.enableUserInput();
        bwapi.setGameSpeed(0);
    }

    @Override
    public void matchFrame() {

        vulture.step();

        if (frame % 1000 == 0) {
            System.out.println("Frame: " + frame);
        }
        frame++;
    }

    @Override
    public void unitDiscover(int unitID) {
        Unit unit = bwapi.getUnit(unitID);
        UnitType type = unit.getType();

        if (type == UnitType.UnitTypes.Terran_Vulture) {
            if (unit.getPlayer() == bwapi.getSelf()) {
                this.vulture = new Vulture(unit, bwapi, enemyUnits);
            }
        } else if (type == UnitType.UnitTypes.Protoss_Zealot) {
            if (unit.getPlayer() != bwapi.getSelf()) {
                enemyUnits.add(unit);
            }
        }
    }

    @Override
    public void unitDestroy(int unitID) {
        Unit rmUnit = null;
        for (Unit u : enemyUnits) {
            if (u.getID() == unitID) {
                rmUnit = u;
                break;
            }
        }
        destroyedEnemy++;
        enemyUnits.remove(rmUnit);
    }

    @Override
    public void connected() {
        System.out.println("Connected");
    }

    @Override
    public void matchEnd(boolean winner) {
    	if(winner){
    		System.out.println("WIN!!");
    	
    		try {
				FileOutputStream fileOut = new FileOutputStream(System.currentTimeMillis() + ".obj");
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(ClassifierSet.instance().getList());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	else{
    		destroyedEnemy = 0;
    		System.out.println("LOOSE!!");
    	}
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
    public void nukeDetect(Position position) {

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
