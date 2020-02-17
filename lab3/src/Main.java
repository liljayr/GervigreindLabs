import java.util.ArrayList;
import java.util.List;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.CSP;
import aima.core.search.csp.CSPStateListener;
import aima.core.search.csp.Domain;
import aima.core.search.csp.ImprovedBacktrackingStrategy;
import aima.core.search.csp.NotEqualConstraint;
import aima.core.search.csp.SolutionStrategy;
import aima.core.search.csp.Variable;

public class Main {

	private static CSP setupCSP() {
		CSP csp = null;
//		In five houses, each with a different color, live five persons of different nationality,
//		each of whom prefers a different brand of cigarettes, a different drink, and a different pet.
//		The five houses are arranged in a row (no house has more than 2 neighbors).   
//		# The Englishman lives in the red house.
//		# The Spaniard owns the dog.
//		# Coffee is drunk in the green house.
//		# The Ukrainian drinks tea.
//		# The green house is immediately to the right of the ivory house.
//		# The Old Gold smoker owns snails.
//		# Kools are smoked in the yellow house.
//		# Milk is drunk in the middle house.
//		# The Norwegian lives in the first house.
//		# The man who smokes Chesterfields lives in the house next to the man with the fox.
//		# Kools are smoked in the house next to the house where the horse is kept.
//		# The Lucky Strike smoker drinks orange juice.
//		# The Japanese smokes Parliaments.
//		# The Norwegian lives next to the blue house.
//
//		Now, who drinks water? Who owns the zebra?
				
				String[] colors = {"Red", "Green", "Ivory", "Yellow", "Blue"};
		String[] nations = {"Englishman", "Spaniard", "Norwegian", "Ukrainian", "Japanese"};
		String[] cigarettes = {"Old Gold", "Kools", "Chesterfields", "Lucky Strike", "Parliaments"};
		String[] drinks = {"Water", "Orange juice", "Tea", "Coffee", "Milk"};
		String[] pets = {"Zebra", "Dog", "Fox", "Snails", "Horse"};
		


		// TODO create variables, e.g.,
		// Variable var1 = new Variable("name of the variable 1");
		// Variable var2 = new Variable("name of the variable 2");

		// TODO add all your variables to this list, e.g.,
		List<Variable> color = new ArrayList<Variable>();
		List<Variable> nation = new ArrayList<Variable>();
		List<Variable> cigarette = new ArrayList<Variable>();
		List<Variable> drink = new ArrayList<Variable>();
		List<Variable> pet = new ArrayList<Variable>();

		for (int i = 0; i < colors.length; i++)
		{
			color.add(new Variable(colors[i]));
			nation.add(new Variable(nations[i]));
			cigarette.add(new Variable(cigarettes[i]));
			drink.add(new Variable(drinks[i]));
			pet.add(new Variable(pets[i]));
		}

		List<Variable> variables = new ArrayList<Variable>();

		for (int i = 0; i < colors.length; i++)
		{
			for (int j = 0; j < colors.length; j++)
			{
				if (i == 0)
					variables.add(color.get(j));
				else if (i == 1)
					variables.add(nation.get(j));
				else if (i == 2)
					variables.add(cigarette.get(j));
				else if (i == 3)
					variables.add(drink.get(j));
				else
					variables.add(pet.get(j));
			}	
		}

		csp = new CSP(variables);

		//set domains of variables, e.g.,
		Domain domain = new Domain(new Integer[]{1, 2, 3, 4, 5});

		for(Variable var: variables)
		{
			if(var.getName().equals("Milk"))
			{
				csp.setDomain(var, new Domain(new Integer[] {3} ));
			}
			else if(var.getName().equals("Norwegian"))
			{
				csp.setDomain(var, new Domain(new Integer[] {1} ));
			}
			
			else
			{
				csp.setDomain(var, domain);
			}
		}

		//add constraints, e.g.,
		
		
		for (int i = 0; i < 5; i++)
		{
			for (int j = 0; j < 5; j++)
			{
				if (i == j)
				{
					// Do nothing
				}
				else
				{
					csp.addConstraint(new NotEqualConstraint(color.get(i), color.get(j)));
					csp.addConstraint(new NotEqualConstraint(nation.get(i), nation.get(j)));
					csp.addConstraint(new NotEqualConstraint(cigarette.get(i), cigarette.get(j)));
					csp.addConstraint(new NotEqualConstraint(drink.get(i), drink.get(j)));
					csp.addConstraint(new NotEqualConstraint(pet.get(i), pet.get(j)));
					
				}
			}
		}
		csp.addConstraint(new EqualConstraint(color.get(0), nation.get(0)));
		csp.addConstraint(new EqualConstraint(nation.get(1), pet.get(1)));
		csp.addConstraint(new EqualConstraint(color.get(1), drink.get(3)));
		csp.addConstraint(new EqualConstraint(nation.get(3), drink.get(2)));
		
		csp.addConstraint(new SuccessorConstraint(color.get(1), color.get(2)));
		
		csp.addConstraint(new EqualConstraint(cigarette.get(0), pet.get(3)));
		csp.addConstraint(new EqualConstraint(cigarette.get(1), color.get(3)));
		
		csp.addConstraint(new DifferByOneConstraint(cigarette.get(2), pet.get(2)));
		csp.addConstraint(new DifferByOneConstraint(cigarette.get(1), pet.get(4)));
		
		csp.addConstraint(new EqualConstraint(cigarette.get(3), drink.get(1)));
		csp.addConstraint(new EqualConstraint(nation.get(4), cigarette.get(4)));
		csp.addConstraint(new DifferByOneConstraint(nation.get(2), color.get(4)));
		
		
		

		return csp;
	}

