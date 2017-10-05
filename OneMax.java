import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OneMax implements GeneticAlgorithm {

	@Override
	/* given two Individuals, execute uniform crossover
	 * return the new List<Integer>
	 * create two new DNA strings, and return two Individuals */
	public List<Individual> crossover(Individual one, Individual two) {
		List<Individual> children = new ArrayList<Individual>(2);
		Random rand = new Random();
		int len = one.getDNALen();
		int chance = rand.nextInt(100);
		
		if (chance <= 60) { /* 60% chance of crossover success*/
			List<Integer> childOneDNA = new ArrayList<Integer>(len);
			List<Integer> childTwoDNA = new ArrayList<Integer>(len);
			int l;
			
			for (l = 0; l < len; l++){ /* build mirror image DNA strings*/
				if (rand.nextInt(2) == 0){ 
					childOneDNA.add(one.getDNA().get(l));
					childTwoDNA.add(two.getDNA().get(l)); } // end if
				else {
					childOneDNA.add(two.getDNA().get(l));
					childTwoDNA.add(one.getDNA().get(l)); } /* end else */  } // end for
			
			children.add(Individual.createChild(childOneDNA, this));
			children.add(Individual.createChild(childTwoDNA, this));
		} // end if crossover successful
		else { /* 40% chance of crossover failure, copy parents */
			children.add(one);
			children.add(two); } // end else, copy parents
		
		// regardless of crossover success, mutate children
		children.get(0).mutate();
		children.get(1).mutate();
		
		return children;
	} // end crossover

	
	@Override
	/* evaluates person's fitness and returns its fitness value
	 * according to the onemax fitness specification
	 * the sum of 1s in the string
	 * (global optimum is length of dna) */
	public int evaluateFitness(Individual person) {
		int l, sum = 0;
		for (l = 0; l < person.getDNALen(); l++){
			if (person.getDNA().get(l) == 1)
				sum++; } // end for
		return sum;
	} // end evaluateFitness
} // end OneMax