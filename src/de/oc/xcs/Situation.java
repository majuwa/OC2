package de.oc.xcs;

import java.io.Serializable;

public class Situation implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4277094478546024971L;
	private String[] situationView;
	public static final int SITUATION_COUNTER = 5;
	public static final int DISTANCE = 0;
	public static final int HP = 1;
	public static final int HP_ENEMY = 2;
	public static final int POSITION_X = 3;
	public static final int POSITION_Y = 4;
	public static final int COUNT_ENEMY = 5;
	public Situation(String distance, String hp, String hpEnemy,
			String positionX, String positionY) {
		situationView = new String[SITUATION_COUNTER];
		situationView[0] = distance;
		situationView[1] = hp;
		situationView[2] = hpEnemy;
		situationView[3] = positionX;
		situationView[4] = positionY;
		//situationView[5] = countEnemy;
	}

	public Situation(String[] t) {
		situationView = new String[SITUATION_COUNTER];
		for (int i = 0; i < situationView.length; i++)
			situationView[i] = t[i];

	}

	public String[] getSituation() {
		return situationView;
	}

	public boolean equals(Object o) {
		if (o instanceof Situation) {
			Situation tmp = (Situation) o;
			for (int i = 0; i < situationView.length; i++) {
				String s0 = situationView[i];
				String s1 = tmp.situationView[i];
				if (s0.equals("#") || s1.equals("#"))
					continue;
				double value1 = Double.parseDouble(s0);
				double value2 = Double.parseDouble(s1);
				switch (i) {
				case DISTANCE:
					if(value1> 100 && value2 > 100)
						continue;
					else if(value1 > 50 && value2 > 50 && value1<= 100 && value2 <= 100)
						continue;
					else if(value1<=50 && value2 <= 50 && value1>=20 && value1 >= 20)
						continue;
					else if(value1<20 && value2 < 20)
						continue;
					else
						return false;
				default:
					if(value1> 80 && value2 > 80)
						continue;
					else if(value1 > 50 && value2 > 50 && value1<= 100 && value2 <= 100)
						continue;
					else if(value1<=50 && value2 <= 50 && value1>=20 && value1 >= 20)
						continue;
					else if(value1<20 && value2 < 20)
						continue;
					else
						return false;
				}
			}
			return true;
		}
		return false;
	}

}