	private static void printSolution(Assignment solution) {
		// TODO print out useful answer
		// You can use the following to get the value assigned to a variable:
		// Object value = solution.getAssignment(var); 
		// For debugging it might be useful to print the complete assignment and check whether
		// it makes sense.
		System.out.println("solution:" + solution);
	}
	
	/**
	 * runs the CSP backtracking solver with the given parameters and print out some statistics
	 * @param description
	 * @param enableMRV
	 * @param enableDeg
	 * @param enableAC3
	 * @param enableLCV
	 */
	private static void findSolution(String description, boolean enableMRV, boolean enableDeg, boolean enableAC3, boolean enableLCV) {
		CSP csp = setupCSP();

		System.out.println("======================");
		System.out.println("running " + description);
		
		long startTime, endTime;
		startTime = System.currentTimeMillis();
		SolutionStrategy solver = new ImprovedBacktrackingStrategy(enableMRV, enableDeg, enableAC3, enableLCV);
		final int nbAssignments[] = {0};
		solver.addCSPStateListener(new CSPStateListener() {
			@Override
			public void stateChanged(Assignment arg0, CSP arg1) {
				nbAssignments[0]++;
			}
			@Override
			public void stateChanged(CSP arg0) {}
		});
		Assignment solution = solver.solve(csp);
		endTime = System.currentTimeMillis();
		System.out.println("runtime " + (endTime-startTime)/1000.0 + "s" + ", number of assignments (visited states):" + nbAssignments[0]);
		printSolution(solution);
	}

	/**
	 * main procedure
	 */
	public static void main(String[] args) throws Exception {
		// run solver with different parameters
		findSolution("backtracking + AC3 + most constrained variable + least constraining value", true, true, true, true);
		findSolution("backtracking + AC3 + most constrained variable", true, true, true, false);
		findSolution("backtracking + AC3", false, false, true, false);
		findSolution("backtracking + forward checking + most constrained variable + least constraining value", true, true, false, true);
		findSolution("backtracking + forward checking + most constrained variable", true, true, false, false);
		findSolution("backtracking + forward checking", false, false, false, false);
	}

}
