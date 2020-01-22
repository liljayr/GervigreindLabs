import java.util.List;

public class AStarSearch implements SearchAlgorithm {

	private int totalNodeExpansions = 0;
	private int maxFrontierSize = 0;
	private int totalCost = 0;
	private List<Action> plan;
	private Map<Node> frontier;

	private Heuristics heuristics;
	public AStarSearch(Heuristics h) {
		this.heuristics = h;
	}

	@Override
	public void doSearch(Environment env) {
		heuristics.init(env);
		List<Action> moves;
		Node root = Node(env.getCurrentState(), null, null, 1, 0);
		frontier.add(root);
		Node currNode = frontier.pop();

		while(plan.size() == 0){

			// get first value of frontier
			// if it contains action TURN_OFF call on getPlan from Node.java and end it all
			// use legalMoves of current environment to determine what leaf nodes to create
			moves = env.legalMoves(env.getCurrentState());
			
			// moves is an action list that needs to be iterated through to create the new leafs at expansion.

			// add new nodes to the frontier and sort

				//rinse and repeat until function trys to expand a node that has action TURN_OFF

				// When last node is reached, call on getPlan() in the Node.java file to get the full action plan

		};
		
		// TODO implement the search her
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
