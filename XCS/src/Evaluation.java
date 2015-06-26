
public class Evaluation {
	
	/**
	 * Dauer die zum töten der Einheiten benötigt wird.
	 */
	private int timeToKill; 
	/**
	 * Anzahl der HitPoints die im Laufe des Spiels verloren wurden.
	 */
	private int lostHitPoints;
	
	public int getTimeToKill() {
		return timeToKill;
	}
	public void setTimeToKill(int timeToKill) {
		this.timeToKill = timeToKill;
	}
	public int getLostHitPoints() {
		return lostHitPoints;
	}
	public void setLostHitPoints(int lostHitPoints) {
		this.lostHitPoints = lostHitPoints;
	}
	
	public Evaluation(int timeToKill, int lostHitPoints) {
		super();
		this.timeToKill = timeToKill;
		this.lostHitPoints = lostHitPoints;
	}
	
	@Override
	public String toString() {
		return "Evaluation [timeToKill=" + timeToKill + ", lostHitPoints="
				+ lostHitPoints + "]";
	}
	
	
	
}
