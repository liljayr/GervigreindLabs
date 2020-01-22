import java.util.List;

public class AStarSearch implements SearchAlgorithm {

	private Heuristics heuristics;
	public AStarSearch(Heuristics h) {
		this.heuristics = h;
	}

	@Override
	public void doSearch(Environment env) {
		heuristics.init(env);
		
		// TODO implement the search here
	}

	@Override
	public List<Action> getPlan() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNbNodeExpansions() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxFrontierSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPlanCost() {
		// TODO Auto-generated method stub
		return 0;
	}

}
