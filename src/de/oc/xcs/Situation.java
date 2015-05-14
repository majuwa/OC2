package de.oc.xcs;

public class Situation {
	private String[] situationView;
	public Situation(String distance,String hp, String hpEnemy, String positionX, String positionY){
		situationView = new String[5];
		situationView[0] = distance;
		situationView[1] = hp;
		situationView[2] = hpEnemy;
		situationView[3] = positionX;
		situationView[4] = positionY;
	}
	public String[] getSituation(){
		return situationView;
	}
	public boolean equals(Object o){
		if(o instanceof Situation){
			Situation tmp =(Situation) o;
			for(int i = 0; i< situationView.length;i++){
				String s0 = situationView[i];
				String s1 = tmp.situationView[i];
				if(s0.equals("#") || s1.equals("#"))
					continue;
				int value1 = Integer.parseInt(s0);
				int value2 = Integer.parseInt(s1);
				if(Math.max(value1, value2) - Math.min(value1, value2) > 10)
					return false;
			}
			return true;
		}
		return false;
	}

}
