import java.util.ArrayList;
import java.util.List;

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
		List<Action> moves = null;
		Node root = new Node(env.getCurrentState(), 0);
		frontier.add(root);
		Node currNode = null;

		while(plan.size() == 0){

			// get first value of frontier //currNode
			if(!frontier.isEmpty()){
				currNode = frontier.get(0);
				frontier.remove(0);
			}

			if(currNode.action == Action.TURN_OFF && env.getCost(env.getCurrentState(), currNode.action)+ currNode.evaluation == 1){
				// if it contains action TURN_OFF call on getPlan from Node.java and end it all
				plan = currNode.getPlan();
			}
			else{
				// use legalMoves of current environment to determine what leaf nodes to create
				moves = env.legalMoves(currNode.state);

				// moves is an action list that needs to be iterated through to create the new leafs at expansion.
				for (Action action : moves) {
					Node tempNode = new Node(currNode, env.getNextState(env.getCurrentState(), action), action, env.getCost(env.getCurrentState(), action)+ currNode.evaluation);
					frontier.add(tempNode);
				}
				// add new nodes to the frontier and sort by total cost
				NodeSorter nodeSorter = new NodeSorter(frontier);         
				frontier = nodeSorter.getSortedNodesByEvaluation();   
			}

				//rinse and repeat until action TURN_OFF is at the front of the frontier.

		};
		
		// TODO implement the search here
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
