import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

public class AStarSearch implements SearchAlgorithm {

	private int totalNodeExpansions = 0;
	private int maxFrontierSize = 0;
	private int totalCost = 0;
	private List<Action> plan = new ArrayList<Action>();
	private ArrayList<Node> frontier = new ArrayList<Node>();

	private Heuristics heuristics;
	public AStarSearch(Heuristics h) {
		this.heuristics = h;
	}

	@Override
	public void doSearch(Environment env) {
		heuristics.init(env);
		List<Action> moves = new LinkedList<Action>();
		Node root = new Node(env.getCurrentState(), 0);
		frontier.add(root);
		Node currNode = null;

		while(plan.size() == 0){

			// get first value of frontier //currNode
			if(!frontier.isEmpty()){
				currNode = frontier.get(0);
				frontier.remove(0);
			}

			if(currNode.action == Action.TURN_OFF){// && env.getCost(env.getCurrentState(), currNode.action)+ currNode.evaluation == 1){
				// if it contains action TURN_OFF call on getPlan from Node.java and end it all
				plan = currNode.getPlan();
			}
			else{
				// use legalMoves of current environment to determine what leaf nodes to create
				System.out.println(currNode);
				moves = env.legalMoves(currNode.state);

				//System.out.println(moves);

				// moves is an action list that needs to be iterated through to create the new leafs at expansion.
				for (Action move : moves) {

					Node tempNode = new Node(currNode, env.getNextState(currNode.state, move), move, env.getCost(currNode.state, move)+ currNode.evaluation);
					//checking for a cycle of only turns
					if(tempNode.depth > 4){
						if(!checkForTurnCycle(tempNode)){
							//System.out.println(tempNode);
							frontier.add(tempNode);
							if(frontier.size() > maxFrontierSize){
								maxFrontierSize = frontier.size();
							}
						}
					}
					else{
						//System.out.println(tempNode);
						frontier.add(tempNode);
						if(frontier.size() > maxFrontierSize){
							maxFrontierSize = frontier.size();
						}
					}
				}
				// add new nodes to the frontier and sort by total cost
				NodeSorter nodeSorter = new NodeSorter(frontier);         
				frontier = nodeSorter.getSortedNodesByEvaluation();   
			}

				//rinse and repeat until action TURN_OFF is at the front of the frontier.

		};
		
		// TODO implement the search here
	}
	private Boolean checkForTurnCycle(Node node){
		if(node.parent.parent.parent.parent.state == node.state || node.parent.parent.state == node.state){
			return true;
		}
		if(node.action == Action.TURN_LEFT && node.parent.action == Action.TURN_RIGHT || node.action == Action.TURN_RIGHT && node.parent.action == Action.TURN_LEFT){
			return true;
		}
		return false;
	}
	private void print(List<Node> frontier){
		frontier.forEach((f) -> System.out.println(f.evaluation + " "));
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
