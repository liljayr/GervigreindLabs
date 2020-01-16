import java.util.Collection;

public class OurAgent implements Agent {
	private enum State {
			OFF,
			FW, //FIND WALL
			B2, //CHECK IF ALREADY IN CORNER
			CD, //changing direction need to swap x & y
			FC, //FIND CORNER
			FGL, //FIND HIGHT OF GRID
			FGW, //fIND WIDTH OF GRID (-1 RIGHT AWAY FOR SPIRALLING)
			YP, // GO UP
			YM, // GO DOWN
			XP, //GO RIGHT
			XM, //GO LEFT
			GH, // CALCULATE & GO HOME
			XiP,
			XiM,
			YiP,
			YiM,
			TT //turning twice in a row
	}
	
	State state = State.OFF;
	int y = 0; //home location y in grid 
	int x = 0; //home location x in grid
	int l,w = 0;
	int L,W = -1;
	Boolean bump = true;
	Boolean changedDirection = false;
	String turn = "TURN_RIGHT";
	Boolean goingHome = false;
	State tempState = State.OFF; //for going home when need to turn twice in a row
	//String[] actions = { "TURN_ON", "TURN_OFF", "TURN_RIGHT", "TURN_LEFT", "GO", "SUCK" };
	
    public String nextAction(Collection<String> percepts) {
		System.out.print("perceiving:");
		for(String percept:percepts) {
			System.out.print("'" + percept + "', ");
		}
		System.out.println("");
		
		if(percepts.contains("DIRT")) {return "SUCK";}
		if(goingHome && x == 0 && y == 0) {
			return "TURN_OFF";
		}
		if(L == 0 && W == 0) {
			goingHome = true;
			if(state == State.XP) { state = State.XiP;}
			if(state == State.YP) { state = State.YiP;}
			if(state == State.XM) { state = State.XiM;}
			if(state == State.YM) { state = State.YiM;}
		}
		
		switch(state) {
		case OFF:
			state = State.FW;
			return "TURN_ON";
		case FW: //find y in home location
			System.out.println("FW");
			
			if(percepts.contains("BUMP")) {
				y--;
				state = State.B2;
				return turn;
			}
			y++;
			return "GO";
		case B2: //checking if 
			System.out.println("B2");
			if(bump == true) {
				x++;
				bump = false;
				return "GO";
			}
			if(percepts.contains("BUMP")) {
				x--;
				turn = "TURN_LEFT";
				state = State.CD;
				return turn;
			}
			state = State.FC;
		case FC: //find y in home location
			System.out.println("FC");
			
			if(percepts.contains("BUMP")) {
				x--;
				state = State.FGL;
				return turn;
			}
			x++;
			return "GO";
		case CD:
			System.out.println("CD");
			int temp = x;
			x = y;
			y = temp;
			state = State.FGL;
			return turn;
		case FGL:
			System.out.println("FGL");
			
			if(percepts.contains("BUMP")) {
				y++;
				state = State.FGW;
				System.out.println("L " + L);
				System.out.println("l " + w);
				System.out.println("W " + W);
				System.out.println("w " + w);
				return turn;
			}
			y--;
			L++;
			System.out.println("L " + L);
			System.out.println("l " + w);
			System.out.println("W " + W);
			System.out.println("w " + w);
			return "GO";
		
		case FGW:
			System.out.println("FGW");
			
			if(percepts.contains("BUMP")) {
				state = State.YP;
				x++;
				System.out.println("L " + L);
				System.out.println("l " + w);
				System.out.println("W " + W);
				System.out.println("w " + w);
				return turn;
			}
			x--;
			W++;
			System.out.println("L " + L);
			System.out.println("l " + w);
			System.out.println("W " + W);
			System.out.println("w " + w);
			return "GO";
		//Go up by L amount, using l as a counter, turn when l becomes L and lower L by 1
		case YP:
			System.out.println("YP");
			if(l < L) {
				y++;
				l++;
				System.out.println("L " + L);
				System.out.println("l " + w);
				System.out.println("W " + W);
				System.out.println("w " + w);
				return "GO";
			}
			y--;
			l--;
			L--;
			System.out.println("L " + L);
			System.out.println("l " + w);
			System.out.println("W " + W);
			System.out.println("w " + w);
			state = State.XP;
			return turn;
		//Go right by W amount, using w as a counter, turn when w becomes W and lower W by 1
		case XP:
			System.out.println("XP");
			if(w < W) {
				x++;
				w++;
				System.out.println("L " + L);
				System.out.println("l " + w);
				System.out.println("W " + W);
				System.out.println("w " + w);
				return "GO";
			}
			x--;
			w--;
			W--;
			System.out.println("L " + L);
			System.out.println("l " + w);
			System.out.println("W " + W);
			System.out.println("w " + w);
			state = State.YM;
			return turn;
		//Go down by L amount, using l as a counter, turn when l becomes 0 and lower L by 1
		case YM:
			System.out.println("XY");
			if(l > 0) {
				y--;
				l--;
				System.out.println("L " + L);
				System.out.println("l " + w);
				System.out.println("W " + W);
				System.out.println("w " + w);
				return "GO";
			}
			y++;
			L--;
			System.out.println("L " + L);
			System.out.println("l " + w);
			System.out.println("W " + W);
			System.out.println("w " + w);
			state = State.XM;
			return turn;
		//Go left by W amount, using w as a counter, turn when w becomes W and lower W by 1
		case XM:
			System.out.println("XM");
			if(w > 0) {
				x--;
				w--;
				System.out.println("L " + L);
				System.out.println("l " + w);
				System.out.println("W " + W);
				System.out.println("w " + w);
				return "GO";
			}
			x++;
			W--;
			System.out.println("L " + L);
			System.out.println("l " + w);
			System.out.println("W " + W);
			System.out.println("w " + w);
			state = State.YP;
			return turn;
		case TT:
			System.out.println("TT");
			state = tempState;
			return turn;
		case YiP:
			System.out.println("YiP");
			if(y < 0) { return "GO"; }
			else {
				if(x < 0) { 
					state = State.XiP;
					return "TURN_LEFT"; 
				}
				if(x > 0) { 
					state = State.XiM;
					return "TURN_RIGHT"; 
				}
				
				tempState = State.YiM;
				state = State.TT;
				return turn;
			}
		case YiM:
			System.out.println("YiM");
			if(y > 0) { return "GO"; }
			else {
				if(x < 0) { 
					state = State.XiM;
					return "TURN_RIGHT"; 
				}
				if(x > 0) { 
					state = State.XiP;
					return "TURN_LEFT"; 
				}
				tempState = State.YiP;
				state = State.TT;
				return turn;
			}
		case XiP:
			System.out.println("XiP");
			if(x < 0) { return "GO"; }
			else {
				if(y < 0) { 
					state = State.YiP;
					return "TURN_LEFT"; 
				}
				if(y > 0) { 
					state = State.YiM;
					return "TURN_RIGHT"; 
				}
				tempState = State.XiM;
				state = State.TT;
				return turn;
			}
		case XiM:
			System.out.println("XiM");
			if(x > 0) { return "GO"; }
			else {
				if(y < 0) { 
					state = State.YiM;
					return "TURN_LEFT"; 
				}
				if(y > 0) { 
					state = State.YiP;
					return "TURN_RIGHT"; 
				}
				tempState = State.XiP;
				state = State.TT;
				return turn;
			}
		default: //Home reached, turn off

			System.out.println("X: " + x);
			System.out.println("Y: " + y);
			
			return "TURN_OFF";
		}
	}
}
