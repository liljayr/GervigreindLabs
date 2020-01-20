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
	int l = 0;
	int w = 0;
	int L = -1;
	int W = -1;
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
			//resetting values if running program multiple times in a row.
			state = State.OFF;
			L = -1;
			W = -1;
			turn = "TURN_RIGHT";
			goingHome = false;
			tempState = State.OFF;
			return "TURN_OFF";
		}
		
		switch(state) {
		case OFF:
			state = State.FW;
			printing("TURN_ON");
			System.out.println("TURNiNG ONNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN");
			return "TURN_ON";
		case FW: //find y in home location
			
			if(percepts.contains("BUMP")) {
				y--;
				state = State.FC;
				printing("turn"); 
				return turn;
			}
			y++;
			printing("GO"); 
			return "GO";
		case FC: //find y in home location
			if(percepts.contains("BUMP")) {
				x--;
				state = State.FGL;
				printing("turn");
				return turn;
			}
			x++;
			printing("GO"); 
			return "GO";
		case FGL:
			
			if(percepts.contains("BUMP")) {
				y++;
				state = State.FGW;
				printing("turn"); 
				return turn;
			}
			y--;
			L++;
			printing("GO"); 
			return "GO";
		
		case FGW:
			
			if(percepts.contains("BUMP")) {
				state = State.YP;
				x++;
				W--;
				printing("turn"); 
				return turn;
			}
			x--;
			W++;
			printing("GO");
			return "GO";
		//Go up by L amount, using l as a counter, turn when l becomes L and lower L by 1
		case YP:
			if(L != 0) {
				if(l < L) {
					y++;
					l++;
					printing("GO");
					return "GO";
				}
				l--;
				L--;
			}

			if(!goingHome(L, W)) {
				state = State.XP;
				printing("turn"); 
				return turn;
			}
			break;
		//Go right by W amount, using w as a counter, turn when w becomes W and lower W by 1
		case XP:
			if(W != 0) {
				if(w < W) {
					x++;
					w++;
					printing("GO"); 
					return "GO";
				}
				w--;
				W--;
			}

			if(!goingHome(L, W)) {
				state = State.YM;
				printing("turn"); 
				return turn;
			}
			break;
		//Go down by L amount, using l as a counter, turn when l becomes 0 and lower L by 1
		case YM:
			if(L != 0) {
				if(l > 0) {
					y--;
					l--;
					printing("GO"); 
					return "GO";
				}
				L--;
			}

			if(!goingHome(L,W)) {
				state = State.XM;
				printing("turn"); 
				return turn;
			}
			break;

		//Go left by W amount, using w as a counter, turn when w becomes W and lower W by 1
		case XM:
			if(W != 0) {
				if(w > 0) {
					x--;
					w--;
					printing("GO");
					return "GO";
				}
				W--;
			}
			if(!goingHome(L, W)) {
				state = State.YP;
				printing("turn"); 
				return turn;
			}
			break;
		}
		
		switch(state) {
		case TT:
			state = tempState;
			printing("turn"); 
			return turn;
		case XiP:
			if(x < 0) {
				x++;
				printing("GO"); 
				return "GO";
			}
			else {
				if(y < 0) {
					state = State.YiP; ////////////////////
					printing("TURN_LEFT"); 
					return "TURN_LEFT"; 
				}
				if(y > 0) {
					state = State.YiM; ///////////////////
					printing("TURN_RIGHT"); 
					return "TURN_RIGHT"; 
				}
				
				tempState = State.XiM;
				state = State.TT;
				printing("turn"); 
				return turn;
			}
		case XiM:
			if(x > 0) {
				x--;
				printing("GO"); 
				return "GO";
			}
			else {
				if(y < 0) {
					state = State.YiM;
					printing("TURN_RIGHT"); 
					return "TURN_RIGHT"; 
				}
				if(y > 0) {
					state = State.YiP;
					printing("TURN_LEFT"); 
					return "TURN_LEFT"; 
				}
				tempState = State.XiP;
				state = State.TT;
				printing("turn"); 
				return turn;
			}
		case YiM:
			if(y > 0) {
				y--;
				printing("GO"); 
				return "GO";
			}
			else {
				if(x < 0) {
					state = State.XiP;
					printing("TURN_LEFT"); 
					return "TURN_LEFT";
				}
				if(x > 0) {
					state = State.XiM;
					printing("TURN_RIGHT"); 
					return "TURN_RIGHT";
				}
				tempState = State.YiP;
				state = State.TT;
				printing("turn"); 
				return turn;
			}
		case YiP:
			if(y < 0) {
				y++;
				printing("GO"); 
				return "GO";
			}
			else {
				if(x < 0) {
					state = State.XiP;
					printing("TURN_RIGHT"); 
					return "TURN_RIGHT"; 
				}
				if(x > 0) {
					state = State.XiM;
					printing("TURN_LEFT"); 
					return "TURN_LEFT"; 
				}
				tempState = State.YiM;
				state = State.TT;
				printing("turn"); 
				return turn;
			}
		default: //Home reached, turn off
			
			printing("TURN_OFF"); 
			return "TURN_OFF";
		}
	}
    private void printing(String action) {
    	System.out.println(state);
    	System.out.println("Action: " + action);
    	System.out.println("X: " + x + ", Y: " + y);
    	System.out.println("W: " + W + ", L: " + L);
    	System.out.println("w: " + w + ", l: " + l);
    	System.out.println("Going home: " + goingHome);
    }
    private Boolean goingHome(int L, int W) {
    	if(L == 0 && W == 0) 
    	{
			if(state == State.YM) { state = State.YiM;}
			if(state == State.XP) { state = State.XiP;}
			if(state == State.YP) { state = State.YiP;}
			if(state == State.XM) { state = State.XiM;}
			
			System.out.println(state);
			
			goingHome = true;
			
			return true;
    	}
    	return false;
	}
}
