import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

public class AStarSearch implements SearchAlgorithm {

	private int totalNodeExpansions = 0;
	private int maxFrontierSize = 0;
	private int totalCost = 0;
	private List<Action> plan = new LinkedList<Action>();
	private List<Action> moves = new LinkedList<Action>();
	private HashMap<State, Node> stateFrontier = new HashMap<State, Node>();
	private ArrayList<Node> frontier = new ArrayList<Node>();
	private HashMap<State, Node> stateVisited = new HashMap<State, Node>();
	
	private Heuristics heuristics;
	public AStarSearch(Heuristics h) {
		this.heuristics = h;
	}

	@Override
	public void doSearch(Environment env) {
		heuristics.init(env);
		Node root = new Node(env.getCurrentState(), 0);
		frontier.add(root);
		stateFrontier.put(root.state, root);
		Node currNode = null;
		//numOfDirt = root.state.dirt.size();

		while(!frontier.isEmpty()){

			// get first value of frontier //currNode
			if(!frontier.isEmpty()){
				currNode = frontier.get(0);
				frontier.remove(0);
				stateFrontier.remove(currNode.state);
			}

			// if endGoalState has been reached call on getPlan from Node.java and end it all

			if(currNode.state.dirt.isEmpty() == true && currNode.state.position.equals(env.home)){
				plan = currNode.getPlan();
				if(!plan.contains(Action.TURN_OFF)){
					plan.add(Action.TURN_OFF);
				}
				totalCost = currNode.evaluation;
				frontier.clear();
			}
			else{
				// use legalMoves of current environment to determine what leaf nodes to create

				moves = env.legalMoves(currNode.state);

				

				// moves is an action list that needs to be iterated through to create the new leafs at expansion.
				for (Action move : moves) {
					//System.out.println(move);
					//print(frontier);
					Node tempNode = new Node(currNode, env.getNextState(currNode.state, move), move, env.getCost(currNode.state, move)+ currNode.evaluation);
					//checking for a cycle of only turns

					
					//System.out.println("checking node: " + tempNode);
					if(!stateVisited.containsKey(tempNode.state)){

						// if depth is more than 4 check for turning cycles.
						if(tempNode.depth >= 4){
							if(!checkForTurnCycle(tempNode)){
								//System.out.println(tempNode);
								if(!stateFrontier.containsKey(tempNode.state)){
									tempNode.heuristicEval = heuristics.eval(tempNode.state);
									frontier.add(tempNode);
									stateFrontier.put(tempNode.state, tempNode);
									//System.out.println("adding node to frontier");
								}
								else{
									if(stateFrontier.get(tempNode.state).evaluation > tempNode.evaluation){
										//System.out.println("shorter way to state X found");
										frontier.remove(stateFrontier.get(tempNode.state));
										tempNode.heuristicEval = heuristics.eval(tempNode.state);
										frontier.add(tempNode);
										stateFrontier.replace(tempNode.state, tempNode);
									}
								}
							}
							//don't add to frontier if it's turning in circles
						}
						else{
							//System.out.println(tempNode);
							
							if(!stateFrontier.containsKey(tempNode.state)){
								tempNode.heuristicEval = heuristics.eval(tempNode.state);
								frontier.add(tempNode);
								stateFrontier.put(tempNode.state, tempNode);
								//System.out.println("adding node to frontier");
							}
							else{
								if(stateFrontier.get(tempNode.state).evaluation > tempNode.evaluation){
									//System.out.println("short way to state X found");
									frontier.remove(stateFrontier.get(tempNode.state));
									tempNode.heuristicEval = heuristics.eval(tempNode.state);
									frontier.add(tempNode);
									stateFrontier.replace(tempNode.state, tempNode);
								}
							}
						}

						totalNodeExpansions += 1;

						if(frontier.size() > maxFrontierSize){
							maxFrontierSize = frontier.size();
						}
					}
				}

			}
			stateVisited.put(currNode.state, currNode);
			Collections.sort(frontier);

				//rinse and repeat until action TURN_OFF is at the front of the frontier.
		};
		
		// TODO implement the search here
		//System.out.println("frontierSize: " + frontier.size());
		//System.out.println("plan: " + plan);
	}
	private Boolean checkForTurnCycle(Node node){
		if(node.parent.parent.parent.parent.state.equals(node.state) || node.parent.parent.state.equals(node.state)){
			return true;
		}
		return false;
	}


	@Override
	public List<Action> getPlan() {
		return plan;
	}

	@Override
	public int getNbNodeExpansions() {
		return totalNodeExpansions;
	}

	@Override
	public int getMaxFrontierSize() {
		return maxFrontierSize;
	}

	@Override
	public int getPlanCost() {
		return totalCost;
	}

}
