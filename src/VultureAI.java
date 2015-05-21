import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.List;

import jnibwapi.BWAPIEventListener;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import de.oc.xcs.ActionSet;
import de.oc.xcs.Classifier;
import de.oc.xcs.ClassifierSet;

public class VultureAI  implements BWAPIEventListener, Runnable {
	public enum MODE{LEARNED,LEARNING};
    private final JNIBWAPI bwapi;
    private VultureAI.MODE mode;
    public static int destroyedEnemy = 0; 
    private Vulture vulture;
    private Evaluation eval;
    private Unit unit;
    private HashSet<Unit> enemyUnits;

    private int frame;

    public VultureAI(String s) {
        System.out.println("This is the VultureAI! :)");
        if(s.equals(""))
        	mode = MODE.LEARNING; 
        else{
        	mode = MODE.LEARNED;
        	try{
        		FileInputStream stream = new FileInputStream(s);
        		ObjectInputStream ob = new ObjectInputStream(stream);
        		Object o = ob.readObject();
        		if(o instanceof List){
        			ClassifierSet.instance().setList((List<Classifier>) o);
        		}
        	}
        	catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
        	
        }
        bwapi = new JNIBWAPI(this, false);
    }

    public static void main(String[] args) {    	
        new VultureAI(args.length == 1 ? args[0] : "").run();
    }

    @Override
    public void matchStart() {
        enemyUnits = new HashSet<Unit>();

        frame = 0;

        bwapi.enablePerfectInformation();
        bwapi.enableUserInput();
        bwapi.setGameSpeed(100);
        
        
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
        	this.unit = unit;
            if (unit.getPlayer() == bwapi.getSelf()) {
                this.vulture = new Vulture(unit, bwapi, enemyUnits, this);
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
    		ActionSet.instance().won();/*
    		try {
				FileOutputStream fileOut = new FileOutputStream(System.currentTimeMillis() + ".obj");
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(ClassifierSet.instance().getList());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
    		
    	}
    	else{
    		ActionSet.instance().lost();
    		destroyedEnemy = 0;
    		System.out.println("LOOSE!!");
    	}
    	eval = new Evaluation(this.frame,unit.getHitPoints());
    	System.out.println(eval);
    }
    public VultureAI.MODE getMode(){
    	return mode;
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
